package edu.virginia.cs2110.rnm6u.ghosthunter;

public class Shopkeeper extends NPC {
	private static final String TAG = Shopkeeper.class.getSimpleName();

	private int actionCount = 0;

	public Shopkeeper(int x, int y, GameView game) {
		super(x, y, game);
		this.sprite = bmGetter.getBitmap(R.drawable.shopkeeper);
		setAnim(STANDING);
	}

	@Override
	public void interact() {
		super.interact();
		switch (actionCount) {
		case 0:
			talk("It's dangerous to go alone!");
			actionCount++;
			break;
		case 1:
			talk("Do you want to buy this spear? It's only $100.");
			actionCount++;
			break;
		case 2:
			Player player = game.getPlayer();
			if (player.getMoney() >= 100) {
				player.subtractMoney(100);
				player.recieveItem(new DragonSpear(game));
				talk("Thanks for your business!");
				actionCount++;
			} else {
				talk("You don't have enough money! It's $100.");
			}
			break;
		case 3:
			talk("Thanks for your business!");
			break;
		}
	}
}
