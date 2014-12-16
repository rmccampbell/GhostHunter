package edu.virginia.cs2110.rnm6u.ghosthunter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.Log;

public class Entity {

	private static final String TAG = Entity.class.getSimpleName();

	public static final int UP = 0;
	public static final int LEFT = 1;
	public static final int DOWN = 2;
	public static final int RIGHT = 3;

	public static final Animation SPELLCASTING = new Animation(0, 64, 64, 32, 32+16, 7);
	public static final Animation STANDING = new Animation(64*4, 64, 64, 32, 32+16, 1);
	public static final Animation WALKING = new Animation(64*4, 64, 64, 32, 32+16, 9);
	public static final Animation BOWDRAWING = new Animation(64*8, 64, 64, 32, 32+16, 13);
	public static final Animation DYING = new Animation(64*12, 64, 64, 32, 32+16, 6, 1, false);
	public static final Animation ATTACKING = new Animation(64*13, 192, 192, 96, 96+16, 6);
	public static final Animation ATTACKING_SPEAR = new Animation(64*13, 192, 192, 96, 96+16, 8);

	private static final int SCALE = 2;

	protected GameView game;

	protected int x, y;
	protected int xSpeed = 0, ySpeed = 0;
	protected int speed;
	protected int width = 64, height = 64;
	protected int direction = DOWN;

	protected Bitmap sprite;
	protected Animation anim;
	protected double frame = 0;
	protected Animation prevAnim = null;
	protected boolean animLocked = false;

	protected int maxHealth, health;
	protected int attack = 0;
	protected double defense = 0;
	protected int attackDist = 64;
	protected int attackCooldown = 6;
	protected int followDist = 6 * 64;
	protected int actionDist = 64;

	protected boolean dying;
	protected boolean dead;
	
	protected int actionTimer = 0;

	public Entity(int x, int y, GameView game) {
		this.game = game;
		this.x = x;
		this.y = y;

		if (hasCollision()) {
			despawn();
		}
	}

	public void update() {
		int prevX = x;
		int prevY = y;
		x += xSpeed;
		y += ySpeed;

		if (hasCollision()) {
			x = prevX;
			y = prevY;
		}

		frame += anim.speed;
		if (frame >= anim.numFrames) {
			frame = 0;
			animLocked = false;
			if (prevAnim != null) {
				setAnim(prevAnim);
				prevAnim = null;
			}
		}

		if (actionTimer > 0) {
			actionTimer--;
			if (actionTimer == 0) {
				if (dying) {
					despawn();
				}
			}
		}
	}

	public void draw(Canvas c) {
		int srcX = ((int) frame) * anim.width;
		int srcY = anim.spriteOffset
				+ (anim.directional ? (direction * anim.height) : 0);
		int dstX = x - anim.xOffset * SCALE + game.getMap().getxOffset();
		int dstY = y - anim.yOffset * SCALE + game.getMap().getyOffset();
		Rect src = new Rect(srcX, srcY, srcX + anim.width, srcY + anim.height);
		Rect dst = new Rect(dstX, dstY, 
				dstX + anim.width * SCALE, dstY + anim.height * SCALE);
		c.drawBitmap(getSprite(), src, dst, null);
	}

	public boolean hasCollision() {
		Rect r = getBoundingRect();
		if (!game.getMap().getRect().contains(r)) {
			return true;
		}

		int tsize = GameMap.TILESIZE;
		boolean[][] walls = game.getMap().getWalls();
		for (int tx = r.left / tsize; tx <= (r.right-1) / tsize; tx++) {
			for (int ty = r.top / tsize; ty <= (r.bottom-1) / tsize; ty++) {
				if (walls[tx][ty])
					return true;
			}
		}

		for (Entity e : game.getEntities()) {
			if (e != this && e.getBoundingRect().intersect(r)) {
				return true;
			}
		}

		return false;
	}

	public boolean attack() {
		if (!canAttack()) return false;
		playOnce(ATTACKING);
		game.sound.play(game.attackSound, 1, 1, 1, 0, 1);
		animLocked = true;
		actionTimer = attackCooldown;
		return true;
	}

	public boolean canAttack() {
		if (actionTimer != 0) return false;
		return true;
	}

	public void takeDamage(int damage) {
		damage -= damage * defense;
		if (dying) return;
		health -= damage;
		if (health <= 0) {
			health = 0;
			die();
		}
	}

	public void die() {
		playOnce(DYING);
		animLocked = true;
		actionTimer = (int) (DYING.numFrames / DYING.speed);
		dying = true;
	}

	public void despawn() {
		dead = true;
		actionTimer = -1;
	}

	public void drop(Item item) {
		int tsize = GameMap.TILESIZE;
		game.getMapItems()[x / tsize][y / tsize] = item;
	}

	public Rect getBoundingRect() {
		return new Rect(x - width / 2, y - height / 2, 
						x + width / 2, y + height / 2);
	}

	public Rect getAttackRect() {
		switch (direction) {
		case UP:
			return new Rect(x - attackDist, y - attackDist, x + attackDist, y);
		case DOWN:
			return new Rect(x - attackDist, y, x + attackDist, y + attackDist);
		case LEFT:
			return new Rect(x - attackDist, y - attackDist, x, y + attackDist);
		case RIGHT:
			return new Rect(x, y - attackDist, x + attackDist, y + attackDist);
		default:
			return null;
		}
	}
	
	public Rect getFollowRect() {
		return new Rect(x - followDist, y - followDist, x + followDist, y + followDist);
	}

	public Rect getActionRect() {
		switch (direction) {
		case UP:
			return new Rect(x - actionDist, y - actionDist, x + actionDist, y);
		case DOWN:
			return new Rect(x - actionDist, y, x + actionDist, y + actionDist);
		case LEFT:
			return new Rect(x - actionDist, y - actionDist, x, y + actionDist);
		case RIGHT:
			return new Rect(x, y - actionDist, x + actionDist, y + actionDist);
		default:
			return null;
		}
	}

	public void playOnce(Animation anim) {
		if (prevAnim == null)
			this.prevAnim = this.anim;
		this.anim = anim;
		this.frame = 0;
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

	public int getxSpeed() {
		return xSpeed;
	}

	public void setxSpeed(int xSpeed) {
		this.xSpeed = xSpeed;
	}

	public int getySpeed() {
		return ySpeed;
	}

	public void setySpeed(int ySpeed) {
		this.ySpeed = ySpeed;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public synchronized Bitmap getSprite() {
		return sprite;
	}

	public synchronized void setSprite(Bitmap sprite) {
		this.sprite = sprite;
	}

	public synchronized void setSprite(int id) {
		this.sprite = null;
		this.sprite = game.bmGetter.getBitmap(id);
	}

	public Animation getAnim() {
		return anim;
	}

	public void setAnim(Animation anim) {
		if (!animLocked) {
			this.anim = anim;
			this.frame = 0;
		} else {
			this.prevAnim = anim;
		}
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public double getFrame() {
		return frame;
	}

	public void setFrame(double frame) {
		this.frame = frame;
	}

	public int getHealth() {
		return health;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public boolean isDead() {
		return dead;
	}


	@Override
	public String toString() {
		return getClass().getSimpleName() + " { " + x + ", " + y + " }";
	}

}
