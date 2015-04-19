package br.unb.play;

import javax.sound.midi.*;
import java.io.*;

public class TocaMidi{
	
    //variáveis de classe
    private Sequencer sequenciador;
    private Sequence sequencia;
    private int iniciar;
    private TocaMidi tempo;
    private long duracao;
    private String formulaCompasso;
    private float bpm;
    private String st;
    private double duracaoTick;
    private int numeroTrilhas;
    private String conjuntoInfo;
    private String nomearq;
    private static String pathAudios;
    private String tempoTotal;
    private static Receiver receptor; //utilizado para alterar o volume
    static final int MENSAGEM_TONALIDADE = 0x59;
    static final int FORMULA_DE_COMPASSO = 0x58;
    
    TocaMidi()
    {
        iniciar = 1;
        nomearq = "";
        pathAudios = "./audios/";
        receptor = null;
    }
    
    public void atualizarVolume(int volume) throws MidiUnavailableException
    {
        volume = Math.round((volume*127)/100);
        
        try
        {
            receptor = sequenciador.getTransmitters().iterator().next().getReceiver();
            sequenciador.getTransmitter().setReceiver(receptor);
        }
        catch(MidiUnavailableException e) { }

        ShortMessage mensagemDeVolume = new ShortMessage();
        
        for(int i = 0; i < ((numeroTrilhas < 16) ? 16 : numeroTrilhas); i++)
        {
            try
            {
                mensagemDeVolume.setMessage(ShortMessage.CONTROL_CHANGE, i, 7, volume);
                receptor.send(mensagemDeVolume, -1);
            }
            catch (InvalidMidiDataException e1) { }
        }
    }
    
    public String[] getDados ()
    {
        String[] Dados = new String[4];
        
        //preenche os dados
        Dados[0] = formulaCompasso;
        Dados[1] = st;
        Dados[2] = conjuntoInfo;
        Dados[3] = tempoTotal;
        
        
        return Dados;
    }
    
    public long getTempo()
    {
        
        return (sequenciador != null) ? sequenciador.getMicrosecondPosition() : -1;
    }
    
    public long getDuracao()
    {
        return (sequenciador != null) ? duracao : -1;
    }
    
    public void setArquivo(String arquivo)
    {
        if(!nomearq.equals(""))
        {
            parar();
        }
        
        nomearq = arquivo + ".mid";
       
        nomearq = pathAudios + nomearq;

        File      arqmidi = new File(nomearq);

        try
        {
            sequencia = MidiSystem.getSequence(arqmidi);                            

            montarDados(sequencia, nomearq);

            //prepara o sequenciador
            sequenciador = MidiSystem.getSequencer();
            sequenciador.setSequence(sequencia);
            sequenciador.open();
        }

        catch(MidiUnavailableException e1)
        {
            System.out.println("Erro1: "+"Dispositivo midi não disponível.");
        }
        
        catch(InvalidMidiDataException e2)
        {
            System.out.println("Erro2: "+"Erro nos dados midi.");
        }
        
        catch(IOException              e3)
        {
            System.out.println("Erro3: "+"O arquivo midi não foi encontrado.");
            System.out.println("Sintaxe: "+"java TocadorMidi arquivo.mid");
        }
    }
    
    public int tocar()
    {
        
        if (iniciar == 1)
        {
            iniciar = 0;
            
            try
            {
                sequenciador.start();
            }
            catch(NullPointerException e) { return -1; }
            
            return 1;
        }
        else
        {
            try
            {
                sequenciador.start();
            }
            catch(NullPointerException e) { return -1; }
            
            return 0;
        }
    }
    
    public void pausar()
    {
        try
        {
            sequenciador.stop();
        }
        catch(NullPointerException e) { }
    }
    
    public void parar ()
    {
        if(sequenciador != null)
        {
            sequenciador.stop();
            sequenciador.setMicrosecondPosition(0);
        }
    }
    
    public void irPara(String s)
    {
        
        sequenciador.stop();
                
        String h   = s.substring(0, 2);
        String m   = s.substring(3, 5);
        String seg = s.substring(6, 8);
        
        int horas    = Integer.parseInt(h);
        int minutos  = Integer.parseInt(m);
        int segundos = Integer.parseInt(seg);
        
        double novaPosicao = ((double) (horas*3600*1000000)) + ((double) (minutos*60*1000000)) + ((double) (segundos*1000000));
        
        novaPosicao = novaPosicao / (duracaoTick*1000000);
        
        sequenciador.setTickPosition((long) novaPosicao);
        
        tocar();
    }
    
    static void retardo(int miliseg)
    {
       try
       {
           Thread.sleep(miliseg);
       }
       catch(InterruptedException e) { }
   }

