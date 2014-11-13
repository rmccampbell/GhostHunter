package edu.virginia.cs2110.rnm6u.ghosthunter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable, SurfaceHolder.Callback {

	Thread thread = null;
	SurfaceHolder holder;
	boolean running = false;

	int x = 80, y = 80;
	int xSpeed = 0, ySpeed = 0;

	public GameView(Context context) {
		super(context);
		holder = getHolder();
		holder.addCallback(this);
	}

	@Override
	public void run() {
		while (running) {
			update();
			Canvas c = holder.lockCanvas();
			drawGame(c);
			holder.unlockCanvasAndPost(c);
		}
	}

	private void update() {
		x += xSpeed;
		y += ySpeed;
	}

	public void drawGame(Canvas c) {
		c.drawRGB(255, 255, 255);
		Paint p = new Paint();
		p.setStyle(Style.FILL);
		p.setColor(Color.LTGRAY);
		c.drawCircle(x, y, 80, p);
		p.setStyle(Style.STROKE);
		p.setStrokeWidth(8);
		p.setColor(Color.BLUE);
		c.drawCircle(x, y, 80, p);
	}

	public void moveUp() {
		Log.d("GameView", "move up");
		ySpeed = -8;
	}
	
	public void moveDown() {
		Log.d("GameView", "move down");
		ySpeed = 8;
	}
	
	public void moveLeft() {
		Log.d("GameView", "move left");
		xSpeed = -8;
	}
	
	public void moveRight() {
		Log.d("GameView", "move right");
		xSpeed = 8;
	}
	
	public void stopUp() {
		Log.d("GameView", "stop up");
		ySpeed = 0;
	}
	
	public void stopDown() {
		Log.d("GameView", "stop down");
		ySpeed = 0;
	}
	
	public void stopLeft() {
		Log.d("GameView", "stop left");
		xSpeed = 0;
	}
	
	public void stopRight() {
		Log.d("GameView", "stop right");
		xSpeed = 0;
	}
	
	public void attack() {
		Log.d("GameView", "attack");

	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d("GameView", "surface created");
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d("GameView", "surface destroyed");
		if (running) {
			pause();
		}
	}

	public void resume() {
		Log.d("GameView", "resumed");
	}

	public void pause() {
		Log.d("GameView", "paused");
		running = false;
		while (true) {
			try {
				thread.join();
				break;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		thread = null;
	}

}
