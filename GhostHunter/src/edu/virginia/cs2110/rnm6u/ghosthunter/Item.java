package edu.virginia.cs2110.rnm6u.ghosthunter;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Item{

	protected int x, y;
	protected Bitmap image;
	protected GameView game;
	
	public Item(  /* Bitmap image, GameView game*/) { 
		
		/*
		this.x = x;
		this.y = y;
		
		this.image = image;
		this.game = game;
		*/
	}
	

	
	public void draw(int x, int y, Bitmap image, GameView game) {
		this.x = x*GameMap.TILESIZE + game.getMap().getxOffset();
		this.y = y*GameMap.TILESIZE + game.getMap().getyOffset();
		this.image = game.bmGetter.getBitmap(0);
		
		
	}
	
	
}
