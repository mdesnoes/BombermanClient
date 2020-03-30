package com.projetBomberman.modele;

import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import com.projetBomberman.controller.ControllerBombermanGame;
import com.projetBomberman.factory.AgentFactory;
import com.projetBomberman.factory.FactoryProvider;
import com.projetBomberman.strategy.*;
import com.projetBomberman.view.InfoAgent;
import com.projetBomberman.view.ViewGagnant;
import com.projetProgReseau.client.Client;


public class BombermanGame extends Game {

	private final static int TURN_MAX_ITEM = 5;
	private final static int PROBABILITE_OBJET = 6;
	private final static int MAX_RAJION_RADIO_TOWER = 10;
	
	
	private ModeJeu mode;
	private ControllerBombermanGame _controllerBombGame;
    private ArrayList<AgentBomberman> _listAgentsBomberman;
    private ArrayList<AgentPNJ> _listAgentsPNJ;
	private boolean[][] _listBreakableWalls;
	private ArrayList<RadioTower> _listRadioTower;
	private ArrayList<Bombe> _listBombs;
	private ArrayList<Item> _listItems;
	private ArrayList<Agent> _listAgentsDetruit = new ArrayList<>();
	private ArrayList<Bombe> _listBombesDetruite = new ArrayList<>();
	private ArrayList<Item> _listItemsUtilise = new ArrayList<>();
	private int reward = 0;
	private Strategy agentStrategy;
	
	private Timestamp dateDebutPartie;
	private String nomJoueur;
	private AgentBomberman bombermanJoueur1;
	private AgentBomberman bombermanJoueur2;
	private PrintWriter sortie;
	
	
	public BombermanGame(Client client, ModeJeu mode, Strategy agentStrategy, int maxturn) {
		super(maxturn);
		this.sortie = client.getSortie();
		this.nomJoueur = client.getNom();
		this.mode = mode;
		this._controllerBombGame = new ControllerBombermanGame(this, client);
		this.agentStrategy = agentStrategy;
	}

	public void initialize_game() {
		Date dateCourante = new Date();
		this.dateDebutPartie = new Timestamp(dateCourante.getTime());
		
		System.out.println("Le jeu est initialisé");
		this._listAgentsBomberman = new ArrayList<>();
		this._listAgentsPNJ = new ArrayList<>();
		
		boolean[][] _startBreakableWalls = this._controllerBombGame.getMap().getStart_breakable_walls();
		
		// Copie profonde du tableau des murs cassables pour pouvoir les faire réaparaitre quand on réinitialise le jeu
		int x = 0;
		int y = 0;
		this._listBreakableWalls = new boolean[_startBreakableWalls.length][_startBreakableWalls[x].length];
		for (boolean[] tab : _startBreakableWalls) {
			for (boolean b : tab) {
				this._listBreakableWalls[x][y] = b;
				++y;
			}
			++x;
			y=0;
		}
		
		
		this._listBombs = new ArrayList<>();
		this._listItems = new ArrayList<>();
		ArrayList<InfoAgent> listAgentInit = this._controllerBombGame.getMap().getStart_agents();

		for(InfoAgent agent : listAgentInit) {
			AgentFactory agentFactory = FactoryProvider.getFactory(agent.getType());

			if(agent.getType() == 'B') {
			    this._listAgentsBomberman.add((AgentBomberman) agentFactory.createAgent(agent.getX(), agent.getY(), agent.getType(), agentStrategy));
			}
			else {
				Strategy strategy = null;
				//En mode PERCEPTRON, tout les ennemis sont en mode Random
				if(this.mode == ModeJeu.PERCEPTRON) {
					strategy = new RandomStrategy();
				}
		    	this._listAgentsPNJ.add((AgentPNJ) agentFactory.createAgent(agent.getX(), agent.getY(), agent.getType(), strategy));
			}

			System.out.println(agent.getX() + " - " + agent.getY() + " type : " + agent.getType());
		}
		
		this.bombermanJoueur1 = this._listAgentsBomberman.get(0);
		//En mode solo, on contrôle le premier agent
		if(this.mode == ModeJeu.SOLO) {
			this.bombermanJoueur1.setStrategy(new InteractifStrategyCommande1());
		}
		//En mode duo ou duel, on peut controler les deux premiers agents (avec des touches différentes)
		else if(this.mode == ModeJeu.DUO || this.mode == ModeJeu.DUEL) {
			this.bombermanJoueur2 = this._listAgentsBomberman.get(1);
			this.bombermanJoueur1.setStrategy(new InteractifStrategyCommande1());
			this.bombermanJoueur2.setStrategy(new InteractifStrategyCommande2());
		}
		
		
		//Création des radio tower
		this._listRadioTower = this._controllerBombGame.getMap().getListRadioTower();
		for(RadioTower rd : this._listRadioTower) {
			rd.clearListRajion();
		}
	}

