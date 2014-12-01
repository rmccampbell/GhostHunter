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
import android.widget.TextView;

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
	private ArrayList<Enemy> enemies;
	private ArrayList<NPC> npcs;
	private Player player;
	private Item[][] mapItems;
	
	public final BitmapGetter bmGetter;
	public final SoundPool sound;
	public final int attackSound;
	public final int actionSound;
	
	private int savedX;
	private int savedY;
	private int savedHealth;
	private int savedMonsters;
	final private int INITIAL_MONSTERS = 100;
	private int kills;

	@SuppressWarnings("deprecation")
	public GameView(Context context) {
		super(context);
		activity = (GameActivity) context;
		holder = getHolder();
		holder.addCallback(this);
		bmGetter = new BitmapGetter(getResources());
		sound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		attackSound = sound.load(getContext(), R.raw.whip, 1);
		actionSound = sound.load(getContext(), R.raw.clicker, 1);
		SharedPreferences prefs = context.getSharedPreferences("savedstate", Context.MODE_PRIVATE);
		this.savedX = prefs.getInt("X", 352);
		this.savedY = prefs.getInt("Y", 352);
		this.savedHealth = prefs.getInt("Health", 100);
		this.savedMonsters = prefs.getInt("Monsters", INITIAL_MONSTERS);
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
		map = new GameMap(R.raw.map3_main_map, R.drawable.tileset1, this);
		mapItems = new Item[map.getTileWidth()][map.getTileHeight()];
		mapItems[9][4] = new ShortSword(this);
		mapItems[3][3] = new DragonSpear(this);

		entities = new ArrayList<Entity>();
		player = new Player(savedX, savedY, savedHealth, this);
		if (player.isDead()) {
			Log.e(TAG, "Player spawned with collision");
			activity.finish();
		}
		entities.add(player);

		enemies = new ArrayList<Enemy>();
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
				enemies.add(monst);
			}
		}

		npcs = new ArrayList<NPC>();
		Shopkeeper shopkeeper = new Shopkeeper(0, 0, this);
		entities.add(shopkeeper);
		npcs.add(shopkeeper);

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
		c.drawRGB(0, 0, 0);
		map.draw(c);
		for (Entity sprite : entities) {
			sprite.draw(c);
		}
	}

	public void setDialogText(String text) {
		TextView dialog = (TextView) findViewById(R.id.dialog);
		dialog.setText(text);
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

	public void action() {
		Log.d(TAG, "Action");
		player.action();
		sound.play(actionSound, 1, 1, 1, 0, 1);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "surface created");
		resume();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "surface destroyed");
		pause();
	}

	public void resume() {
		Log.d(TAG, "resumed");
		if (holder.getSurface().isValid()) {
			running = true;
			thread = new Thread(this);
			thread.start();
		}
	}

	public void pause() {
		Log.d(TAG, "paused");
		if (running && thread !=null) {
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
	
	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}
	
	public ArrayList<NPC> getNPCs() {
		return npcs;
	}

	public Player getPlayer() {
		return player;
	}

	public Item[][] getMapItems() {
		return mapItems;
	}

	public GameActivity getGameActivity() {
		return activity;
	}
	
	public int getSavedMonsters() {
		return this.savedMonsters;
	}
	
	public void addKill() {
		kills++;
	}
	
	public int getKills() {
		return kills;
	}
	
	public int getInitialMonsters() {
		return INITIAL_MONSTERS;
	}
}
