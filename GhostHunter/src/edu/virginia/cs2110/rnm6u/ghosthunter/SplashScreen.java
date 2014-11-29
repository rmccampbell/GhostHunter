package edu.virginia.cs2110.rnm6u.ghosthunter;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends Activity {
	private static final String TAG = SplashScreen.class.getSimpleName();

	private boolean started = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// These two lines hide action bar and make activity full screen,
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// setContentView must be last to prevent crash
		setContentView(R.layout.activity_splash_screen);
	}

	public void onTap(View view) {
		if (started) return;
		started = true;
		final Intent menuIntent = new Intent(this, MainMenu.class);
		final MediaPlayer chime = MediaPlayer.create(SplashScreen.this, R.raw.chime);
		chime.start();
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				chime.release();
				startActivity(menuIntent);
				finish();
			}
		}, 900);
	}

}
