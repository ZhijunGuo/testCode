package com.example.galaxy08.message.audio;

import java.io.File;
import java.io.IOException;

import android.media.MediaPlayer;

public class AudioPlayer2 {
	private MediaPlayer mMediaPlayer = null;
	public AudioPlayer2(){
		mMediaPlayer = new MediaPlayer();
	}
	
	public void play(String path){
		stop();
		try {
			if (new File(path).exists()) {
				if(mMediaPlayer == null) {
					mMediaPlayer = new MediaPlayer();
				}
				mMediaPlayer.setDataSource(path);
				mMediaPlayer.prepare();
				mMediaPlayer.start();
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void stop() {
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}
}
