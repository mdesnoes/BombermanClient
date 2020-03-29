package com.projetBomberman.strategy;

import com.projetBomberman.modele.Agent;
import com.projetBomberman.modele.AgentAction;
import com.projetBomberman.modele.BombermanGame;

public interface Strategy {
	
	AgentAction chooseAction(BombermanGame bombermanGame, Agent agent);
}
