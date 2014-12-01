package edu.virginia.cs2110.rnm6u.ghosthunter;

import android.graphics.Rect;
import android.util.Log;

public class Player extends Entity {
	private static final String TAG = Player.class.getSimpleName();

	private Weapon weapon;
	private Armor armor;
	private int money;

	public Player(int x, int y, int health, GameView game) {
		super(x, y, game);

		this.sprite = bmGetter.getBitmap(R.drawable.no_armor_no_weapon);
		this.setAnim(STANDING);

		this.speed = 16;
		this.maxHealth = 100;
		this.health = health;
		this.attack = 5;
		this.money = 100;

		this.weapon = null;
		this.armor = null;
		this.attackDist = 0;
		this.atkCooldown = 0;
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public boolean attack() {
		if (!super.attack()) return false;
		if (weapon instanceof DragonSpear) {
			playOnce(ATTACKING);
		}
		Rect attRect = getAttackRect();
		for (Entity entity : game.getEntities()) {
			if (entity == this) continue;
			if (entity instanceof Enemy) {
				if (attRect.intersect(entity.getBoundingRect())) {
					entity.takeDamage(attack);
				}
			}
		}
		return true;
	}

	@Override
	public boolean canAttack() {
		return super.canAttack() && weapon != null;
	};

	@Override
	public void takeDamage(int damage) {
		super.takeDamage(damage);
		game.activity.displayHealth(health);
	}

	public void action() {
		Log.d(TAG, "Action");
		Rect rect = getActionRect();
		for (NPC npc : game.getNPCs()) {
			if (npc.getBoundingRect().intersect(rect)) {
				npc.interact();
				return;
			}
		}
		pickUpItem();
	}

	public void pickUpItem() {
		int tsize = GameMap.TILESIZE;
		Item item = game.getMapItems()[x / tsize][y / tsize];
		game.getMapItems()[x / tsize][y / tsize] = null;
		recieveItem(item);
	}

	public void recieveItem(Item item) {
		if (item instanceof Weapon) {
			drop(this.weapon);
			Weapon weapon = (Weapon) item;
			this.weapon = weapon;
			this.attack = weapon.attack;
			this.attackDist = weapon.attackDist;
			this.atkCooldown = weapon.cooldown;
			switchSprite();
		} else if (item instanceof Armor) {
			drop(this.armor);
			Armor armor = (Armor) item;
			this.armor = armor;
			this.defense = armor.defense;
			switchSprite();
		} else if (item instanceof Coins) {
			this.money += ((Coins) item).amount;
		}
	}

	@Override
	public void despawn() {
		super.despawn();
		game.activity.gameOver();
	}

	public void moveUp() {
		ySpeed = -speed;
		setDirection(UP);
		setAnim(WALKING);
	}
	
	public void moveDown() {
		ySpeed = speed;
		setDirection(DOWN);
		setAnim(WALKING);
	}
	
	public void moveLeft() {
		xSpeed = -speed;
		setDirection(LEFT);
		setAnim(WALKING);
	}
	
	public void moveRight() {
		xSpeed = speed;
		setDirection(RIGHT);
		setAnim(WALKING);
	}
	
	public void stopUp() {
		ySpeed = 0;
		setAnim(STANDING);
	}
	
	public void stopDown() {
		ySpeed = 0;
		setAnim(STANDING);
	}
	
	public void stopLeft() {
		xSpeed = 0;
		setAnim(STANDING);
	}
	
	public void stopRight() {
		xSpeed = 0;
		setAnim(STANDING);
	}

	public void switchSprite() {
		if (armor == null) {
			if (weapon == null) {
				sprite = bmGetter.getBitmap(R.drawable.no_armor_no_weapon);
			} else if (weapon instanceof ShortSword) {
				sprite = bmGetter.getBitmap(R.drawable.no_armor_short_sword);
			} else if (weapon instanceof LongSword) {
				sprite = bmGetter.getBitmap(R.drawable.no_armor_longsword);
			} else if (weapon instanceof DragonSpear) {
				sprite = bmGetter.getBitmap(R.drawable.no_armor_dragon_spear);
			}
		} else if (armor instanceof PlateArmor) {
			if (weapon == null) {
				sprite = bmGetter.getBitmap(R.drawable.armor_no_weapon);
			} else if (weapon instanceof Weapon) {
				sprite = bmGetter.getBitmap(R.drawable.armor_short_sword);
			} else if (weapon instanceof LongSword) {
				sprite = bmGetter.getBitmap(R.drawable.armor_longsword);
			} else if (weapon instanceof DragonSpear) {
				sprite = bmGetter.getBitmap(R.drawable.armor_dragon_spear);
			}
		} else if (armor instanceof GoldArmor) {
			if (weapon == null) {
				sprite = bmGetter.getBitmap(R.drawable.gold_armor_short_sword);
			} else if (weapon instanceof Weapon) {
				sprite = bmGetter.getBitmap(R.drawable.gold_armor_short_sword);
			} else if (weapon instanceof LongSword) {
				sprite = bmGetter.getBitmap(R.drawable.gold_armor_longsword);
			} else if (weapon instanceof DragonSpear) {
				sprite = bmGetter.getBitmap(R.drawable.gold_armor_dragon_spear);
			}
		}
	}

	public int getMoney() {
		return money;
	}
	
	public void addMoney(int amount) {
		money += amount;
		game.activity.displayMoney(money);
	}

	public void subtractMoney(int amount) {
		money -= amount;
		game.activity.displayMoney(money);
	}

}
