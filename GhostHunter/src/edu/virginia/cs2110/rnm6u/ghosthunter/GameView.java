package edu.virginia.cs2110.rnm6u.ghosthunter;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {

	Thread thread = null;
	SurfaceHolder holder;
	boolean paused = false;

	public GameView(Context context) {
		super(context);
		holder = getHolder();
	}

	@Override
	public void run() {
		if (!paused) {
			// game
		}

	}

	public void pause() {
		paused = true;
		while (true) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		}
		thread = null;
	}

	public void resume() {
		paused = false;
		thread = new Thread(this);
		thread.start();
	}

}
