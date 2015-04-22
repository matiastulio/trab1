package br.unb.play;

import javax.sound.midi.*;

import java.awt.Dimension;
import java.io.*;

public class TocaMidi{
	
	
	private Sequencer sequenciador;
    private Sequence  sequencia   ;
    private boolean 		tocando = false, 
    						pausado = false, 
    						parado = false,
    						novoTocador = true;
    
    private static Receiver receptor; //utilizado para alterar o volume
    
    static final int MESSAGEM_ANDAMENTO  = 0x51;
    static final int FORMULA_DE_COMPASSO = 0x58;
    static final int MENSAGEM_TONALIDADE = 0x59;
    
    public TocaMidi(){
		try {
			sequenciador = MidiSystem.getSequencer(false);
			receptor = MidiSystem.getReceiver();
			if (sequenciador == null)
	              throw new MidiUnavailableException();
			sequenciador.getTransmitter().setReceiver(receptor);
			
		} catch (MidiUnavailableException e) {e.printStackTrace();}
    }
     
    public void tocar() throws MidiUnavailableException{
    	if (sequenciador== null)
    		throw new MidiUnavailableException();
    	else{
    		if(pausado){
    			sequenciador.start();
    			tocando = true;
    			pausado = false;
    		}else if(parado){
				sequenciador.setMicrosecondPosition(0);
				sequenciador.start();
				tocando = true;
				parado = false;
    		}
    	}
    }
    
    public void pausar(){
		if(parado)
			return;
		else if(tocando){
			sequenciador.stop();
			tocando = false;
			pausado = true;
		}
		else if (pausado){
			sequenciador.start();
			tocando = true;
			pausado = false;
		}
    }
    
    public void parar (){
        if(sequenciador != null && !parado &&!novoTocador){
            sequenciador.stop();
            sequenciador.setMicrosecondPosition(0);
            parado = true;
            pausado = false;
            tocando = false;
        }
    }
    
	public void vaiPara(int segundos){
		sequenciador.setMicrosecondPosition(segundos*1000000);
	}
	
    public Dimension getFormulaDeCompasso(){
        
    	int p=1;
        int q=1;
        Track[] trilha = sequencia.getTracks();
        Track t = trilha[0];
        for(int i=0; i<t.size(); i++){
            
        	MidiMessage m = t.get(i).getMessage();
            if(m instanceof MetaMessage) {
                    
            	if(((MetaMessage)m).getType()==FORMULA_DE_COMPASSO){
                    MetaMessage mm = (MetaMessage)m;
                    byte[] data = mm.getData();
                    p = data[0];
                    q = data[1];
                    return new Dimension(p,(int)Math.pow(2,q));
                }
            }
        }
        return new Dimension(p,q);
    }
    