	public void takeTurn() {
		this._listAgentsDetruit = new ArrayList<>();
		this._listBombesDetruite = new ArrayList<>();
		this._listItemsUtilise = new ArrayList<>();

		//Action des agents bomberman
		for (AgentBomberman agentBomberman: this._listAgentsBomberman) {
			//Verification des malus/bonus des bomberman
			if(agentBomberman.isInvincible()) {
				//Au bout de 5 tours, l'agent n'est plus invincible
				if(agentBomberman.getNbTurnBonusInvincible() >= TURN_MAX_ITEM) {
					agentBomberman.setIsInvincible(false);
					agentBomberman.setNbTurnBonusInvincible(0);
				} else {
					agentBomberman.setNbTurnBonusInvincible(agentBomberman.getNbTurnBonusInvincible() + 1);
				}
			}

			if(agentBomberman.getIsSick()) {
				//Au bout de 5 tours, l'agent n'est plus malade
				if(agentBomberman.getNbTurnMalusSick() >= TURN_MAX_ITEM) {
					agentBomberman.setIsSick(false);
					agentBomberman.setNbTurnMalusSick(0);
				} else {
					agentBomberman.setNbTurnMalusSick(agentBomberman.getNbTurnMalusSick() + 1);
				}
			}

			//On effectuer l'action du bomberman
			agentBomberman.executer(this);
		}

		//On supprime les items ramassés par les agents
		for(Item item : this._listItemsUtilise) {
			this._listItems.remove(item);
		}

		//Actions des PNJ survivants
		for(AgentPNJ agentPNJ : this._listAgentsPNJ) {
			agentPNJ.executer(this);
		}

		//Explosion des bombes
		for(Bombe bomb : this._listBombs) {
			bomb.explosion(this);
		}

		//On supprime les agents qui ont été detruit suite à l'explosion des bombes
		for(Agent agent : this._listAgentsDetruit) {
			if(agent instanceof AgentBomberman) {
				this._listAgentsBomberman.remove(agent);
			}
			else if(agent instanceof AgentPNJ){
				this._listAgentsPNJ.remove(agent);
			}
		}

		//On supprime les bombes qui ont explosé
		for(Bombe bomb : this._listBombesDetruite) {
			this._listBombs.remove(bomb);
		}
		
		
		//Action des radio tower, si elle existe
		for(RadioTower rd : this._listRadioTower) {
			//Un rajion apparait tout les 4 tours.
			if(this._turn % 4 == 0) {
				//Il peut y avoir que 10 rajions maximum
				if(rd.getListRaijon().size() < MAX_RAJION_RADIO_TOWER) {
					AgentFactory agentFactory = FactoryProvider.getFactory('R');
					
					Agent rajion = agentFactory.createAgent(rd.getX(), rd.getY(), 'R', null);
					this._listAgentsPNJ.add((AgentPNJ) rajion);
					
					rd.addListRajion((AgentRajion) rajion);
				}
			}
		}
		
   	}

	


	public boolean gameContinue() {
		//S'il y a au moins une radio tower, le jeu s'arrete seulement si le bomberman est détruit, le but du bomberman est de survivre jusqu'à la fin
		if(!this._listRadioTower.isEmpty()) {
			if(this._listAgentsBomberman.isEmpty()) { 
				return false;
			}
			return true;
		}
		
		if(this._listAgentsBomberman.size() == 1) {
			if(this._listAgentsPNJ.size() > 0) {
				return true;
			}
			else return false;
		}
		
		if(this._listAgentsBomberman.isEmpty()) { // S'il n'y a plus d'agent bomberman
			return false;
		}
		return true;
	}

	public void gameOver() {
		System.out.println("Fin du jeu");
		
		String vainqueur = "";
		
		if(this._listAgentsBomberman.size() <= 0) {
			System.out.println("Victoire des agents PNJ !");
			if(this.mode != ModeJeu.PERCEPTRON) {
				ViewGagnant.getInstance(this._controllerBombGame, "PNJ", "");
			}
			vainqueur = "PNJ";
		}
		else if(this._listAgentsBomberman.size() == 1) {
			AgentBomberman bombermanVainqueur = this._listAgentsBomberman.get(0);
			System.out.println("Victoire de l'agent '" + bombermanVainqueur.getColor() + "'");
			if(this.mode != ModeJeu.PERCEPTRON) {
				String color = colorAgentToColor(bombermanVainqueur.getColor());
				ViewGagnant.getInstance(this._controllerBombGame, bombermanVainqueur.getColor().toString(), color);
			}
			
			if(bombermanVainqueur == this.bombermanJoueur1) {
				vainqueur = this.nomJoueur;
			} else {
				vainqueur = "PNJ";
			}
		}
		
		this.sortie.println("FIN_PARTIE > " + this.dateDebutPartie + ";" + vainqueur);		
	}
	
