package edu.virginia.cs2110.rnm6u.ghosthunter;

import java.util.Random;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Monster extends Entity {
	private static final String TAG = Monster.class.getSimpleName();

	private Random rand = new Random();

	public Monster(int x, int y, GameView game) {
		super(x, y, game);

		changeDir();
	}

	@Override
	public void update() {
		super.update();
		if (rand.nextFloat() < 0.05) {
			changeDir();
		}
	}

	public void changeDir() {
		direction = rand.nextInt(4);
		switch (direction) {
		case UP:
			xSpeed = 0;
			ySpeed = -6;
			break;
		case DOWN:
			xSpeed = 0;
			ySpeed = 6;
			break;
		case LEFT:
			xSpeed = -6;
			ySpeed = 0;
			break;
		case RIGHT:
			xSpeed = 6;
			ySpeed = 0;
			break;
		}
	}

	@Override
	public void draw(Canvas c) {
		Paint p = new Paint();
		p.setARGB(255, 0, 0, 255);
		c.drawCircle(x - 25 + game.getMap().getxOffset(), y - 25 + game.getMap().getyOffset(), 20, p);
	}

}
