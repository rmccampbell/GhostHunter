package edu.virginia.cs2110.rnm6u.ghosthunter;

public class Coins extends Item {

	public final int amount;

	public Coins(int amount, GameView game) {
		super(0, game);
		this.amount = amount;
	}

}
