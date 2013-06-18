package com.example.galaxy08.chat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import com.example.galaxy08.entity.ChatMessage;
import com.example.galaxy08.entity.ChatMessage.MESSAGE_MEDIA_TYPE;

public class AsyncMultiMediaDownloader {
	private boolean isLoop;
	private Thread workThread;
	private ArrayList<Task> tasks;
	private Handler handler;
	
	private final int AUDIO_FILE_DOWN = 0x1001;
	private final int IMAGE_FILE_DOWN = 0x1002;

	public AsyncMultiMediaDownloader() {
		this.tasks = new ArrayList<Task>();
		this.isLoop = true;
		
		this.handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case AUDIO_FILE_DOWN:
				case IMAGE_FILE_DOWN:
					Task t = (Task)msg.obj;
					t.callback.fileLoaded(t.message);
					break;

				default:
					break;
				}
			};
		};
		
		this.workThread = new Thread(){
			public void run() {
				while(isLoop){
					//轮询任务集合，执行下载任务
					while(tasks.size()>0&&isLoop){
						//取出一个
						Task t = tasks.remove(0);
						ChatMessage message = t.message;
						String path = message.getContent();
						if(path!=null){

							/*
							 * 目前需要进行一下替换
							 */
							path = path.replaceFirst("img.internalshare.com", "42.96.186.145");

							try {
								HttpEntity entity = HttpUtils.getEntity(path, null, HttpUtils.METHOD_GET);
								
								String targetPath = "/mnt/sdcard/galaxy/"+path.substring(path.lastIndexOf("/"));

								message.setMediaCache(targetPath);

								if(message.getMediatype()==MESSAGE_MEDIA_TYPE.MEDIA_AUDIO){

									File targetFile = new File(targetPath);
									if(!targetFile.getParentFile().exists()){
										targetFile.getParentFile().mkdirs();
									}
									if(!targetFile.exists()){
										try {
											targetFile.createNewFile();
											InputStream in = entity.getContent();
											FileOutputStream out = new FileOutputStream(targetFile);
											byte[] buffer = new byte[1024];
											while(in.read(buffer)>0){
												out.write(buffer);
											}
											//handler.sendEmptyMessage(AUDIO_FILE_DOWN);
											Message msg = Message.obtain();
											msg.what = AUDIO_FILE_DOWN;
											msg.obj = t;
											handler.sendMessage(msg);
										} catch (FileNotFoundException e) {
											e.printStackTrace();
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
								}else{
									byte[] data = EntityUtils.toByteArray(entity);
									Bitmap bitmap = BitmapUtils
											.loadBitmap(data, 100, 100);
									BitmapUtils.save(bitmap, targetPath);
									
									Message msg = Message.obtain();
									msg.what = IMAGE_FILE_DOWN;
									msg.obj = t;
									handler.sendMessage(msg);
								}

							} catch (ConnectTimeoutException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();

							}
						}
					}
					if(!isLoop)
						break;

					synchronized (this) {
						try {
							this.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		};
		this.workThread.start();
	}

	public void loadFile(ChatMessage message,Callback call){
		Task task = new Task();
		task.message = message;
		task.callback = call;
		this.tasks.add(task);
		synchronized (workThread) {
			workThread.notify();
		}
	}
	
	class Task{
		ChatMessage message;
		Callback callback;
	}

	public interface Callback {
		void fileLoaded(ChatMessage message);
	}
	
	public void quit() {
		isLoop = false;
		synchronized (workThread) {
			workThread.notify();
		}
	}

//	public void loadFile(ChatMessage c, Callback callback) {
//		// TODO 自动生成的方法存根
//		
//	}
}
