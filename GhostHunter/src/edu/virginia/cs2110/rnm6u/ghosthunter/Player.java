package edu.virginia.cs2110.rnm6u.ghosthunter;

import android.graphics.Rect;
import android.util.Log;

public class Player extends Entity {
	private static final String TAG = Player.class.getSimpleName();

	private Item weapon;
	private Item armor;

	public Player(int x, int y, GameView game) {
		super(x, y, game);

		this.sprite = bmGetter.getBitmap(R.drawable.no_armor_short_sword);
		setAnim(STANDING);
		
		this.speed = 10;
		this.maxHealth = 100;
		this.health = 100;
		this.attack = 5;
		
		this.weapon = new Item();
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public boolean attack() {
//		Log.d("Player", "attack");
		if (!super.attack()) return false;
		Rect attRect = getAttackRect();
		for (Entity entity : game.getEntities()) {
			if (entity == this) continue;
			if (entity instanceof Enemy) {
				if (attRect.intersect(entity.getBoundingRect())) {
					entity.takeDamage(attack);
				}
			}
		}
		return true;
	}

	@Override
	public boolean canAttack() {
		return super.canAttack() && weapon != null;
	};

	@Override
	public void die() {
		super.die();
		// respawn?
	}

	public void moveUp() {
//		Log.d("Player", "move up");
		ySpeed = -speed;
		setDirection(UP);
		setAnim(WALKING);
	}
	
	public void moveDown() {
//		Log.d("Player", "move down");
		ySpeed = speed;
		setDirection(DOWN);
		setAnim(WALKING);
	}
	
	public void moveLeft() {
//		Log.d("Player", "move left");
		xSpeed = -speed;
		setDirection(LEFT);
		setAnim(WALKING);
	}
	
	public void moveRight() {
//		Log.d("Player", "move right");
		xSpeed = speed;
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

}
