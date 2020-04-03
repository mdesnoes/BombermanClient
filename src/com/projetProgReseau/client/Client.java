package com.projetProgReseau.client;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Observable;

import javax.swing.AbstractButton;
import javax.swing.JFileChooser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetBomberman.modele.BombermanGame;
import com.projetBomberman.view.Map;
import com.projetBomberman.view.ViewBombermanGame;
import com.projetBomberman.view.ViewCommand;
import com.projetBomberman.view.ViewGagnant;
import com.projetBomberman.view.ViewModeInteractif;



public class Client extends Observable implements Runnable {

	private static final String EXT_LAYOUT = ".lay";
	private static final String MSG_FIN_PARTIE = "FIN_PARTIE";
	private static final String SEP_MSG_FIN_PARTIE = ">";
	private static final String SEP_DONNEES_FIN_PARTIE = ";";

	private Socket connexion;
	private PrintWriter sortie;
	private DataInputStream entree;
	private String nom;

	private String modeJeu;
	private String strategy;
	private int maxturn;
	private BombermanGame game;
	private ViewCommand viewCommand;
	private ViewBombermanGame viewBombermanGame;
	private ViewModeInteractif viewModeInteractif;
	
	
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
		
		// Creation de la connexion et des entrees/sorties
		try {
			this.connexion = new Socket(nomServ, port);
			this.sortie = new PrintWriter(this.connexion.getOutputStream(), true);
			this.entree = new DataInputStream(this.connexion.getInputStream());
		} catch (IOException e) {
			System.out.println("Aucun serveur n’est rattaché au port");
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
            		/* Envoie au serveur des options pour créer le jeu */
            		envoyerConfigurationJeu();
            		
            		/*  Nom du client */
            		nom = entree.readUTF();
            		
            		/* JFileChooser pour le choix de la map */
            		Map map = choixMapInitiale();
            		
            		/* Le serveur initialise la map et nous renvoie l'etat du jeu au debut */
            		receptionEtatJeu(entree.readUTF());
        			
            		/* Création des vues */
        			viewCommand = new ViewCommand(Client.this);
        			viewBombermanGame = new ViewBombermanGame(map);
        			if(modeJeu.equals("solo") || modeJeu.equals("duo")) {
        				viewModeInteractif = ViewModeInteractif.getInstance();
        			}

        			/* Tant que le client n'a pas quitter le jeu, on recupere l'etat courant du jeu etc */
	            	while(!connexion.isClosed()) {
	            		
	            		msg = entree.readUTF();
						
	            		if(msg.contains( MSG_FIN_PARTIE )) {
	            			finDePartie(msg);
	            		}
	            		else {
	            			receptionEtatJeu(msg);
		      
		        			/* Mise à jour du plateau du jeu */
		        			viewBombermanGame.update(game);
		        			/* Mise à jour du plateau de commande */
		        			viewCommand.update(game);
	            		}
						
	            	}
	            	
	            	
            	} catch(EOFException e) {
             	    System.out.println("Le serveur est fermé !");
             	   	fermeture();
            	} catch(SocketException e) {
            		System.out.println("Connexion fermée !");
            		fermeture();
                } catch (IOException e) {
					e.printStackTrace();
				}
            }
        });
        t.start();
	}
	
	
	private void envoyerConfigurationJeu() {
    	this.sortie.println(modeJeu);
    	this.sortie.println(maxturn);
    	this.sortie.println(strategy);
	}
	
	
	private void receptionEtatJeu(String etat) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.addMixIn(AbstractButton.class, MixIn.class); /* Pour empêcher un conflit de setter dans la classe AbstractButton */
			game = mapper.readValue(etat, BombermanGame.class);
		} catch (Exception e) {
			System.out.println("ERREUR Etat du jeu non trouvé");
			e.printStackTrace();
			System.exit(-1);
		}
		System.out.println("[SERVEUR] Etat du jeu reçu avec succès pour le client " + this.nom );
	}
	
	private void finDePartie(String msg) {
		
		String donnees = msg.split( SEP_MSG_FIN_PARTIE )[1];
		String[] tabDonnees = donnees.split( SEP_DONNEES_FIN_PARTIE );
		String vainqueur = tabDonnees[0];
		String couleur = tabDonnees[1];
		
		ViewGagnant.getInstance(this, vainqueur, couleur);
	}

	
	
	public Map choixMapInitiale() {
		/* Impl JFileChooser */
		JFileChooser fc = new JFileChooser();
		
		String cheminLayout = "";
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		String layoutGame = "";
		if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			layoutGame = fc.getSelectedFile().getName();
			cheminLayout = fc.getSelectedFile().getAbsolutePath();
		}
		System.out.println(cheminLayout);
		
		/* Creation de la map en fonction du layout choisi */
		Map map = null;
		if(layoutGame != null && layoutGame.endsWith( EXT_LAYOUT )) {
			try {
				map = new Map( cheminLayout );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/* Envoie de la map au serveur via Jackson */
		ObjectMapper mapper = new ObjectMapper();
		String mapJson = "";
		try {
			mapJson = mapper.writeValueAsString(map);
		} catch (JsonProcessingException e1) {
			System.out.println("ERREUR lors de l'envoie de la map initiale");
			e1.printStackTrace();
			System.exit(-1);
		}
		sortie.println(mapJson);
		System.out.println("[SERVEUR] Map envoyé à " + this.nom + " avec succès");
		
		return map;
	}
	
	
	
	
	public void fermeture() {
		try {
			this.viewBombermanGame.setVisible(false);
			this.viewCommand.setVisible(false);
			if(this.viewModeInteractif != null) {
				this.viewModeInteractif.setVisible(false);
			}
	    	entree.close();
	    	sortie.close();
	    	connexion.close();
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


	public static void main(String[] argu) {

		if (argu.length == 5) {
			
			String s = argu[0];
			int p = Integer.parseInt(argu[1]);
			String modeJeu = argu[2];
			String strategy = argu[3];
			int maxturn = Integer.parseInt(argu[4]);
			
			Client c1 = new Client(s, p, modeJeu, strategy, maxturn);
			Thread t = new Thread(c1);
			t.start();
			
		} else {
			System.out.println("syntaxe d’appel : java client serveur port mode_de_jeu=>(normal|solo|duo) strategie_des_agents=>(random|put_bomb|break_wall|esquive) nombre_max_de_tour\n");
		} 
		
	}

}
