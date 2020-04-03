package com.projetBomberman.modele;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

import java.util.ArrayList;

import com.projetBomberman.modele.info.AgentAction;
import com.projetBomberman.modele.info.ColorAgent;
import com.projetBomberman.modele.info.InfoAgent;

/** 
 * Classe qui permet de charger une carte de Bomberman à partir d'un fichier de layout d'extension .lay
 * 
 * @author Kevin Balavoine, Victor Lelu--Ribaimont 
 * 
 */

public class Map implements Serializable {

	private static final long serialVersionUID = 1L;
	private String filename;
	private int sizeX;
	private int sizeY;
	private boolean[][] walls;
	private boolean[][] start_breakable_walls;
	private ArrayList<InfoAgent> start_agents;

	
	public Map() {
	}
	
	public Map(String filename) throws Exception {
		
		this.filename = filename;
		
		try {
			InputStream flux = new FileInputStream(filename);
			InputStreamReader lecture = new InputStreamReader(flux);
			BufferedReader tampon = new BufferedReader(lecture);
			String ligne;
			int nbX=0;
			int nbY=0;

			while ((ligne = tampon.readLine())!=null) {
				ligne = ligne.trim();

				if (nbX==0) {
					nbX = ligne.length();
				}
				else if (nbX != ligne.length()) {
					throw new Exception("Toutes les lignes doivent avoir la même longueur");
				}

				nbY++;
			}

			tampon.close();
			sizeX = nbX;
			sizeY = nbY;
			walls = new boolean [sizeX][sizeY];
			start_breakable_walls  = new boolean [sizeX][sizeY];
			flux = new FileInputStream(filename);
			lecture = new InputStreamReader(flux);
			tampon = new BufferedReader(lecture);
			int y=0;
			ColorAgent[] color = ColorAgent.values();
			int cpt_col = 0;
			start_agents = new ArrayList<>();

			while ((ligne=tampon.readLine())!=null) {
				ligne=ligne.trim();

				for(int x=0;x<ligne.length();x++) {
					walls[x][y]= ligne.charAt(x) == '%';
					start_breakable_walls[x][y]= ligne.charAt(x) == '$';

					if (ligne.charAt(x)=='E' || ligne.charAt(x)=='V' || ligne.charAt(x)=='R') {
						start_agents.add(new InfoAgent(x,y,AgentAction.STOP,ligne.charAt(x),ColorAgent.DEFAULT,false,false));
					}

					if (ligne.charAt(x)=='B') {
						ColorAgent col;
						if (cpt_col < color.length) col = color[cpt_col];
						else col = ColorAgent.DEFAULT;
						start_agents.add(new InfoAgent(x,y,AgentAction.STOP, ligne.charAt(x),col,false,false));
						cpt_col++;
					}
				}

				y++;
			}

			tampon.close();

			//On verifie que le labyrinthe est clos
			for(int x=0;x<sizeX;x++) {
				if (!walls[x][0]) {
					throw new Exception("Mauvais format du fichier: la carte doit etre close");
				}
			}

			for(int x=0;x<sizeX;x++) {
				if (!walls[x][sizeY-1]) {
					throw new Exception("Mauvais format du fichier: la carte doit etre close");
				}
			}

			for(y=0;y<sizeY;y++) {
				if (!walls[0][y]) {
					throw new Exception("Mauvais format du fichier: la carte doit etre close");
				}
			}

			for(y=0;y<sizeY;y++) {
				if (!walls[sizeX-1][y]) {
					throw new Exception("Mauvais format du fichier: la carte doit etre close");
				}
			}
		
		} catch (Exception e) {
			System.out.println("Erreur : "+e.getMessage());
		}
	}

	
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public int getSizeX() {
		return sizeX;
	}
	public void setSizeX(int sizeX) {
		this.sizeX = sizeX;
	}
	public int getSizeY() {
		return sizeY;
	}
	public void setSizeY(int sizeY) {
		this.sizeY = sizeY;
	}
	public boolean[][] getWalls() {
		return walls;
	}
	public void setWalls(boolean[][] walls) {
		this.walls = walls;
	}
	public boolean[][] getStart_breakable_walls() {
		return start_breakable_walls;
	}
	public void setStart_breakable_walls(boolean[][] start_breakable_walls) {
		this.start_breakable_walls = start_breakable_walls;
	}
	public ArrayList<InfoAgent> getStart_agents() {
		return start_agents;
	}
	public void setStart_agents(ArrayList<InfoAgent> start_agents) {
		this.start_agents = start_agents;
	}

	
}