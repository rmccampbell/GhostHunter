package edu.virginia.cs2110.rnm6u.ghosthunter;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Arrow {

	protected int x;
	protected int y;
	protected int direction;
	protected int speed = 48;
	protected int damage = 5;
	protected boolean isAlive = true;
	
	public static final int UP = 0;
	public static final int LEFT = 1;
	public static final int DOWN = 2;
	public static final int RIGHT = 3;
	
	protected GameView game;
	protected Bitmap image;
	
	public Arrow(int x, int y, int direction, GameView game) {
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.game = game;
		
		switch(direction) {
		case UP:
			this.image = game.bmGetter.getBitmap(R.drawable.arrow_up);
			break;
		case DOWN:
			this.image = game.bmGetter.getBitmap(R.drawable.arrow_down);
			break;
		case RIGHT:
			this.image = game.bmGetter.getBitmap(R.drawable.arrow_right);
			break;
		case LEFT:
			this.image = game.bmGetter.getBitmap(R.drawable.arrow_left);
			break;
		}
	}
	
	public void update() {
		
		switch(direction) {
		case UP:
			y -= speed;
			break;
		case DOWN:
			y += speed;
			break;
		case RIGHT:
			x += speed;
			break;
		case LEFT:
			x -= speed;
			break;
		}
		
		Enemy e = collidesEnemy();
		if (e != null) {
			e.takeDamage(damage);
			remove();
		}
		if (collidesWall()) {
			remove();
		}
		
	}
	
	public void draw(Canvas c) {
		c.drawBitmap(image, x + game.getMap().getxOffset()-32, y + game.getMap().getyOffset()-32, null);
	}
	
	public Enemy collidesEnemy() {
		for (Enemy e : game.getEnemies()) {
			if (e.getBoundingRect().contains(x,y)) {
				return e;
			}
		}
		return null;
	}
	
	public boolean collidesWall() {
		if (!game.getMap().getRect().contains(x,y)) {
			return true;
		}
		return false;
	}
	
	public void remove() {
		isAlive = false;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}
	
	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public boolean isAlive() {
		return this.isAlive;
	}

}
