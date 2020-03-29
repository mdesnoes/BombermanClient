package com.projetBomberman.view;

import javax.swing.*;

import com.projetBomberman.controller.ControllerBombermanGame;
import com.projetBomberman.controller.InterfaceController;
import com.projetBomberman.modele.Game;

import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Observable;
import java.util.Observer;

public class ViewCommand extends JFrame implements Observer {

	private final static int SPEEDMIN = 1;		// Vitesse minimum des tours
	private final static int SPEEDMAX = 10;		// Vitesse maximum des tours
	private JLabel _labelTurn;					// Le label qui affiche le tour courant
	private JSlider _slider;					// Le slider qui affiche la vitesse des tours en seconde
	private ControllerBombermanGame _controllerGame;
	private String _layoutGame = null;
	
	public ViewCommand(InterfaceController controllerGame, Game game) {
		this._controllerGame = (ControllerBombermanGame) controllerGame;
		game.addObserver(this);
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
		
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new java.io.File("./layout"));
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			this._layoutGame = fc.getSelectedFile().getName();
		}

		if(this._layoutGame != null && this._layoutGame.endsWith(".lay")) {
			JPanel panelPrincipal = new JPanel();
			panelPrincipal.setLayout(new GridLayout(2,1));
			JPanel panelButton = new JPanel();		
			panelButton.setLayout(new GridLayout(1,4));
			Icon icon_restart = new ImageIcon("./icones/icon_restart.png");
			JButton buttonRestart = new JButton(icon_restart);
			Icon icon_run = new ImageIcon("./icones/icon_run.png");
			JButton buttonRun = new JButton(icon_run);
			Icon icon_step = new ImageIcon("./icones/icon_step.png");
			JButton buttonStep = new JButton(icon_step);
			Icon icon_stop = new ImageIcon("./icones/icon_pause.png");
			JButton buttonStop = new JButton(icon_stop);
			
			//Permet d'appeler le controleur afin d'initialiser le jeu
			buttonRestart.addActionListener(evenement -> {
				_controllerGame.start();
				buttonRestart.setEnabled(false);
				buttonRun.setEnabled(true);
				buttonStep.setEnabled(true);
				buttonStop.setEnabled(true);
			});
			
			//Permet d'appeler le controleur afin de demarrer le jeu
			buttonRun.addActionListener(evenement -> {
				_controllerGame.run();
				buttonRestart.setEnabled(false);
				buttonRun.setEnabled(false);
				buttonStep.setEnabled(false);
				buttonStop.setEnabled(true);
			});
			
			//Permet d'appeler le controleur afin de passer au tour suivant
			buttonStep.addActionListener(evenement -> {
				_controllerGame.step();
				buttonRestart.setEnabled(true);
				buttonRun.setEnabled(true);
				buttonStep.setEnabled(true);
				buttonStop.setEnabled(false);
			});
			
			//Permet d'appeler le controleur afin de faire une pause
			buttonStop.addActionListener(evenement -> {
				_controllerGame.stop();
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
			this._slider = new JSlider(JSlider.HORIZONTAL,SPEEDMIN,SPEEDMAX,SPEEDMIN);
			this._slider.setPaintTicks(true);
			this._slider.setPaintLabels(true);
			this._slider.setMinorTickSpacing(SPEEDMIN);
			this._slider.setMajorTickSpacing(SPEEDMIN);
			this._slider.setValue((int)this._controllerGame.getTime()/1000);

			//Permet d'appeler le constructeur afin de modifier le temps des tours
			this._slider.addChangeListener(arg0 -> _controllerGame.setTime(_controllerGame.getInitTime()/_slider.getValue()));
			
			panelSlider.add(this._slider);
			
			JPanel panelLabel = new JPanel();
			panelLabel.setLayout(new GridLayout(2,1));
			
			this._labelTurn = new JLabel("Turn : 0");
			this._labelTurn.setHorizontalAlignment(JLabel.CENTER);
			this._labelTurn.setVerticalAlignment(JLabel.CENTER);
			
			JButton buttonClose = new JButton("Quitter le jeu");
			buttonClose.addActionListener(evenement -> {
				_controllerGame.quitter();
			});
			
			panelLabel.add(this._labelTurn);
			panelLabel.add(buttonClose);
			
			panelSliderLabel.add(panelSlider);
			panelSliderLabel.add(panelLabel);
			panelPrincipal.add(panelButton);
			panelPrincipal.add(panelSliderLabel);
			
			this.setContentPane(panelPrincipal);
			this.setVisible(true);
		}
	}

	public String[] getLayouts() {
		File repertoire = new File(System.getProperty("user.dir") + "/layout");
		
		FilenameFilter layoutFilter = (dir, name) -> name.endsWith(".lay");
		
		File[] files = repertoire.listFiles(layoutFilter);
		assert files != null;
		String[] layouts = new String[files.length];

		for(int i=0; i<files.length; ++i) {
			layouts[i] = files[i].getName();
		}
		
		return layouts;
	}
	
	public String getLayoutGame() {
		return this._layoutGame;
	}

	public void update(Observable obs, Object arg) {
		Game game = (Game)obs;
		this._labelTurn.setText("Turn : " + game.getTurn());
	}

}
