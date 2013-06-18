package com.example.galaxy08.message.audio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;


public class AudioRecorderAndPlayer {
	//音频文件的存储位置
		private String filePath = null;//Environment.getExternalStorageDirectory().getAbsolutePath()+"/test_audio.pcm";
		
		//记录录音
		private AudioRecord recorder;
		
		//记录状态
		
		private boolean isPlaying = false;
		
		private boolean isRecording = false;
		
		// 音频获取源
		private int audioSource = MediaRecorder.AudioSource.MIC;
		// 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
		private static int sampleRateInHz = 8000;
		// 设置音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道
		private static int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
		// 音频数据格式:PCM 16位每个样本。保证设备支持。PCM 8位每个样本。不一定能得到设备支持。
		private static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
		
		private AudioTrack audioTrack;

		public String getFilePath() {
			return filePath;
		}

		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}
		
		public static AudioRecorderAndPlayer instance;
		
		private AudioRecorderAndPlayer(){
			
		}
		
		public static AudioRecorderAndPlayer getInstance(){
			if(instance==null){
				instance = new AudioRecorderAndPlayer();
			}
			return instance;
		}
		
		public void startRecord(){
			
			File file = new File(filePath);
			
			if(file.exists()){
				file.delete();
			}
			
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				final FileOutputStream out = new FileOutputStream(file);
				final int bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
				recorder = new AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes);
			
				final byte[] buffer = new byte[bufferSizeInBytes];
				recorder.startRecording();
				isRecording = true;
				
				new Thread(){
					public void run() {
						while(isRecording){
							recorder.read(buffer,0,bufferSizeInBytes);
							try {
								out.write(buffer);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						recorder.stop();
						recorder.release();
						recorder = null;
						try {
							out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					};
				}.start();
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
		}
		public void stopRecord(){
			isRecording = false;
		}

		
		public void startPlay(){
			File file = new File(filePath);
			FileInputStream in = null;
			
			try {
				in = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			int bufferSizeinByte = recorder.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
			byte[] buffer = new byte[bufferSizeinByte];
			int byteRead = 0;
			audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRateInHz, channelConfig, audioFormat, bufferSizeinByte, AudioTrack.MODE_STREAM);
			
			audioTrack.play();
			
			try {
				while((byteRead = in.read(buffer))!=-1){
					//System.out.write(buffer, 0, byteRead);
					//System.out.flush();
					audioTrack.write(buffer, 0, bufferSizeinByte);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void stopPlay(){
			isPlaying = false;
		}
}
