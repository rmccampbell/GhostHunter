package edu.virginia.cs2110.rnm6u.ghosthunter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainMenu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main);
	}
	
	public void startButtonPressed(View view) {
		Intent intent = new Intent(this, GameActivity.class);
		startActivity(intent);
		finish();
	}
	
	public void musicButtonPressed(View view) {
		Log.d("MainMenu", "Music");
	}
	
	public void aboutButtonPressed(View view) {
		Log.d("MainMenu", "About");
	}
}
