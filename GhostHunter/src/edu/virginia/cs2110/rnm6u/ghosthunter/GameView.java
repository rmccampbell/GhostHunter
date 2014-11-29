package edu.virginia.cs2110.rnm6u.ghosthunter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable, SurfaceHolder.Callback {
	
	private static final String TAG = GameView.class.getSimpleName();

	GameActivity activity;
	private Thread thread = null;
	private SurfaceHolder holder;
	private boolean running = false;
	private boolean isInit = false;
	private Random rand = new Random();

	private Comparator<Entity> compByYPos = new Comparator<Entity>() {
		@Override
		public int compare(Entity a, Entity b) {
			return a.getY() - b.getY();
		}
	};

	private GameMap map;
	private ArrayList<Entity> entities;
	private Player player;
	
	public final BitmapGetter bmGetter;
	public final SoundPool sound;
	public final int attackSound;
	
	private int savedX;
	private int savedY;
	private int savedHealth;
	private int savedMonsters;

	@SuppressWarnings("deprecation")
	public GameView(Context context) {
		super(context);
		activity = (GameActivity) context;
		holder = getHolder();
		holder.addCallback(this);
		bmGetter = new BitmapGetter(getResources());
		sound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		attackSound = sound.load(getContext(), R.raw.whip, 1);
		SharedPreferences prefs = context.getSharedPreferences("savedstate", Context.MODE_PRIVATE);
		this.savedX = prefs.getInt("X", 352);
		this.savedY = prefs.getInt("Y", 352);
		this.savedHealth = prefs.getInt("Health", 100);
		this.savedMonsters = prefs.getInt("Monsters", 10);
	}

	@Override
	public void run() {
		if (!isInit) {
			init();
		}

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
		map = new GameMap(R.raw.map1, R.drawable.tileset1, this);
		entities = new ArrayList<Entity>();
		player = new Player(savedX, savedY, savedHealth, this);
		if (player.isDead()) {
			Log.e(TAG, "Player spawned with collision");
		}
		entities.add(player);
				
		for (int i = 0; i < savedMonsters; i++) {
			int x = rand.nextInt(map.getWidth() / 4) * 4;
			int y = rand.nextInt(map.getHeight() / 4) * 4;

			Enemy monst;
			float chance = rand.nextFloat();
			if (chance < .25) {
				monst = new Skeleton(x, y, this);
			} else {
				monst = new Goblin(x, y, this);
			}

			if (monst.isDead()) {
				i--;
			} else {
				entities.add(monst);
			}
		}
		isInit = true;
	}

	private void update() {
		Collections.sort(entities, compByYPos);
		Iterator<Entity> iter = entities.iterator();
		while (iter.hasNext()) {
			Entity entity = iter.next();
			entity.update();
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
		resume();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d("GameView", "surface destroyed");
		pause();
	}

	public void resume() {
		Log.d("GameView", "resumed");
		if (holder.getSurface().isValid()) {
			running = true;
			thread = new Thread(this);
			thread.start();
		}
	}

	public void pause() {
		Log.d("GameView", "paused");
		if (running && thread != null) {
			running = false;
			while (true) {
				try {
					thread.join();
					break;
				} catch (InterruptedException e) {
					Log.e(TAG, "Thread interrupted", e);
				}
			}
			thread = null;
		}
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

	public GameActivity getGameActivity() {
		return activity;
	}
	
	public int getSavedMonsters() {
		return this.savedMonsters;
	}

}
