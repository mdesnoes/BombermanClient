package com.projetBomberman.strategy;

import java.util.ArrayList;

import com.projetBomberman.modele.Agent;
import com.projetBomberman.modele.AgentAction;
import com.projetBomberman.modele.AgentBomberman;
import com.projetBomberman.modele.BombermanGame;

public class ReveilStrategy implements Strategy {

	private static final int rayonAction = 5;
    private ArrayList<AgentBomberman> agentRayon = new ArrayList<>();

    public AgentAction chooseAction(BombermanGame b, Agent a) {
    	agentRayon.clear();

        for (AgentBomberman i : b.getListAgentBomberman()) {
            if ((Math.abs(a.getX() - i.getX()) < rayonAction) && (Math.abs(a.getY() - i.getY()) < rayonAction)) {
                agentRayon.add(i);
            }
        }
        
        if (!agentRayon.isEmpty()) {
            int procheDist = rayonAction*2;
            AgentBomberman procheBomb = null;

            for (AgentBomberman i : agentRayon) {
                int dist = Math.abs(a.getX() - i.getX()) + Math.abs(a.getY() - i.getY());
                if (dist < procheDist) {
                    procheDist = dist;
                    procheBomb = i;
                }
            }

            assert procheBomb != null;

            if (Math.abs(a.getX() - procheBomb.getX()) >= Math.abs(a.getY() - procheBomb.getY())) {
                if (a.getX() < procheBomb.getX()) {
                    return AgentAction.MOVE_RIGHT;
                } else {
                    return AgentAction.MOVE_LEFT;
                }
            } else {
                if (a.getY() < procheBomb.getY()) {
                    return AgentAction.MOVE_DOWN;
                } else {
                    return AgentAction.MOVE_UP;
                }
            }
        } else {
            return AgentAction.STOP;
        }
    }

}
