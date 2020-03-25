package com.projetProgReseau.entity;

import java.sql.Timestamp;

public class Partie {
	
	private long id;
	private Timestamp dateDebut;
	private Timestamp dateFin;
	private String vainqueur;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Timestamp getDateDebut() {
		return dateDebut;
	}
	public void setDateDebut(Timestamp dateDebut) {
		this.dateDebut = dateDebut;
	}
	public Timestamp getDateFin() {
		return dateFin;
	}
	public void setDateFin(Timestamp dateFin) {
		this.dateFin = dateFin;
	}
	public String getVainqueur() {
		return vainqueur;
	}
	public void setVainqueur(String vainqueur) {
		this.vainqueur = vainqueur;
	}
}
