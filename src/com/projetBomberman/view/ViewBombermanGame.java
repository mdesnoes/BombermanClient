package com.projetBomberman.view;

import javax.swing.*;

import com.projetBomberman.controller.ControllerBombermanGame;
import com.projetBomberman.modele.BombermanGame;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class ViewBombermanGame extends JFrame implements Observer {

	private Map _map;
	private PanelBomberman _panel;
	private ControllerBombermanGame _controllerGame;

	
	public ViewBombermanGame(ControllerBombermanGame controllerGame, BombermanGame bombermanGame) {
		this._controllerGame = controllerGame;
		bombermanGame.addObserver(this);
		this.createView();
	}
	
	private void createView() {
		this.setTitle("Bomberman Game");
		this.setSize(new Dimension(1100,700));
		Dimension windowSize = this.getSize();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Point centerPoint = ge.getCenterPoint();
		int dx = centerPoint.x - windowSize.width / 2 - 600;
		int dy = centerPoint.y - windowSize.height / 2 - 600;
		this.setLocation(dx,dy);

		if(this._controllerGame.getLayout() != null && this._controllerGame.getLayout().endsWith(".lay")) {
			try {
				this._map = new Map("layout/" + this._controllerGame.getLayout());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			this._panel = new PanelBomberman(this._map);
			this.setContentPane(this._panel);
			this.setVisible(true);
		}
	}
	
	public Map getMap() {
		return this._map;
	}
	
	public void setMap(Map map) {
		this._map = map;
	}
	
	public void update(Observable obs, Object arg) {
		this._panel.setInfoGame(this._controllerGame.getListBreakableWall(), this._controllerGame.getListInfoAgent(),
			this._controllerGame.getListInfoItems(), this._controllerGame.getListInfoBombs());
		this._panel.repaint();
	}

}
