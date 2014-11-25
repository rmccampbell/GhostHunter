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

	public static final Animation SPELLCASTING = new Animation(0, 64, 64, 32, 32+16, 7);
	public static final Animation STANDING = new Animation(64*4, 64, 64, 32, 32+16, 1);
	public static final Animation WALKING = new Animation(64*4, 64, 64, 32, 32+16, 9);
	public static final Animation BOWDRAWING = new Animation(64*8, 64, 64, 32, 32+16, 13);
	public static final Animation DYING = new Animation(64*12, 64, 64, 32, 32+16, 6, 1, false);
	public static final Animation ATTACKING = new Animation(64*13, 192, 192, 96, 96+16, 6);

	private static final int SCALE = 2;

	protected GameView game;
	protected BitmapGetter bmGetter;

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
	protected int attack;
	protected int attackDist = 64;

	protected boolean dying;
	protected boolean dead;
	
	protected int actionTimer = 0;
	protected ArrayList<Entity> collidingEnts = new ArrayList<Entity>();

	public Entity(int x, int y, GameView game) {
		this.game = game;
		this.bmGetter = game.bmGetter;
		this.x = x;
		this.y = y;

		if (hasCollision()) {
			dead = true;
			return;
		}

		int tsize = GameMap.TILESIZE;
		Rect r = getBoundingRect();
		Entity[][] positions = game.getEntityPositions();
		for (int tx = r.left / tsize; tx <= (r.right-1) / tsize; tx++) {
			for (int ty = r.top / tsize; ty <= (r.bottom-1) / tsize; ty++) {
				positions[tx][ty] = this;
			}			
		}
	}

	public void update() {
		int prevX = x;
		int prevY = y;
		Rect r = getBoundingRect();

		x += xSpeed;
		y += ySpeed;

		if (hasCollision()) {
			x = prevX;
			y = prevY;
		} 

		if (x != prevX || y != prevY) {
			int tsize = GameMap.TILESIZE;
			Entity[][] positions = game.getEntityPositions();
			for (int tx = r.left / tsize; tx <= (r.right-1) / tsize; tx++) {
				for (int ty = r.top / tsize; ty <= (r.bottom-1) / tsize; ty++) {
					positions[tx][ty] = null;
				}			
			}

			Rect newr = getBoundingRect();
			for (int tx = newr.left / tsize; tx <= (newr.right-1) / tsize; tx++) {
				for (int ty = newr.top / tsize; ty <= (newr.bottom-1) / tsize; ty++) {
					positions[tx][ty] = this;
				}			
			}
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
		c.drawBitmap(sprite, src, dst, null);
		Paint p = new Paint();
		p.setStyle(Style.STROKE);
		p.setStrokeWidth(2);
		Rect r = getBoundingRect();
		r.offset(game.getMap().getxOffset(), game.getMap().getyOffset());
		c.drawRect(r, p);
		r = getAttackRect();
		r.offset(game.getMap().getxOffset(), game.getMap().getyOffset());
		c.drawRect(r, p);
	}

	public boolean hasCollision() {
		Rect r = getBoundingRect();
		if (!game.getMap().getRect().contains(r)) {
			return true;
		}
		int tsize = GameMap.TILESIZE;
		Entity[][] positions = game.getEntityPositions();
		boolean[][] walls = game.getMap().getWalls();
		for (int tx = r.left / tsize; tx <= (r.right-1) / tsize; tx++) {
			for (int ty = r.top / tsize; ty <= (r.bottom-1) / tsize; ty++) {
				if (walls[tx][ty])
					return true;
				Entity e = positions[tx][ty];
				if (e != null && e != this && e.getBoundingRect().intersect(r))
					return true;
			}			
		}
		return false;
	}

//	public boolean collidesWith(Entity e) {
//		return this.getBoundingRect().intersect(e.getBoundingRect());
//	}

	public boolean attack() {
		if (!canAttack()) return false;
		Log.d(TAG, this + " attacking");
		playOnce(ATTACKING);
		animLocked = true;
		actionTimer = 6;
		return true;
	}

	public boolean canAttack() {
		if (actionTimer != 0) return false;
		return true;
	}

	public void takeDamage(int damage) {
//		Log.d(TAG, this + " taking damage: " + damage);
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
		Log.d(TAG, this + " dying");
	}

	public void despawn() {
		Log.d(TAG, this + " dead");
		dead = true;
		actionTimer = -1;
		int tsize = GameMap.TILESIZE;
		Rect r = getBoundingRect();
		Entity[][] positions = game.getEntityPositions();
		for (int tx = r.left / tsize; tx <= (r.right-1) / tsize; tx++) {
			for (int ty = r.top / tsize; ty <= (r.bottom-1) / tsize; ty++) {
				if (tx >= 0 && tx < positions.length && ty >= 0 && ty < positions[tx].length)
					positions[tx][ty] = null;
			}
		}
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

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Bitmap getSprite() {
		return sprite;
	}

	public void setSprite(Bitmap sprite) {
		this.sprite = sprite;
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
