package com.projetBomberman.view;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.projetBomberman.controller.ControllerBombermanGame;


public class ViewGagnant extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private static ViewGagnant uniqueInstance = null;
	
	private String strLabelGagnant;


	
	public String getStrLabelGagnant() {
		return strLabelGagnant;
	}

	public void setStrLabelGagnant(String strLabelGagnant) {
		this.strLabelGagnant = strLabelGagnant;
	}

	private ViewGagnant(ControllerBombermanGame contBombGame, String str) {
		this.strLabelGagnant = str;

		setTitle("Fin du jeu");
		setSize(new Dimension(400,150));
		Dimension windowSize = getSize();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Point centerPoint = ge.getCenterPoint();
		int dx = centerPoint.x - windowSize.width / 2;
		int dy = centerPoint.y - windowSize.height / 2;
		setLocation(dx,dy);
	
		JPanel panelPrincipal = new JPanel();
		panelPrincipal.setLayout(new GridLayout(2,1));
		
		JLabel labelGagnant = new JLabel(str, JLabel.CENTER);
		panelPrincipal.add(labelGagnant);
		
		JPanel panelButton = new JPanel();
		panelButton.setLayout(new GridLayout(1,2));
		
		JButton buttonRecommencer = new JButton("Recommencer");
		JButton buttonFermer = new JButton("Fermer le jeu");
		
		panelButton.add(buttonRecommencer);
		panelButton.add(buttonFermer);

		buttonRecommencer.addActionListener(evenement -> {
			contBombGame.start();
			setVisible(false);
		});
		
		buttonFermer.addActionListener(evenement -> {
			contBombGame.quitter();
		});
		panelPrincipal.add(panelButton);
		
		setContentPane(panelPrincipal);
		setVisible(true);
	}
	
	public static ViewGagnant getInstance(ControllerBombermanGame contBombGame, String victoire, String color) {

		String str = "<html><body>Victoire de l'agent <font color='"+color+"'>" + victoire + "</font> !</body></html>";
		if(uniqueInstance == null) {
			uniqueInstance = new ViewGagnant(contBombGame, str);
		}
		
		//Si le phrase de victoire est différente, on  créer une nouvelle instance
		if(!uniqueInstance.getStrLabelGagnant().equals(str)) {
			uniqueInstance = new ViewGagnant(contBombGame, str);
		}
		
		uniqueInstance.setVisible(true);
		return uniqueInstance;
	}

}
