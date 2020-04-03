package com.projetBomberman.modele;


public abstract class Game {

	private int turn;
	private int maxturn;
	private long time;

	public Game() {
	}
	
	
	public int getTurn() {
		return turn;
	}
	public void setTurn(int turn) {
		this.turn = turn;
	}
	public int getMaxturn() {
		return maxturn;
	}
	public void setMaxturn(int maxturn) {
		this.maxturn = maxturn;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	

}
