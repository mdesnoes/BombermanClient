package com.projetProgReseau.client;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.projetProgReseau.view.ViewConnexion;


public class Client implements Runnable {
	
	private Socket connexion;
	private PrintWriter sortie;
	private DataInputStream entree;
	
	private String nom;



	public Client(String nomServ, int port) {
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
			String msg = "";
			
            @Override
            public void run() {
            	
            	ViewConnexion view = new ViewConnexion(Client.this);
            	
            	while(true) {
            		
            	}
            	
//            	fermeture();
            }
        });
        envoyer.start();
	}
	
	private void recevoir() {
		Thread recevoir = new Thread(new Runnable() {
            String msg;
            @Override
            public void run() {
            	
            	while(true) {
            		
            	}
            	
//               fermeture();
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
	
	
	public PrintWriter getSortie() {
		return sortie;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public static void main(String[] argu) {

		if (argu.length == 2) { // on récupère les paramètres
			String s = argu[0];
			int p = Integer.parseInt(argu[1]);
			
			Client c1 = new Client(s, p);
			Thread t = new Thread(c1);
			t.start();
		} else {
			System.out.println("syntaxe d’appel : java client serveur port \n");
		} 
		
	}

}
