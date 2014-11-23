package edu.virginia.cs2110.rnm6u.ghosthunter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainMenu extends Activity {

	GlobalVariable global;
	Button musicButton;
	SoundPool soundPool;
	int chimeSound;
	int clickerSound;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main_menu);
		global = ((GlobalVariable) getApplicationContext());
		musicButton = (Button) findViewById(R.id.music_button);
		if (global.getMusicOn()) {
			musicButton.setTextColor(Color.argb(255, 255, 255, 255));
			musicButton.setText("Music On");
		}
		else if (!global.getMusicOn()) {
			musicButton.setTextColor(Color.argb(255, 0, 0, 0));
			musicButton.setText("Music Off");
		}
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		chimeSound = soundPool.load(this, R.raw.chime, 1);
		clickerSound = soundPool.load(this, R.raw.clicker, 1);
	}

	public void startButtonPressed(View view) {
		soundPool.stop(chimeSound);
		soundPool.stop(clickerSound);
		Intent intent = new Intent(this, GameActivity.class);
		startActivity(intent);
	}

	public void musicButtonPressed(View view) {
		soundPool.play(clickerSound, 1, 1, 1, 0, 1);
		if (global.getMusicOn()) {
			musicButton.setTextColor(Color.argb(255, 0, 0, 0));
			musicButton.setText("Music Off");
			global.setMusicOn(false);
		}
		else if (!global.getMusicOn()) {
			musicButton.setTextColor(Color.argb(255, 255, 255, 255));
			musicButton.setText("Music On");
			global.setMusicOn(true);
		}
		Log.d("MainMenu", "Music");
	}

	public void aboutButtonPressed(View view) {
		Log.d("MainMenu", "About");
		soundPool.play(chimeSound, 1, 1, 1, 0, 1);
		final Toast toast = Toast.makeText(this, "Hi from Charlottesville :)",
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
		toast.show();
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				toast.cancel();
			}
		}, 750);
	}
}
