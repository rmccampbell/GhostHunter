package edu.virginia.cs2110.rnm6u.ghosthunter;

public class Skeleton extends Enemy {
	private static final String TAG = Skeleton.class.getSimpleName();

	public Skeleton(int x, int y, GameView game) {
		super(x, y, game);

		this.sprite = bmGetter.getBitmap(R.drawable.skeleton);
		this.speed = 8;
		this.maxHealth = 20;
		this.health = 20;
		this.attack = 5;
		this.attackDist = 64;
	}

}
