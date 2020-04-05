package com.projet.modele;

import com.projet.modele.type.ItemType;

public class Item {

	private int posX;
	private int posY;
	private ItemType type;

	public Item() {
	}

	
	
	public int getPosX() {
		return posX;
	}
	public void setPosX(int posX) {
		this.posX = posX;
	}
	public int getPosY() {
		return posY;
	}
	public void setPosY(int posY) {
		this.posY = posY;
	}
	public ItemType getType() {
		return type;
	}
	public void setType(ItemType type) {
		this.type = type;
	}

	
	


}
