package br.unb.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import java.awt.Color;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.UIManager;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import java.awt.SystemColor;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

public class MainGui {

	private JFrame frmMidiPlayer;
	private JMenuItem mntmSair;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGui window = new MainGui();
					window.frmMidiPlayer.setVisible(true);
					//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public MainGui() {
		initialize();
		listeners();
	}

	private void listeners(){
		
		mntmSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmMidiPlayer.dispose();}
			});
	}
	private void initialize() {
		frmMidiPlayer = new JFrame();
		frmMidiPlayer.setResizable(false);
		JMenuBar menuBar = new JMenuBar();
		JMenu mnArquivo = new JMenu("Arquivo");
		JMenuItem mntmEscolherMidi = new JMenuItem("Escolher MIDI");
		mntmSair = new JMenuItem("Sair");
				
		frmMidiPlayer.setBounds(200, 200, 540, 300);
		frmMidiPlayer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMidiPlayer.setJMenuBar(menuBar);
		frmMidiPlayer.setTitle("MIDI Player");
		

		menuBar.add(mnArquivo);
		mnArquivo.add(mntmEscolherMidi);
		mnArquivo.add(mntmSair);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		frmMidiPlayer.getContentPane().setLayout(gridBagLayout);
		
		JLabel lbNomeArquivo = new JLabel("Nome do arquivo MIDI ");
		GridBagConstraints gbc_lbNomeArquivo = new GridBagConstraints();
		gbc_lbNomeArquivo.fill = GridBagConstraints.HORIZONTAL;
		gbc_lbNomeArquivo.gridwidth = 16;
		gbc_lbNomeArquivo.insets = new Insets(0, 0, 5, 0);
		gbc_lbNomeArquivo.gridx = 0;
		gbc_lbNomeArquivo.gridy = 1;
		frmMidiPlayer.getContentPane().add(lbNomeArquivo, gbc_lbNomeArquivo);
		
		JSlider sliderTempoMusica = new JSlider();
		GridBagConstraints gbc_sliderTempoMusica = new GridBagConstraints();
		gbc_sliderTempoMusica.gridheight = 3;
		gbc_sliderTempoMusica.gridwidth = 16;
		gbc_sliderTempoMusica.fill = GridBagConstraints.HORIZONTAL;
		gbc_sliderTempoMusica.insets = new Insets(0, 0, 5, 0);
		gbc_sliderTempoMusica.gridx = 0;
		gbc_sliderTempoMusica.gridy = 2;
		frmMidiPlayer.getContentPane().add(sliderTempoMusica, gbc_sliderTempoMusica);
		
		JLabel lblInicioTempo = new JLabel("23:59:59");
		GridBagConstraints gbc_lblInicioTempo = new GridBagConstraints();
		gbc_lblInicioTempo.anchor = GridBagConstraints.EAST;
		gbc_lblInicioTempo.insets = new Insets(0, 0, 5, 5);
		gbc_lblInicioTempo.gridx = 1;
		gbc_lblInicioTempo.gridy = 5;
		frmMidiPlayer.getContentPane().add(lblInicioTempo, gbc_lblInicioTempo);
		
		JLabel label = new JLabel("/");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 2;
		gbc_label.gridy = 5;
		frmMidiPlayer.getContentPane().add(label, gbc_label);
		
		JLabel lbFimTempo = new JLabel("23:59:59");
		GridBagConstraints gbc_lbFimTempo = new GridBagConstraints();
		gbc_lbFimTempo.insets = new Insets(0, 0, 5, 5);
		gbc_lbFimTempo.gridx = 3;
		gbc_lbFimTempo.gridy = 5;
		frmMidiPlayer.getContentPane().add(lbFimTempo, gbc_lbFimTempo);
		
		JButton btnParar = new JButton("\u220e");
		btnParar.setFont(UIManager.getFont("TextArea.font"));
		GridBagConstraints gbc_btnParar = new GridBagConstraints();
		gbc_btnParar.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnParar.insets = new Insets(0, 0, 5, 5);
		gbc_btnParar.gridx = 6;
		gbc_btnParar.gridy = 5;
		frmMidiPlayer.getContentPane().add(btnParar, gbc_btnParar);
		
		JButton btnTocar = new JButton("\u2023");
		btnTocar.setFont(UIManager.getFont("TextArea.font"));
		GridBagConstraints gbc_btnTocar = new GridBagConstraints();
		gbc_btnTocar.insets = new Insets(0, 0, 5, 5);
		gbc_btnTocar.gridx = 7;
		gbc_btnTocar.gridy = 5;
		frmMidiPlayer.getContentPane().add(btnTocar, gbc_btnTocar);
		
		JButton btnPausar = new JButton("\u275A\u275A");
		btnPausar.setFont(UIManager.getFont("TextArea.font"));
		GridBagConstraints gbc_btnPausar = new GridBagConstraints();
		gbc_btnPausar.insets = new Insets(0, 0, 5, 5);
		gbc_btnPausar.gridx = 8;
		gbc_btnPausar.gridy = 5;
		frmMidiPlayer.getContentPane().add(btnPausar, gbc_btnPausar);
		
		JLabel lblVolume = new JLabel("Volume");
		GridBagConstraints gbc_lblVolume = new GridBagConstraints();
		gbc_lblVolume.insets = new Insets(0, 0, 5, 5);
		gbc_lblVolume.gridx = 11;
		gbc_lblVolume.gridy = 5;
		frmMidiPlayer.getContentPane().add(lblVolume, gbc_lblVolume);
		
		JSlider slider = new JSlider();
		GridBagConstraints gbc_slider = new GridBagConstraints();
		gbc_slider.gridwidth = 4;
		gbc_slider.insets = new Insets(0, 0, 5, 0);
		gbc_slider.gridx = 12;
		gbc_slider.gridy = 5;
		frmMidiPlayer.getContentPane().add(slider, gbc_slider);
		
		JSeparator separatorDados = new JSeparator();
		separatorDados.setForeground(Color.WHITE);
		separatorDados.setBackground(Color.BLACK);
		GridBagConstraints gbc_separatorDados = new GridBagConstraints();
		gbc_separatorDados.gridwidth = 16;
		gbc_separatorDados.fill = GridBagConstraints.HORIZONTAL;
		gbc_separatorDados.insets = new Insets(0, 0, 5, 0);
		gbc_separatorDados.gridx = 0;
		gbc_separatorDados.gridy = 7;
		frmMidiPlayer.getContentPane().add(separatorDados, gbc_separatorDados);
		
		JPanel painelAux = new JPanel();
		painelAux.setBackground(Color.WHITE);
		painelAux.setLayout(null);
		GridBagConstraints gbc_painelAux = new GridBagConstraints();
		gbc_painelAux.fill = GridBagConstraints.BOTH;
		gbc_painelAux.gridheight = 6;
		gbc_painelAux.gridwidth = 16;
		gbc_painelAux.gridx = 0;
		gbc_painelAux.gridy = 8;
		frmMidiPlayer.getContentPane().add(painelAux, gbc_painelAux);
		
		JPanel painelInfo = new JPanel();
		painelInfo.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, new Color(0, 0, 255), new Color(0, 0, 255), new Color(0, 0, 255), new Color(0, 0, 255)));
		painelInfo.setBackground(new Color(64, 224, 208));
		painelInfo.setBounds(78, 35, 370, 128);
		painelAux.add(painelInfo);
		painelInfo.setLayout(null);
		
		JLabel lblFormulaDeCompasso = new JLabel("Formula de Compasso:");
		lblFormulaDeCompasso.setBounds(10, 45, 146, 14);
		painelInfo.add(lblFormulaDeCompasso);
		
		JLabel lblMetro = new JLabel("Metro:");
		lblMetro.setBounds(10, 95, 146, 14);
		painelInfo.add(lblMetro);
		
		JLabel lblAndamento = new JLabel("Andamento:");
		lblAndamento.setBounds(10, 70, 146, 14);
		painelInfo.add(lblAndamento);
		
		JLabel lblArmaduraDeTonalidade = new JLabel("Armadura de Tonalidade:");
		lblArmaduraDeTonalidade.setBounds(10, 20, 146, 14);
		painelInfo.add(lblArmaduraDeTonalidade);
		
		JLabel lblArqArmadura = new JLabel("do#");
		lblArqArmadura.setBounds(166, 20, 60, 14);
		painelInfo.add(lblArqArmadura);
		
		JLabel lblArqCompasso = new JLabel("3/4");
		lblArqCompasso.setBounds(166, 45, 46, 14);
		painelInfo.add(lblArqCompasso);
		
		JLabel lblArqBpm = new JLabel("50bpm");
		lblArqBpm.setBounds(166, 70, 46, 14);
		painelInfo.add(lblArqBpm);
		
		JLabel lblArqMetro = new JLabel("kkk");
		lblArqMetro.setBounds(166, 95, 46, 14);
		painelInfo.add(lblArqMetro);
		
	}
}
