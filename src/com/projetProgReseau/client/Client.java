package com.projetProgReseau.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import com.projetBomberman.modele.BombermanGame;
import com.projetBomberman.modele.ModeJeu;
import com.projetBomberman.strategy.BreakWallStrategy;
import com.projetBomberman.strategy.EsquiveStrategy;
import com.projetBomberman.strategy.PutBombStrategy;
import com.projetBomberman.strategy.RandomStrategy;
import com.projetBomberman.strategy.Strategy;



public class Client implements Runnable {
	
	/* A passée en paramètre du client lorsque le serveur sera adapté pour tous les modes de jeu */
	private static final ModeJeu MODE_JEU = ModeJeu.SOLO;
	
	private static final String RANDOM_STRATEGY = "random";
	private static final String PUT_BOMB_STRATEGY = "put_bomb";
	private static final String BREAK_WALL_STRATEGY = "break_wall";
	private static final String ESQUIVE_STRATEGY = "esquive";
	

	private Socket connexion;
	private PrintWriter sortie;
	private DataInputStream entree;
	private String nom;

	private Strategy strategyAgent;
	private int maxturn;
	private BombermanGame game;

	public Client(String nomServ, int port, String strategyAgent, int maxturn) {
		this.maxturn = maxturn;
		this.strategyAgent = initStrategyAgent(strategyAgent);
		
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


	private void envoyer() {
		Thread envoyer = new Thread(new Runnable() {
			
            @Override
            public void run() {
            	
            	//ViewConnexion view = new ViewConnexion(Client.this);
            	
            	while(true) {
            		
            	}
            	
//            	fermeture();
            }
        });
        envoyer.start();
	}
	
	private void recevoir() {
		Thread recevoir = new Thread(new Runnable() {
            String msg = "";
            
            @Override
            public void run() {
            	try {
            		nom = entree.readUTF();
            		
        			game = new BombermanGame(sortie, nom, MODE_JEU, strategyAgent, maxturn);
            		
	            	while(true) {
						 msg = entree.readUTF();
						
//						System.out.println(msg);
	            	}
	            	
            	} catch (IOException e) {
					e.printStackTrace();
				}
            }
        });
        recevoir.start();
	}
	
	@Override
	public void run() {		
		envoyer();
		recevoir();
	}
	
	private void fermeture() {
		try {
	    	entree.close();
	    	sortie.close();
	    	connexion.close();
	    	System.exit(-1);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private Strategy initStrategyAgent(String strategyAgent) {
		switch(strategyAgent) {
			case RANDOM_STRATEGY: return new RandomStrategy();
			case PUT_BOMB_STRATEGY: return new PutBombStrategy();
			case BREAK_WALL_STRATEGY: return new BreakWallStrategy();
			case ESQUIVE_STRATEGY: return new EsquiveStrategy();
			default: return new RandomStrategy();
		}
	}

	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public static void main(String[] argu) {

		if (argu.length == 4) {
			
			String s = argu[0];
			int p = Integer.parseInt(argu[1]);
			String strategy = argu[2];
			int maxturn = Integer.parseInt(argu[3]);
			
			Client c1 = new Client(s, p, strategy, maxturn);
			Thread t = new Thread(c1);
			t.start();
			
		} else {
			System.out.println("syntaxe d’appel : java client serveur port strategie_des_agents nombre_max_de_tour\n");
		} 
		
	}

}
