package com.projetBomberman.view;


import com.projetBomberman.modele.BombermanGame;
import com.projetProgReseau.client.Client;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.io.PrintWriter;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;


public class ViewCommand extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private static final String REP_ICONE = "/icones";
	private static final String ICONE_RESTART = REP_ICONE + "/icon_restart.png";
	private static final String ICONE_RUN = REP_ICONE + "/icon_run.png";
	private static final String ICONE_STEP = REP_ICONE + "/icon_step.png";
	private static final String ICONE_PAUSE = REP_ICONE + "/icon_pause.png";

	private static final String MSG_DECO_CLIENT = "DECONNEXION";
	private static final String MSG_INIT_GAME = "INITIALISATION";
	private static final String MSG_PAUSE_GAME = "PAUSE";
	private static final String MSG_ETAPE_GAME = "ETAPE";
	private static final String MSG_DEBUT_GAME = "DEBUT";
	private static final String MSG_MODIF_TIME = "TIME";
	
	private static final int SPEED_MIN = 1;	
	private static final int SPEED_MAX = 10;
	private static final int INIT_TIME = 1000;
	private static final int VAL_SLIDER_INIT = 5;

	
	private JLabel labelTurn;
	private JSlider slider;
	private Client client;
	private PrintWriter sortie;
	
	public ViewCommand(Client client) {
		this.client = client;
		this.sortie = client.getSortie();
		this.createView();
	}
	
	private void createView() {
		this.setTitle("Commande");
		this.setSize(new Dimension(1100,300));
		Dimension windowSize = this.getSize();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Point centerPoint = ge.getCenterPoint();
		int dx = centerPoint.x - windowSize.width / 2 - 600;
		int dy = centerPoint.y - windowSize.height / 2 + 500;
		this.setLocation(dx,dy);

		JPanel panelPrincipal = new JPanel();
		panelPrincipal.setLayout(new GridLayout(2,1));
		JPanel panelButton = new JPanel();		
		panelButton.setLayout(new GridLayout(1,4));
		Icon icon_restart = new ImageIcon( getClass().getResource( ICONE_RESTART ));
		JButton buttonRestart = new JButton(icon_restart);
		Icon icon_run = new ImageIcon( getClass().getResource( ICONE_RUN ));
		JButton buttonRun = new JButton(icon_run);
		Icon icon_step = new ImageIcon( getClass().getResource( ICONE_STEP ));
		JButton buttonStep = new JButton(icon_step);
		Icon icon_stop = new ImageIcon( getClass().getResource( ICONE_PAUSE ));
		JButton buttonStop = new JButton(icon_stop);
		
		buttonRestart.addActionListener(evenement -> {
			this.sortie.println( MSG_INIT_GAME );
			buttonRestart.setEnabled(false);
			buttonRun.setEnabled(true);
			buttonStep.setEnabled(true);
			buttonStop.setEnabled(true);
		});
		
		buttonRun.addActionListener(evenement -> {
			this.sortie.println( MSG_DEBUT_GAME );
			buttonRestart.setEnabled(false);
			buttonRun.setEnabled(false);
			buttonStep.setEnabled(false);
			buttonStop.setEnabled(true);
		});
		
		buttonStep.addActionListener(evenement -> {
			this.sortie.println( MSG_ETAPE_GAME );
			buttonRestart.setEnabled(true);
			buttonRun.setEnabled(true);
			buttonStep.setEnabled(true);
			buttonStop.setEnabled(false);
		});
		
		buttonStop.addActionListener(evenement -> {
			this.sortie.println( MSG_PAUSE_GAME );
			buttonRestart.setEnabled(true);
			buttonRun.setEnabled(true);
			buttonStep.setEnabled(true);
			buttonStop.setEnabled(false);
		});
		
		buttonRestart.setEnabled(true);
		buttonRun.setEnabled(false);
		buttonStep.setEnabled(false);
		buttonStop.setEnabled(false);
		panelButton.add(buttonRestart);
		panelButton.add(buttonRun);
		panelButton.add(buttonStep);
		panelButton.add(buttonStop);
		JPanel panelSliderLabel = new JPanel();
		panelSliderLabel.setLayout(new GridLayout(1,2));
		JPanel panelSlider = new JPanel();
		panelSlider.setLayout(new GridLayout(2,2));
		JLabel labelSlider = new JLabel("Number of turns par second");
		labelSlider.setHorizontalAlignment(JLabel.CENTER);
		panelSlider.add(labelSlider);
		this.slider = new JSlider(JSlider.HORIZONTAL,SPEED_MIN,SPEED_MAX,SPEED_MIN);
		this.slider.setPaintTicks(true);
		this.slider.setPaintLabels(true);
		this.slider.setMinorTickSpacing(SPEED_MIN);
		this.slider.setMajorTickSpacing(SPEED_MIN);
		this.slider.setValue( VAL_SLIDER_INIT );

		this.slider.addChangeListener(arg0 -> 
			this.sortie.println( MSG_MODIF_TIME + INIT_TIME / slider.getValue() )
		);
		
		panelSlider.add(this.slider);
		
		JPanel panelLabel = new JPanel();
		panelLabel.setLayout(new GridLayout(2,1));
		
		this.labelTurn = new JLabel("Turn : 0");
		this.labelTurn.setHorizontalAlignment(JLabel.CENTER);
		this.labelTurn.setVerticalAlignment(JLabel.CENTER);
		
		JButton buttonClose = new JButton("Quitter le jeu");
		buttonClose.addActionListener(evenement -> {
			this.sortie.println( MSG_DECO_CLIENT );
			this.client.fermeture();
		});
		
		panelLabel.add(this.labelTurn);
		panelLabel.add(buttonClose);
		
		panelSliderLabel.add(panelSlider);
		panelSliderLabel.add(panelLabel);
		panelPrincipal.add(panelButton);
		panelPrincipal.add(panelSliderLabel);
		
		this.setContentPane(panelPrincipal);
		this.setVisible(true);
	}

	
	public void update(BombermanGame game) {
		this.labelTurn.setText("Turn : " + game.getTurn());
	}

}
