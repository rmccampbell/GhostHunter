package edu.virginia.cs2110.rnm6u.ghosthunter;

public class Weapon extends Item {

	public final int attack;
	public final int attackDist;
	public final int cooldown;

	public Weapon(int attack, int attackDist, int cooldown, int imageID, GameView game) {
		super(imageID, game);
		this.attack = attack;
		this.attackDist = attackDist;
		this.cooldown = cooldown;
	}
	
	public Weapon(int attack, int imageID, GameView game) {
		super(imageID, game);
		this.attack = attack;
		this.attackDist = 64;
		this.cooldown = 6;
	}

}
