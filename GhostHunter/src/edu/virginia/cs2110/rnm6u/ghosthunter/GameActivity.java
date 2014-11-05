package edu.virginia.cs2110.rnm6u.ghosthunter;

import android.app.Activity;
import android.os.Bundle;

public class GameActivity extends Activity {

	GameView v;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		v = new GameView(this);
		setContentView(v);
	}

	@Override
	protected void onPause() {
		super.onPause();
		v.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		v.resume();
	}

}
