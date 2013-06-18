package com.example.galaxy08.register_login;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONObject;

import com.example.galaxy08.app.PreferenceWrapper;
import com.example.galaxy08.http.GalaxyApi;
import com.example.galaxy08.http.exception.GalaxyHttpException;
import com.example.galaxy08.http.model.GalaxyResponse;

import android.os.Handler;
import android.os.Message;

public class WorkThread extends Thread {

//	public static final int REQUESTCODE_WAIT= 0x100;
//	public static final int REQUESTCODE_LOGIN = 0x101;
//	public static final int REQUESTCODE_GET_SECURITY_CODE = 0x102;
//	public static final int REQUESTCODE_ACTIVATE_ACCOUNT = 0x103;
//	public static final int REQUESTCODE_SET_PASSWD = 0x104;
//	public static final int REQUESTCODE_GET_INVITATIONS = 0x105;
//	public static final int REQUESTCODE_JOIN_COMPANY = 0x106;
//
//	public static final int MESSAGE_WHAT_START = 0x201;
//	public static final int MESSAGE_WHAT_END = 0x202;
//	public static final int MESSAGE_WHAT_ERROR = 0x203;
//
//	private ArrayList<Task> tasks;
//
//	private boolean isLoop = true;
//
//	private static WorkThread thread=null;
//
//	public static WorkThread getInstance(){
//		if(thread==null){
//			thread = new WorkThread();
//		}
//		return thread;
//	}
//
//	private WorkThread(){
//		tasks = new ArrayList<WorkThread.Task>();
//	}
//
//	@Override
//	public void run() {
//		super.run();
//		while (isLoop) {
//			while(tasks.size()>0){
//				//取出一个任务执行
//				Task task = tasks.remove(0);
//
//				task.handler.sendEmptyMessage(MESSAGE_WHAT_START);
//				GalaxyResponse response = null;
//				JSONObject object = null;
//				try {
//					switch (task.requestCode) {
//					case REQUESTCODE_LOGIN:
//						response = GalaxyApi.userLogin(task.p1,
//								task.p2);
//						break;
//					case REQUESTCODE_GET_SECURITY_CODE:
//						response = GalaxyApi.getSecurityCode(task.p1,
//								task.p2);
//						break;
//					case REQUESTCODE_ACTIVATE_ACCOUNT:
//						response = GalaxyApi.activateAccount(task.p1,
//								task.p2);
//						break;
//					case REQUESTCODE_SET_PASSWD:
//						response = GalaxyApi.setPasswd(task.p1,
//								task.p2);
//						break;
//					case REQUESTCODE_GET_INVITATIONS:
//						object = GalaxyApi.getInvitations(task.p1,
//								task.p2);
//						break;
//					case REQUESTCODE_JOIN_COMPANY:
//						response = GalaxyApi.joinCompany(task.p1,
//								task.p2);
//						break;
//
//					default:
//						break;
//					}
//					if(task.requestCode!=REQUESTCODE_WAIT){
//						Message message = Message.obtain();
//						message.arg1 = task.requestCode;
//						if(task.requestCode==REQUESTCODE_GET_INVITATIONS){
//							message.obj = object;
//						}else{
//							message.obj = response;
//						}
//						message.what = MESSAGE_WHAT_END;
//						task.handler.sendMessage(message);
//					}
//				} catch (IOException e) {
//					task.handler.sendEmptyMessage(MESSAGE_WHAT_ERROR);
//					e.printStackTrace();
//				} catch (GalaxyHttpException e) {
//					task.handler.sendEmptyMessage(MESSAGE_WHAT_ERROR);
//					e.printStackTrace();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//
//			synchronized (thread) {
//				try {
//					thread.wait();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//			if (!isLoop)
//				break;
//		}
//	}
//
//	public boolean isLoop() {
//		return isLoop;
//	}
//
//	public void setLoop(boolean isLoop) {
//		this.isLoop = isLoop;
//	}
//
//	
//	public void quit(){
//		isLoop = false;
//		synchronized (thread) {
//			thread.notify();
//		}
//	}
//
//	public void addTask(String p1,String p2,int requestCode,Handler handler){
//		Task task = new Task();
//		task.p1 = p1;
//		task.p2 = p2;
//		task.requestCode = requestCode;
//		task.handler = handler;
//		tasks.add(task);
//		
//		if(WorkThread.getInstance().isAlive()){
//			synchronized (WorkThread.getInstance()) {
//				WorkThread.getInstance().notify();
//			}
//		}else{
//			WorkThread.getInstance().start();
//		}
//	}
//
//	class Task{
//		String p1;
//		String p2;
//		int requestCode;
//		Handler handler;
//	}

}
