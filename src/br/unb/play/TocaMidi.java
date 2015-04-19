package br.unb.play;

import javax.sound.midi.*;
import java.io.*;

public class TocaMidi
{      
	public static void main(String args[]) 
	{        
		String            nomearq = "mclarineta1.mid";
		if(args.length>0) nomearq = args[0];
		 
		File      arqmidi = new File(nomearq);
		Sequencer sequenciador;
		Sequence  sequencia;
		
		try
		{  
			sequencia = MidiSystem.getSequence(arqmidi);
			exibirDados(sequencia, nomearq);
			
			//---preparar o sequenciador: ------------------
			sequenciador = MidiSystem.getSequencer();
			sequenciador.setSequence(sequencia);
			sequenciador.open();             
			retardo(500);
			sequenciador.start();  //--aqui começa a tocar.
			//-----------------------------------------------
			
			
			
			//-- O laço abaixo verifica (a cada 1 segundo) se a execução já está
			//-- completada. Quando estiver, então o sequenciador será 'fechado';
			
			int i=0;
			//System.out.println("Instante em segundos: ");
						
			while(true)
			{ 
				if(sequenciador.isRunning())
				{ 
				  retardo(1000); 
				
				  //----exibir o instante real em segundos:---------
				  long  posicao = sequenciador.getMicrosecondPosition();
				  int   seg     = Math.round(posicao*0.000001f);
				  //System.out.print(seg + " ");
				  i++;
				  if(i==20) { System.out.println("");
				              i=0;
				            }            
				  //------------------------------------------------             
				} 
				else break;        
			}      
			
			System.out.println("");
			System.out.println("* * * \n");
			
			retardo(1000); 
			sequenciador.stop();
			sequenciador.close();
		} 
		
		catch(MidiUnavailableException e1) { System.out.println(e1+" : Dispositivo midi não disponível.");}
		catch(InvalidMidiDataException e2) { System.out.println(e2+" : Erro nos dados midi."); }
		catch(IOException              e3) { System.out.println(e3+" : O arquivo midi não foi encontrado."); 
                                                     System.out.println("Sintaxe: "+"java TocaMidi arquivo.mid"); 		                                   
		                                   }
	}  
	
	
	static void retardo(int miliseg)
	{  
		try { Thread.sleep(miliseg);
		    }
		catch(InterruptedException e) { }
	}
	
	
	
	static void exibirDados(Sequence sequencia, String nome)
	{  
		long duracao     = sequencia.getMicrosecondLength()/1000000;
		int  resolucao   = sequencia.getResolution();
		long totaltiques = sequencia.getTickLength();
		
		
		float durtique       = (float)duracao/totaltiques;
		float durseminima    = durtique*resolucao;
		float bpm            = 60/durseminima;
		int   totalseminimas = (int)(duracao/durseminima);
		
		System.out.println("");
		System.out.println("------------------------------------------");
		System.out.println("--------Arquivo Midi: " + nome + " ----");
		System.out.println("------------------------------------------");
		System.out.println("resolução            = "+resolucao+" tiques   (número de divisões da semínima)");
		System.out.println("duração              = "+duracao+" s");
		System.out.println("número de tiques     = "+totaltiques+" ");
		System.out.println("duração do tique     = "+durtique+" s");
		System.out.println("duração da semínima  = "+durseminima+" s");
		System.out.println("total de seminimas   = "+totalseminimas);
		System.out.println("andamento            = "+Math.round(bpm)+ " bpm");
		System.out.println("---");
		
		System.out.println("");
	}

}   //--fim da classe TocaMidi


//----------------------------
//"italiano.mid";
//"paisagemintervalar.mid";
//"pastoral.mid";
//"fuga6.mid";
//"mclarineta1.mid";
//"mpiano2.mid";