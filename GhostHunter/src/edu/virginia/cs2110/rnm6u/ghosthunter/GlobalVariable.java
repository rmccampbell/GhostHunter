package edu.virginia.cs2110.rnm6u.ghosthunter;

import android.app.Application;
// To use create an object in your class like this:
// GlobalVariable global = ((GlobalVariable) getApplicationContext());


public class GlobalVariable extends Application {

	private boolean musicOn;

	public boolean getMusicOn() {
		return musicOn;
	}

	public void setMusicOn(boolean m) {
		musicOn = m;
	}

}