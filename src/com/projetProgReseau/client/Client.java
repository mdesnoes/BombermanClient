package com.projetProgReseau.client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;

import javax.swing.AbstractButton;
import javax.swing.JFileChooser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetBomberman.modele.BombermanGame;
import com.projetBomberman.modele.Map;
import com.projetBomberman.view.ViewBombermanGame;
import com.projetBomberman.view.ViewCommand;
import com.projetBomberman.view.ViewConnexion;
import com.projetBomberman.view.ViewGagnant;
import com.projetBomberman.view.ViewModeInteractif;



public class Client extends Observable implements Runnable, KeyListener {

	private static final String EXT_LAYOUT = ".lay";
	private static final String MSG_FIN_PARTIE = "FIN_PARTIE";
	private static final String SEP_MSG_FIN_PARTIE = ">";
	private static final String SEP_DONNEES_FIN_PARTIE = ";";
	
	private static final String CONNEXION_OK = "Connexion acceptee";

	private Socket connexion;
	private PrintWriter sortie;
	private DataInputStream entree;
	private String nom;

	private String modeJeu;
	private String strategy;
	private int maxturn;
	private BombermanGame game;
	private ViewCommand commande;
	private ViewBombermanGame plateau;
	private ViewModeInteractif infoClavier;
	
	
	/* 
	 * Pour empêcher un conflit de setter dans la classe AbstractButton
	 * On ignore l'un des deux setter (qui ont le meme nom) qui pose problème pour Jackson
	 */
	public static interface MixIn {
        @JsonIgnore
        public void setMnemonic(char mnemonic);
    }
	
	

