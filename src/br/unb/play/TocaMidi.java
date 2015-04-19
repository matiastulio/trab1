package br.unb.play;

import javax.sound.midi.*;
import java.io.*;

public class TocaMidi{
	
    //variáveis de classe
    private TocaMidi  tempo;
	
	private Sequencer sequenciador;
    private Sequence  sequencia   ;
    
    private int             iniciar        ;
    private int             numeroTrilhas  ;
    private long            duracao        ;
    private double          duraTick       ;
    private float           bpm            ;
    private String          formulaCompasso;
    private String          st             ;
    private String          conjuntoInfo   ;
    private String          nomearq        ;
    private String          tempoTotal     ;
    private static String   pathAudios     ;
    
    private static Receiver receptor; //utilizado para alterar o volume
    
    static final int FORMULA_DE_COMPASSO = 0x58;
    static final int MENSAGEM_TONALIDADE = 0x59;
    
    TocaMidi(){
        iniciar = 1;
        nomearq = "";
        pathAudios = "caminho";//alterar
        receptor = null;
    }
    
    public void atualizarVolume(int volume) throws MidiUnavailableException{
        
    	volume = Math.round((volume*127)/100);
        
        try{
            receptor = sequenciador.getTransmitters().iterator().next().getReceiver();
            sequenciador.getTransmitter().setReceiver(receptor);
        }catch(MidiUnavailableException e) { }//exceção

        ShortMessage mensagemDeVolume = new ShortMessage();
        
        for(int i = 0; i < ((numeroTrilhas < 16) ? 16 : numeroTrilhas); i++){//Minino 16 trilhas
            try{
                mensagemDeVolume.setMessage(ShortMessage.CONTROL_CHANGE, i, 7, volume);
                receptor.send(mensagemDeVolume, -1);
            }catch (InvalidMidiDataException e1) { }
        }
    }
    
    public String[] getDados (){
        String[] Dados = new String[4];
        
        //preenche os dados
        Dados[0] = formulaCompasso;
        Dados[1] = st;
        Dados[2] = conjuntoInfo;
        Dados[3] = tempoTotal;
        
        return Dados;
    }
    
    public long getTempo(){
        
        return (sequenciador != null) ? sequenciador.getMicrosecondPosition() : -1;
    }
    
    public long getDuracao(){
        return (sequenciador != null) ? duracao : -1;
    }
    
    public void setArquivo(String arquivo){
        
    	if(!nomearq.equals("")){
            parar();
        }
        
        nomearq = arquivo + ".mid";
        nomearq = pathAudios + nomearq;

        File arqMidi = new File(nomearq);

        try{
            sequencia = MidiSystem.getSequence(arqMidi);                            

            montarDados(sequencia, nomearq);

            //prepara o sequenciador
            sequenciador = MidiSystem.getSequencer();
            sequenciador.setSequence(sequencia);
            sequenciador.open();
        }

        //possíveis erros
        catch(MidiUnavailableException e1){
            System.out.println("Dispositivo midi não disponível.");
        }
        
        catch(InvalidMidiDataException e2){
            System.out.println("Dados midi corrompidos.");
        }
        
        catch(IOException e3){
            System.out.println("Arquivo midi não foi encontrado.");
        }
    }
    
    public int tocar(){
        
        if (iniciar == 1){
            
        	iniciar = 0;
            
            try{
                sequenciador.start();
            }catch(NullPointerException e){ 
            	return -1; 
            }
            return 1;
        }
        else{
            try{
                sequenciador.start();
            }catch(NullPointerException e){ 
            	return -1; 
            }
            return 0;
        }
    }
    
    public void pausar(){
        try{
            sequenciador.stop();
        }
        catch(NullPointerException e) { }//exceção
    }
    
    public void parar (){
        if(sequenciador != null){
            sequenciador.stop();
            sequenciador.setMicrosecondPosition(0);
        }
    }
    
    public void irPara(String durando){
        
        sequenciador.stop();
                
        String hora = durando.substring(0, 2);
        String min  = durando.substring(3, 5);
        String seg  = durando.substring(6, 8);
        
        int horas    = Integer.parseInt(hora);
        int minutos  = Integer.parseInt(min);
        int segundos = Integer.parseInt(seg);
        
        double posiNova = ((double) (horas*3600*1000000)) + 
        		          ((double) (minutos*60*1000000)) + 
        		          ((double) (segundos*1000000));
        
        posiNova = posiNova / (duraTick*1000000);
        
        sequenciador.setTickPosition((long)posiNova);
        
        tocar();
    }
    
    static void retardo(int miliseg){
       try{
           Thread.sleep(miliseg);
       }
       catch(InterruptedException e) { }//exceção
   }

