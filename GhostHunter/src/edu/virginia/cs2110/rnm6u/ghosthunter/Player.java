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
		this.setSprite(R.drawable.no_armor_no_weapon);
		this.setAnim(STANDING);
		this.money = 0;
		this.speed = 16;
		this.maxHealth = GameView.INITIAL_HEALTH;
		this.health = health;
		this.attack = 5;
		this.weapon = null;
		this.armor = null;
		this.attackDist = 0;
		this.attackCooldown = 0;
		game.activity.displayHealth(health);
		game.activity.displayMoney(money);
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public boolean attack() {
		if (!super.attack()) return false;
		if (weapon instanceof Bow) {
			playOnce(BOWDRAWING);
			game.addArrow(this.x, this.y , this.direction);
		} else {
			if (weapon instanceof DragonSpear) {
				playOnce(ATTACKING_SPEAR);
			}
			Rect attRect = getAttackRect();
			for (Enemy enemy : game.getEnemies()) {
				if (attRect.intersect(enemy.getBoundingRect())) {
					enemy.takeDamage(attack);
				}
			}
		}
		return true;
	}

	@Override
	public boolean canAttack() {
		return super.canAttack() && weapon != null;
	}

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
		receiveItem(item);
	}

	public void receiveItem(Item item) {
		if (item instanceof Weapon) {
			drop(this.weapon);
			Weapon weapon = (Weapon) item;
			this.weapon = weapon;
			this.attack = weapon.attack;
			this.attackDist = weapon.attackDist;
			this.attackCooldown = weapon.cooldown;
			switchSprite();
		} else if (item instanceof Armor) {
			drop(this.armor);
			Armor armor = (Armor) item;
			this.armor = armor;
			this.defense = armor.defense;
			switchSprite();
		} else if (item instanceof Coins) {
			this.addMoney(((Coins) item).amount);
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
				setSprite(R.drawable.no_armor_no_weapon);
			} else if (weapon instanceof ShortSword) {
				setSprite(R.drawable.no_armor_short_sword);
			} else if (weapon instanceof LongSword) {
				setSprite(R.drawable.no_armor_longsword);
			} else if (weapon instanceof DragonSpear) {
				setSprite(R.drawable.no_armor_dragon_spear);
			}
		} else if (armor instanceof PlateArmor) {
			if (weapon == null) {
				setSprite(R.drawable.armor_no_weapon);
			} else if (weapon instanceof ShortSword) {
				setSprite(R.drawable.armor_short_sword);
			} else if (weapon instanceof LongSword) {
				setSprite(R.drawable.armor_longsword);
			} else if (weapon instanceof DragonSpear) {
				setSprite(R.drawable.armor_dragon_spear);
			}
		} else if (armor instanceof GoldArmor) {
			if (weapon == null) {
				setSprite(R.drawable.gold_armor_short_sword);
			} else if (weapon instanceof ShortSword) {
				setSprite(R.drawable.gold_armor_short_sword);
			} else if (weapon instanceof LongSword) {
				setSprite(R.drawable.gold_armor_longsword);
			} else if (weapon instanceof DragonSpear) {
				setSprite(R.drawable.gold_armor_dragon_spear);
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

	public Weapon getWeapon() {
		return weapon;
	}

	public Armor getArmor() {
		return armor;
	}

}
