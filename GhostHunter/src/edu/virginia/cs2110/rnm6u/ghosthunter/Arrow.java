package edu.virginia.cs2110.rnm6u.ghosthunter;

import android.graphics.Bitmap;

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
	protected BitmapGetter bmGetter;
	protected Bitmap sprite;
	
	public Arrow(int x, int y, int direction, GameView game) {
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.game = game;
		this.bmGetter = game.bmGetter;
	}
	
	public void update() {
		x += speed;
		y += speed;
	}

	
	

}
