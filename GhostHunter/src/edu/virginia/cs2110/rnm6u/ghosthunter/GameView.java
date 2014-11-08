package edu.virginia.cs2110.rnm6u.ghosthunter;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {

	Thread thread = null;
	SurfaceHolder holder;
	boolean running = false;

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
			c.drawRGB(255, 255, 255);
			// Game loop

			holder.unlockCanvasAndPost(c);
		}
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

}
