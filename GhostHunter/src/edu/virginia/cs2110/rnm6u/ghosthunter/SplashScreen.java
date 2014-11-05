package edu.virginia.cs2110.rnm6u.ghosthunter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class SplashScreen extends Activity {

	Button button;

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
		Intent intent = new Intent(this, MainActivity.class);
	    startActivity(intent);
	    // Prevent from going back
	    finish();
	}

}
