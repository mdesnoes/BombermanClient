package com.projetBomberman.modele;

public class SimpleGame extends Game {

	public SimpleGame(int maxturn) {
		super(maxturn);
	}

	public void initialize_game() {
		System.out.println("Le jeu est initialisé");
	}

	public void takeTurn() {
		System.out.println("Tour " + this._turn + " du jeu en cours");
	}

	public boolean gameContinue() {
		System.out.println("Le jeu continue");
		return true;
	}

	public void gameOver() {
		System.out.println("Fin du jeu");
	}

}
