package com.projetBomberman.strategy;

import com.projetBomberman.modele.Agent;
import com.projetBomberman.modele.AgentAction;
import com.projetBomberman.modele.BombermanGame;

// Si l'agent detecte un mur cassable devant lui, il pose une bombe

public class BreakWallStrategy implements Strategy {

	private AgentAction action = null;
	
	public void setAction(AgentAction action) {
		this.action = action;
	}
	
	public AgentAction chooseAction(BombermanGame bombermanGame, Agent agent) {
			
		//Coordonnée de la case dans lequelle l'agent veut avancer
		int newX = agent.getX();
		int newY = agent.getY();

		switch(this.action) {
			case MOVE_UP: newY--; break;
			case MOVE_DOWN: newY++; break;
			case MOVE_LEFT: newX--; break;
			case MOVE_RIGHT: newX++; break;
		}
			
		//Si sur cette case il y a un mur cassable, alors l'agent pose une bombe
		if(bombermanGame.getListBreakableWall()[newX][newY]) {
			if(agent.canPutBomb()) {
				return AgentAction.PUT_BOMB;
			}
		}
				
		return AgentAction.STOP;
	}

}
