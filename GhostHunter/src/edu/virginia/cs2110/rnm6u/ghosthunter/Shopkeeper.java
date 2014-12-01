package edu.virginia.cs2110.rnm6u.ghosthunter;

public class Shopkeeper extends NPC {

	private int actionCount = 0;

	public Shopkeeper(int x, int y, GameView game) {
		super(x, y, game);
		this.sprite = bmGetter.getBitmap(R.drawable.shopkeeper);
	}

	@Override
	public void interact() {
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
			if (game.getPlayer().getMoney() >= 100) {
				game.getPlayer().recieveItem(new DragonSpear(game));
				actionCount++;
			} else {
				talk("You don't have enough money! It's $100.");
			}
			break;
		default:
			talk("Thanks for your business!");
		}
	}
}
