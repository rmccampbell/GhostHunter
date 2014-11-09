package edu.virginia.cs2110.rnm6u.ghosthunter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable, SurfaceHolder.Callback {

	Thread thread = null;
	SurfaceHolder holder;
	boolean running = false;
	int x = 80;

	public GameView(Context context) {
		super(context);
		holder = getHolder();
	}

	@Override
	public void run() {
		while (running) {
			if (!holder.getSurface().isValid()) {
				continue;
			}
			Canvas c = holder.lockCanvas();
			drawTo(c);
			holder.unlockCanvasAndPost(c);
			x+=6;
			x %= 1200;
		}
	}
	
	public void drawTo(Canvas c) {
		c.drawRGB(255, 255, 255);
		Paint p = new Paint();
		p.setStyle(Style.STROKE);
		p.setStrokeWidth(8);
		p.setColor(Color.BLUE);
		c.drawCircle(x, 350, 80, p);
	}

	public void pause() {
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

	public void resume() {
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

}
