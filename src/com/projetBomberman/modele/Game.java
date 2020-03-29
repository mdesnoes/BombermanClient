package com.projetBomberman.modele;

import java.util.Observable;

public abstract class Game extends Observable implements Runnable {

	public static final int INIT_TIME = 1000;
	protected int _turn;
	private int _maxturn;
	private boolean _isRunning;
	private long _time;
	private Thread _thread;

	Game(int maxturn) {
		this._maxturn = maxturn;
		this._time = INIT_TIME;
	}
	
	public void init() {
		this._turn = 0;
		this._isRunning = true;
		initialize_game();
		this.setChanged();
		this.notifyObservers();
	}
	
	public void step() {
		if(gameContinue() && this._turn < this._maxturn) {
			this._turn++;
			takeTurn();
		}
		else {
			this._isRunning = false;
			gameOver();
		}
		
		this.setChanged();
		this.notifyObservers();
	}
	
	public void run() {
		while(this._isRunning) {
			step();
			
			try {
				Thread.sleep(this._time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void stop() {
		this._isRunning = false;
	}
	
	public void launch() {
		this._isRunning = true;
		this._thread = new Thread(this);
		_thread.start();
	}
	
	public void setTime(long time) {
		this._time = time;
	}

	public int getTurn( ) {
		return this._turn;
	}
	
	public int getMaxTurn() {
		return this._maxturn;
	}

	public long getTime() {
		return this._time;
	}
	
	public Thread getThread() {
		return this._thread;
	}

	public abstract void initialize_game();
	public abstract void takeTurn();
	public abstract boolean gameContinue();
	public abstract void gameOver();

}
