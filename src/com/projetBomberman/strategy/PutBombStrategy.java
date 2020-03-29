package com.projetBomberman.strategy;

import com.projetBomberman.modele.Agent;
import com.projetBomberman.modele.AgentAction;
import com.projetBomberman.modele.AgentBomberman;
import com.projetBomberman.modele.BombermanGame;

// Strategy pour les bomberman : 
// S'il y a un agent dans le rayon de ça bombe, il pose une bombe
// S'il est gené par un mur cassable, il pose une bombe grâce à la stratégy BreakWallStrategy
// S'il est gené par un mur non cassable ou qu'il ne peut pas poser de bombe, il fait un deplacement random (cela permet egalement de debloquer les agents)
// Sinon il se deplace avec la RaijonStrategy
// Mais ça priorité est d'abord d'esquiver les bombes grâce à la stratégie EsquiveStrategy

public class PutBombStrategy implements Strategy {
	
	private EsquiveStrategy esquiveStrategy = new EsquiveStrategy();
	private TeteChercheuseStrategy raijonStrategy = new TeteChercheuseStrategy();
	private BreakWallStrategy breakWallStrategy = new BreakWallStrategy();
	private RandomStrategy randomStrategy = new RandomStrategy();

	public AgentAction chooseAction(BombermanGame bombermanGame, Agent agent) {
		
		// Priorité : il esquive les bombes
		AgentAction esquiveAction = esquiveStrategy.chooseAction(bombermanGame, agent);

		if(esquiveAction != AgentAction.STOP) {
			return esquiveAction;
		}

		// Il pose une bombe si il detecte un agent ennemie
		for(int i=agent.getX() - ((AgentBomberman) agent).getRangeBomb(); i<=agent.getX() + ((AgentBomberman) agent).getRangeBomb(); ++i) {
			for(int j=agent.getY() - ((AgentBomberman) agent).getRangeBomb(); j<=agent.getY() + ((AgentBomberman) agent).getRangeBomb(); ++j) {
				Agent agentDetecte = bombermanGame.getAgentByCoord(i, j);

				if(agentDetecte != agent && agentDetecte != null) {
					//System.out.println("L'agent " +agent.getColor() + " a detecté l'agent " +  bombermanGame.getAgentBombermanByCoord(i, j).getColor());
					if(agent.canPutBomb()) {
						return AgentAction.PUT_BOMB;
					}

					break; // il ne peut pas poser de bombe donc ça ne sert à rien de parcourir le reste de la boucle
				}
			}
		}
		
		AgentAction actionRaijonStrat = raijonStrategy.chooseAction(bombermanGame, agent);

		if(!agent.isLegalMove(bombermanGame, actionRaijonStrat)) {
			breakWallStrategy.setAction(actionRaijonStrat);

			if(breakWallStrategy.chooseAction(bombermanGame, agent) != AgentAction.STOP) {
				//System.out.println("L'agent " +agent.getColor() + " est bloqué par un mur cassable, il pose une bombe");
				return AgentAction.PUT_BOMB;
			}
			else {
				//System.out.println("L'agent " +agent.getColor() + " est bloqué, il fait une action random");
				return randomStrategy.chooseAction(bombermanGame, agent);
			}
		}
		
		//System.out.println("L'agent " +agent.getColor() + " avance vers un agent");
		return actionRaijonStrat;
	}

}
