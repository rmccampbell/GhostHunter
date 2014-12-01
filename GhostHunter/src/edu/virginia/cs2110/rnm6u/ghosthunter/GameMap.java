package edu.virginia.cs2110.rnm6u.ghosthunter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.util.Log;

public class GameMap {

	private static final String TAG = GameMap.class.getSimpleName();

	private static final int SCALE = 2;
	private static final int SRC_TILESIZE = 32;
	private static final int WALL_TILE = 24;

	public static final int TILESIZE = SCALE * SRC_TILESIZE;

	private GameView game;
	private Bitmap tileSet;
	private int[][] tiles;
	private int tileWidth, tileHeight;
	private int width, height;
	private int xOffset = 0, yOffset = 0;
	private boolean[][] walls;

	public GameMap(int dataResId, int tilesetResId, GameView game) {
		this.game = game;
		this.tileSet = BitmapFactory.decodeResource(game.getResources(), tilesetResId);
		this.tiles = new ParseMap(game.getResources()).get2dArray(dataResId);
		tileWidth = tiles.length;
		tileHeight = tiles[0].length;
		width = tileWidth * TILESIZE;
		height = tileHeight * TILESIZE;
		walls = new boolean[tileWidth][tileHeight];
		for (int x = 0; x < tileWidth; x++) {
			for (int y = 0; y < tileHeight; y++) {
				walls[x][y] = tiles[x][y] >= WALL_TILE;
			}
		}
	}

	public void update() {
		xOffset = game.getWidth() / 2 - game.getPlayer().getX();
		yOffset = game.getHeight() / 2 - game.getPlayer().getY();
	}

	public void draw(Canvas c) {
//		Paint p = new Paint();
//		p.setARGB(255, 0, 0, 255);
//		p.setStyle(Style.STROKE);
//		p.setStrokeWidth(5);
		Item[][] items = game.getMapItems();
		int tilesPerRow = tileSet.getWidth() / SRC_TILESIZE;
		for (int y = 0; y < tileHeight; y++) {
			for (int x = 0; x < tileWidth; x++) {
				int tile = tiles[x][y];
				int srcX = tile % tilesPerRow * SRC_TILESIZE;
				int srcY = tile / tilesPerRow * SRC_TILESIZE;
				Rect srcRect = new Rect(srcX, srcY, srcX + SRC_TILESIZE, srcY + SRC_TILESIZE);
				int dstX = x * TILESIZE + xOffset;
				int dstY = y * TILESIZE + yOffset;
				Rect dstRect = new Rect(dstX, dstY, dstX + TILESIZE, dstY + TILESIZE);
				c.drawBitmap(tileSet, srcRect,  dstRect, null);

				Item item = items[x][y];
				if (item != null) {
					c.drawBitmap(item.image, x * TILESIZE + xOffset, 
							y * TILESIZE + yOffset, null);
				}
			}
		}
	}

	public int getTileWidth() {
		return tileWidth;
	}

	public int getTileHeight() {
		return tileHeight;
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

	public int getyOffset() {
		return yOffset;
	}

	public int[][] getTiles() {
		return tiles;
	}

	public boolean[][] getWalls() {
		return walls;
	}
	
	public Rect getRect() {
		return new Rect(0, 0, width, height);
	}

}
