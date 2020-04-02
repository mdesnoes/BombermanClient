package com.projetBomberman.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.projetBomberman.modele.info.AgentAction;
import com.projetBomberman.modele.info.ItemType;
import com.projetBomberman.modele.info.StateBomb;

/** 
 * Classe qui permet de charger d'afficher le panneau du jeu à partir d'une carte et de listes d'agents avec leurs positions.
 * Inspiré du code de Kevin Balavoine et Victor Lelu--Ribaimont.
 */


public class PanelBomberman extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static final String IMG_WALL = "/wall.png";
	private static final String IMG_BRIQUE = "/brique_2.png";
	private static final String IMG_GRASS = "/grass.png";
	private static final String IMG_ITEM_FIRE_UP = "/Item_FireUp.png";
	private static final String IMG_ITEM_FIRE_DOWN = "/Item_FireDown.png";
	private static final String IMG_ITEM_BOMB_UP = "/Item_BombUp.png";
	private static final String IMG_ITEM_BOMB_DOWN = "/Item_BombDown.png";
	private static final String IMG_ITEM_FIRESUIT = "/Item_FireSuit.png";
	private static final String IMG_ITEM_SKULL = "/Item_Skull.png";
	private static final String IMG_BOMB0 = "/Bomb_0.png";
	private static final String IMG_BOMB1 = "/Bomb_1_jaune.png";
	private static final String IMG_BOMB2 = "/Bomb_2_rouge.png";
	private static final String IMG_RANGE_CENTRE = "/Range_CENTRE.png";
	private static final String IMG_RANGE_SOUTH_FIN = "/Range_SOUTH_Fin.png";
	private static final String IMG_RANGE_SOUTH = "/Range_SOUTH.png";
	private static final String IMG_RANGE_NORTH_FIN = "/Range_NORTH_Fin.png";
	private static final String IMG_RANGE_NORTH = "/Range_NORTH.png";
	private static final String IMG_RANGE_EAST_FIN = "/Range_EAST_Fin.png";
	private static final String IMG_RANGE_EAST = "/Range_EAST.png";	
	private static final String IMG_RANGE_WEST_FIN = "/Range_WEST_Fin.png";
	private static final String IMG_RANGE_WEST = "/Range_WEST.png";	
	
			
	protected Color wallColor=Color.GRAY;
	protected Color brokable_walls_Color=Color.lightGray;
	private Color ground_Color= new Color(50,175,50);
	private int taille_x;
	private int taille_y;
	private float[] invincible = { 200, 200, 200, 1.0f };
	private float[] skull = { 0.5f, 0.5f, 0.5f, 0.75f };
	private Map map;
	private List<InfoAgent> listInfoAgents;
	private List<InfoItem> listInfoItems;
	private List<InfoBomb> listInfoBombs;
	private boolean[][] breakable_walls;
	private int cpt;

	PanelBomberman(Map map) {
		this.map = map;
		this.breakable_walls = map.getStart_breakable_walls();
		listInfoAgents = map.getStart_agents();	
		listInfoItems = new ArrayList<>();
		listInfoBombs = new ArrayList<>();
	}

	public void paint(Graphics g) {
		int fen_x=getSize().width;
		int fen_y=getSize().height;
		g.setColor(ground_Color);
		g.fillRect(0, 0,fen_x,fen_y);
		double stepx=fen_x/(double)taille_x;
		double stepy=fen_y/(double)taille_y;
		double position_x=0;
		taille_x= map.getSizeX();
		taille_y= map.getSizeY();
		boolean[][] walls = map.getWalls();
		
		for(int x=0; x<taille_x; x++) {
			double position_y = 0 ;

			for(int y=0; y<taille_y; y++) {
				if (walls[x][y]){
					try {
						Image img = ImageIO.read( getClass().getResource( IMG_WALL ));
						g.drawImage(img, (int)position_x, (int)position_y, (int)stepx, (int)stepy, this);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (this.breakable_walls[x][y]) {
					try {
						Image img = ImageIO.read( getClass().getResource( IMG_BRIQUE ));
						g.drawImage(img, (int)position_x, (int)position_y, (int)stepx, (int)stepy, this);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					try {
						Image img = ImageIO.read( getClass().getResource( IMG_GRASS ));
						g.drawImage(img, (int)position_x, (int)position_y, (int)stepx, (int)stepy, this);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				position_y+=stepy;				
			}

			position_x+=stepx;
		}

		for (InfoItem listInfoItem : listInfoItems) {
			dessine_Items(g, listInfoItem);
		}

		for (InfoBomb listInfoBomb : listInfoBombs) {
			dessine_Bomb(g, listInfoBomb);
		}

		for (InfoAgent listInfoAgent : listInfoAgents) {
			dessine_Agent(g, listInfoAgent);
		}

		cpt++;
	}


	private void dessine_Agent(Graphics g, InfoAgent infoAgent) {
		int fen_x = getSize().width;
		int fen_y = getSize().height;
		double stepx = fen_x/(double)taille_x;
		double stepy = fen_y/(double)taille_y;
		int px = infoAgent.getX();
		int py = infoAgent.getY();
		double pos_x=px*stepx;
		double pos_y=py*stepy;
		AgentAction agentAction = infoAgent.getAgentAction();
		int direction;
		
		if(agentAction == AgentAction.MOVE_UP) {
			direction = 0;
		} else if(agentAction == AgentAction.MOVE_DOWN) {
			direction = 1;
		} else if(agentAction == AgentAction.MOVE_RIGHT) {
			direction = 2;
		} else if(agentAction == AgentAction.MOVE_LEFT) {
			direction = 3;			
		} else {
			direction = 4;
		}
		
		BufferedImage img = null;
		
		try {
			if(infoAgent.getType() == 'R') {
				img = ImageIO.read( getClass().getResource("/" + infoAgent.getType() + direction + this.cpt % 2 + ".png"));	
			}else {
				img = ImageIO.read( getClass().getResource("/" + infoAgent.getType() + direction + this.cpt % 3 + ".png"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}			

		float [] scales = new float[]{1 ,1, 1, 1.0f };

		if(infoAgent.getColor()!= null) {
			switch(infoAgent.getColor()) {
			case ROUGE :
				scales = new float[]{3 ,0.75f, 0.75f, 1.0f };
				break;
			case VERT :
				scales = new float[]{0.75f ,3, 0.75f, 1.0f };
				break;
			case BLEU :
				scales = new float[]{0.75f ,0.75f, 3, 1.0f };
				break;
			case JAUNE :
				scales = new float[]{3 ,3, 0.75f, 1.0f };
				break;
			case BLANC :
				scales = new float[]{2 ,2, 2, 1.0f };
				break;
			case DEFAULT :
				scales = new float[]{1 ,1, 1, 1.0f };
				break;
			}
		}

		float[] contraste;

		if (infoAgent.isInvincible() & cpt % 2 == 0) {
			contraste = invincible;
		}
		else {
			contraste = new float[]{ 0, 0, 0, 1.0f };
		}
		
		if (infoAgent.isSick() & cpt % 2 == 0) {
			scales = skull;
		}
		
		RescaleOp op = new RescaleOp(scales, contraste, null);
		assert img != null;
		img = op.filter( img, null);
		
		if(img != null) {
			g.drawImage(img, (int)pos_x, (int)pos_y, (int)stepx, (int)stepy, this);
		}
	}

	private void dessine_Items(Graphics g, InfoItem item){
		int fen_x = getSize().width;
		int fen_y = getSize().height;
		double stepx = fen_x/(double)taille_x;
		double stepy = fen_y/(double)taille_y;
		int px = item.getX();
		int py = item.getY();
		double pos_x=px*stepx;
		double pos_y=py*stepy;

		if (item.getType() == ItemType.FIRE_UP) {
			try {
				Image img = ImageIO.read( getClass().getResource( IMG_ITEM_FIRE_UP ));
				g.drawImage(img, (int)pos_x, (int)pos_y, (int)stepx, (int)stepy, this);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (item.getType() == ItemType.FIRE_DOWN) {
			try {
				Image img = ImageIO.read( getClass().getResource( IMG_ITEM_FIRE_DOWN ));
				g.drawImage(img, (int)pos_x, (int)pos_y, (int)stepx, (int)stepy, this);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (item.getType() == ItemType.BOMB_UP) {
			try {
				Image img = ImageIO.read( getClass().getResource( IMG_ITEM_BOMB_UP ));
				g.drawImage(img, (int)pos_x, (int)pos_y, (int)stepx, (int)stepy, this);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (item.getType() == ItemType.BOMB_DOWN) {
			try {
				Image img = ImageIO.read( getClass().getResource( IMG_ITEM_BOMB_DOWN ));
				g.drawImage(img, (int)pos_x, (int)pos_y, (int)stepx, (int)stepy, this);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (item.getType() == ItemType.FIRE_SUIT) {
			try {
				Image img = ImageIO.read( getClass().getResource( IMG_ITEM_FIRESUIT ));
				g.drawImage(img, (int)pos_x, (int)pos_y, (int)stepx, (int)stepy, this);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (item.getType() == ItemType.SKULL) {
			try {
				Image img = ImageIO.read( getClass().getResource( IMG_ITEM_SKULL ));
				g.drawImage(img, (int)pos_x, (int)pos_y, (int)stepx, (int)stepy, this);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void dessine_Bomb(Graphics g, InfoBomb bomb) {
		int fen_x = getSize().width;
		int fen_y = getSize().height;
		double stepx = fen_x/(double)taille_x;
		double stepy = fen_y/(double)taille_y;
		int px = bomb.getX();
		int py = bomb.getY();
		double pos_x=px*stepx;
		double pos_y=py*stepy;

		if (bomb.getStateBomb() == StateBomb.Step1 ) {
			try {
				Image img = ImageIO.read( getClass().getResource( IMG_BOMB0 ));
				g.drawImage(img, (int)pos_x, (int)pos_y, (int)stepx, (int)stepy, this);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (bomb.getStateBomb() == StateBomb.Step2) {
			try {
				Image img = ImageIO.read( getClass().getResource( IMG_BOMB1 ));
				g.drawImage(img, (int)pos_x, (int)pos_y, (int)stepx, (int)stepy, this);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (bomb.getStateBomb() == StateBomb.Step3 ) {
			try {
				Image img = ImageIO.read( getClass().getResource( IMG_BOMB2 ));
				g.drawImage(img, (int)pos_x, (int)pos_y, (int)stepx, (int)stepy, this);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (bomb.getStateBomb() == StateBomb.Boom) {
			try {
				Image img = ImageIO.read( getClass().getResource( IMG_RANGE_CENTRE ));
				g.drawImage(img, (int)pos_x, (int)pos_y, (int)stepx, (int)stepy, this);
			} catch (IOException e) {
				e.printStackTrace();
			}

			int range = bomb.getRange();

			for (int i = 1 ; i <= range; i++){
				if(py+i < map.getSizeY()) {
					if(i == range ) {
						try {
							Image img = ImageIO.read( getClass().getResource( IMG_RANGE_SOUTH_FIN ));
							g.drawImage(img, (int)pos_x, (int)(pos_y + (stepy*i)), (int)stepx, (int)stepy, this);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						try {
							Image img = ImageIO.read( getClass().getResource( IMG_RANGE_SOUTH ));
							g.drawImage(img, (int)pos_x, (int)(pos_y + (stepy*i)), (int)stepx, (int)stepy, this);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

				if(py-i >= 0) {
					if(i == range) {
						try {
							Image img = ImageIO.read( getClass().getResource( IMG_RANGE_NORTH_FIN ));
							g.drawImage(img, (int)pos_x, (int)(pos_y - (stepy*i)), (int)stepx, (int)stepy, this);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						try {
							Image img = ImageIO.read( getClass().getResource( IMG_RANGE_NORTH ));
							g.drawImage(img, (int)pos_x, (int)(pos_y - (stepy*i)), (int)stepx, (int)stepy, this);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

				if(px+i < map.getSizeX()) {
					if( i == range ) {
						try {
							Image img = ImageIO.read( getClass().getResource( IMG_RANGE_EAST_FIN ));
							g.drawImage(img, (int)(pos_x + (stepy*i)), (int)(pos_y), (int)stepx, (int)stepy, this);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						try {
							Image img = ImageIO.read( getClass().getResource( IMG_RANGE_EAST ));
							g.drawImage(img, (int)(pos_x + (stepy*i)), (int)(pos_y), (int)stepx, (int)stepy, this);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

				if(px-i >= 0) {
					if( i == range) {
						try {
							Image img = ImageIO.read( getClass().getResource( IMG_RANGE_WEST_FIN ));
							g.drawImage(img, (int)(pos_x - (stepy*i)), (int)(pos_y), (int)stepx, (int)stepy, this);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						try {
							Image img = ImageIO.read( getClass().getResource( IMG_RANGE_WEST ));
							g.drawImage(img, (int)(pos_x - (stepy*i)), (int)(pos_y), (int)stepx, (int)stepy, this);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	void setInfoGame(boolean[][] breakable_walls, List<InfoAgent> listInfoAgents, List<InfoItem> listInfoItems, List<InfoBomb> listInfoBombs) {
		this.listInfoAgents = listInfoAgents;
		this.listInfoItems = listInfoItems;
		this.listInfoBombs = listInfoBombs;
		this.breakable_walls = breakable_walls;
	}

}