    private void montarDados(Sequence sequencia, String nome){

       duracao              = sequencia.getMicrosecondLength()/1000000;
       int   resolucao      = sequencia.getResolution();
       long  totalTicks     = sequencia.getTickLength();
        
       duraTick             = (double)duracao/totalTicks;
       float durseminima    = (float)duraTick*resolucao;
       int   totalseminimas = (int)(duracao/durseminima);
       int   horas          = (int) Math.floor(duracao/3600);
       int   minutos        = (int) Math.floor(duracao/60) - 60*horas;
       int   segundos       = (int) (duracao - 60*minutos - 3600*horas);
       
       bpm                  = Math.round(60/durseminima);
       
       String sHoras = "" + horas;
       String sMinutos = "" + minutos;
       String sSegundos = "" + segundos;
       
       if(horas < 10){
           sHoras = "0" + horas;
       }
       
       if(minutos < 10){
           sMinutos = "0" + minutos;
       }
       
       if(segundos < 10){
           sSegundos = "0" + segundos;
       }
       
       tempoTotal = sHoras + ":" + sMinutos + ":" + sSegundos;
       
       Track[] trilhas = sequencia.getTracks();
       numeroTrilhas = trilhas.length;
       Track trilha =  trilhas[0];

       Par formComp =  getFormulaDeCompasso(trilha);
       st = "--------";
       
       try
       {
           st =  getTonalidade(trilha);
       }
       catch(Exception e){}
       
       formulaCompasso = formComp.getX() +"/"+ (int)(Math.pow(2, formComp.getY()));
       
       conjuntoInfo = "andamento                     = "      +bpm+ " bpm\n" 
                    + "resolução                       = "    +resolucao
                    + " tiques (nº de divisões da semínima)\n" 
                    + "número de tiques           = "         +totalTicks
                    + "\n" + "duração do tique             = "+duraTick
                    + " s\n" + "duração da semínima   = "     +durseminima
                    + " s\n" + "total de seminimas        = " +totalseminimas
                    + "\n" + "Número de trilhas          = "  +numeroTrilhas;
   }

    static Par getFormulaDeCompasso(Track trilha){
        
    	int p=1;
        int q=1;

        for(int i=0; i<trilha.size(); i++){
            
        	MidiMessage m = trilha.get(i).getMessage();
            if(m instanceof MetaMessage) {
                    
            	if(((MetaMessage)m).getType()==FORMULA_DE_COMPASSO){
                    MetaMessage mm = (MetaMessage)m;
                    byte[] data = mm.getData();
                    p = data[0];
                    q = data[1];
                    return new Par(p,q);
                }
            }
        }
        return new Par(p,q);
    }

    static private class Par{
        int x, y;

        Par (int x_, int y_)  {
            this.x = x_;
            this.y = y_;
        }

        int getX()
        {
            return x;
        }

        int getY(){
            return y;
        }
    }

    private static String getTonalidade(Track trilha) throws InvalidMidiDataException{
    	
        String sTom = "";
        for(int i=0; i<trilha.size(); i++){
            
        	MidiMessage m = trilha.get(i).getMessage();
        	if(((MetaMessage)m).getType() == MENSAGEM_TONALIDADE){
        		
                MetaMessage mm        = (MetaMessage)m;
                
                byte[]     data       = mm.getData();
                byte       tonalidade = data[0];
                byte       maior      = data[1];

                String       sMaior = "Maior";
                if(maior==1){ 
                	sMaior = "Menor";
                }
                if(sMaior.equalsIgnoreCase("Maior")){
                    switch (tonalidade){
                    
                        case -7: sTom = "Dób Maior"; break;
                        case -6: sTom = "Solb Maior"; break;
                        case -5: sTom = "Réb Maior"; break;
                        case -4: sTom = "Láb Maior"; break;
                        case -3: sTom = "Mib Maior"; break;
                        case -2: sTom = "Sib Maior"; break;
                        case -1: sTom = "Fá Maior"; break;
                        case  0: sTom = "Dó Maior"; break;
                        case  1: sTom = "Sol Maior"; break;
                        case  2: sTom = "Ré Maior"; break;
                        case  3: sTom = "Lá Maior"; break;
                        case  4: sTom = "Mi Maior"; break;
                        case  5: sTom = "Si Maior"; break;
                        case  6: sTom = "Fá# Maior"; break;
                        case  7: sTom = "Dó# Maior"; break;
                    }
                }
                else if(sMaior.equalsIgnoreCase("Menor")){
                    switch (tonalidade){
                    
                        case -7: sTom = "Láb Menor"; break;
                        case -6: sTom = "Mib Menor"; break;
                        case -5: sTom = "Sib Menor"; break;
                        case -4: sTom = "Fá Menor"; break;
                        case -3: sTom = "Dó Menor"; break;
                        case -2: sTom = "Sol Menor"; break;
                        case -1: sTom = "Ré Menor"; break;
                        case  0: sTom = "Lá Menor"; break;
                        case  1: sTom = "Mi Menor"; break;
                        case  2: sTom = "Si Menor"; break;
                        case  3: sTom = "Fá# Menor"; break;
                        case  4: sTom = "Dó# Menor"; break;
                        case  5: sTom = "Sol# Menor"; break;
                        case  6: sTom = "Ré# Menor"; break;
                        case  7: sTom = "Lá# Menor"; break;
                    }
                }
            }
        }
        return sTom;
    }
}