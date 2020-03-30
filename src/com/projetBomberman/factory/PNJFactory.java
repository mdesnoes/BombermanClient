package com.projetBomberman.factory;

import com.projetBomberman.modele.Agent;
import com.projetBomberman.modele.AgentBird;
import com.projetBomberman.modele.AgentEnnemyBasique;
import com.projetBomberman.modele.AgentRajion;
import com.projetBomberman.modele.ColorAgent;
import com.projetBomberman.strategy.BasiqueStrategy;
import com.projetBomberman.strategy.ReveilStrategy;
import com.projetBomberman.strategy.Strategy;
import com.projetBomberman.strategy.TeteChercheuseStrategy;

public class PNJFactory implements AgentFactory {

	public Agent createAgent(int pos_x, int pos_y, char type, Strategy strategy) {
		switch(type) {
			case 'R':
				return new AgentRajion(pos_x, pos_y, type, ColorAgent.ROUGE, new TeteChercheuseStrategy());
			case 'V':
				return new AgentBird(pos_x, pos_y, type, ColorAgent.JAUNE, new ReveilStrategy());
			case 'E':
				return new AgentEnnemyBasique(pos_x, pos_y, type, ColorAgent.BLANC, new BasiqueStrategy());
			default:
				return null;
		}
	}

}
