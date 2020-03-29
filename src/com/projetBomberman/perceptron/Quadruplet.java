package com.projetBomberman.perceptron;

import com.projetBomberman.modele.AgentAction;

public class Quadruplet {

	private SparseVector etat, atteint;
	private AgentAction action;
	private double reward;
	
	public Quadruplet(SparseVector etat,AgentAction action,SparseVector atteint,double reward){
		this.etat=etat;
		this.action=action;
		this.atteint=atteint;
		this.reward=reward;
	}

	public SparseVector getEtat() {
		return etat;
	}

	public SparseVector getAtteint() {
		return atteint;
	}

	public AgentAction getAction() {
		return action;
	}

	public double getReward() {
		return reward;
	}
	
	
	
}
