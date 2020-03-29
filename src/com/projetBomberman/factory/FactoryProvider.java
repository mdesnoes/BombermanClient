package com.projetBomberman.factory;

public abstract class FactoryProvider {
	
	public static AgentFactory getFactory(char typeAgent) {
		AgentFactory agentFactory;

		if (typeAgent == 'B') {
			agentFactory = new BombermanFactory();
		} else {
			agentFactory = new PNJFactory();
		}

		return agentFactory;
	}

}
