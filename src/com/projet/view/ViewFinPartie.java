package com.projet.view;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.projet.client.Client;


public class ViewFinPartie extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private static final String MSG_DECO_CLIENT = "DECONNEXION";
	private static final String MSG_INIT_GAME = "INITIALISATION";
	
	private static ViewFinPartie uniqueInstance = null;
	
	private String strLabelGagnant;


	private ViewFinPartie(Client client, String str) {
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
			client.getSortie().println( MSG_INIT_GAME );
			setVisible(false);
		});
		
		buttonFermer.addActionListener(evenement -> {
			client.getSortie().println( MSG_DECO_CLIENT );
			client.fermeture();
			setVisible(false);
		});
		panelPrincipal.add(panelButton);
		
		setContentPane(panelPrincipal);
		setVisible(true);
	}
	
	public static ViewFinPartie getInstance(Client client, String victoire, String color) {
		
		String str = "<html><body>Victoire de l'agent <font color='"+color+"'>" + victoire + "</font> !</body></html>";
		if(uniqueInstance == null) {
			uniqueInstance = new ViewFinPartie(client, str);
		}
		
		//Si le phrase de victoire est différente, on  créer une nouvelle instance
		if(!uniqueInstance.getStrLabelGagnant().equals(str)) {
			uniqueInstance = new ViewFinPartie(client, str);
		}
		
		uniqueInstance.setVisible(true);
		return uniqueInstance;
	}
	
	
	
	public String getStrLabelGagnant() {
		return strLabelGagnant;
	}

}
