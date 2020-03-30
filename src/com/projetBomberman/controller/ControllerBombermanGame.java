package com.projetBomberman.controller;

import java.io.PrintWriter;
import java.util.ArrayList;

import com.projetBomberman.modele.*;
import com.projetBomberman.view.InfoAgent;
import com.projetBomberman.view.InfoBomb;
import com.projetBomberman.view.InfoItem;
import com.projetBomberman.view.Map;
import com.projetBomberman.view.ViewBombermanGame;
import com.projetBomberman.view.ViewCommand;
import com.projetBomberman.view.ViewModeInteractif;
import com.projetProgReseau.client.Client;

public class ControllerBombermanGame implements InterfaceController {

	private BombermanGame _bombGame;
	private ViewCommand _viewCommand;
	private ViewBombermanGame _viewBombGame;
	private ViewModeInteractif _viewModeInteractif;
	private PrintWriter sortie;
	private Client client;
	
	public ControllerBombermanGame(BombermanGame bombGame, Client client) {
		this._bombGame = bombGame;
		this.client = client;
		this.sortie = client.getSortie();
		
		createView();
	}
	
	public void createView() {
		this._viewCommand = new ViewCommand(this, this._bombGame);
		this._viewBombGame = new ViewBombermanGame(this, this._bombGame);
	}
	
	
	public void start() {
		this._bombGame.init();
		
		this.sortie.println("[CLIENT : " + this._bombGame.getNomJoueur() + "] INITIALISATION DE LA PARTIE");
	}
	public void step() {
		this._bombGame.step();
		
		this.sortie.println("[CLIENT : " + this._bombGame.getNomJoueur() + "] AVANCEMENT D'UNE ETAPE DE LA PARTIE");
	}
	public void run() {
		this._bombGame.launch();
		
		this.sortie.println("[CLIENT : " + this._bombGame.getNomJoueur() + "] DEMARRAGE DE LA PARTIE");
	}
	public void stop() {
		this._bombGame.stop();
		
		this.sortie.println("[CLIENT : " + this._bombGame.getNomJoueur() + "] PARTIE EN PAUSE");
	}
	public void quitter() {
		this._viewCommand.setVisible(false);
		this._viewBombGame.setVisible(false);
		if(this._viewModeInteractif != null) {
			this._viewModeInteractif.setVisible(false);
		}
		
		this.sortie.println("[CLIENT : " + this._bombGame.getNomJoueur() + "] DECONNEXION");
		this.client.fermeture();
	}

	
	
	//Permet de renseigner les nouvelles coordonnées des agents à la liste d'InfoAgent et retourner cette liste
	public ArrayList<InfoAgent> getListInfoAgent() {
		ArrayList<InfoAgent> infoListAgent = new ArrayList<>();

		for(AgentBomberman agent : this._bombGame.getListAgentBomberman()) {
			infoListAgent.add(new InfoAgent(agent.getX(), agent.getY(), agent.getAction(), agent.getType(), agent.getColor(),agent.isInvincible(),agent.getIsSick()));
		}

		for(AgentPNJ agent : this._bombGame.getListAgentPNJ()) {
			infoListAgent.add(new InfoAgent(agent.getX(), agent.getY(), agent.getAction(), agent.getType(), agent.getColor(), false, false));
		}

		return infoListAgent;
	}
	
	//Permet de renseigner les nouvelles coordonnées des items à la liste d'InfoItem et retourner cette liste
	public ArrayList<InfoItem> getListInfoItems() {
		ArrayList<InfoItem> infoItemList = new ArrayList<>();

		for(Item item : this._bombGame.getListItem()) {
			infoItemList.add(new InfoItem(item.getX(), item.getY(), item.getType()));
		}

		return infoItemList;
	}
	
	//Permet de renseigner les nouvelles coordonnées des bombes à la liste d'InfoBombs et retourner cette liste
	public ArrayList<InfoBomb> getListInfoBombs() {
		ArrayList<InfoBomb> infoBombList = new ArrayList<>();

		for(Bombe bomb : this._bombGame.getListBomb()) {
			infoBombList.add(new InfoBomb(bomb.getX(), bomb.getY(), bomb.getRange(), bomb.getStateBomb()));
		}

		return infoBombList;
	}
	
	public Map getMap() {
		return this._viewBombGame.getMap();
	}
	public String getLayout() {
		return this._viewCommand.getLayoutGame();
	}
	public boolean[][] getListBreakableWall() {
		return this._bombGame.getListBreakableWall();
	}
	public void setViewModeInteractif() {
		this._viewModeInteractif = ViewModeInteractif.getInstance();
	}
	public void setViewBombermanGame(ViewBombermanGame vbombGame) {
		this._viewBombGame = vbombGame;
	}
	public ViewModeInteractif getViewModeInteractif() {
		return this._viewModeInteractif;
	}
	public ViewCommand getViewCommand() {
		return this._viewCommand;
	}
	public ViewBombermanGame getViewBombGame() {
		return this._viewBombGame;
	}
	public void setTime(long time) {
		this._bombGame.setTime(time);
	}
	public long getTime() {
		return _bombGame.getTime();
	}
	public int getInitTime() {
		return Game.INIT_TIME;
	}

}
