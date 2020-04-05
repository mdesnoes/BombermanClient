package com.projet.modele;


public class AgentBomberman extends Agent {

	private boolean isInvincible = false;
	private boolean isSick = false;

	public AgentBomberman() {
		super();
	}
	
	

	public boolean isInvincible() {
		return isInvincible;
	}
	public void setInvincible(boolean isInvincible) {
		this.isInvincible = isInvincible;
	}
	public boolean isSick() {
		return isSick;
	}
	public void setSick(boolean isSick) {
		this.isSick = isSick;
	}
	
}
