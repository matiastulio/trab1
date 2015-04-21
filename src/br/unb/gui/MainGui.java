package br.unb.gui;

import java.awt.EventQueue;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.JSeparator;

import java.awt.Color;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import br.unb.play.TocaMidi;

public class MainGui {

	protected static TocaMidi tocaMidi;
	
	private static MainGui 	   window;
	private Thread			   atualizador;
	protected long			   tempoTotal,tempoAtual;
	private JFrame             frmMidiPlayer;
	private JMenuItem          mntmSair,mntmEscolherMidi;
	private JMenuBar           menuBar;
	private JMenu              mnArquivo;
	private GridBagLayout      gridBagLayout;
	private JPanel             painelAux,painelInfo;
	private JSeparator         separadorDados;
	private String             caminhoArquivo;
	protected JSlider          sliderTempoMusica,sliderVolume;
	private JButton            btnParar,btnTocar,btnPausar;
	private GridBagConstraints gbc_sliderTempoMusica,gbc_lblNomeArquivo,gbc_lblInicioTempo,gbc_lblBarraTime,
	                           gbc_lblFimTempo,gbc_btnParar,gbc_btnTocar,
							   gbc_btnPausar,gbc_lblVolume,gbc_sliderVolume,gbc_separadorDados,gbc_painelAux;
	private JLabel             lblNomeArquivo,lblBarraTime,lblFimTempo,lblVolume,
	                           lblFormulaDeCompasso,lblMetro,lblAndamento,lblArmaduraDeTonalidade,
							   lblArqArmadura,lblArqCompasso,lblArqBpm,lblArqMetro;
	protected JLabel 			   lblInicioTempo;
	

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new MainGui();
					window.frmMidiPlayer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public MainGui() {
		inicializa();
		setaOLayout();
		listeners();
	}
	
	private void inicializa() {
		//paineis, frames e layout
		tocaMidi = new TocaMidi();
		frmMidiPlayer = new JFrame();
		painelInfo = 	new JPanel();
		painelAux = 	new JPanel();
		gridBagLayout = new GridBagLayout();
		
		//menu
		menuBar = 			new JMenuBar();
		mnArquivo = 		new JMenu("Arquivo");
		mntmSair = 			new JMenuItem("Sair");
		mntmEscolherMidi = 	new JMenuItem("Abrir");
		
		//sliders 
		sliderTempoMusica = new JSlider();
		sliderVolume = 		new JSlider(0,126,75);
		
		//botoes
		btnParar = 	new JButton("\u220e");
		btnTocar = 	new JButton("\u2023");
		btnPausar = new JButton("\u275A\u275A");
		
		//GridBagConstraints
		gbc_sliderTempoMusica = new GridBagConstraints();
		gbc_lblInicioTempo = 	new GridBagConstraints();
		gbc_lblBarraTime = 		new GridBagConstraints();
		gbc_lblFimTempo =		new GridBagConstraints();
		gbc_btnParar = 			new GridBagConstraints();
		gbc_btnTocar = 			new GridBagConstraints();
		gbc_btnPausar = 		new GridBagConstraints();
		gbc_lblVolume = 		new GridBagConstraints();
		gbc_sliderVolume = 		new GridBagConstraints();
		gbc_separadorDados = 	new GridBagConstraints();
		gbc_painelAux = 		new GridBagConstraints();
		gbc_lblNomeArquivo = 	new GridBagConstraints();
		
		//labels
		lblFimTempo               =	new JLabel("        "               );
		lblInicioTempo            = new JLabel("        "               );
		lblNomeArquivo            = new JLabel("  "  					);
		lblBarraTime              = new JLabel("/"                      );
		lblVolume                 = new JLabel("Volume:"                 );
		lblFormulaDeCompasso      = new JLabel("Formula de Compasso:"   );
		lblMetro  				  = new JLabel("Metro:"                 );
		lblAndamento              = new JLabel("Andamento:"             );
		lblArmaduraDeTonalidade   =	new JLabel("Armadura de Tonalidade:");
		
		//labels com os dados vindos dos arquivos, valores iniciais de exemplo. o Certo � eles estarem zerados ao iniciar o programa
		lblArqCompasso = 			new JLabel("  ");
		lblArqBpm = 				new JLabel("  ");
		lblArqMetro = 				new JLabel("  ");
		lblArqArmadura = 			new JLabel("  ");
		

		separadorDados = new JSeparator();
		
		tempoAtual = tempoTotal = 0;
	}


