package edu.virginia.cs2110.rnm6u.ghosthunter;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class GameActivity extends Activity implements OnTouchListener,
		OnClickListener {

	GameView game;
	MediaPlayer musicPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.game_ui);
		musicPlayer = MediaPlayer.create(GameActivity.this, R.raw.song2);
		musicPlayer.start();

		game = new GameView(this);
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.game_layout);
		layout.addView(game, 0);

		findViewById(R.id.button_down).setOnTouchListener(this);
		findViewById(R.id.button_up).setOnTouchListener(this);
		findViewById(R.id.button_right).setOnTouchListener(this);
		findViewById(R.id.button_left).setOnTouchListener(this);
		findViewById(R.id.button_attack).setOnClickListener(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			switch (v.getId()) {
			case R.id.button_down:
				game.moveDown();
				break;
			case R.id.button_up:
				game.moveUp();
				break;
			case R.id.button_right:
				game.moveRight();
				break;
			case R.id.button_left:
				game.moveLeft();
				break;
			}
			break;
		case MotionEvent.ACTION_UP:
			switch (v.getId()) {
			case R.id.button_down:
				game.stopDown();
				break;
			case R.id.button_up:
				game.stopUp();
				break;
			case R.id.button_right:
				game.stopRight();
				break;
			case R.id.button_left:
				game.stopLeft();
				break;
			}
			break;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_attack:
			game.attack();
			break;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		game.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		game.resume();
	}

}
