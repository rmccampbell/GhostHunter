package edu.virginia.cs2110.rnm6u.ghosthunter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.util.Log;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;

public class GameView extends SurfaceView implements Runnable, SurfaceHolder.Callback {
	
	private static final String TAG = GameView.class.getSimpleName();

//	public final int WIDTH = 0;
//	public final int HEIGHT = 0;

	Thread thread = null;
	SurfaceHolder holder;
	boolean running = false;
	BitmapGetter bmGetter;

	private Random rand = new Random();

	private GameMap map;
	private ArrayList<Entity> entities;
	private Player player;

	public GameView(Context context) {
		super(context);
		holder = getHolder();
		holder.addCallback(this);
		bmGetter = new BitmapGetter(getResources());
	}

	@Override
	public void run() {
		init();

		while (running) {
			Canvas c = holder.lockCanvas();
			drawGame(c);
			holder.unlockCanvasAndPost(c);

			update();
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void init() {
		map = new GameMap(this);
		entities = new ArrayList<Entity>();
		player = new Player(400, 400, this);
		entities.add(player);
		for (int i = 0; i < 1 + rand.nextInt(15); i++) {
			int x = rand.nextInt(map.getWidth());
			int y = rand.nextInt(map.getHeight());
			Enemy monst;
			if (rand.nextFloat() > .75) {
				monst = new Skeleton(x, y, this);
			} else {
				monst = new Goblin(x, y, this);
			}
			entities.add(monst);
		}
	}

	private void update() {
		Collections.sort(entities, new Comparator<Entity>() {
			@Override
			public int compare(Entity a, Entity b) {
				return a.getY() - b.getY();
			}
		});
		Iterator<Entity> iter = entities.iterator();
		while (iter.hasNext()) {
			Entity entity = iter.next();
			entity.update();
			entity.updateCollides(entities);
			if (entity.isDead()) {
				iter.remove();
			}
		}
		map.update();
	}

	public void drawGame(Canvas c) {
		c.drawRGB(255, 255, 255);
		map.draw(c);
		for (Entity sprite : entities) {
			sprite.draw(c);
		}
	}

	public void moveUp() {
		player.moveUp();
	}

	public void moveDown() {
		player.moveDown();
	}

	public void moveLeft() {
		player.moveLeft();
	}

	public void moveRight() {
		player.moveRight();
	}

	public void stopUp() {
		player.stopUp();
	}

	public void stopDown() {
		player.stopDown();
	}

	public void stopLeft() {
		player.stopLeft();
	}

	public void stopRight() {
		player.stopRight();
	}

	public void attack() {
		player.attack();
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

	public GameMap getMap() {
		return map;
	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}

	public Player getPlayer() {
		return player;
	}

}
