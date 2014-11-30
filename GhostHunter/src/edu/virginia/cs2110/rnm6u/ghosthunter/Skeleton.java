package edu.virginia.cs2110.rnm6u.ghosthunter;

public class Skeleton extends Enemy {
	private static final String TAG = Skeleton.class.getSimpleName();

	public Skeleton(int x, int y, GameView game) {
		super(x, y, game);

		this.sprite = bmGetter.getBitmap(R.drawable.skeleton);
		this.maxHealth = 10;
		this.health = 10;
		this.attack = 5;
		this.attackDist = 64;
	}

}
