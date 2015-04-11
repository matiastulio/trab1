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
		JMenuBar menuBar = new JMenuBar();
		JMenu mnArquivo = new JMenu("Arquivo");
		JMenuItem mntmEscolherMidi = new JMenuItem("Escolher MIDI");
		mntmSair = new JMenuItem("Sair");
				
		frmMidiPlayer.setBounds(100, 100, 650, 500);
		frmMidiPlayer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMidiPlayer.setJMenuBar(menuBar);
		frmMidiPlayer.setTitle("MIDI Player");
		

		menuBar.add(mnArquivo);
		mnArquivo.add(mntmEscolherMidi);
		mnArquivo.add(mntmSair);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		frmMidiPlayer.getContentPane().setLayout(gridBagLayout);
		
		JLabel lbNomeArquivo = new JLabel("Nome do arquivo MIDI");
		GridBagConstraints gbc_lbNomeArquivo = new GridBagConstraints();
		gbc_lbNomeArquivo.anchor = GridBagConstraints.WEST;
		gbc_lbNomeArquivo.gridwidth = 15;
		gbc_lbNomeArquivo.insets = new Insets(0, 0, 5, 0);
		gbc_lbNomeArquivo.gridx = 0;
		gbc_lbNomeArquivo.gridy = 1;
		frmMidiPlayer.getContentPane().add(lbNomeArquivo, gbc_lbNomeArquivo);
		
		JSlider sliderTempoMusica = new JSlider();
		GridBagConstraints gbc_sliderTempoMusica = new GridBagConstraints();
		gbc_sliderTempoMusica.gridheight = 3;
		gbc_sliderTempoMusica.gridwidth = 15;
		gbc_sliderTempoMusica.fill = GridBagConstraints.HORIZONTAL;
		gbc_sliderTempoMusica.insets = new Insets(0, 0, 5, 0);
		gbc_sliderTempoMusica.gridx = 0;
		gbc_sliderTempoMusica.gridy = 2;
		frmMidiPlayer.getContentPane().add(sliderTempoMusica, gbc_sliderTempoMusica);
		
		JLabel lblInicioTempo = new JLabel("23:59:59");
		GridBagConstraints gbc_lblInicioTempo = new GridBagConstraints();
		gbc_lblInicioTempo.insets = new Insets(0, 0, 5, 5);
		gbc_lblInicioTempo.gridx = 1;
		gbc_lblInicioTempo.gridy = 5;
		frmMidiPlayer.getContentPane().add(lblInicioTempo, gbc_lblInicioTempo);
		
		JLabel lbFimTempo = new JLabel("23:59:59");
		GridBagConstraints gbc_lbFimTempo = new GridBagConstraints();
		gbc_lbFimTempo.insets = new Insets(0, 0, 5, 5);
		gbc_lbFimTempo.gridx = 2;
		gbc_lbFimTempo.gridy = 5;
		frmMidiPlayer.getContentPane().add(lbFimTempo, gbc_lbFimTempo);
		
		JButton btnParar = new JButton("Stop");
		GridBagConstraints gbc_btnParar = new GridBagConstraints();
		gbc_btnParar.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnParar.insets = new Insets(0, 0, 5, 5);
		gbc_btnParar.gridx = 5;
		gbc_btnParar.gridy = 5;
		frmMidiPlayer.getContentPane().add(btnParar, gbc_btnParar);
		
		JButton btnTocar = new JButton("Play");
		GridBagConstraints gbc_btnTocar = new GridBagConstraints();
		gbc_btnTocar.insets = new Insets(0, 0, 5, 5);
		gbc_btnTocar.gridx = 6;
		gbc_btnTocar.gridy = 5;
		frmMidiPlayer.getContentPane().add(btnTocar, gbc_btnTocar);
		
		JButton btnPausar = new JButton("Pause");
		GridBagConstraints gbc_btnPausar = new GridBagConstraints();
		gbc_btnPausar.insets = new Insets(0, 0, 5, 5);
		gbc_btnPausar.gridx = 7;
		gbc_btnPausar.gridy = 5;
		frmMidiPlayer.getContentPane().add(btnPausar, gbc_btnPausar);
		
		JLabel lblVolume = new JLabel("Volume");
		GridBagConstraints gbc_lblVolume = new GridBagConstraints();
		gbc_lblVolume.insets = new Insets(0, 0, 5, 5);
		gbc_lblVolume.gridx = 10;
		gbc_lblVolume.gridy = 5;
		frmMidiPlayer.getContentPane().add(lblVolume, gbc_lblVolume);
		
		JSlider slider = new JSlider();
		GridBagConstraints gbc_slider = new GridBagConstraints();
		gbc_slider.gridwidth = 4;
		gbc_slider.insets = new Insets(0, 0, 5, 0);
		gbc_slider.gridx = 11;
		gbc_slider.gridy = 5;
		frmMidiPlayer.getContentPane().add(slider, gbc_slider);
		
		JSeparator separatorDados = new JSeparator();
		separatorDados.setForeground(Color.WHITE);
		separatorDados.setBackground(Color.BLACK);
		GridBagConstraints gbc_separatorDados = new GridBagConstraints();
		gbc_separatorDados.gridwidth = 15;
		gbc_separatorDados.fill = GridBagConstraints.HORIZONTAL;
		gbc_separatorDados.insets = new Insets(0, 0, 5, 0);
		gbc_separatorDados.gridx = 0;
		gbc_separatorDados.gridy = 7;
		frmMidiPlayer.getContentPane().add(separatorDados, gbc_separatorDados);
		
	}
}
