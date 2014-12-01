package edu.virginia.cs2110.rnm6u.ghosthunter;

public class Goblin extends Enemy {
	private static final String TAG = Goblin.class.getSimpleName();

	public Goblin(int x, int y, GameView game) {
		super(x, y, game);

		this.sprite = bmGetter.getBitmap(R.drawable.goblin);
		this.maxHealth = 10;
		this.health = 10;
		this.attack = 10;
		this.attackDist = 64;
	}

}
