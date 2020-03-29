package com.projetBomberman.strategy;

import com.projetBomberman.modele.Agent;
import com.projetBomberman.modele.AgentAction;
import com.projetBomberman.modele.AgentBomberman;
import com.projetBomberman.modele.AgentPNJ;
import com.projetBomberman.modele.BombermanGame;

public class TeteChercheuseStrategy implements Strategy {
		
	private RandomStrategy randomStrategy = new RandomStrategy();
	
	public AgentAction chooseAction(BombermanGame bombermanGame, Agent agent) {
		AgentAction action = AgentAction.STOP;
		int procheDist = 1000;
		Agent procheBomb = null;

		for (AgentBomberman i : bombermanGame.getListAgentBomberman()) {
			if(i != agent) {
				int dist = Math.abs(agent.getX() - i.getX()) + Math.abs(agent.getY() - i.getY());

				if (dist < procheDist) {
					procheDist = dist;
					procheBomb = i;
				}
			}
		}
		
		//S'il n'y a plus de bomberman sur la carte, l'agent va en direction d'un PNJ
		if(procheBomb == null) {
			for (AgentPNJ i : bombermanGame.getListAgentPNJ()) {
				if(i != agent) {
					int dist = Math.abs(agent.getX() - i.getX()) + Math.abs(agent.getY() - i.getY());
					if (dist < procheDist) {
						procheDist = dist;
						procheBomb = i;
					}
				}
			}		
		}
		
		if(procheBomb != null) { // condition pour verifier s'il y a bien un autre bomberman sur la map
			if (Math.abs(agent.getX() - procheBomb.getX()) >= Math.abs(agent.getY() - procheBomb.getY())) {
				if (agent.getX() < procheBomb.getX()) {
					action = AgentAction.MOVE_RIGHT;
				} else {
					action = AgentAction.MOVE_LEFT;
				}
			} else {
				if (agent.getY() < procheBomb.getY()) {
					action = AgentAction.MOVE_DOWN;
				} else {
					action = AgentAction.MOVE_UP;
				}
			}
		}
		
		// Si l'agent est bloqué par un mur, il faut une action random pour se debloquer
		if(!agent.isLegalMove(bombermanGame, action)) {
			return randomStrategy.chooseAction(bombermanGame, agent);
		}

		return action;
	}

}
