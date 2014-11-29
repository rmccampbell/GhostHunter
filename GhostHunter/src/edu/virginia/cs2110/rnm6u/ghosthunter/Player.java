package edu.virginia.cs2110.rnm6u.ghosthunter;

import android.graphics.Rect;
import android.util.Log;

public class Player extends Entity {
	private static final String TAG = Player.class.getSimpleName();

	private Item weapon;
	private Item armor;

	public Player(int x, int y, GameView game) {
		super(x, y, game);

		this.sprite = bmGetter.getBitmap(R.drawable.gold_armor_longsword);
		this.setAnim(STANDING);

		this.speed = 16;
		this.maxHealth = 100;
		this.health = 100;
		this.attack = 5;

		this.weapon = new Item();
		this.attackDist = 96;
		this.atkCooldown = 6;
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public boolean attack() {
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
	public void despawn() {
		super.despawn();
		game.getGameActivity().gameOver();
	}

	public void moveUp() {
		ySpeed = -speed;
		setDirection(UP);
		setAnim(WALKING);
	}
	
	public void moveDown() {
		ySpeed = speed;
		setDirection(DOWN);
		setAnim(WALKING);
	}
	
	public void moveLeft() {
		xSpeed = -speed;
		setDirection(LEFT);
		setAnim(WALKING);
	}
	
	public void moveRight() {
		xSpeed = speed;
		setDirection(RIGHT);
		setAnim(WALKING);
	}
	
	public void stopUp() {
		ySpeed = 0;
		setAnim(STANDING);
	}
	
	public void stopDown() {
		ySpeed = 0;
		setAnim(STANDING);
	}
	
	public void stopLeft() {
		xSpeed = 0;
		setAnim(STANDING);
	}
	
	public void stopRight() {
		xSpeed = 0;
		setAnim(STANDING);
	}

}
