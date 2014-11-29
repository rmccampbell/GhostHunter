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
		prefs.edit().putInt("X",x)
					.putInt("Y",y).commit();

		Intent intent = new Intent(this, MainMenu.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
/*	public void load(View v) {
		int x = prefs.getInt("X", 352);
		int y = prefs.getInt("Y", 352);
		Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra("X", x);
		intent.putExtra("Y", y);
		startActivity(intent);
	}
*/
}
