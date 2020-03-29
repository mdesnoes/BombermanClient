package com.projetBomberman.modele;

import com.projetBomberman.perceptron.SparseVector;
import com.projetBomberman.strategy.Strategy;

public abstract class Agent {

	private static int _compteur = 0;
	private int _id;
	private int _pos_x;
	private int _pos_y;
	private ColorAgent _color;
	private AgentAction _action;
	private char _type;
	private Strategy _strategy;
	
	public Agent(int pos_x, int pos_y, char type, ColorAgent color, Strategy strategy) {
		this._pos_x = pos_x;
		this._pos_y = pos_y;
		this._type = type;
		this._color = color;
		this._strategy = strategy;
		this._id = Agent._compteur;
		Agent._compteur++;
	}
	
	public void setX(int x) {
		this._pos_x = x;
	}
	
	public void setY(int y) {
		this._pos_y = y;
	}
	
	public int getX() {
		return this._pos_x;
	}
	
	public int getY() {
		return this._pos_y;
	}

	public int getId() {
		return this._id;
	}
	
	public ColorAgent getColor() {
		return this._color;
	}
	
	public char getType() {
		return this._type;
	}
	
	public AgentAction getAction() {
		return this._action;
	}
	
	public void setAction(AgentAction action) {
		this._action = action;
	}
	
	public Strategy getStrategy() {
		return this._strategy;
	}
	
	public void setStrategy(Strategy strategy) {
		this._strategy = strategy;
	}
	
	public SparseVector encodeEtat(BombermanGame bombGame) {
		int champsVision = 2;
		SparseVector etatEnnemy = new SparseVector(25);
		SparseVector etatBombe = new SparseVector(25);
		SparseVector etatMur = new SparseVector(25);
		
		int cpt = 1;
		for(int i=this.getX() - champsVision; i<=this.getX() + champsVision; ++i) {
			for(int j=this.getY() - champsVision; j<=this.getY() + champsVision; ++j) {

				if(bombGame.appartientMap(i, j)) {
					if(bombGame.getAgentByCoord(i, j) != null && bombGame.getAgentByCoord(i, j) != this) {
						etatEnnemy.setValue(cpt, 1);
					}
					else if(bombGame.getBombByCoord(i, j) != null) {
						etatBombe.setValue(cpt, 1);
					}
					else if(bombGame.getListBreakableWall()[i][j]) {
						etatMur.setValue(cpt, 1);
					}
				}
				
				++cpt;
			}
		}
		
		SparseVector etat = new SparseVector(75);
		etat.addVector(etatEnnemy, 25);
		etat.addVector(etatBombe, 50);
		etat.addVector(etatMur, 75);
		
		return etat;
	}
	

	public abstract boolean isInvincible();
	public abstract boolean canPutBomb();
	public abstract void executer(BombermanGame bombermanGame);
	public abstract boolean isLegalMove(BombermanGame bombGame, AgentAction action);
	public abstract void moveAgent(AgentAction action);

}
