package com.projetBomberman.strategy;

import com.projetBomberman.modele.Agent;
import com.projetBomberman.modele.AgentAction;
import com.projetBomberman.modele.BombermanGame;
import com.projetBomberman.perceptron.SparseVector;

public class PerceptronStrategy implements Strategy {

	@Override
	public AgentAction chooseAction(BombermanGame bombermanGame, Agent agent) {

		SparseVector v = agent.encodeEtat(bombermanGame);

		System.out.println(v);
		
		return null;
	}

}
