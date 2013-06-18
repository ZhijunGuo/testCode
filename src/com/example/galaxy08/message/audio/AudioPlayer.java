/**
 * 
 */
package com.example.galaxy08.message.audio;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

import net.afpro.utils.Decoder;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.os.Message;

import com.example.galaxy08.tool.DebugLog;


/**
 * AudioPlayer 音频播放类 2013/04/15
 */
public class AudioPlayer {

	private AudioDecoder mDecoder;

	public static int SIMPLE_RATE = 8000;

	private static AudioPlayer instance;
	
	private int streamType = AudioManager.STREAM_MUSIC;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (mDecoder != null)
				mDecoder.forceClose(false);
		}
	};

	public static interface PlayerListener {
		public void onPlayStart();

		public void onPlayStop(boolean interrupt);
	}

	private PlayerListener mPlayerListener;

	public void setPlayerListener(PlayerListener mPlayerListener) {
		this.mPlayerListener = mPlayerListener;
	}

	public synchronized void stop(boolean interrupt) {
		if (mDecoder != null) {
			mDecoder.stop(interrupt);
			mDecoder = null;
		}
	}

	public synchronized void release() {
		if(mDecoder !=null)
			mDecoder.release();
	}

	private AudioPlayer() {
	}

	public synchronized static AudioPlayer getInstance() {
		if (instance == null)
			instance = new AudioPlayer();
		return instance;
	}

	public synchronized void play(final String file) {
		stop(true);
		new AudioPlayThread(file).start();
	}

	public class AudioPlayThread extends Thread {

		private final String file;

		public AudioPlayThread(String file) {
			this.file = file;
		}

		@Override
		public void run() {
			mDecoder = new AudioDecoder(file);
			mDecoder.run();
			DebugLog.logd("AudioPlayer", "thread stop");
		}

	}
	
	public synchronized void switchStreamType(int streamType){
//		stop(true);
//		release();
		this.streamType = streamType;
	}
	
	public int getStreamType(){
		return this.streamType;
	}

	public class AudioDecoder extends Decoder {

		private InputStream inputStream;

		private boolean isEof;
		private AtomicBoolean isRunning;
		private AudioTrack mAudioTrack;

		public AudioDecoder(String path) {
			isRunning = new AtomicBoolean(true);
			try {
				inputStream = new BufferedInputStream(new FileInputStream(path));
				isEof = false;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				isEof = true;
			}
		}

		@Override
		protected boolean eof() {
			return isEof || !isRunning.get();
		}

		@Override
		protected void frame(short[] frame) {
			if (!eof()) {
//				System.out.println("frame:" + frame.length);
				if (mAudioTrack != null)
					mAudioTrack.write(frame, 0, frame.length);
			}
		}

		@Override
		protected byte[] read() {
			if (inputStream == null)
				return new byte[0];
			byte[] b = new byte[512];
			try {
				int size = inputStream.read(b);
				isEof = size == -1;
				if (isEof) {
					b = new byte[0];
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				isEof = true;
				b = new byte[0];
			}
			return b;
		}

		private boolean interrupt;

		public synchronized void stop(boolean interrupt) {
			isRunning.set(false);
			this.interrupt = interrupt;
			if (interrupt){
				forceClose(interrupt);
				handler.removeMessages(0);
			}
		}

		@Override
		protected void onStartDecode() {
			DebugLog.logd("onStartDecode");

			if (mPlayerListener != null)
				mPlayerListener.onPlayStart();

			int bufferSize = Math.max(SIMPLE_RATE, AudioTrack.getMinBufferSize(
					SIMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO,
					AudioFormat.ENCODING_PCM_16BIT));
			if (mAudioTrack == null)

				mAudioTrack = new AudioTrack(streamType,
						SIMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO,
						AudioFormat.ENCODING_PCM_16BIT, bufferSize,
						AudioTrack.MODE_STREAM);
			mAudioTrack.play();
			DebugLog.logd("AudioPlayer", "onStartDecode");
		}

		@Override
		protected void onEndDecode() {
			DebugLog.logd("AudioPlayer", "onEndDecode");
			isRunning.set(false);
			if (!interrupt)
				// forceClose();
				handler.sendEmptyMessageDelayed(0, 500);// 延时关闭以播完所有frame
		}

		public void release(){
			if(mAudioTrack!=null){
				if(mAudioTrack.getPlayState()==AudioTrack.PLAYSTATE_PLAYING){
					mAudioTrack.stop();
				}
				mAudioTrack.release();
				mAudioTrack = null;
			}
		}
		
		public void forceClose(boolean interrupt) {
			if (mAudioTrack != null && mAudioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
				mAudioTrack.flush();
				mAudioTrack.stop();
			}
			mDecoder = null;
			DebugLog.logd("AudioPlayer", "mAudioTrack stop");
			if (mPlayerListener != null)
				mPlayerListener.onPlayStop(interrupt);
		}

	}
	
	
}
