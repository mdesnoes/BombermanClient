package com.projetBomberman.modele;

public class Bombe {
	
	private int _pos_x;
	private int _pos_y;
	private int _range;
	private StateBomb _stateBomb;

	Bombe(int x, int y, int range, StateBomb stateBomb) {
		this._pos_x = x;
		this._pos_y = y;
		this._range = range;
		this._stateBomb = stateBomb;
	}
	
	public int getX() {
		return this._pos_x;
	}

	public int getY() {
		return this._pos_y;
	}

	public int getRange() {
		return this._range;
	}
	
	void setRange(int range) {
		if(range > 1) {
			this._range = range;
		}
	}

	public StateBomb getStateBomb() {
		return this._stateBomb;
	}

	private void changeStateBomb() {
		switch(this._stateBomb) {
			case Step1:
				this._stateBomb = StateBomb.Step2;
				break;
			case Step2:
				this._stateBomb = StateBomb.Step3;
				break;
			case Step3:
				this._stateBomb = StateBomb.Boom;
				break;
			case Boom:
				break;
		}
	}
	
	void explosion(BombermanGame bombermanGame) {
		AgentBomberman agentBomberman = bombermanGame.getAgentBombermanByBomb(this);
		
		//Si agentBomberman != null, cela veut dire que la bombe appartient à un agent
		if(agentBomberman != null) {
			if(this._stateBomb == StateBomb.Boom) {					
				this.destroyBreakableWall(bombermanGame); // On detruit les murs cassables
				this.destroyAgent(agentBomberman, bombermanGame); // On detruit les autres agents
				bombermanGame.addListBombeDetruite(this); //Bombe à supprimer de la liste
				agentBomberman.removeBombe(this); //L'agent peut reposer une nouvelle bombe
			}
			else {
				//Nouvelle etat des bombes de l'agent
				for(Bombe bomb : agentBomberman.getListBombe()) {
					bomb.changeStateBomb();
				}
			}
		}
		// Sinon cela veut dire que la bombe n'a plus de propriétaire (l'agent à été detruit avant que ça bombe n'explose) 
		else {
			if(this.getStateBomb() == StateBomb.Boom) {
				this.destroyBreakableWall(bombermanGame);
				this.destroyAgent(null, bombermanGame);
				bombermanGame.addListBombeDetruite(this); //Bombe à supprimer de la liste
			}
			else {
				this.changeStateBomb();
			}
		}
	}
	
	private void destroyBreakableWall(BombermanGame bombermanGame) {
		//Sur la ligne 
		for(int i=this._pos_x - this._range; i<=this._pos_x + this._range; ++i) {
			if(bombermanGame.appartientMap(i, this._pos_y)) { // On verifie que les coordonnées appartiennent à la map
				if(bombermanGame.getListBreakableWall()[i][this._pos_y]) { // On regarde si il y a un mur au coordonnées courante
					bombermanGame.getListBreakableWall()[i][this._pos_y] = false;
					bombermanGame.setReward(bombermanGame.getReward() + 10);
					
					//Probabilité qu'un item apparaisse (1 chance sur 10), tout les items on la meme probabilite
					int nb = (int) (Math.random() * bombermanGame.getProbabiliteObjet());

					if(nb == 1) {
						ItemType[] tabItem = {ItemType.FIRE_UP,ItemType.FIRE_DOWN,ItemType.BOMB_UP,ItemType.BOMB_DOWN,ItemType.FIRE_SUIT,ItemType.SKULL}; 
						int nbRandom = (int) (Math.random() * tabItem.length);
						Item item = new Item(i, this._pos_y, tabItem[nbRandom]);
						bombermanGame.addListItems(item);
					}
				}
			}
		}
		
		//Sur la colonne
		for(int i=this._pos_y - this._range; i<=this._pos_y + this._range; ++i) {
			if(bombermanGame.appartientMap(this._pos_x, i)) {
				if(bombermanGame.getListBreakableWall()[this._pos_x][i]) {
					bombermanGame.getListBreakableWall()[this._pos_x][i] = false;
					bombermanGame.setReward(bombermanGame.getReward() + 10);

					//Probabilité qu'un item apparaisse (1 chance sur 10)
					int nb = (int) (Math.random() * bombermanGame.getProbabiliteObjet());

					if(nb == 1) {
						ItemType[] tabItem = {ItemType.FIRE_UP,ItemType.FIRE_DOWN,ItemType.BOMB_UP,ItemType.BOMB_DOWN,ItemType.FIRE_SUIT,ItemType.SKULL}; 
						int nbRandom = (int) (Math.random() * tabItem.length);
						Item item = new Item(this._pos_x, i, tabItem[nbRandom]);
						bombermanGame.addListItems(item);
					}
				}
			}
		}
	}

	private void destroyAgent(AgentBomberman agentProprietaireBomb, BombermanGame bombermanGame) {
		//Sur la ligne 
		for(int i=this._pos_x - this._range; i<=this._pos_x + this._range; ++i) {
			Agent agent = bombermanGame.getAgentByCoord(i, this._pos_y);

			if(agent != null && agent != agentProprietaireBomb) { // Si on trouve un agent different de celui qui a posé la bombe
				if(!agent.isInvincible()) {
					if(agentProprietaireBomb != null) { 
						System.out.println("L'agent " + agentProprietaireBomb.getColor() + " à détruit l'agent " + agent.getColor());
					}
					
					for(RadioTower rd : bombermanGame.getListRadioTower()) {
						if(rd.getListRaijon().contains(agent)) {
							rd.removeRajion((AgentRajion) agent);
						}
					}

					bombermanGame.addListAgentDetruit(agent);
					bombermanGame.setReward(bombermanGame.getReward() + 50);
				}
			}
		}
		
		//Sur la colonne
		for(int i=this._pos_y - this._range; i<=this._pos_y + this._range; ++i) {
			Agent agent = bombermanGame.getAgentByCoord(this._pos_x, i);

			if(agent != null && agent != agentProprietaireBomb) {
				if(!agent.isInvincible()) {
					if(agentProprietaireBomb != null) {
						System.out.println("L'agent " + agentProprietaireBomb.getColor() + " à détruit l'agent " + agent.getColor());
					}
					
					for(RadioTower rd : bombermanGame.getListRadioTower()) {
						if(rd.getListRaijon().contains(agent)) {
							rd.removeRajion((AgentRajion) agent);
						}
					}

					bombermanGame.addListAgentDetruit(agent);
					bombermanGame.setReward(bombermanGame.getReward() + 50);
				}
			}
		}
	}

}
