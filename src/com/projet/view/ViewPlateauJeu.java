package com.projet.view;


import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.projet.modele.AgentBomberman;
import com.projet.modele.AgentPNJ;
import com.projet.modele.Bombe;
import com.projet.modele.BombermanGame;
import com.projet.modele.InfoAgent;
import com.projet.modele.InfoBomb;
import com.projet.modele.InfoItem;
import com.projet.modele.Item;
import com.projet.modele.Map;
import com.projet.modele.PanelBomberman;


public class ViewPlateauJeu extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private PanelBomberman _panel;
	
	public ViewPlateauJeu(Map map) {
		this.createView(map);
	}
	
	private void createView(Map map) {
		this.setTitle("Plateau Bomberman");
		this.setSize(new Dimension(1100,700));
		Dimension windowSize = this.getSize();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Point centerPoint = ge.getCenterPoint();
		int dx = centerPoint.x - windowSize.width / 2 - 600;
		int dy = centerPoint.y - windowSize.height / 2 - 600;
		this.setLocation(dx,dy);

		
		this._panel = new PanelBomberman(map);
		this.setContentPane(this._panel);
		this.setVisible(true);
	}
	
	public void update(BombermanGame game) {
		
		List<InfoAgent> listeInfoAgent = getListInfoAgent(game.getListAgentsBomberman(), game.getListAgentsPNJ());
		List<InfoItem> listeInfoItem = getListInfoItems(game.getListItems());
		List<InfoBomb> listeInfoBomb = getListInfoBombs(game.getListBombs());
		
		this._panel.setInfoGame(game.getListBreakableWalls(), listeInfoAgent, listeInfoItem, listeInfoBomb);
		this._panel.repaint();
	}
	
	
	public List<InfoAgent> getListInfoAgent(List<AgentBomberman> listeBomberman, List<AgentPNJ> listePNJ) {
		List<InfoAgent> infoListAgent = new ArrayList<>();
		for(AgentBomberman agent : listeBomberman) {
			infoListAgent.add(new InfoAgent(agent.getPosX(), agent.getPosY(), agent.getAction(), agent.getType(), agent.getColor(),agent.isInvincible(),agent.isSick()));
		}
		for(AgentPNJ agent : listePNJ) {
			infoListAgent.add(new InfoAgent(agent.getPosX(), agent.getPosY(), agent.getAction(), agent.getType(), agent.getColor(), false, false));
		}
		return infoListAgent;
	}
	
	public List<InfoItem> getListInfoItems(List<Item> listeItem) {
		List<InfoItem> infoItemList = new ArrayList<>();
		for(Item item : listeItem) {
			infoItemList.add(new InfoItem(item.getPosX(), item.getPosY(), item.getType()));
		}
		return infoItemList;
	}
	
	public List<InfoBomb> getListInfoBombs(List<Bombe> listeBombe) {
		List<InfoBomb> infoBombList = new ArrayList<>();
		for(Bombe bomb : listeBombe) {
			infoBombList.add(new InfoBomb(bomb.getPosX(), bomb.getPosY(), bomb.getRange(), bomb.getStateBomb()));
		}
		return infoBombList;
	}

}
