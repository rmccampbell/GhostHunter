package edu.virginia.cs2110.rnm6u.ghosthunter;

public class DarkKnight extends Boss {
	private static final String TAG = DarkKnight.class.getSimpleName();

	public DarkKnight(int x, int y, GameView game) {
		super(x, y, game);

		this.sprite = bmGetter.getBitmap(R.drawable.dark_knight);
		this.speed = 2;
		this.maxHealth = 50;
		this.health = 50;
		this.attack = 20;
		this.attackDist = 96;
		this.attackCooldown = 10;
	}

	@Override
	public void die() {
		
	}
	
	
}
