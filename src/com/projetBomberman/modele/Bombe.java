package com.projetBomberman.modele;

import com.projetBomberman.modele.info.StateBomb;

public class Bombe {

	private int posX;
	private int posY;
	private int range;
	private StateBomb stateBomb;

	public Bombe() {
	}

	
	public int getPosX() {
		return posX;
	}
	public void setPosX(int posX) {
		this.posX = posX;
	}
	public int getPosY() {
		return posY;
	}
	public void setPosY(int posY) {
		this.posY = posY;
	}
	public int getRange() {
		return range;
	}
	public void setRange(int range) {
		this.range = range;
	}
	public StateBomb getStateBomb() {
		return stateBomb;
	}
	public void setStateBomb(StateBomb stateBomb) {
		this.stateBomb = stateBomb;
	}


}
