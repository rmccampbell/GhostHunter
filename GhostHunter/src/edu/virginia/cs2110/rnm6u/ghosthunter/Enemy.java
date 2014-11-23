package edu.virginia.cs2110.rnm6u.ghosthunter;

import java.util.Random;

import android.util.Log;

public class Enemy extends Entity {
	private static final String TAG = Enemy.class.getSimpleName();

	private Random rand = new Random();

	public Enemy(int x, int y, GameView game) {
		super(x, y, game);

		this.speed = 4;
		this.setAnim(WALKING);
		changeDir();
	}

	@Override
	public void update() {
		super.update();
		if (rand.nextFloat() < .2 && canAttack()) {
			attack();
		} else if (rand.nextFloat() < .05) {
			changeDir();
		}
	}

	@Override
	public boolean attack() {
		if (!super.attack()) return false;
		game.getPlayer().takeDamage(attack);
		return true;
	}

	@Override
	public boolean canAttack() {
		if (!super.canAttack()) return false;
		return getAttackRect().intersect(game.getPlayer().getBoundingRect());
	}

	public void changeDir() {
		setDirection(rand.nextInt(4));
		switch (direction) {
		case UP:
			xSpeed = 0;
			ySpeed = -speed;
			break;
		case DOWN:
			xSpeed = 0;
			ySpeed = speed;
			break;
		case LEFT:
			xSpeed = -speed;
			ySpeed = 0;
			break;
		case RIGHT:
			xSpeed = speed;
			ySpeed = 0;
			break;
		}
	}

}
