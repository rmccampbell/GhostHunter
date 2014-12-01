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
		this.changeDirRandom();
		this.atkCooldown = 8;
	}

	@Override
	public void update(Player player) {
		super.update(player);
		if (canAttack()) {
			attack();
		}
		if (hasCollision()) {
			changeDirRandom();
		}
		if (canFollow()) {
			changeDirFollow(player);
		}
		else if (rand.nextFloat() < .05) {
			changeDirRandom();
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
	
	public boolean canFollow() {
		return getFollowRect().intersect(game.getPlayer().getBoundingRect());
	}

	public void changeDirRandom() {
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
	
	public int distanceFromPlayer(char axis, Player player) {
		if (axis == 'x') {
			return Math.abs(player.getX()-this.x);
		}
		else if (axis == 'y') {
			return Math.abs(player.getY()-this.y);
		}
		return 0;
	}
	
	public void changeDirFollow(Player player) {
		
		if(this.distanceFromPlayer('y', player) >= this.distanceFromPlayer('x', player)) {
			if (this.y > player.getY()) {
				this.direction = UP;
			}
			else if (this.y < player.getY()) {
				this.direction = DOWN;
			}
		}
		
		if(this.distanceFromPlayer('x', player) > this.distanceFromPlayer('y', player)) {
			if (this.x > player.getX()) {
				this.direction = LEFT;
			}
			else if (this.x < player.getX()) {
				this.direction = RIGHT;
			}
		}
				
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
