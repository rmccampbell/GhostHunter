package edu.virginia.cs2110.rnm6u.ghosthunter;

public class Goblin extends Enemy {
	private static final String TAG = Goblin.class.getSimpleName();

	public Goblin(int x, int y, GameView game) {
		super(x, y, game);

		this.setSprite(R.drawable.goblin);
		this.speed = 4;
		this.maxHealth = 10;
		this.health = 10;
		this.attack = 10;
		this.attackDist = 64;
		this.attackCooldown = 8;
	}

}
