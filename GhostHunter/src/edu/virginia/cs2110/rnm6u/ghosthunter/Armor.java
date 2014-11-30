package edu.virginia.cs2110.rnm6u.ghosthunter;

public class Armor extends Item {

	double defense;

	public Armor(double defense, int imageID, GameView game) {
		super(imageID, game);
		this.defense = defense;
	}

}