	private void setaOLayout() {
		
		//configuracao do frame
		frmMidiPlayer.setResizable(false);
		frmMidiPlayer.setBounds(200, 200, 540, 300);
		frmMidiPlayer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMidiPlayer.setJMenuBar(menuBar);
		frmMidiPlayer.setTitle("MIDI Player");
		
		//configuracao do menu de opcoes
		menuBar.add(mnArquivo);
		mnArquivo.add(mntmEscolherMidi);
		mnArquivo.add(mntmSair);
		
		// configuracao do layout em gridbag, posicionamento de cada elemento do layout
		gridBagLayout.columnWidths  = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights    = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights    = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		
		gbc_lblNomeArquivo.fill      = GridBagConstraints.HORIZONTAL;
		gbc_lblNomeArquivo.gridwidth = 16;
		gbc_lblNomeArquivo.insets    = new Insets(0, 0, 5, 0);
		gbc_lblNomeArquivo.gridx     = 0;
		gbc_lblNomeArquivo.gridy     = 1;
		
		sliderTempoMusica.setValue(0);
		gbc_sliderTempoMusica.gridheight = 3;
		gbc_sliderTempoMusica.gridwidth  = 16;
		gbc_sliderTempoMusica.fill       = GridBagConstraints.HORIZONTAL;
		gbc_sliderTempoMusica.insets     = new Insets(0, 0, 5, 0);
		gbc_sliderTempoMusica.gridx      = 0;
		gbc_sliderTempoMusica.gridy      = 2;
		
		gbc_lblInicioTempo.anchor = GridBagConstraints.EAST;
		gbc_lblInicioTempo.insets = new Insets(0, 0, 5, 5);
		gbc_lblInicioTempo.gridx  = 1;
		gbc_lblInicioTempo.gridy  = 5;
		
		gbc_lblBarraTime.insets = new Insets(0, 0, 5, 5);
		gbc_lblBarraTime.gridx = 2;
		gbc_lblBarraTime.gridy = 5;
		
		gbc_lblFimTempo.insets = new Insets(0, 0, 5, 5);
		gbc_lblFimTempo.gridx  = 3;
		gbc_lblFimTempo.gridy  = 5;
	
		btnParar.setFont(UIManager.getFont("TextArea.font"));
		gbc_btnParar.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnParar.insets = new Insets(0, 0, 5, 5);
		gbc_btnParar.gridx = 6;
		gbc_btnParar.gridy = 5;
		
		btnTocar.setFont(UIManager.getFont("TextArea.font"));
		gbc_btnTocar.insets = new Insets(0, 0, 5, 5);
		gbc_btnTocar.gridx = 7;
		gbc_btnTocar.gridy = 5;
		
		btnPausar.setFont(UIManager.getFont("TextArea.font"));
		gbc_btnPausar.insets = new Insets(0, 0, 5, 5);
		gbc_btnPausar.gridx  = 8;
		gbc_btnPausar.gridy  = 5;
		
		gbc_lblVolume.insets = new Insets(0, 0, 5, 5);
		gbc_lblVolume.fill   = GridBagConstraints.HORIZONTAL;
		gbc_lblVolume.gridx  = 11;
		gbc_lblVolume.gridy  = 5;
		
		gbc_sliderVolume.gridwidth = 4;
		gbc_sliderVolume.insets    = new Insets(0, 0, 5, 0);
		gbc_sliderVolume.gridx     = 12;
		gbc_sliderVolume.gridy     = 5;
		
		separadorDados.setForeground(Color.WHITE);
		separadorDados.setBackground(Color.BLACK);
		gbc_separadorDados.gridwidth = 16;
		gbc_separadorDados.fill      = GridBagConstraints.HORIZONTAL;
		gbc_separadorDados.insets    = new Insets(0, 0, 5, 0);
		gbc_separadorDados.gridx     = 0;
		gbc_separadorDados.gridy     = 7;
		
		
		painelAux.setBackground(Color.WHITE);
		painelAux.setLayout(null);
		gbc_painelAux.fill       = GridBagConstraints.BOTH;
		gbc_painelAux.gridheight = 6;
		gbc_painelAux.gridwidth  = 16;
		gbc_painelAux.gridx      = 0;
		gbc_painelAux.gridy      = 8;
		

		//adiciona tudo que foi criado e configurado ao frame
		frmMidiPlayer.getContentPane().setLayout(gridBagLayout);
		
		frmMidiPlayer.getContentPane().add(painelAux        , gbc_painelAux        );
		frmMidiPlayer.getContentPane().add(separadorDados   , gbc_separadorDados   );
		frmMidiPlayer.getContentPane().add(sliderVolume     , gbc_sliderVolume     );
		frmMidiPlayer.getContentPane().add(lblVolume        , gbc_lblVolume        );
		frmMidiPlayer.getContentPane().add(btnPausar        , gbc_btnPausar        );
		frmMidiPlayer.getContentPane().add(btnTocar         , gbc_btnTocar         );
		frmMidiPlayer.getContentPane().add(btnParar         , gbc_btnParar         );
		frmMidiPlayer.getContentPane().add(lblFimTempo      , gbc_lblFimTempo      );
		frmMidiPlayer.getContentPane().add(lblBarraTime     , gbc_lblBarraTime     );
		frmMidiPlayer.getContentPane().add(lblInicioTempo   , gbc_lblInicioTempo   );
		frmMidiPlayer.getContentPane().add(sliderTempoMusica, gbc_sliderTempoMusica);
		frmMidiPlayer.getContentPane().add(lblNomeArquivo   , gbc_lblNomeArquivo   );
		
		//********config do painel de informacao********//
		painelInfo.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, new Color(0, 0, 255), new Color(0, 0, 255), new Color(0, 0, 255), new Color(0, 0, 255)));
		painelInfo.setBackground(new Color(64, 224, 208));
		painelInfo.setBounds(78, 35, 370, 128);
		painelAux.add(painelInfo);
		painelInfo.setLayout(null);
		
		
		lblFormulaDeCompasso.setBounds(10, 45, 146, 14);
		lblMetro.setBounds(10, 95, 146, 14);
		lblAndamento.setBounds(10, 70, 146, 14);
		lblArmaduraDeTonalidade.setBounds(10, 20, 146, 14);
		lblArqArmadura.setBounds(166, 20, 60, 14);
		lblArqCompasso.setBounds(166, 45, 46, 14);
		lblArqBpm.setBounds(166, 70, 46, 14);
		lblArqMetro.setBounds(166, 95, 46, 14);
		
		painelInfo.add(lblFormulaDeCompasso);
		painelInfo.add(lblArqMetro);
		painelInfo.add(lblMetro);
		painelInfo.add(lblArmaduraDeTonalidade);
		painelInfo.add(lblArqArmadura);
		painelInfo.add(lblArqCompasso);
		painelInfo.add(lblArqBpm);
		painelInfo.add(lblAndamento);
		
	}


	private void listeners(){
		
		//listener do botao sair do menu
		mntmSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmMidiPlayer.dispose();}
			});
		
		//listener do botao abrir do menu
		mntmEscolherMidi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand() == "Abrir"){
					caminhoArquivo = abrirArquivo();
					if (caminhoArquivo != ""){
						setupNovoMidi(caminhoArquivo);
					}
				}
			}
		});
		
		//listener do botao parar
		btnParar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!tocaMidi.novoTocador()){
					atualizador.interrupt();
		        	tocaMidi.parar();
					tempoAtual = 0;
					lblInicioTempo.setText(toFormatoDeHora(tempoAtual));
					sliderTempoMusica.setValue(0);
				}

			}
		});
		
		
		//listener do botao tocar
		btnTocar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(!tocaMidi.tocando()){
						tocaMidi.tocar();
						trabalhadorTempo();
					}
				} catch (MidiUnavailableException e1) {}
			}
		});
		
		
		//listener do botao pausar
		btnPausar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tocaMidi.tocando()){
					tocaMidi.pausar();
					atualizador.interrupt();
				}else if(tocaMidi.pausado()){
					try {
						tocaMidi.tocar();
						trabalhadorTempo();
					} catch (MidiUnavailableException e1) {}
				}
				
			}
		});
		
		//listener do slider de volume
		sliderVolume.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				tocaMidi.mudaVolume(sliderVolume.getValue());
			}
		});
		
		sliderTempoMusica.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}			
			@Override
			public void mousePressed(MouseEvent e) {}			
			@Override
			public void mouseExited(MouseEvent e) {}			
			@Override
			public void mouseEntered(MouseEvent e) {}			
			@Override
			public void mouseClicked(MouseEvent e) {
				int mouseX = e.getX();
				int tempoSelecionado = (int) Math.round(( (double) mouseX/ (double) sliderTempoMusica.getWidth())*
												sliderTempoMusica.getMaximum());
				if(!tocaMidi.novoTocador()){
					atualizador.interrupt();
					tempoAtual = tempoSelecionado;
					sliderTempoMusica.setValue(tempoSelecionado);
					tocaMidi.vaiPara(tempoSelecionado);
					trabalhadorTempo();
				}
			}
		});
	}
	
	
	private String abrirArquivo(){
		 String caminhoArquivo = "";
		  
		  JFileChooser arquivo = new JFileChooser(".");

		  UIManager.put("FileChooser.openDialogTitleText"          , "Abrir"                      );
		  UIManager.put("FileChooser.lookInLabelText"              , "Consultar em"               );
		  UIManager.put("FileChooser.openButtonText"               , "Abrir"                      );
		  UIManager.put("FileChooser.cancelButtonText"             , "Cancelar"                   );                 
		  UIManager.put("FileChooser.fileNameLabelText"            , "Nome do Arquivo"            );
		  UIManager.put("FileChooser.filesOfTypeLabelText"         , "Tipo de Arquivo"            );
		  UIManager.put("FileChooser.openButtonToolTipText"        , "Abrir o Arquivo Selecionado");
		  UIManager.put("FileChooser.cancelButtonToolTipText"      , "Cancelar"                   );
		  UIManager.put("FileChooser.fileNameHeaderText"           , "Nome"                       );
		  UIManager.put("FileChooser.upFolderToolTipText"          , "Subir um Nivel"             );
		  UIManager.put("FileChooser.homeFolderToolTipText"        ,"Area de Trabalho"            );
		  UIManager.put("FileChooser.newFolderToolTipText"         ,"Criar Nova Pasta"            );
		  UIManager.put("FileChooser.listViewButtonToolTipText"    ,"Lista"                       );
		  UIManager.put("FileChooser.newFolderButtonText"          ,"Criar Nova Pasta"            );
		  UIManager.put("FileChooser.renameFileButtonText"         , "Renomear"                   );
		  UIManager.put("FileChooser.deleteFileButtonText"         , "Deletar"                    );
		  UIManager.put("FileChooser.filterLabelText"              , "Tipo"                       );
		  UIManager.put("FileChooser.detailsViewButtonToolTipText" , "Detalhes"                   );
		  UIManager.put("FileChooser.fileSizeHeaderText"           ,"Tamanho"                     );
		  UIManager.put("FileChooser.fileDateHeaderText"           , "Data de Modificacao"        );
		  UIManager.put("FileChooser.acceptAllFileFilterText"      , "Todos os Arquivos"          );
		  
		  SwingUtilities.updateComponentTreeUI(arquivo);

		  int retorno = arquivo.showOpenDialog(null);

		  if(retorno == JFileChooser.APPROVE_OPTION)
		  {
			  caminhoArquivo = arquivo.getSelectedFile().getAbsolutePath();
			  return caminhoArquivo;
		  }
		  return "";
	}
	
	
	public void setupNovoMidi(String caminhoArquivo){
		lblNomeArquivo.setText(caminhoArquivo);
		try {
			if(tocaMidi.tocando())
				atualizador.interrupt();
			
			tempoTotal = tocaMidi.criaSequencia(caminhoArquivo,sliderVolume.getValue());
			tempoTotal = tempoTotal/1000000;
			tempoAtual = 0;
			sliderTempoMusica.setMaximum((int) tempoTotal);
			sliderTempoMusica.setValue(0);
			lblFimTempo.setText(toFormatoDeHora(tempoTotal));
			lblInicioTempo.setText(toFormatoDeHora(tempoAtual));
			lblArqBpm.setText(tocaMidi.getAndamento()+" BPM");
			lblArqArmadura.setText(tocaMidi.getTonalidade());
			Dimension d = tocaMidi.getFormulaDeCompasso();
			lblArqCompasso.setText((int)d.getWidth()+"/"+(int)d.getHeight());
			lblArqMetro.setText("1/"+d.height);
		} 
		catch (InvalidMidiDataException e1) {} 
		catch (IOException e1) {} 
		catch (MidiUnavailableException e1) {}
	}

	public String toFormatoDeHora(long seconds){
		return String.format("%d:%d:%d",
				TimeUnit.SECONDS.toHours(seconds), 
			    TimeUnit.SECONDS.toMinutes(seconds),
			    seconds%60);
	}
	
	private void trabalhadorTempo() {
		atualizador = new Thread() {
					private boolean parar = false;
					
					@Override
					public void interrupt() {
						parar = true;
						super.interrupt();
					}
		
		
					public void run() {
					while(!parar && tempoAtual < tempoTotal){
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
		
						}
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								if(tempoAtual+1 == tempoTotal){
									sliderTempoMusica.setValue(0);
									interrupt();
								}
								sliderTempoMusica.setValue((int) tempoAtual);
								lblInicioTempo.setText(toFormatoDeHora(tempoAtual));
							}
						});
						tempoAtual++;
					}
				}
				
			};
		atualizador.start();	
	}
}