	public String getTonalidade() throws InvalidMidiDataException
    {
	   Track[] trilha = sequencia.getTracks();
	   int MENSAGEM_TONALIDADE = 0x59;
	   String sTonalidade = "";
	   MidiMessage m;
	   MetaMessage mm;
	   if ( trilha != null ) {
		   for ( int i = 0; i < trilha.length; i++ ) {
			      Track track = trilha[ i ];
			      for ( int j = 0; j < track.size(); j++ ) {
				       m = track.get( j ).getMessage();
				       if(m instanceof MetaMessage && ((MetaMessage) m).getType() == MENSAGEM_TONALIDADE){
					    	mm  = (MetaMessage)m;
							byte[] data = mm.getData();
							byte tonalidade = data[0];
							byte maior = data[1];
	
							String smaior = "Maior";
							if (maior == 1)
								smaior = "Menor";
	
							if(smaior.equalsIgnoreCase("Maior")){
				                switch (tonalidade){
				                    case -7: sTonalidade = "Dob Maior" ; break;
				                    case -6: sTonalidade = "Solb Maior"; break;
				                    case -5: sTonalidade = "Reb Maior" ; break;
				                    case -4: sTonalidade = "Lab Maior" ; break;
				                    case -3: sTonalidade = "Mib Maior" ; break;
				                    case -2: sTonalidade = "Sib Maior" ; break;
				                    case -1: sTonalidade = "Fa Maior"  ; break;
				                    case  0: sTonalidade = "Do Maior"  ; break;
				                    case  1: sTonalidade = "Sol Maior" ; break;
				                    case  2: sTonalidade = "Re Maior"  ; break;
				                    case  3: sTonalidade = "La Maior"  ; break;
				                    case  4: sTonalidade = "Mi Maior"  ; break;
				                    case  5: sTonalidade = "Si Maior"  ; break;
				                    case  6: sTonalidade = "Fa# Maior" ; break;
				                    case  7: sTonalidade = "Do# Maior" ; break;
				                }
				            }
				
				            else if(smaior.equalsIgnoreCase("Menor")){
				                switch (tonalidade){
				                    case -7: sTonalidade = "Lab Menor" ; break;
				                    case -6: sTonalidade = "Mib Menor" ; break;
				                    case -5: sTonalidade = "Sib Menor" ; break;
				                    case -4: sTonalidade = "Fa Menor"  ; break;
				                    case -3: sTonalidade = "Do Menor"  ; break;
				                    case -2: sTonalidade = "Sol Menor" ; break;
				                    case -1: sTonalidade = "Re Menor"  ; break;
				                    case  0: sTonalidade = "La Menor"  ; break;
				                    case  1: sTonalidade = "Mi Menor"  ; break;
				                    case  2: sTonalidade = "Si Menor"  ; break;
				                    case  3: sTonalidade = "Fa# Menor" ; break;
				                    case  4: sTonalidade = "Do# Menor" ; break;
				                    case  5: sTonalidade = "Sol# Menor"; break;
				                    case  6: sTonalidade = "Re# Menor" ; break;
				                    case  7: sTonalidade = "La# Menor" ; break;
				                }
				            }
							return sTonalidade;
				       }
			     }
		   }
	}
	return sTonalidade;
    }

	public long criaSequencia(String local,int volume) throws InvalidMidiDataException, IOException, MidiUnavailableException{
		FileInputStream is = new FileInputStream(local);
		sequencia = MidiSystem.getSequence(is);
		if (sequenciador.isRunning())
			sequenciador.stop();
		
		sequenciador.open();
		sequenciador.setSequence(sequencia);
		mudaVolume(volume);
		tocando = false;
		pausado = false;
		parado = true;
		novoTocador = false;
		return sequencia.getMicrosecondLength();
	}
	
	public void mudaVolume(int novoVolume){
		ShortMessage volMessage = new ShortMessage();
		for (int i = 0; i < 16; i++) {
		  try {
		    volMessage.setMessage(ShortMessage.CONTROL_CHANGE, i, 7, novoVolume+1);
		  } catch (InvalidMidiDataException e) {}
		  receptor.send(volMessage, -1);
		}
	}

	public long posicaoAtualSequencia(){
		return sequenciador.getMicrosecondPosition()/1000000;
	}
	
	public String getAndamento() throws InvalidMidiDataException
	{
		Track[] trilha = sequencia.getTracks();
		MidiMessage m;
		MetaMessage mm;
		if ( trilha != null ) {
			for ( int i = 0; i < trilha.length; i++ ) {
				Track track = trilha[ i ];
				for ( int j = 0; j < track.size(); j++ ) {
				m = track.get( j ).getMessage();
				if(m instanceof MetaMessage && ((MetaMessage) m).getType() == MESSAGEM_ANDAMENTO){
					mm   = (MetaMessage)m;
					byte[]      data = mm.getData();

					byte primeiro = data[0];
					byte segundo  = data[1];
					byte terceiro = data[2];

					long microseg = (long)(primeiro*Math.pow(2, 16) +
					                       segundo *Math.pow(2,  8) +
					                       terceiro);   
					                                                       
					return Float.toString((int)(60000000.0/microseg));
				}
			}
		}
	}
	return "";
}
	
	
	public boolean tocando(){return tocando;}
	public boolean parado(){return parado;}
	public boolean pausado(){return pausado;}
	public boolean novoTocador(){return novoTocador;}
}