    private void montarDados(Sequence sequencia, String nome)
    {
       //--
       duracao              = sequencia.getMicrosecondLength()/1000000;
       int   resolucao      = sequencia.getResolution();
       long  totaltiques    = sequencia.getTickLength();
        
       duracaoTick          = (double)duracao/totaltiques;
       float durseminima    = (float)duracaoTick*resolucao;
       bpm                  = 60/durseminima; bpm = Math.round(bpm);
       int   totalseminimas = (int)(duracao/durseminima);
        
       int   horas          = (int) Math.floor(duracao/3600);
       int   minutos        = (int) Math.floor(duracao/60) - 60*horas;
       int   segundos       = (int) (duracao - 60*minutos - 3600*horas);
       
       String sHoras = "" + horas;
       String sMinutos = "" + minutos;
       String sSegundos = "" + segundos;
       
       if(horas < 10)
       {
           sHoras = "0" + horas;
       }
       
       if(minutos < 10)
       {
           sMinutos = "0" + minutos;
       }
       
       if(segundos < 10)
       {
           sSegundos = "0" + segundos;
       }
       
       tempoTotal = sHoras + ":" + sMinutos + ":" + sSegundos;
       
       Track[] trilhas = sequencia.getTracks();
       numeroTrilhas = trilhas.length;
       Track trilha =  trilhas[0];

       Par    fc =  getFormulaDeCompasso(trilha);
       st = "--------";
       
       try
       {
           st =  getTonalidade(trilha);
       }
       catch(Exception e){}
       
       formulaCompasso = fc.getX() +"/"+ (int)(Math.pow(2, fc.getY()));
       
       conjuntoInfo = "andamento                     = "+bpm+ " bpm\n" + "resolução                       = "+resolucao+" tiques (nº de divisões da semínima)\n" + "número de tiques           = "+totaltiques+"\n" + "duração do tique             = "+duracaoTick+" s\n" + "duração da semínima   = "+durseminima+" s\n" + "total de seminimas        = "+totalseminimas+"\n" + "Número de trilhas          = "+numeroTrilhas;
   }

    static Par getFormulaDeCompasso(Track trilha)
    {
        int p=1;
        int q=1;

        for(int i=0; i<trilha.size(); i++)
        {
            MidiMessage m = trilha.get(i).getMessage();
            if(m instanceof MetaMessage) 
            {
                    if(((MetaMessage)m).getType()==FORMULA_DE_COMPASSO)
                    {
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

    static private class Par
    {
        int x, y;

        Par (int x_, int y_)  
        {
            this.x = x_;
            this.y = y_;
        }

        int getX()
        {
            return x;
        }

        int getY()
        {
            return y;
        }
    }

    private static String getTonalidade(Track trilha) throws InvalidMidiDataException
    {
        String stonalidade = "";
        for(int i=0; i<trilha.size(); i++)
        {
            MidiMessage m = trilha.get(i).getMessage();

            if(((MetaMessage)m).getType() == MENSAGEM_TONALIDADE)    
            {
                MetaMessage mm        = (MetaMessage)m;
                byte[]     data       = mm.getData();
                byte       tonalidade = data[0];
                byte       maior      = data[1];

                String       smaior = "Maior";
                if(maior==1) smaior = "Menor";

                if(smaior.equalsIgnoreCase("Maior"))
                {
                    switch (tonalidade)
                    {
                        case -7: stonalidade = "Dób Maior"; break;
                        case -6: stonalidade = "Solb Maior"; break;
                        case -5: stonalidade = "Réb Maior"; break;
                        case -4: stonalidade = "Láb Maior"; break;
                        case -3: stonalidade = "Mib Maior"; break;
                        case -2: stonalidade = "Sib Maior"; break;
                        case -1: stonalidade = "Fá Maior"; break;
                        case  0: stonalidade = "Dó Maior"; break;
                        case  1: stonalidade = "Sol Maior"; break;
                        case  2: stonalidade = "Ré Maior"; break;
                        case  3: stonalidade = "Lá Maior"; break;
                        case  4: stonalidade = "Mi Maior"; break;
                        case  5: stonalidade = "Si Maior"; break;
                        case  6: stonalidade = "Fá# Maior"; break;
                        case  7: stonalidade = "Dó# Maior"; break;
                    }
                }
                else if(smaior.equalsIgnoreCase("Menor"))
                {
                    switch (tonalidade)
                    {
                        case -7: stonalidade = "Láb Menor"; break;
                        case -6: stonalidade = "Mib Menor"; break;
                        case -5: stonalidade = "Sib Menor"; break;
                        case -4: stonalidade = "Fá Menor"; break;
                        case -3: stonalidade = "Dó Menor"; break;
                        case -2: stonalidade = "Sol Menor"; break;
                        case -1: stonalidade = "Ré Menor"; break;
                        case  0: stonalidade = "Lá Menor"; break;
                        case  1: stonalidade = "Mi Menor"; break;
                        case  2: stonalidade = "Si Menor"; break;
                        case  3: stonalidade = "Fá# Menor"; break;
                        case  4: stonalidade = "Dó# Menor"; break;
                        case  5: stonalidade = "Sol# Menor"; break;
                        case  6: stonalidade = "Ré# Menor"; break;
                        case  7: stonalidade = "Lá# Menor"; break;
                    }
                }
            }
        }
        return stonalidade;
    }
}