	public String colorAgentToColor(ColorAgent colorAgent) {
		switch(colorAgent) {
			case BLEU: return "blue";
			case ROUGE: return "red";
			case VERT: return "green";
			case JAUNE: return "yellow";
			case BLANC: return "white";
			default: return "";
		}
	}
	
	   
	
	// retourne vrai si les coordonnées appartient à la map
	boolean appartientMap(int x, int y) {
		return x >=0 && x <= this._controllerBombGame.getMap().getSizeX()-1
				&& y>=0 && y <= this._controllerBombGame.getMap().getSizeY()-1;
	}
		
		
	public Bombe getBombByCoord(int x, int y) {
		for(Bombe bomb : this._listBombs) {
			if(bomb.getX() == x && bomb.getY() == y) {
				return bomb;
			}
		}
		return null;
	}
	// Recupere l'agent PNJ en fonction des coordonnées passées en paramètre
	public AgentPNJ getAgentPNJByCoord(int x, int y) {
		for(AgentPNJ agent : this._listAgentsPNJ) {
			if(agent.getX() == x && agent.getY() == y) {
				return agent;
			}
		}

		return null;
	}
	// Recupere l'agent Bomberman en fonction des coordonnées passées en paramètre
	public AgentBomberman getAgentBombermanByCoord(int x, int y) {
		for(AgentBomberman agent : this._listAgentsBomberman) {
			if(agent.getX() == x && agent.getY() == y) {
				return agent;
			}
		}
		return null;
	}
	// Recupere l'agent en fonction des coordonnées passées en paramètre
	public Agent getAgentByCoord(int x, int y) {
		AgentBomberman agentBomberman = this.getAgentBombermanByCoord(x, y);
		AgentPNJ agentPNJ = this.getAgentPNJByCoord(x, y);

		if(agentBomberman != null) {
			return agentBomberman;
		} else {
			return agentPNJ;
		}
	}
	// Recupere l'agent Bomberman en fonction de la bombe passée en paramètre
	public AgentBomberman getAgentBombermanByBomb(Bombe bomb) {
		for(AgentBomberman agent : this._listAgentsBomberman) {
			for(Bombe bombAgent : agent.getListBombe()) {
				if(bombAgent == bomb) {
					return agent;
				}
			}
		}
		return null;
	}
	
	
	
	
	public void addListBombs(Bombe bomb) {
		this._listBombs.add(bomb);
	}
	public void addListItems(Item item) {
		this._listItems.add(item);
	}
	public void addListItemUtilise(Item item) {
		this._listItemsUtilise.add(item);
	}
	public void addListBombeDetruite(Bombe bomb) {
		this._listBombesDetruite.add(bomb);
	}
	public void addListAgentDetruit(Agent agent) {
		this._listAgentsDetruit.add(agent);
	}
	public void removeAgentBomberman(AgentBomberman bomberman) {
	    this._listAgentsBomberman.remove(bomberman);
	}
	
	
	
	
	public ArrayList<AgentBomberman> getListAgentBomberman() {
    	return this._listAgentsBomberman;
    }
    public ArrayList<AgentPNJ> getListAgentPNJ() {
    	return this._listAgentsPNJ;
    }
	public boolean[][] getListBreakableWall() {
		return this._listBreakableWalls;
	}
	public ArrayList<Item> getListItem() {
		return this._listItems;
	}
	public ArrayList<Bombe> getListBomb() {
		return this._listBombs;
	}
	public ArrayList<RadioTower> getListRadioTower() {
		return this._listRadioTower;
	}
	public int getProbabiliteObjet() {
		return PROBABILITE_OBJET;
	}
	public ControllerBombermanGame getControllerBombGame() {
		return this._controllerBombGame;
	}
	public ModeJeu getModeJeu() {
		return this.mode;
	}
	public int getReward() {
		return this.reward;
	}
	public void setReward(int reward) {
		this.reward = reward;
	}
	public String getNomJoueur() {
		return nomJoueur;
	}
	public void setNomJoueur(String nomJoueur) {
		this.nomJoueur = nomJoueur;
	}

}
