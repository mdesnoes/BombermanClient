package com.projetProgReseau.metier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetProgReseau.entity.Partie;

public class PartieForm {
	
	public PartieForm() {
	}
	
	public void enregistrerPartie(Partie partie) {
		// Conversion de la Partie ( Objet Java ) en JSON
		ObjectMapper mapper = new ObjectMapper();
		String partieJson = "";
		try {
			partieJson = mapper.writeValueAsString(partie);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
		
		//  Appel du web service REST Partie
		if(partieJson != "") {
			try {
				URL url = new URL("http://localhost:8080/SiteWebBomberman/api/parties");
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/json");
		        connection.setDoOutput(true);
		        // Body de la requête
		        OutputStream stream = connection.getOutputStream();
		        OutputStreamWriter writer = new OutputStreamWriter(stream);
		        writer.write(partieJson);
		        writer.close();		        
		        
		        // On recupère la partie qui vient d'être enregistrée
		        StringBuilder response = new StringBuilder();
		        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		        String line;
		        while ((line = in.readLine()) != null) {
		            response.append(line);
		        }
		        in.close();
		        System.out.println("La partie " + response.toString() + " a été enregistré");
		        
		        connection.disconnect();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
