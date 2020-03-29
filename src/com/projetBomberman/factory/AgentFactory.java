package com.projetBomberman.factory;

import com.projetBomberman.modele.Agent;
import com.projetBomberman.strategy.Strategy;

public interface AgentFactory {
		
	Agent createAgent(int pos_x, int pos_y, char type, Strategy strategy);
	
}
