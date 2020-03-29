package com.projetBomberman.modele;

public class Item {

	private int _pos_x;
	private int _pos_y;
	private ItemType _type;

	Item(int x, int y, ItemType type) {
		this._pos_x=x;
		this._pos_y=y;
		this._type=type;
	}

	public int getX() {
		return _pos_x;
	}

	public void setX(int x) {
		this._pos_x = x;
	}

	public int getY() {
		return _pos_y;
	}

	public void setY(int y) {
		this._pos_y = y;
	}

	public ItemType getType() {
		return _type;
	}

	public void setType(ItemType type) {
		this._type = type;
	}

}
