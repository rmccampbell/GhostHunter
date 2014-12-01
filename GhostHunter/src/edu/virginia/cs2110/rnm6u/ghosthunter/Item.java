package edu.virginia.cs2110.rnm6u.ghosthunter;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Item {

	protected Bitmap image;
	protected GameView game;
	
	public Item(int imageID, GameView game) { 
		this.game = game;
		this.image = game.bmGetter.getBitmap(imageID);
	}

}
