package com.projetBomberman.modele;

import com.projetBomberman.strategy.Strategy;

public class AgentBird extends AgentPNJ {

	public AgentBird(int pos_x, int pos_y, char type, ColorAgent color, Strategy strategy) {
		super(pos_x, pos_y, type, color, strategy);
	}
	
	public boolean isLegalMove(BombermanGame bombGame, AgentAction action) {
		int newX = this.getX();
    	int newY = this.getY();

    	switch(action) {
			case MOVE_UP:
				newY--;
				break;
			case MOVE_DOWN:
				newY++;
				break;
			case MOVE_LEFT:
				newX--;
				break;
			case MOVE_RIGHT:
				newX++;
				break;
			default:
				break;
    	}
    	
    	//On verifie si l'agent sort de la map ou non
    	if(!bombGame.appartientMap(newX,newY)) {
    		return false;
    	}
    	
    	//Un agent Bird ne peut pas se deplacer sur un autre agent PNJ
		return bombGame.getAgentPNJByCoord(newX, newY) == null;
	}

}
