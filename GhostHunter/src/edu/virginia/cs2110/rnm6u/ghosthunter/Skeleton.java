package edu.virginia.cs2110.rnm6u.ghosthunter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Skeleton extends Monster {
	private static final String TAG = Skeleton.class.getSimpleName();

//	public static Bitmap SKELETON;

//	private static boolean spritesLoaded = false;

//	private static void loadSprites(Resources res) {
//		SKELETON = BitmapFactory.decodeResource(res, R.drawable.skeleton);
//		spritesLoaded = true;
//	}

	public Skeleton(int x, int y, GameView game) {
		super(x, y, game);

//		if (!spritesLoaded) {
//			loadSprites(game.getResources());
//		}

//		this.sprite = game.SKELETON;
		this.animation = WALKING;
		this.width = 32;//64;
		this.height = 32;//64;
		this.xOffset = 16;//32;
		this.yOffset = 16;//32;
		this.numFrames = 9;
	}

}
