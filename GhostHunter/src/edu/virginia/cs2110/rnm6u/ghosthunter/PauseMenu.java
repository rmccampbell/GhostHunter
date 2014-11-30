package edu.virginia.cs2110.rnm6u.ghosthunter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class PauseMenu extends Activity {
	private static final String TAG = PauseMenu.class.getSimpleName();

	SharedPreferences prefs;
	GameView game;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.pause_menu);

		prefs = this.getSharedPreferences("savedstate", Context.MODE_PRIVATE);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			saveAndQuit(null);
//			return true;
//		}
		return super.onKeyDown(keyCode, event);
	}

	public void resume(View v) {
		finish();
	}

	public void saveAndQuit(View v) {
		int x = getIntent().getIntExtra("X", 352);
		int y = getIntent().getIntExtra("Y", 352);
		int health = getIntent().getIntExtra("Health", 100);
		int monsters = getIntent().getIntExtra("Monsters", 10);
		prefs.edit().putInt("X", x)
					.putInt("Y", y)
					.putInt("Health", health)
					.putInt("Monsters", monsters).commit();

		Intent intent = new Intent(this, MainMenu.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	public void reset(View v) {
		prefs.edit().putInt("X", 352)
					.putInt("Y", 352)
					.putInt("Health", 100)
					.putInt("Monsters", 10).commit();
		Intent intent = new Intent(this, MainMenu.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

}
