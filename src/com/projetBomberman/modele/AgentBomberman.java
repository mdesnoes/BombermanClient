package com.projetBomberman.modele;


public class AgentBomberman extends Agent {

	private boolean isInvincible = false;
	private boolean isSick = false;
	private int rangeBomb = 2;

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
	public int getRangeBomb() {
		return rangeBomb;
	}
	public void setRangeBomb(int rangeBomb) {
		this.rangeBomb = rangeBomb;
	}
	
}
