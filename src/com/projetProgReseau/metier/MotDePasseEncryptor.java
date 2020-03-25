package com.projetProgReseau.metier;

import org.jasypt.util.text.BasicTextEncryptor;


public class MotDePasseEncryptor {
	
	private static MotDePasseEncryptor uniqueInstance = null;
	
    private BasicTextEncryptor textEncryptor;

    
    private MotDePasseEncryptor() {
    	this.textEncryptor = new BasicTextEncryptor();
        this.textEncryptor.setPassword("keyMDPEncryptor"); // Clé secrete

    }
    
    public static MotDePasseEncryptor getInstance() {
    	if(uniqueInstance == null) {
			uniqueInstance = new MotDePasseEncryptor();
		}
		
		return uniqueInstance;
    }
    
    public String crypter(String mdp) {
        return this.textEncryptor.encrypt(mdp);
    }
    
    public String decrypter(String mdpChiffre) {
    	return this.textEncryptor.decrypt(mdpChiffre);
    }
    
}
