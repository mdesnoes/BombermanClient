package com.projetBomberman.modele;

import java.util.ArrayList;

import com.projetBomberman.perceptron.SparseVector;
import com.projetBomberman.strategy.Strategy;

public class AgentBomberman extends Agent {

	private ArrayList<Bombe> _listBombes = new ArrayList<>();
	private int _nbBombe = 1;
	private boolean _isInvincible = false;
	private boolean _isSick = false;
	private int _nbTurnBonusInvincible = 0;
	private int _nbTurnMalusSick = 0;
	private int _rangeBomb = 2;

	public AgentBomberman(int pos_x, int pos_y, char type, ColorAgent color, Strategy strategy) {
		super(pos_x,pos_y, type, color, strategy);
	}
	
	public void executer(BombermanGame bombermanGame) {

		AgentAction action = this.getStrategy().chooseAction(bombermanGame, this);
		this.setAction(action);
		
		if(action == AgentAction.PUT_BOMB) {
			Bombe bombe = new Bombe(this.getX(), this.getY(), this.getRangeBomb(), StateBomb.Step1);
			bombermanGame.addListBombs(bombe);
			this.addBombe(bombe);
		}
		else if(action == AgentAction.MOVE_UP || action == AgentAction.MOVE_DOWN 
				|| action == AgentAction.MOVE_LEFT || action == AgentAction.MOVE_RIGHT) {
			if(this.isLegalMove(bombermanGame, action)) {
				this.moveAgent(action);
				
				if(bombermanGame.getModeJeu() != ModeJeu.PERCEPTRON) {
					for(Item item : bombermanGame.getListItem()) {
						if(this.getX() == item.getX() && this.getY() == item.getY()) {
							this.takeItem(item);
							bombermanGame.addListItemUtilise(item);
						}
					}
				}
			}
		}
	}
	
	
	
	public SparseVector encoderCoupleEtatAction(SparseVector v, AgentAction action) {
		
		return v;
	}
	
	public boolean isLegalMove(BombermanGame bombGame, AgentAction action) {
		int newX = this.getX();
    	int newY = this.getY();

    	switch(action) {
			case MOVE_UP:
				newY--;
				break;
			case MOVE_DOWN:
				newY++;
				break;
			case MOVE_LEFT:
				newX--;
				break;
			case MOVE_RIGHT:
				newX++;
				break;
			default:
				break;
    	}
    	
    	//On verifie si l'agent sort de la map ou non
    	if(!bombGame.appartientMap(newX,  newY)) {
    		return false;
    	}
    	
    	//On verifie s'il y a un mur, un mur cassable ou une bombe sur la nouvelle case
    	if(bombGame.getControllerBombGame().getMap().get_walls()[newX][newY] || bombGame.getListBreakableWall()[newX][newY] 
    		|| bombGame.getBombByCoord(newX, newY) != null) {
    		return false;
    	}
    	
    	 //Un agent bomberman ne peut pas se deplacer sur un autre agent
		return bombGame.getAgentByCoord(newX, newY) == null;
	}

	public void moveAgent(AgentAction action) {
		switch(action) {
			case MOVE_UP:
				this.setY(this.getY() - 1);
				break;
			case MOVE_DOWN:
				this.setY(this.getY() + 1);
				break;
			case MOVE_LEFT:
				this.setX(this.getX() - 1);
				break;
			case MOVE_RIGHT:
				this.setX(this.getX() + 1);
				break;
			default:
				break;
		}
	}
	
	
	private void takeItem(Item item) {
		switch(item.getType()) {
		case FIRE_UP:
			this._rangeBomb++;
			
			//On met a jour les bombes déjà poser sur le terrain
			for(Bombe bombAgent : this._listBombes) {
				bombAgent.setRange(this._rangeBomb);
			}

			break;
		case FIRE_DOWN:
			this._rangeBomb--;

			//On met a jour les bombes déjà poser sur le terrain
			for(Bombe bombAgent : this._listBombes) {
				bombAgent.setRange(this._rangeBomb);
			}

			break;	
		case BOMB_UP:
			this._nbBombe++;
			break;
		case BOMB_DOWN:
			this._nbBombe --;
			break;
		case FIRE_SUIT:
			this._isInvincible = true;
			break;
		case SKULL:
			this._isSick = true;
			break;
		}
	}
	
	public boolean isInvincible() {
		return this._isInvincible;
	}

	private void addBombe(Bombe bomb) {
		this._listBombes.add(bomb);
	}
	
	void removeBombe(Bombe bomb) {
		this._listBombes.remove(bomb);
	}
	
	public boolean canPutBomb() {
		if(this._isSick) {
			return false;
		}

		return this._listBombes.size() < this._nbBombe;
	}

	public void setListBombe(ArrayList<Bombe> bombes) {
		this._listBombes = bombes;
	}
	
	ArrayList<Bombe> getListBombe() {
		return this._listBombes;
	}
	
	int getNbTurnBonusInvincible() {
		return this._nbTurnBonusInvincible;
	}
	
	void setNbTurnBonusInvincible(int i) {
		this._nbTurnBonusInvincible = i;
	}
	
	int getNbTurnMalusSick() {
		return this._nbTurnMalusSick;
	}
	
	void setNbTurnMalusSick(int i) {
		this._nbTurnMalusSick = i;
	}
	
	public int getNbBombe() {
		return this._nbBombe;
	}

	public void setNbBombe(int nb) {
		if(nb >= 1) {
			this._nbBombe = nb;
		}
	}
	
	public int getRangeBomb() {
		return this._rangeBomb;
	}
	
	public void setRangeBomb(int range) {
		if(range >= 1) {
			this._rangeBomb = range;
		}
	}
	
	void setIsInvincible(boolean b) {
		this._isInvincible = b;
	}
	
	public boolean getIsSick() {
		return this._isSick;
	}
	
	void setIsSick(boolean b) {
		this._isSick = b;
	}
}
