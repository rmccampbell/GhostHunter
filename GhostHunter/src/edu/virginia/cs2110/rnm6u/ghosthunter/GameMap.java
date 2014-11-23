package edu.virginia.cs2110.rnm6u.ghosthunter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class GameMap {

	private static final String TAG = GameMap.class.getSimpleName();
	private static final int SCALE = 1;

	private GameView game;
	private Bitmap tileSet;
	private int[][] tiles;
	private boolean[][] walls;
	private int width, height;
	private int xOffset = 0, yOffset = 0;

	public GameMap(GameView game) {
		this.game = game;
		width = game.getWidth();
		height = game.getHeight();
		tiles = new int[32][32];
		walls = new boolean[32][32];
	}

	public void update() {
//		Log.d(TAG, "updating: " + xOffset + ", " + yOffset);
		xOffset = game.getWidth() / 2 - game.getPlayer().getX();
		yOffset = game.getHeight() / 2 - game.getPlayer().getY();
	}

	public void draw(Canvas c) {
//		Log.d(TAG, "drawing: " + xOffset + ", " + yOffset);
//		c.drawBitmap(image, -xOffset, -yOffset, null);
		Paint p = new Paint();
		p.setARGB(255, 127, 127, 255);
		c.drawRect(xOffset, yOffset, game.getWidth() + xOffset, game.getHeight() + yOffset, p);
	}
	
	public boolean[][] getWalls() {
		return walls;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getxOffset() {
		return xOffset;
	}

	public void setxOffset(int xOffset) {
		this.xOffset = xOffset;
	}

	public int getyOffset() {
		return yOffset;
	}

	public void setyOffset(int yOffset) {
		this.yOffset = yOffset;
	}

}
