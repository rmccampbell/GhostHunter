package edu.virginia.cs2110.rnm6u.ghosthunter;

import android.util.Log;

public class NPC extends Entity {
	private static final String TAG = NPC.class.getSimpleName();

	public NPC(int x, int y, GameView game) {
		super(x, y, game);
	}

	public void interact() {
	}

	public void talk(String text) {
//		Log.d(TAG,"Talk: " + text);
		game.activity.displayDialog(text);
	}

}
