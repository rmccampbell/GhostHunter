package edu.virginia.cs2110.rnm6u.ghosthunter;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GameActivity extends Activity implements OnTouchListener,
		OnClickListener {
	private static final String TAG = GameActivity.class.getSimpleName();

	private GameView game;
	SharedPreferences prefs;
	private MediaPlayer music;
	private Handler handler;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d(TAG, "onCreate");

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.game_ui);
		
		prefs = this.getSharedPreferences("savedstate", Context.MODE_PRIVATE);

		// MUSIC
		Random randy = new Random();
		int songNumber = 1 + randy.nextInt(4);
		switch (songNumber) {
		case 1:
			music = MediaPlayer.create(this, R.raw.song1);
			break;
		case 2:
			music = MediaPlayer.create(this, R.raw.song2);
			break;
		case 3:
			music = MediaPlayer.create(this, R.raw.song3);
			break;
		case 4:
			music = MediaPlayer.create(this, R.raw.song4);
			break;
		}
		music.setLooping(true);
		if (prefs.getBoolean("Music", true)) {
			music.start();
		}

		game = new GameView(this);
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.game_layout);
		layout.addView(game, 0);

		findViewById(R.id.button_down).setOnTouchListener(this);
		findViewById(R.id.button_up).setOnTouchListener(this);
		findViewById(R.id.button_right).setOnTouchListener(this);
		findViewById(R.id.button_left).setOnTouchListener(this);
		findViewById(R.id.button_attack).setOnClickListener(this);
		findViewById(R.id.button_action).setOnClickListener(this);
		findViewById(R.id.pause_button).setOnClickListener(this);

		handler = new Handler();
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
		case R.id.button_action:
			game.action();
			break;
		case R.id.pause_button:
			openPauseMenu();
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			openPauseMenu();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void openPauseMenu() {
		Intent intent = new Intent(this, PauseMenu.class);
		intent.putExtra("X", game.getPlayer().getX());
		intent.putExtra("Y", game.getPlayer().getY());
		intent.putExtra("Health", game.getPlayer().getHealth());
		intent.putExtra("Monsters", (game.getEntities().size() - 1));
		startActivity(intent);
	}

	public void gameOver() {
		Intent intent = new Intent(this, GameOver.class);
		startActivity(intent);
	}

	public void displayHealth(int health) {
		TextView view = (TextView) findViewById(R.id.health);
		view.setText(String.valueOf(health));
	}

	public void displayMoney(int money) {
		TextView view = (TextView) findViewById(R.id.money);
		view.setText(String.valueOf(money));
	}

	public void displayKills(int kills) {
		TextView view = (TextView) findViewById(R.id.kills);
		view.setText(String.valueOf(kills));
	}

	public void displayDialog(String text) {
		final TextView dialog = (TextView) findViewById(R.id.dialog);
		dialog.setText(text);

		handler.removeCallbacksAndMessages(null);
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				dialog.setText("");
			}
		}, 5000);
	}

	@Override
	protected void onPause() {
		Log.d(TAG, "onPause");
		super.onPause();
		music.pause();
		game.pause();
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
		if (prefs.getBoolean("Music", true)) {
			music.start();
		}
		game.resume();
	}

	@Override
	protected void onStop() {
		Log.d(TAG, "onStop");
		super.onStop();
		music.pause();
	}

	@Override
	protected void onRestart() {
		Log.d(TAG, "onRestart");
		super.onRestart();
		if (prefs.getBoolean("Music", true)) {
			music.start();
		}
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy");
		super.onDestroy();
	}

}
