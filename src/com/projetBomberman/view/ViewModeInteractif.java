package com.projetBomberman.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class ViewModeInteractif extends JFrame implements KeyListener {
	
	private static final long serialVersionUID = 1L;
	private static ViewModeInteractif uniqueInstance = null;
	int codeKeyPressed;
	
	private ViewModeInteractif() {
		setTitle("Fenêtre de commande");
		setSize(new Dimension(300,500));
		Dimension windowSize = getSize();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Point centerPoint = ge.getCenterPoint();
		int dx = centerPoint.x - windowSize.width / 2 + 325;
		int dy = centerPoint.y - windowSize.height / 2 - 300;
		setLocation(dx,dy);
	
		JPanel panelPrincipal = new JPanel();
		panelPrincipal.setLayout(new GridLayout(8,1));
		
		JLabel labelJoueur1 = new JLabel("Touches Joueur 1 : ", JLabel.CENTER);
		labelJoueur1.setForeground(Color.RED);
		panelPrincipal.add(labelJoueur1);
		
		JPanel panelCommandeJ1 = new JPanel();
		panelCommandeJ1.setLayout(new GridLayout(2,3));
		
		
		panelCommandeJ1.add(new JLabel(""));
		panelCommandeJ1.add(createLabelTouche("Haut"));
		panelCommandeJ1.add(new JLabel(""));
		panelCommandeJ1.add(createLabelTouche("Bas"));
		panelCommandeJ1.add(createLabelTouche("Gauche"));
		panelCommandeJ1.add(createLabelTouche("Droite"));
		
		panelPrincipal.add(panelCommandeJ1);
		
		JLabel labelBombeJ1 = new JLabel("Poser Bombe : Touche 0");
		panelPrincipal.add(labelBombeJ1);

		JLabel labelStopJ1 = new JLabel("Ne rien faire : Touche 1");
		panelPrincipal.add(labelStopJ1);
		
		JLabel labelJoueur2 = new JLabel("Touches Joueur 2 : ", JLabel.CENTER);
		labelJoueur2.setForeground(Color.BLUE);
		panelPrincipal.add(labelJoueur2);
		
		JPanel panelCommandeJ2 = new JPanel();
		panelCommandeJ2.setLayout(new GridLayout(2,3));
		
		panelCommandeJ2.add(new JLabel(""));
		panelCommandeJ2.add(createLabelTouche("Z"));
		panelCommandeJ2.add(new JLabel(""));
		panelCommandeJ2.add(createLabelTouche("Q"));
		panelCommandeJ2.add(createLabelTouche("S"));
		panelCommandeJ2.add(createLabelTouche("D"));
		
		panelPrincipal.add(panelCommandeJ2);
		
		JLabel labelBombeJ2 = new JLabel("Poser Bombe : F");
		panelPrincipal.add(labelBombeJ2);

		JLabel labelStopJ2 = new JLabel("Ne rien faire : A");
		panelPrincipal.add(labelStopJ2);
		
		setContentPane(panelPrincipal);
		setVisible(true);
	}
	
	public static ViewModeInteractif getInstance() {
		if(uniqueInstance == null) {
			uniqueInstance = new ViewModeInteractif();
		}
		return uniqueInstance;
	}
	
	public JLabel createLabelTouche(String nom) {
		JLabel label = new JLabel(nom, JLabel.CENTER);
		label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		
		return label;
	}

	@Override
	public void keyPressed(KeyEvent key) {
		this.codeKeyPressed = key.getKeyCode();
	}

	@Override
	public void keyReleased(KeyEvent key) {	
	}

	@Override
	public void keyTyped(KeyEvent key) {
	}
	
	public int getKeyPressed() {
		return this.codeKeyPressed;
	}
}
