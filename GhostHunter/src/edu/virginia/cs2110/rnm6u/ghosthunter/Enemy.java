package edu.virginia.cs2110.rnm6u.ghosthunter;

import java.util.Random;

import android.content.Context;
import android.content.SharedPreferences;

public class Enemy extends Entity {
	
	private static final String TAG = Enemy.class.getSimpleName();
	private Random rand = new Random();
	private SharedPreferences prefs;
	private boolean difficulty;

	public Enemy(int x, int y, GameView game) {
		super(x, y, game);

		this.setAnim(WALKING);
		this.changeDirRandom();
		this.speed = 4;
		this.attackDist = 64;
		this.attackCooldown = 8;

		this.prefs = game.activity.giveMePrefs();
		this.difficulty = prefs.getBoolean("Difficulty", false);
	}

	@Override
	public void update() {
		super.update();
		if (canAttack()) {
			attack();
		}
		if (hasCollision()) {
			changeDirRandom();
		}
		if (difficulty && canFollow()) {
			changeDirFollow();
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
	public void die() {
		super.die();
		game.addKill();
		Coins enemyCoin = new Coins(rand.nextInt(20) + 5, game);
		drop(enemyCoin);
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
	
	public int distanceFromPlayer(char axis) {
		if (axis == 'x') {
			return Math.abs(game.getPlayer().getX()-this.x);
		}
		else if (axis == 'y') {
			return Math.abs(game.getPlayer().getY()-this.y);
		}
		return 0;
	}
	
	public void changeDirFollow() {
		
		if(this.distanceFromPlayer('y') >= this.distanceFromPlayer('x')) {
			if (this.y > game.getPlayer().getY()) {
				this.direction = UP;
			}
			else if (this.y < game.getPlayer().getY()) {
				this.direction = DOWN;
			}
		}
		
		else {
			if (this.x > game.getPlayer().getX()) {
				this.direction = LEFT;
			}
			else if (this.x < game.getPlayer().getX()) {
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
