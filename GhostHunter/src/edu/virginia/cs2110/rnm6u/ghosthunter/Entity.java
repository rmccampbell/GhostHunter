package edu.virginia.cs2110.rnm6u.ghosthunter;

import java.util.ArrayList;
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

	public static final Animation SPELLCASTING = new Animation(0, 64, 64, 32, 32+8, 7);
	public static final Animation STANDING = new Animation(64*4, 64, 64, 32, 32+8, 1);
	public static final Animation WALKING = new Animation(64*4, 64, 64, 32, 32+8, 9);
	public static final Animation BOWDRAWING = new Animation(64*8, 64, 64, 32, 32+8, 13);
	public static final Animation DYING = new Animation(64*12, 64, 64, 32, 32+8, 6, .5, false);
	public static final Animation ATTACKING = new Animation(64*13, 192, 192, 96, 96+8, 6);

	private static final int SCALE = 2;

	protected GameView game;
	protected BitmapGetter bmGetter;

	protected int x, y;
	protected int xSpeed = 0, ySpeed = 0;
	protected int speed;
	protected int width = 48, height = 96;
	protected int direction = DOWN;

	protected Bitmap sprite;
	protected Animation anim;
	protected double frame = 0;
	protected boolean playOnce = false;
	protected Animation prevAnim = null;
	protected boolean animLocked = false;

	protected int health, maxHealth;
	protected int attack;
	protected int attackDist = 64;

	protected boolean dying;
	protected boolean dead;

	protected int actionLock = 0;
	protected ArrayList<Entity> collidingEnts = new ArrayList<Entity>();

	public Entity(int x, int y, GameView game) {
		this.game = game;
		this.bmGetter = game.bmGetter;
		this.x = x;
		this.y = y;
	}

	public void update() {
		x += xSpeed;
		y += ySpeed;
		frame += anim.speed;
		if (frame >= anim.numFrames) {
			frame = 0;
			animLocked = false;
			if (playOnce && prevAnim != null) {
				setAnim(prevAnim);
				prevAnim = null;
				playOnce = false;
			}
			if (dying) {
				dead = true;
			}
		}
		if (actionLock > 0) actionLock--;
	}

	public void draw(Canvas c) {
		int srcX = ((int) frame) * anim.width;
		int srcY = anim.spriteOffset
				+ (anim.directional ? (direction * anim.height) : 0);
		int dstX = x - anim.xOffset * SCALE + game.getMap().getxOffset();
		int dstY = y - anim.yOffset * SCALE + game.getMap().getyOffset();
		Rect src = new Rect(srcX, srcY, srcX + anim.width, srcY + anim.height);
		Rect dst = new Rect(dstX, dstY, dstX + anim.width * SCALE, dstY
				+ anim.height * SCALE);
		c.drawBitmap(sprite, src, dst, null);
		Paint p = new Paint();
		p.setStyle(Style.STROKE);
		Rect r = getBoundingRect();
		r.offset(game.getMap().getxOffset(), game.getMap().getyOffset());
		c.drawRect(r, p);
		r = getAttackRect();
		r.offset(game.getMap().getxOffset(), game.getMap().getyOffset());
		c.drawRect(r, p);
	}

	public boolean attack() {
		if (!canAttack()) return false;
		Log.d(TAG, this + " attacked");
		playOnce(ATTACKING);
		animLocked = true;
		actionLock = 6;
		return true;
	}

	public boolean canAttack() {
		if (actionLock > 0) return false;
		return true;
	}

//	public void takeDamage(int damage) {
//		Log.d(TAG, "Taking damage: " + damage);
//		if (dying) return;
		// Paint p = new Paint();
		// p.setStyle(Style.STROKE);
		// Rect r = getAttackRect();
		// r.offset(game.getMap().getxOffset(), game.getMap().getyOffset());
		// c.drawRect(r, p);
		// Log.d(TAG, "drawing: " + dstX + ", " + dstY);
//	}

	public void takeDamage(int damage) {
		// Log.d(TAG, "Taking damage: " + damage);
		health -= damage;
		if (health <= 0) {
			health = 0;
			die();
		}
	}

	public void die() {
		playOnce(DYING);
		animLocked = true;
		actionLock = 6;
		dying = true;
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

	public void playOnce(Animation anim) {
		if (prevAnim == null)
			this.prevAnim = this.anim;
		this.anim = anim;
		this.frame = 0;
		this.playOnce = true;
	}

	public void updateCollides(ArrayList<Entity> entities) {
		Rect rectThis = new Rect(10, 10, this.x, this.y);
		for (Entity entity : entities) {
			Rect rectThat = new Rect(10, 10, this.x, this.y);
			if (rectThis.intersect(rectThat))
				collidingEnts.add(entity);
		}
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

	public Bitmap getCurrSprite() {
		return sprite;
	}

	public void setCurrSprite(Bitmap currSprite) {
		this.sprite = currSprite;
	}

	public Animation getAnim() {
		return anim;
	}

	public void setAnim(Animation anim) {
		if (!animLocked) {
			this.anim = anim;
			this.frame = 0;
		}
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		if (actionLock == 0) {
			this.direction = direction;
		}
	}

	public double getFrame() {
		return frame;
	}

	public void setFrame(double frame) {
		this.frame = frame;
	}

	public boolean isDead() {
		return dead;
	}

	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " { " + x + ", " + y + " }";
	}

}
