package edu.virginia.cs2110.rnm6u.ghosthunter;

import java.util.Random;

public class Enemy extends Entity {
	private static final String TAG = Enemy.class.getSimpleName();

	private Random rand = new Random();

	public Enemy(int x, int y, GameView game) {
		super(x, y, game);

		this.setAnim(WALKING);
		changeDir();
	}

	@Override
	public void update() {
		super.update();
		if (canAttack()) {
			attack();
		} else if (rand.nextFloat() < 0.05) {
			changeDir();
		}
	}

	public void changeDir() {
		setDirection(rand.nextInt(4));
		switch (direction) {
		case UP:
			xSpeed = 0;
			ySpeed = -6;
			break;
		case DOWN:
			xSpeed = 0;
			ySpeed = 6;
			break;
		case LEFT:
			xSpeed = -6;
			ySpeed = 0;
			break;
		case RIGHT:
			xSpeed = 6;
			ySpeed = 0;
			break;
		}
	}

	@Override
	public void attack() {
		super.attack();
		if (dying) return;
		game.getPlayer().takeDamage(attack);
	}

	public boolean canAttack() {
		return false;//rand.nextFloat() < .05;
	}

}
