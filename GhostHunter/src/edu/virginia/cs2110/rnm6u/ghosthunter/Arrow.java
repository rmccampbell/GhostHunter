package edu.virginia.cs2110.rnm6u.ghosthunter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Arrow {

	protected int x;
	protected int y;
	protected int direction;
	protected int speed = 20;
	protected int damage = 5;
	
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
		this.image = game.bmGetter.getBitmap(0);
	}
	
	public void update() {
		x += speed;
		y += speed;
	}
	
	public void draw(Canvas c) {
		c.drawBitmap(image, x + game.getMap().getxOffset(), y + game.getMap().getyOffset(), null);
	}

	
	

	
	

}
