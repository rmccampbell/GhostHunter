package edu.virginia.cs2110.rnm6u.ghosthunter;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
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
	private GlobalVariable global;
	private MediaPlayer music;
	private SoundPool sound;
	private int chimeSound;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d(TAG, "onCreate");

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.game_ui);

		// MUSIC
		global = ((GlobalVariable) getApplicationContext());
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
		if (global.getMusicOn()) {
			music.start();
		}

		// SOUND Fx
		sound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		chimeSound = sound.load(this, R.raw.chime, 1);
		sound.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				if (sampleId == chimeSound) {
					sound.play(chimeSound, 1, 1, 1, 0, 1);
				}
			} 
		});

		game = new GameView(this);
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.game_layout);
		layout.addView(game, 0);

		findViewById(R.id.button_down).setOnTouchListener(this);
		findViewById(R.id.button_up).setOnTouchListener(this);
		findViewById(R.id.button_right).setOnTouchListener(this);
		findViewById(R.id.button_left).setOnTouchListener(this);
		findViewById(R.id.button_attack).setOnClickListener(this);
		findViewById(R.id.pause_button).setOnClickListener(this);
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
		if (global.getMusicOn()) {
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
		if (global.getMusicOn()) {
			music.start();
		}
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy");
		super.onDestroy();
	}

}
