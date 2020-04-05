package com.projet.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class ViewInfoClavier extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private static ViewInfoClavier uniqueInstance = null;
	
	private ViewInfoClavier() {
		setTitle("Fenêtre de commande");
		setSize(new Dimension(300,500));
		Dimension windowSize = getSize();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Point centerPoint = ge.getCenterPoint();
		int dx = centerPoint.x - windowSize.width / 2 + 325;
		int dy = centerPoint.y - windowSize.height / 2 - 300;
		setLocation(dx,dy);
	
		JPanel panelPrincipal = new JPanel();
		panelPrincipal.setLayout(new GridLayout(6,1));
		
		JLabel labelJoueur1 = new JLabel("Touches Joueur 1 : ", JLabel.CENTER);
		labelJoueur1.setForeground(Color.RED);
		panelPrincipal.add(labelJoueur1);
		
		JPanel panelCommandeJ1 = new JPanel();
		panelCommandeJ1.setLayout(new GridLayout(2,3));
		
		
		panelCommandeJ1.add(new JLabel(""));
		panelCommandeJ1.add(createLabelTouche("Z"));
		panelCommandeJ1.add(new JLabel(""));
		panelCommandeJ1.add(createLabelTouche("Q"));
		panelCommandeJ1.add(createLabelTouche("S"));
		panelCommandeJ1.add(createLabelTouche("D"));
		
		panelPrincipal.add(panelCommandeJ1);
		
		JLabel labelBombeJ1 = new JLabel("Poser Bombe : Touche F");
		panelPrincipal.add(labelBombeJ1);

		
		JLabel labelJoueur2 = new JLabel("Touches Joueur 2 : ", JLabel.CENTER);
		labelJoueur2.setForeground(Color.BLUE);
		panelPrincipal.add(labelJoueur2);
		
		JPanel panelCommandeJ2 = new JPanel();
		panelCommandeJ2.setLayout(new GridLayout(2,3));
		
		panelCommandeJ2.add(new JLabel(""));
		panelCommandeJ2.add(createLabelTouche("HAUT"));
		panelCommandeJ2.add(new JLabel(""));
		panelCommandeJ2.add(createLabelTouche("GAUCHE"));
		panelCommandeJ2.add(createLabelTouche("BAS"));
		panelCommandeJ2.add(createLabelTouche("DROITE"));
		
		panelPrincipal.add(panelCommandeJ2);
		
		JLabel labelBombeJ2 = new JLabel("Poser Bombe : Numero 0");
		panelPrincipal.add(labelBombeJ2);
		
		
		setContentPane(panelPrincipal);
		setVisible(true);
	}
	
	public static ViewInfoClavier getInstance() {
		if(uniqueInstance == null) {
			uniqueInstance = new ViewInfoClavier();
		}
		return uniqueInstance;
	}
	
	
	private JLabel createLabelTouche(String nom) {
		JLabel label = new JLabel(nom, JLabel.CENTER);
		label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		
		return label;
	}

}