	public Client(String nomServ, int port, String modeJeu, String strategyAgent, int maxturn) {
		this.modeJeu = modeJeu;
		this.maxturn = maxturn;
		this.strategy = strategyAgent;
		
		/* Creation du socket de connexion et des entrees/sorties */
		try {
			this.connexion = new Socket(nomServ, port);
			this.sortie = new PrintWriter(this.connexion.getOutputStream(), true);
			this.entree = new DataInputStream(this.connexion.getInputStream());
		} catch (IOException e) {
			System.out.println("[CLIENT] [ERREUR] Aucun serveur n’est rattaché au port");
	    	System.exit(-1);
		}
	}
	
	
	@Override
	public void run() {
		Thread t = new Thread(new Runnable() {
            String msg;
            
            @Override
            public void run() {
            	try {
            		
            		/* Connexion au serveur */
            		ViewConnexion viewConnexion = new ViewConnexion(Client.this);
            		msg = entree.readUTF();
            		while(!msg.equals( CONNEXION_OK )) {
            			viewConnexion.afficherLabelMessageErreur();
            			msg = entree.readUTF();
            		}
            		viewConnexion.setVisible(false);
            		System.out.println("[CLIENT] Bienvenue sur le serveur " + nom);
            		
            		
            		/* Envoie au serveur des options pour créer le jeu */
            		envoyerConfigurationJeu();
            		
            		/* JFileChooser pour le choix de la map */
            		Map map = choixMapInitiale();
            		
            		/* Le serveur initialise la map et nous renvoie l'etat du jeu au debut */
            		receptionEtatJeu(entree.readUTF());
        			
            		/* Création des vues */
        			commande = new ViewCommand(Client.this);
        			plateau = new ViewBombermanGame(Client.this, map);
        			if( modeInteractif() ) {
        				infoClavier = ViewModeInteractif.getInstance();
        				
        				/* Le plateau gère l'ecoute des actions sur le clavier */
            			plateau.addKeyListener(Client.this);
        			}

        			
        			/* Tant que le client n'a pas quitter le jeu, on recupere l'etat courant du jeu etc */
	            	while(!connexion.isClosed()) {
	            		msg = entree.readUTF();
	            		
	            		/* Après chaque action, on remet le focus sur le plateau pour gerer l'ecoute du clavier */
	        			if( modeInteractif() ) {
	        				plateau.requestFocus();
	        			}
	        			
	            		if(msg.contains( MSG_FIN_PARTIE )) {
	            			finDePartie(msg);
	            		}
	            		else {
	            			receptionEtatJeu(msg);
		      
		        			/* Mise à jour du plateau du jeu */
		        			plateau.update(game);
		        			/* Mise à jour du plateau de commande */
		        			commande.update(game);
	            		}
	            	}
	            	
            	} catch(EOFException e) {
             	    System.out.println("[CLIENT] [ERREUR] Le serveur est fermé ou vous avez quitter le serveur !");
             	   	fermeture();
                } catch (IOException e) {
					e.printStackTrace();
				}
            }
        });
        t.start();
	}
	
	
	private void envoyerConfigurationJeu() {
    	this.sortie.println( this.modeJeu );
    	this.sortie.println( this.maxturn );
    	this.sortie.println( this.strategy );
	}
	
	
	private void receptionEtatJeu(String etat) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.addMixIn(AbstractButton.class, MixIn.class); /* Pour empêcher un conflit de setter dans la classe AbstractButton */
			this.game = mapper.readValue(etat, BombermanGame.class);
		} catch (Exception e) {
			System.out.println("[CLIENT] [ERREUR] Etat du jeu non reçu");
			e.printStackTrace();
			fermeture();
		}
	}
	
	
	private boolean modeInteractif() {
		return modeJeu.equals("solo") || modeJeu.equals("duo");
	}
	
	
	private void finDePartie(String msg) {
		String donnees = msg.split( SEP_MSG_FIN_PARTIE )[1];
		String[] tabDonnees = donnees.split( SEP_DONNEES_FIN_PARTIE );
		String vainqueur = tabDonnees[0];
		String couleur = tabDonnees[1];
			
		ViewGagnant.getInstance(this, vainqueur, couleur);
	}
	
	
	public Map choixMapInitiale() {
		/* JFileChooser */
		JFileChooser fc = new JFileChooser();
		
		String cheminLayout = "";
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		String layoutGame = "";
		if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			layoutGame = fc.getSelectedFile().getName();
			cheminLayout = fc.getSelectedFile().getAbsolutePath();
		}
		
		/* Creation de la map en fonction du layout choisi */
		Map map = null;
		if(layoutGame != null && !layoutGame.equals("") && layoutGame.endsWith( EXT_LAYOUT )) {
			try {
				map = new Map( cheminLayout );
			} catch (Exception e) {
				System.out.println("[CLIENT] [ERREUR] erreur lors de la creation de la map");
				e.printStackTrace();
				fermeture();
			}
		} else {
			System.out.println("[CLIENT] [ERREUR] erreur lors du choix de la map ! Ce n'est pas un fichier de layout (.lay)");
			System.exit(-1);
		}
		
		/* Envoie de la map au serveur via Jackson */
		ObjectMapper mapper = new ObjectMapper();
		String mapJson = "";
		try {
			mapJson = mapper.writeValueAsString(map);
		} catch (JsonProcessingException e1) {
			System.out.println("[CLIENT] [ERREUR] erreur lors de l'envoie de la map initiale");
			e1.printStackTrace();
			fermeture();
		}
		this.sortie.println(mapJson);
		
		return map;
	}
	
	
	public void fermeture() {
		try {
			this.plateau.setVisible(false);
			this.commande.setVisible(false);
			if(this.infoClavier != null) {
				this.infoClavier.setVisible(false);
			}
	    	this.entree.close();
	    	this.sortie.close();
	    	this.connexion.close();
	    	System.exit(-1);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	
	
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public PrintWriter getSortie() {
		return sortie;
	}
	public void setSortie(PrintWriter sortie) {
		this.sortie = sortie;
	}
	public BombermanGame getGame() {
		return game;
	}
	public void setGame(BombermanGame game) {
		this.game = game;
	}
	public String getModeJeu() {
		return modeJeu;
	}
	public void setModeJeu(String modeJeu) {
		this.modeJeu = modeJeu;
	}


	@Override
	public void keyPressed(KeyEvent e) {
		if( modeInteractif() ) {
			this.sortie.println(e.getKeyCode());
		}
	}


	@Override
	public void keyReleased(KeyEvent arg0) {
	}


	@Override
	public void keyTyped(KeyEvent arg0) {
	}

}
