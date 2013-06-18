package com.example.galaxy08.message.audio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;

import net.afpro.utils.Encoder;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;

import com.example.galaxy08.tool.DebugLog;
import com.sound.dubbler.encode.EncodeingInterruptException;
import com.sound.dubbler.encode.Mp3EncoderHelper;
/**
 * AudioRecorder 音频记录类 2013/04/15
 */
public class AudioRecorder extends Thread {

	private AudioRecord mAudioRecord;
	private Encoder mEncoder;

	private Mp3EncoderHelper mMp3EncoderHelper;

	public final static int sampleRate = 8000;

	public final static long MAX_RECORD_TIME = 60000;

	private String path;
	private RecordListener mRecordListener;

	private AtomicBoolean isRunning;

	private int bufferSize;

	public static final int MESSAGE_START = 1010;
	public static final int MESSAGE_PROCESS = 1011;
	public static final int MESSAGE_FINISH = 1012;

	public AudioRecorder(String path, RecordListener mRecordListener) {
		this.path = path;
		this.mRecordListener = mRecordListener;
		isRunning = new AtomicBoolean();
		isRunning.set(true);
		init();
	}

	public String getPath() {
		return path;
	}

	private void init() {
		try {
			mEncoder = new Encoder(sampleRate, false, true, true, 4) {

				private OutputStream outputStream = new FileOutputStream(path);

				@Override
				protected void page(byte[] header, byte[] body) {
					try {
						outputStream.write(header);
						outputStream.write(body);
						outputStream.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				@Override
				protected void onEncoderEnd() {
					if (outputStream != null) {
						try {
							outputStream.flush();
							outputStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						DebugLog.logd("XMPP", "onEncoderEnd");
						outputStream = null;
					}

				}

				@Override
				protected void onEncoderBegin() {
					DebugLog.logd("XMPP", "onEncoderBegin");
				}

			};
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		mEncoder.setOnePacketPerPage(true);
		
		int size = AudioRecord.getMinBufferSize(
				sampleRate, AudioFormat.CHANNEL_IN_MONO,
				AudioFormat.ENCODING_PCM_16BIT);
		bufferSize = Math.max(sampleRate, size);

		mMp3EncoderHelper = new Mp3EncoderHelper(size);

		mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.DEFAULT,
				sampleRate, AudioFormat.CHANNEL_IN_MONO,
				AudioFormat.ENCODING_PCM_16BIT, bufferSize);
	}

	@Override
	public void run() {
		if (mAudioRecord != null
				&& mAudioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
			mAudioRecord.startRecording();

			if (mRecordListener != null) {
				sendMessage(MESSAGE_START, null);
			}
			long startSecond = System.currentTimeMillis();
			//mEncoder.beg();
			//short[] frame = new short[mEncoder.getSpeexEncoder().getFrameSize()];

			//int size = mAudioRecord.getMinBufferSize(sampleRate,  AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
			byte[] buffer = new byte[bufferSize];

			File tempFile = new File("/mnt/sdcard/tempfile");

			if(!tempFile.exists()){
				try {
					tempFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			try {
				FileOutputStream out = new FileOutputStream(tempFile);

				while (isRunning.get()) {
					if (MAX_RECORD_TIME >= (System.currentTimeMillis()-startSecond)) {
						int size = mAudioRecord.read(buffer, 0, buffer.length);
						out.write(buffer);
						//mMp3EncoderHelper.encode(is, inSampleRate, numChannels, name, title, artist, album, remoteController)

						//mEncoder.enc(frame);
						//发送 音量
						//					if (mRecordListener != null) {
						//						int volumn = PCMUtil.getVolume(frame, size);
						//						sendMessage(MESSAGE_PROCESS, volumn);
						//					}

					}else{
						DebugLog.logd("AUDIO RECORDER", "reach max record time");
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				out.flush();
				out.close();
				mAudioRecord.stop();
				mAudioRecord.release();
				
				FileInputStream in = new FileInputStream(tempFile);
				mMp3EncoderHelper.encode(in, sampleRate, AudioFormat.CHANNEL_IN_MONO, path, "title", "artist", "album", null);
				in.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (EncodeingInterruptException e) {
				e.printStackTrace();
			}
			
			if (mRecordListener != null) {
				long audioTime = System.currentTimeMillis() - startSecond;
				sendMessage(MESSAGE_FINISH, Math.min(audioTime, MAX_RECORD_TIME));
			}
		}
	}

	public void finish() {
		isRunning.set(false);
	}

	private boolean isCancel = false;

	public void cancel() {
		isCancel = true;
		finish();
	}

	public interface RecordListener {
		public void onRecordStart();

		public void onRecordProcess(int volumn);

		public void onRecordFinish(boolean isCancel, int second);
	}

	private void sendMessage(int what, Object obj) {
		handler.sendMessage(handler.obtainMessage(what, obj));
	}

	public Handler handler = new Handler() {

		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MESSAGE_START:
				mRecordListener.onRecordStart();
				break;
			case MESSAGE_PROCESS:
				if (msg.obj instanceof Integer)
					mRecordListener.onRecordProcess((Integer) msg.obj);
				break;
			case MESSAGE_FINISH:
				if (msg.obj instanceof Long) {
					long audioTime = (Long) msg.obj;
					mRecordListener.onRecordFinish(isCancel,
							audioTime < 1000 ? 0 : (int) (audioTime / 1000));
				}
				break;
			}
		}

	};
}
