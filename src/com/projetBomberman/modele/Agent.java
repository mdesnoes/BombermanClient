package com.projetBomberman.modele;

import com.projetBomberman.modele.info.AgentAction;
import com.projetBomberman.modele.info.ColorAgent;

public abstract class Agent {
	
	private int posX;
	private int posY;
	private ColorAgent color;
	private AgentAction action;
	private char type;
	
	public Agent() {
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
	public ColorAgent getColor() {
		return color;
	}
	public void setColor(ColorAgent color) {
		this.color = color;
	}
	public AgentAction getAction() {
		return action;
	}
	public void setAction(AgentAction action) {
		this.action = action;
	}
	public char getType() {
		return type;
	}
	public void setType(char type) {
		this.type = type;
	}

}
