package edu.virginia.cs2110.rnm6u.ghosthunter;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable, SurfaceHolder.Callback {
	private static final String TAG = GameView.class.getSimpleName();

	public Bitmap NO_ARMOR_NO_WEAPON;
	public Bitmap NO_ARMOR_SHORT_SWORD;
	public Bitmap ARMOR_SHORT_SWORD;
	public Bitmap GOLD_ARMOR_LONGSWORD;
	public Bitmap SKELETON;

//	public final int WIDTH = 0;
//	public final int HEIGHT = 0;

	Thread thread = null;
	SurfaceHolder holder;
	boolean running = false;
	private Random rand = new Random();

	private GameMap map;
	private ArrayList<Entity> entities;
	private Player player;

	public GameView(Context context) {
		super(context);
		holder = getHolder();
		holder.addCallback(this);
		loadSprites();
	}

	private void loadSprites() {
		BitmapFactory.Options opts = new BitmapFactory.Options();
//		opts.inSampleSize = 2;
		Resources res = getResources();
		NO_ARMOR_NO_WEAPON = BitmapFactory.decodeResource(res, R.drawable.no_armor_no_weapon, opts);
//		NO_ARMOR_SHORT_SWORD = BitmapFactory.decodeResource(res, R.drawable.no_armor_short_sword, opts);
//		ARMOR_SHORT_SWORD = BitmapFactory.decodeResource(res, R.drawable.armor_short_sword, opts);
//		GOLD_ARMOR_LONGSWORD = BitmapFactory.decodeResource(res, R.drawable.gold_armor_longsword, opts);
//		SKELETON = BitmapFactory.decodeResource(res, R.drawable.skeleton, opts);
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
			entities.add(new Skeleton(x, y, this));
		}
	}

	private void update() {
		for (Entity sprite : entities) {
			sprite.update();
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
