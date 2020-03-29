package com.projetBomberman.strategy;

import java.awt.event.KeyEvent;

import com.projetBomberman.modele.Agent;
import com.projetBomberman.modele.AgentAction;
import com.projetBomberman.modele.BombermanGame;
import com.projetBomberman.view.ViewModeInteractif;

public class InteractifStrategyCommande2 implements Strategy {

	@Override
	public AgentAction chooseAction(BombermanGame bombermanGame, Agent agent) {
		ViewModeInteractif viewModeInteractif = ViewModeInteractif.getInstance();
		viewModeInteractif.addKeyListener(viewModeInteractif);

		viewModeInteractif.requestFocus();		
		
		switch (viewModeInteractif.getKeyPressed())
        {
            case KeyEvent.VK_Z:
                return AgentAction.MOVE_UP;
            case KeyEvent.VK_Q:
            	return AgentAction.MOVE_LEFT;
            case KeyEvent.VK_D:
            	return AgentAction.MOVE_RIGHT;
            case KeyEvent.VK_S:
            	return AgentAction.MOVE_DOWN;
            case KeyEvent.VK_F:
            	return AgentAction.PUT_BOMB;
            case KeyEvent.VK_A:
            	return AgentAction.STOP;
            default:
            	return AgentAction.STOP;
        }
	}

}
