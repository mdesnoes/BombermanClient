package com.projetBomberman.view;


import com.projetBomberman.modele.BombermanGame;
import com.projetProgReseau.client.Client;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.io.File;
import java.io.FilenameFilter;
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
	
	private static final int SPEED_MIN = 1;		// Vitesse minimum des tours
	private static final int SPEED_MAX = 10;		// Vitesse maximum des tours
	private static final String EXT_LAYOUT = ".lay";
	private static final String REP_ICONES = "./icones";
	private static final int INIT_TIME = 1000;

	
	private JLabel labelTurn;					// Le label qui affiche le tour courant
	private JSlider slider;					// Le slider qui affiche la vitesse des tours en seconde
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
		Icon icon_restart = new ImageIcon(REP_ICONES + "/icon_restart.png");
		JButton buttonRestart = new JButton(icon_restart);
		Icon icon_run = new ImageIcon(REP_ICONES + "/icon_run.png");
		JButton buttonRun = new JButton(icon_run);
		Icon icon_step = new ImageIcon(REP_ICONES + "/icon_step.png");
		JButton buttonStep = new JButton(icon_step);
		Icon icon_stop = new ImageIcon(REP_ICONES + "/icon_pause.png");
		JButton buttonStop = new JButton(icon_stop);
		
		//Permet d'appeler le controleur afin d'initialiser le jeu
		buttonRestart.addActionListener(evenement -> {
			this.sortie.println("[CLIENT : " + this.client.getNom() + "] INITIALISATION DE LA PARTIE");
			buttonRestart.setEnabled(false);
			buttonRun.setEnabled(true);
			buttonStep.setEnabled(true);
			buttonStop.setEnabled(true);
		});
		
		//Permet d'appeler le controleur afin de demarrer le jeu
		buttonRun.addActionListener(evenement -> {
			this.sortie.println("[CLIENT : " + this.client.getNom() + "] DEBUT DE LA PARTIE");
			buttonRestart.setEnabled(false);
			buttonRun.setEnabled(false);
			buttonStep.setEnabled(false);
			buttonStop.setEnabled(true);
		});
		
		//Permet d'appeler le controleur afin de passer au tour suivant
		buttonStep.addActionListener(evenement -> {
			this.sortie.println("[CLIENT : " + this.client.getNom() + "] AVANCEMENT D'UNE ETAPE DE LA PARTIE");
			buttonRestart.setEnabled(true);
			buttonRun.setEnabled(true);
			buttonStep.setEnabled(true);
			buttonStop.setEnabled(false);
		});
		
		//Permet d'appeler le controleur afin de faire une pause
		buttonStop.addActionListener(evenement -> {
			this.sortie.println("[CLIENT : " + this.client.getNom() + "] PARTIE EN PAUSE");
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
		this.slider.setValue((int)this.client.getGame().getTime()/1000);

		//Permet d'appeler le constructeur afin de modifier le temps des tours
		this.slider.addChangeListener(arg0 -> 
			this.client.getGame().setTime(INIT_TIME / slider.getValue())
		);
		
		panelSlider.add(this.slider);
		
		JPanel panelLabel = new JPanel();
		panelLabel.setLayout(new GridLayout(2,1));
		
		this.labelTurn = new JLabel("Turn : 0");
		this.labelTurn.setHorizontalAlignment(JLabel.CENTER);
		this.labelTurn.setVerticalAlignment(JLabel.CENTER);
		
		JButton buttonClose = new JButton("Quitter le jeu");
		buttonClose.addActionListener(evenement -> {
			this.sortie.println("[CLIENT : " + this.client.getNom() + "] DECONNEXION");
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

	public String[] getLayouts() {
		File repertoire = new File(System.getProperty("user.dir") + "/layout");
		
		FilenameFilter layoutFilter = (dir, name) -> name.endsWith( EXT_LAYOUT );
		
		File[] files = repertoire.listFiles(layoutFilter);
		assert files != null;
		String[] layouts = new String[files.length];

		for(int i=0; i<files.length; ++i) {
			layouts[i] = files[i].getName();
		}
		
		return layouts;
	}


//	public void update(Observable obs, Object arg) {
//		Game game = (Game)obs;
//		this._labelTurn.setText("Turn : " + game.getTurn());
//	}
	
	public void update(BombermanGame game) {
		this.labelTurn.setText("Turn : " + game.getTurn());
	}

}
