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

	public final GameActivity activity;

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
	protected ArrayList<Arrow> arrows;
	private Player player;
	private DarkKnight darkKnight;
	private Shopkeeper shopkeeper;
	private Item[][] mapItems;
	
	public final BitmapGetter bmGetter;
	public final SoundPool sound;
	public final int attackSound;
	public final int actionSound;

	public static final int INITIAL_MONSTERS = 20;
	public static final int INITIAL_HEALTH = 200;
	public static final int INITIAL_X = 2*64 + 32;
	public static final int INITIAL_Y = 5*64 + 32;

	private int savedX;
	private int savedY;
	private int savedHealth;
	private int savedMoney;
	private int savedKills;
	private String savedWeapon;
	private String savedArmor;
	private int savedMonsters;
	private boolean savedDarkKnight;

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
		this.savedX = prefs.getInt("X", INITIAL_X);
		this.savedY = prefs.getInt("Y", INITIAL_Y);
		this.savedHealth = prefs.getInt("Health", INITIAL_HEALTH);
		this.savedMoney = prefs.getInt("Money", 0);
		this.savedKills = prefs.getInt("Kills", 0);
		this.savedWeapon = prefs.getString("Weapon", "");
		this.savedArmor = prefs.getString("Armor", "");
		this.savedMonsters = prefs.getInt("Monsters", INITIAL_MONSTERS);
		this.savedDarkKnight = prefs.getBoolean("DarkKnight", true);
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
		mapItems[3][4] = new ShortSword(this);
		mapItems[14][38] = new LongSword(this);
		mapItems[3][6] = new PlateArmor(this);
		mapItems[3][7] = new Bow(this);		

		entities = new ArrayList<Entity>();
		arrows = new ArrayList<Arrow>();
		player = new Player(savedX, savedY, savedHealth, this);
		if (player.isDead()) {
			Log.e(TAG, "Player spawned with collision");
			activity.finish();
		}
		player.addMoney(savedMoney);
		player.receiveItem(weaponFromString(savedWeapon));
		player.receiveItem(armorFromString(savedArmor));
		entities.add(player);

		npcs = new ArrayList<NPC>();
		shopkeeper = new Shopkeeper(37*64 + 32,3*64 + 32, this);
		entities.add(shopkeeper);
		npcs.add(shopkeeper);

		enemies = new ArrayList<Enemy>();

		if (savedDarkKnight) {
			darkKnight = new DarkKnight(29*64+32, 25*64+32, this);
			entities.add(darkKnight);
			enemies.add(darkKnight);
		}

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

		kills = savedKills;
		activity.displayKills(kills);

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
				if (entity instanceof Enemy) 
					enemies.remove(entity);
				else if (entity instanceof NPC)
					npcs.remove(entity);
			}
		}
		
		Iterator<Arrow> arrowIt = arrows.iterator();
		while (arrowIt.hasNext()) {
			Arrow arrow = arrowIt.next();
			arrow.update();
			if (!arrow.isAlive()) {
				arrowIt.remove();
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
		for(Arrow arrow : arrows) {
			arrow.draw(c);
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

	public void action() {
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

	public DarkKnight getDarkKnight() {
		return darkKnight;
	}

	public Shopkeeper getShopkeeper() {
		return shopkeeper;
	}

	public Item[][] getMapItems() {
		return mapItems;
	}

	public int getSavedMonsters() {
		return this.savedMonsters;
	}

	public void addKill() {
		kills++;
		activity.displayKills(kills);
	}

	public int getKills() {
		return kills;
	}

	public String weaponToString(Weapon weapon) {
		if (weapon == null)
			return "";
		return weapon.getClass().getName();
	}

	public Weapon weaponFromString(String name) {
		if (name.equals("")) return null;
		try {
			Class<? extends Weapon> clazz = Class.forName(name).asSubclass(Weapon.class);
			return clazz.getConstructor(GameView.class).newInstance(this);
		} catch(ClassNotFoundException e) {
			Log.e(TAG, "Weapon class not found: " + name, e);
			return null;
		} catch (ClassCastException e) {
			Log.e(TAG,  "Class not subclass of Weapon: " + name, e);
			return null;
		} catch(Exception e) {
			Log.e(TAG,  "Error loading weapon class: " + name, e);
			return null;
		}
	}

	public String armorToString(Armor armor) {
		if (armor == null)
			return "";
		return armor.getClass().getName();
	}

	public Armor armorFromString(String name) {
		if (name.equals("")) return null;
		try {
			Class<? extends Armor> clazz = Class.forName(name).asSubclass(Armor.class);
			return clazz.getConstructor(GameView.class).newInstance(this);
		} catch(ClassNotFoundException e) {
			Log.e(TAG, "Armor class not found: " + name, e);
			return null;
		} catch (ClassCastException e) {
			Log.e(TAG,  "Class not subclass of Armor: " + name, e);
			return null;
		} catch(Exception e) {
			Log.e(TAG,  "Error loading armor class: " + name, e);
			return null;
		}
	}

	public void addArrow(int x, int y, int direction) {
		Arrow arrow = new Arrow(x, y, direction, this);
		arrows.add(arrow);
	}

}
