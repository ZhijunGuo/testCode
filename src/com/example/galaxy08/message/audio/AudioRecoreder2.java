package com.example.galaxy08.message.audio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

public class AudioRecoreder2 extends Thread {
	
	private String filePtah;
	
	private AudioRecord recorder = null;
	
	public final static long MAX_RECORD_TIME = 60000;
	
	public AudioRecoreder2(String filePath){
		this.filePtah = filePath;
		initAudioRecorder();
	}
	public String getFilePtah() {
		return filePtah;
	}
	/*
	 * 音频记录器 配置
	 */
	// 音频获取源
	private int audioSource = MediaRecorder.AudioSource.MIC;
	// 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
	private static int sampleRateInHz = 8000;
	// 设置音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道
	private static int channelConfig = AudioFormat.CHANNEL_IN_MONO;
	// 音频数据格式:PCM 16位每个样本。保证设备支持。PCM 8位每个样本。不一定能得到设备支持。
	private static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
	
	private int bufferSizeInByte = 0;
	
	private FileOutputStream out;
	
	private byte[] buffer;
	
	private boolean isRecording = false;
	
	private void initAudioRecorder(){
		File file = new File(filePtah);
		if(file.exists()){
			file.delete();
		}
		
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			out = new FileOutputStream(file);
			bufferSizeInByte = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
			recorder = new AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat, bufferSizeInByte);
			buffer = new byte[bufferSizeInByte];
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	
	@Override
	public void run() {
		if (recorder != null
				&& recorder.getState() == AudioRecord.STATE_INITIALIZED) {
			recorder.startRecording();
			long startSecond = System.currentTimeMillis();
			isRecording = true;
			while(isRecording){
				if(System.currentTimeMillis()-startSecond>MAX_RECORD_TIME)
					break;
				recorder.read(buffer,0,bufferSizeInByte);
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
		}
		super.run();
	}
	
	public void finish(){
		isRecording = false;
	}
	
	
}
