package com.projetBomberman.modele;

import com.projetBomberman.strategy.Strategy;

public class AgentEnnemyBasique extends AgentPNJ {

	public AgentEnnemyBasique(int pos_x, int pos_y, char type, ColorAgent color, Strategy strategy) {
		super(pos_x, pos_y, type, color, strategy);
	}

}
