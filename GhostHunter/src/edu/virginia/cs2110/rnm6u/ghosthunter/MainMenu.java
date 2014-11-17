package edu.virginia.cs2110.rnm6u.ghosthunter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainMenu extends Activity {

	GlobalVariable gvObj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main_menu);
		gvObj = ((GlobalVariable) getApplicationContext());
	}

	public void startButtonPressed(View view) {
		Intent intent = new Intent(this, GameActivity.class);
		startActivity(intent);
	}

	public void musicButtonPressed(View view) {
		Button musicButton = (Button) findViewById(R.id.music_button);
		if (!gvObj.getMusicOn()) {
			musicButton.setTextColor(Color.argb(255, 255, 255, 255));
			musicButton.setText("Music On");
			gvObj.setMusicOn(true);
		} else {
			musicButton.setTextColor(Color.argb(255, 0, 0, 0));
			musicButton.setText("Music Off");
			gvObj.setMusicOn(false);
		}
		Log.d("MainMenu", "Music");
	}

	public void aboutButtonPressed(View view) {
		Log.d("MainMenu", "About");
		Toast toast = Toast.makeText(this, "Hi from Charlottesville :)",
				Toast.LENGTH_SHORT);
		toast.show();
	}
}
