package edu.virginia.cs2110.rnm6u.ghosthunter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class Entity {
	private static final String TAG = Entity.class.getSimpleName();

	public static final int UP = 0;
	public static final int LEFT = 1;
	public static final int DOWN = 2;
	public static final int RIGHT = 3;

	public static final int WALKING = 2;

	private static final int SCALE = 2;

	protected GameView game;
	protected int x, y;
	protected int xSpeed = 0, ySpeed = 0;
	protected Bitmap sprite;
	protected int animation;
	protected int width, height;
	protected int xOffset, yOffset;
	protected int numFrames;
	protected int direction = DOWN;
	protected int frame = 0;

	public Entity(int x, int y, GameView game) {
		this.game = game;
		this.x = x;
		this.y = y;
	}

	public void update() {
		x += xSpeed;
		y += ySpeed;
		frame++;
		if (frame >= numFrames) {
			frame = 0;
		}
	}

	public void draw(Canvas c) {
		int srcX = frame * width;
		int srcY = (animation * 4 + direction) * height;
		int dstX = x - xOffset * SCALE + game.getMap().getxOffset();
		int dstY = y - yOffset * SCALE + game.getMap().getyOffset();
		Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
		Rect dst = new Rect(dstX, dstY, dstX + width * SCALE, dstY + height * SCALE);
		c.drawBitmap(sprite, src, dst, null);
//		Log.d(TAG, "drawing: " + dstX + ", " + dstY);
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

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getAnimation() {
		return animation;
	}

	public void setAnimation(int animation) {
		this.animation = animation;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getFrame() {
		return frame;
	}

	public void setFrame(int frame) {
		this.frame = frame;
	}

}
