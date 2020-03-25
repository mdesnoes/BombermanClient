package com.projetProgReseau.metier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetProgReseau.entity.Utilisateur;

public class UtilisateurForm {
	
	private static final MotDePasseEncryptor mdpEncryptor = MotDePasseEncryptor.getInstance();

	public UtilisateurForm() {		
	}
	
	private Utilisateur getUtilisateur(String pseudo) {
		Utilisateur u = null;
		
		if(!pseudo.isEmpty()) {
			// Appel du web service REST Utilisateur du projet JEE
			BufferedReader in = null;
			try {
				URL url = new URL("http://localhost:8080/SiteWebBomberman/api/utilisateurs/" + pseudo);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.setRequestProperty("Accept", "application/json");
	            if (connection.getResponseCode() != 200) {
	                throw new RuntimeException("Failed : HTTP Error code : " + connection.getResponseCode());
	            }
				in = new BufferedReader(new InputStreamReader(url.openStream()));
				connection.disconnect();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// On convertit le resultat de la requete ( au format JSON ) en Utilisateur ( Objet JAVA )
			ObjectMapper mapper = new ObjectMapper();
			try {
				u = mapper.readValue(in.readLine(), Utilisateur.class);
			} catch (Exception e) {
				System.out.println("Nom d'utilisateur non trouvé");
			}
		}
				
		return u;
	}
	
	// Renvoie vrai si le nom d'utilisateur et le mot de passe sont trouvé dans la base de donnée
	public boolean verifConnexion(String pseudo, String password) {
		Utilisateur u = getUtilisateur(pseudo);
		
		if(u != null) {
			String passwordDecrypte = mdpEncryptor.decrypter(u.getPassword());
			return u.getPseudo().equals(pseudo) && passwordDecrypte.equals(password);
		}
		return false;
	}

}
