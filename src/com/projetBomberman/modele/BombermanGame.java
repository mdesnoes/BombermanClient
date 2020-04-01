package com.projetBomberman.modele;

import java.util.ArrayList;

public class BombermanGame extends Game {
	
    private ArrayList<AgentBomberman> listAgentsBomberman;
    private ArrayList<AgentPNJ> listAgentsPNJ;
	private boolean[][] listBreakableWalls;
	private ArrayList<Bombe> listBombs;
	private ArrayList<Item> listItems;
	private String nomJoueur;
	
	public BombermanGame() {
	}

	
	
	public ArrayList<AgentBomberman> getListAgentsBomberman() {
		return listAgentsBomberman;
	}
	public void setListAgentsBomberman(ArrayList<AgentBomberman> listAgentsBomberman) {
		this.listAgentsBomberman = listAgentsBomberman;
	}
	public ArrayList<AgentPNJ> getListAgentsPNJ() {
		return listAgentsPNJ;
	}
	public void setListAgentsPNJ(ArrayList<AgentPNJ> listAgentsPNJ) {
		this.listAgentsPNJ = listAgentsPNJ;
	}
	public boolean[][] getListBreakableWalls() {
		return listBreakableWalls;
	}
	public void setListBreakableWalls(boolean[][] listBreakableWalls) {
		this.listBreakableWalls = listBreakableWalls;
	}
	public ArrayList<Bombe> getListBombs() {
		return listBombs;
	}
	public void setListBombs(ArrayList<Bombe> listBombs) {
		this.listBombs = listBombs;
	}
	public ArrayList<Item> getListItems() {
		return listItems;
	}
	public void setListItems(ArrayList<Item> listItems) {
		this.listItems = listItems;
	}
	public String getNomJoueur() {
		return nomJoueur;
	}
	public void setNomJoueur(String nomJoueur) {
		this.nomJoueur = nomJoueur;
	}
	
}
