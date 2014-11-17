package edu.virginia.cs2110.rnm6u.ghosthunter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Player extends Entity {
	private static final String TAG = Player.class.getSimpleName();

//	public static Bitmap NO_ARMOR_NO_WEAPON;
//	public static Bitmap NO_ARMOR_SHORT_SWORD;
//	public static Bitmap ARMOR_SHORT_SWORD;
//	public static Bitmap GOLD_ARMOR_LONGSWORD;

//	private static boolean spritesLoaded = false;

//	private static void loadSprites(Resources res) {
//		NO_ARMOR_NO_WEAPON = BitmapFactory.decodeResource(res, R.drawable.no_armor_no_weapon);
//		NO_ARMOR_SHORT_SWORD = BitmapFactory.decodeResource(res, R.drawable.no_armor_short_sword);
//		ARMOR_SHORT_SWORD = BitmapFactory.decodeResource(res, R.drawable.armor_short_sword);
//		GOLD_ARMOR_LONGSWORD = BitmapFactory.decodeResource(res, R.drawable.gold_armor_longsword);
//		spritesLoaded = true;
//	}

	public Player(int x, int y, GameView game) {
		super(x, y, game);
//		if (!spritesLoaded) {
//			loadSprites(game.getResources());
//		}

		this.sprite = game.NO_ARMOR_NO_WEAPON;
		this.animation = WALKING;
		this.width = 64;
		this.height = 64;
		this.xOffset = 32;
		this.yOffset = 32;
		this.numFrames = 9;
	}

	@Override
	public void update() {
		super.update();
//		Log.d(TAG, "updating: " + x + ", " + y);
	}

	public void moveUp() {
		Log.d("Player", "move up");
		ySpeed = -8;
		direction = UP;
	}
	
	public void moveDown() {
		Log.d("Player", "move down");
		ySpeed = 8;
		direction = DOWN;
	}
	
	public void moveLeft() {
		Log.d("Player", "move left");
		xSpeed = -8;
		direction = LEFT;
	}
	
	public void moveRight() {
		Log.d("Player", "move right");
		xSpeed = 8;
		direction = RIGHT;
	}
	
	public void stopUp() {
		Log.d("Player", "stop up");
		ySpeed = 0;
	}
	
	public void stopDown() {
		Log.d("Player", "stop down");
		ySpeed = 0;
	}
	
	public void stopLeft() {
		Log.d("Player", "stop left");
		xSpeed = 0;
	}
	
	public void stopRight() {
		Log.d("Player", "stop right");
		xSpeed = 0;
	}
	
	public void attack() {
		Log.d("Player", "attack");

	}

}
