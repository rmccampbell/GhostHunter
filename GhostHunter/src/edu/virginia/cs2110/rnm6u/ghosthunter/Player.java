package edu.virginia.cs2110.rnm6u.ghosthunter;

import android.util.Log;

public class Player extends Entity {
	private static final String TAG = Player.class.getSimpleName();

	private Item weapon;
	private Item armor;

	public Player(int x, int y, GameView game) {
		super(x, y, game);

		this.sprite = bmGetter.getBitmap(R.drawable.no_armor_short_sword);
		setAnim(STANDING);
		this.maxHealth = 100;
		this.health = 100;
		this.attack = 5;
		
		this.weapon = new Item();
	}

	@Override
	public void update() {
		super.update();
//		Log.d(TAG, "updating: " + x + ", " + y);
	}

	public void moveUp() {
//		Log.d("Player", "move up");
		ySpeed = -8;
		setDirection(UP);
		setAnim(WALKING);
	}
	
	public void moveDown() {
//		Log.d("Player", "move down");
		ySpeed = 8;
		setDirection(DOWN);
		setAnim(WALKING);
	}
	
	public void moveLeft() {
//		Log.d("Player", "move left");
		xSpeed = -8;
		setDirection(LEFT);
		setAnim(WALKING);
	}
	
	public void moveRight() {
//		Log.d("Player", "move right");
		xSpeed = 8;
		setDirection(RIGHT);
		setAnim(WALKING);
	}
	
	public void stopUp() {
//		Log.d("Player", "stop up");
		ySpeed = 0;
		setAnim(STANDING);
	}
	
	public void stopDown() {
//		Log.d("Player", "stop down");
		ySpeed = 0;
		setAnim(STANDING);
	}
	
	public void stopLeft() {
//		Log.d("Player", "stop left");
		xSpeed = 0;
		setAnim(STANDING);
	}
	
	public void stopRight() {
//		Log.d("Player", "stop right");
		xSpeed = 0;
		setAnim(STANDING);
	}

	@Override
	public void attack() {
//		Log.d("Player", "attack");
		if (dying) return;
		if (weapon == null) return;
		super.attack();
		for (Entity entity : game.getEntities()) {
			if (entity == this) continue;
			if (entity instanceof Enemy) {
				// attack enemy
				entity.takeDamage(attack);
			}
		}
	}
	
	@Override
	public void die() {
		super.die();
		// respawn?
	}

}
