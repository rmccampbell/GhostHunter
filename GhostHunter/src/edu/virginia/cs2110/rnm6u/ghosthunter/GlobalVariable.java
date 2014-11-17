package edu.virginia.cs2110.rnm6u.ghosthunter;

import android.app.Application;

public class GlobalVariable extends Application {

	private boolean musicOn;

	public boolean getMusicOn() {
		return musicOn;
	}

	public void setMusicOn(boolean m) {
		musicOn = m;
	}

}