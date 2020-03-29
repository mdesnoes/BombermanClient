package com.projetBomberman.strategy;

import com.projetBomberman.modele.Agent;
import com.projetBomberman.modele.AgentAction;
import com.projetBomberman.modele.BombermanGame;

// L'agent avance tout droit et quand il rencontre un mur, il tourne a droite ou à gauche

public class BasiqueStrategy implements Strategy {

	public AgentAction chooseAction(BombermanGame bombermanGame, Agent agent) {
		AgentAction action = agent.getAction(); // L'agent conserve son action précedente

		if(action == null) {
			action = AgentAction.STOP;
		}
				
		//Si l'action précedente n'est pas un déplacement, il choisit un deplacement
		if(action == AgentAction.PUT_BOMB || action == AgentAction.STOP) {
			AgentAction[] tabAction = {AgentAction.MOVE_UP, AgentAction.MOVE_DOWN, AgentAction.MOVE_LEFT, AgentAction.MOVE_RIGHT};
			int nbRandom = (int) (Math.random() * tabAction.length);
			action = tabAction[nbRandom];
		}
		
		//Mais on verifie si le déplacement est possible, si ce n'est pas le cas, il tourne a droite/gauche ou en haut/bas
		if(!agent.isLegalMove(bombermanGame, action)) {
			AgentAction[] tabAction = new AgentAction[2];
			
			if(agent.getAction() == AgentAction.MOVE_UP || agent.getAction() == AgentAction.MOVE_DOWN) {
				tabAction[0] = AgentAction.MOVE_LEFT;
				tabAction[1] = AgentAction.MOVE_RIGHT;
			}
			else if(agent.getAction() == AgentAction.MOVE_LEFT || agent.getAction() == AgentAction.MOVE_RIGHT) {
				tabAction[0] = AgentAction.MOVE_DOWN;
				tabAction[1] = AgentAction.MOVE_UP;
			}
			
			int nbRandom = (int) (Math.random() * tabAction.length);
			return tabAction[nbRandom];
		}
				
		return action;
	}

}
