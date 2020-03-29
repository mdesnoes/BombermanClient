package com.projetBomberman.modele;

import java.util.ArrayList;

public class RadioTower {

	private int x;
	private int y;
	private ArrayList<AgentRajion> listRaijons;
	
	public RadioTower(int x, int y) {
		this.x = x;
		this.y = y;
		this.listRaijons = new ArrayList<AgentRajion>();
	}
	
	public void addListRajion(AgentRajion rajion) {
		this.listRaijons.add(rajion);
	}
	
	public void removeRajion(AgentRajion rajion) {
		this.listRaijons.remove(rajion);
	}
	
	public void clearListRajion() {
		this.listRaijons.clear();
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public ArrayList<AgentRajion> getListRaijon() {
		return this.listRaijons;
	}
	
}
