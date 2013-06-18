/**
 * 
 */
package com.example.galaxy08.message;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.ProviderManager;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.example.galaxy08.SysApplication;
import com.example.galaxy08.app.PreferenceWrapper;
import com.example.galaxy08.finals.SysConstants;
import com.example.galaxy08.message.iq.RedirectIQ;
import com.example.galaxy08.message.iq.RedirectIQProvider;
import com.example.galaxy08.tool.DebugLog;
import com.example.galaxy08.tool.DebugReceiver;
import com.example.galaxy08.tool.SystemInfo;
import com.example.galaxy08.tool.ToastUtil;
import com.example.galaxy08.tool.Tool;

/**
 * 
 */
public class MessageManager {

	public static final String TAG = "XMPP";

	public static final String CHAT_SERVER_DOMAIN = SysConstants.SERVER_SIXIN;

	public static final String ACCOUNT_SUFFIX = "@gc.changxiang.com";// + CHAT_SERVER_DOMAIN;

	private static MessageManager instance = null;

	public static final int MESSAGE_XMPP_LOGIN_SUCCESS = 1024;
	public static final int MESSAGE_XMPP_LOGIN_ERROR = 1023;

	// public static String XMPP_SERVER = "211.151.99.37";
	public static String XMPP_SERVER;

	private Connection mConnection;
	private String resource;
	private String version;
	private String info;

	public static final String PREFFERENCE_KEY_XMPP_SERVER = "PREFFERENCE_KEY_XMPP_SERVER";

	public static final String PREFFERENCE_KEY_VERSION_CODE = "PREFFERENCE_KEY_VERSION_CODE";

	private MessageManager() {
		XMPP_SERVER = PreferenceWrapper.get(PREFFERENCE_KEY_XMPP_SERVER, "");
		
		
		SystemInfo  sysInfo = SysApplication.getSystemInfo(); 
		String uid = sysInfo.getUniqid();
		String appId = sysInfo.getAppid();
		String manufacturer = sysInfo.getManufacturer();
		String model = sysInfo.getModel();
		String os = sysInfo.getOs();
		String version = sysInfo.getVersion();
		String publishid = String.valueOf(sysInfo.getFrom());
		
        this.resource = uid+"_"+appId;
        this.version = PreferenceWrapper.get(PREFFERENCE_KEY_VERSION_CODE, "1");
        this.info = Tool.combineStrings(manufacturer, "_", model, "_", os, "_", version,"_",publishid);
		ProviderManager.getInstance().addIQProvider(RedirectIQ.ELEMENT,
				RedirectIQ.NAMESPACE, new RedirectIQProvider());
	}

	public synchronized static MessageManager getInstance() {
		if (instance == null) {
			instance = new MessageManager();
		}
		return instance;

	}

	public boolean isLogin(String userid) {
		String account = MessageManager.getChatAccount(userid);
		return mConnection != null && mConnection.isConnected() && mConnection.isAuthenticated() &&mConnection.getUser()!=null&& mConnection.getUser().startsWith(account);
	}

	private Thread mMessageManagerThread;

	public void login(Context context) {
		if (!Tool.isNetworkServiceAvailable(SysApplication.application.getApplicationContext())) {
			DebugLog.logd("XMPP", "NetworkService unavailable");
			//MainActivity.UpdateOfflineMsgCount();
			return;
		}
		final String userid = PreferenceWrapper.get("userID", "0");
		if (!MessageManager.getInstance().isLogin(userid) && (mMessageManagerThread == null || !mMessageManagerThread.isAlive()) && Tool.hasInternet(context) ) {
			final String token = PreferenceWrapper.get("token", "");
			mMessageManagerThread = new Thread(new StartMessageManagerRunnable(
					userid, token));
			mMessageManagerThread.start();
			obtainOfflineMessage();
		}
	}

	private synchronized void login(final String username, final String password)
			throws XMPPException {
		if (isLogin(username))
			return;
		if (!TextUtils.isEmpty(XMPP_SERVER))
			loginIp(XMPP_SERVER, username, password);
		else
			loginDomain(username, password);
	}

	private synchronized void loginIp(String ip, String username,
			String password) {
		try {
			login(ip, username, password);
		} catch (XMPPException e) {
			DebugLog.logd("XMPP", "loginIp error", e);
			loginDomain(username, password);
		}
	}

	private synchronized void loginDomain(String username, String password) {
		List<String> servers = getXmppServer();
		for (String server : servers) {
			try {
				login(server, username, password);
				break;
			} catch (XMPPException e) {
				DebugLog.logd("XMPP", "login error", e);
				disconnect();
				continue;
			}
		}
	}

	private synchronized void login(String host, final String username,
			final String password) throws XMPPException {
		DebugLog.logd("XMPP", "host:" + host + "username:" + username
				+ ",password:" + password);
		if (TextUtils.isEmpty(host))
			return;
		if (mConnection == null) {
			ConnectionConfiguration cc = new ConnectionConfiguration(host);
			cc.setVersion("1.0.0");//version);
			cc.setInfo(info);
			cc.setReconnectionAllowed(false);
			cc.setSASLAuthenticationEnabled(true);
			cc.setSendPresence(false);
			cc.setRosterLoadedAtLogin(false);
			cc.setDebuggerEnabled(DebugReceiver.sm_debug);
			cc.setTruststorePath("/system/etc/security/cacerts.bks");
			cc.setTruststorePassword("changeit");
			cc.setTruststoreType("bks");
//			SmackConfiguration.setKeepAliveInterval(3*60*1000);//set tcp/ip keep alive interval 
			mConnection = new XMPPConnection(cc);
			mConnection.addPacketListener(iqListener, new PacketTypeFilter(IQ.class));
			//这里注册了信息接收
			mConnection.addPacketListener(messageListener, new PacketTypeFilter(Message.class));
		}
		if (!mConnection.isConnected()) {
			mConnection.connect();
			DebugLog.logd("XMPP", "connect success");
			mConnection.addConnectionListener(connectionListener);
		}
		if (!mConnection.isAuthenticated()) {
			try {
				mConnection.login(username, password, resource);
				DebugLog.logd("XMPP", "login success");
				PreferenceWrapper.put(PREFFERENCE_KEY_XMPP_SERVER, XMPP_SERVER);
				PreferenceWrapper.commit();
				if(handler!=null)
					handler.sendEmptyMessage(MESSAGE_XMPP_LOGIN_SUCCESS);
			} catch (XMPPException e) {
				String condition = e.getMessage();
				int ipIndex = condition.indexOf("redirect_");
				if (ipIndex > -1) {
					DebugLog.logd("XMPP", "login exception redirect");
					String ip = condition.substring(ipIndex + "redirect_".length()).replace("_", ".");
					if (mConnection.isConnected())
						mConnection.disconnect();
					mConnection = null;
					XMPP_SERVER = ip;
					DebugLog.logd("XMPP", "redirect to:" + ip);
					try {
						login(ip, username, password);
					} catch (Exception e1) {
						if(handler!=null)
							handler.sendEmptyMessage(MESSAGE_XMPP_LOGIN_ERROR);
						throw new RuntimeException("redirect server error", e1);
					}
					return;
				} else
					throw e;
			}
			DebugLog.logd("XMPP", "MessageManager start success");
		}

	}

	public Connection getConnection() {
		return this.mConnection;
	}

	public static String getChatAccount(String userid) {
		if (userid == null)
			return "";
		int pos = userid.indexOf(ACCOUNT_SUFFIX);
		if (pos > -1)
			return userid;
		else
			return Tool.combineStrings(userid, ACCOUNT_SUFFIX);
	}

	public static String getUserid(String account) {
		if (account == null)
			return "";
		int pos = account.indexOf(ACCOUNT_SUFFIX);
		if (pos > -1)
			return account.substring(0, pos);
		else
			return account;
	}

	public void disconnect() {
		if (mConnection != null) {
			if (mConnection.isConnected())
				mConnection.disconnect();
			mConnection = null;
		}
	}

	public com.example.galaxy08.message.chat.Chat chatWith(String targetid) {
		
		Log.i("testgalaxy", "MYID:"+PreferenceWrapper.get(PreferenceWrapper.USER_ID, "")+"TID:"+targetid);
		
		return new com.example.galaxy08.message.chat.Chat(PreferenceWrapper.get(
				PreferenceWrapper.USER_ID, ""), targetid);
	}

	public static Set<String> invalidServers = new HashSet<String>();

	private List<String> getXmppServer() {
		List<String> servers;
		try {
			InetAddress[] ips = InetAddress.getAllByName(CHAT_SERVER_DOMAIN);
			if (ips == null || ips.length == 0)
				return Collections.emptyList();
			DebugLog.logd("XMPP", "getXmppServer:" + Arrays.toString(ips));
			servers = new ArrayList<String>(ips.length);
			for (InetAddress ia : ips) {
				servers.add(ia.getHostAddress());
			}
		} catch (Exception e) {
			DebugLog.logd("XMPP", "getXmppServer", e);
			servers = Collections.emptyList();
		}
		return servers;
	}

	public static class StartMessageManagerRunnable implements Runnable {

		public String userid;
		public String token;

		public StartMessageManagerRunnable(String userid, String token) {
			this.userid = userid;
			this.token = token;
		}

		@Override
		public void run() {
			try {
				MessageManager.getInstance().disconnect();
				MessageManager.getInstance().login(MessageManager.getChatAccount(this.userid), this.token);
			} catch (Exception e) {
				DebugLog.logd("XMPP", "MessageManager start fail", e);
			}
		}

	}

	// public void reconnect(){
	// DebugLog.logd("XMPP", "reconnect start");
	// disconnect();
	// login(JwApplication.getAppContext());
	// }

	private ConnectionListener connectionListener = new ConnectionListener() {

		@Override
		public void connectionClosed() {
			DebugLog.logd("XMPP", "connectionClosed");
			login(SysApplication.application.getApplicationContext());
		}

		@Override
		public void connectionClosedOnError(Exception error) {
			DebugLog.logd("XMPP", "connectionClosedOnError", error);
			login(SysApplication.application.getApplicationContext());
		}

		@Override
		public void reconnectingIn(int arg0) {
			DebugLog.logd("XMPP", "reconnectingIn:" + arg0);
		}

		@Override
		public void reconnectionFailed(Exception arg0) {
			DebugLog.logd("XMPP", "reconnectionFailed", arg0);
		}

		@Override
		public void reconnectionSuccessful() {
			DebugLog.logd("XMPP", "reconnectionSuccessful");
		}

	};
	
	private Handler handler;
	
	public void setHandler(Handler handler) {
		this.handler = handler;
	}

//	private Handler handler = new Handler(Looper.getMainLooper()) {
//
//		@Override
//		public void handleMessage(android.os.Message msg) {
//			switch (msg.what) {
//			case MESSAGE_XMPP_LOGIN_SUCCESS:
//				//登陆成功之后 发送广播
//				
////				Message fake = new Message();
////				fake.setFrom(getChatAccount("0"));
////				fake.setTo(mConnection.getUser());
////				fake.setType(Message.Type.headline);
////				fake.setAction();
////				fake.setBodyType("http");
////				MessageHandler.getInstance().handleMessage(account, message);
//				break;
//			}
//		}
//
//	};
	
	public void obtainOfflineMessage(){
		if(offlineMessageThread!=null){
			offlineMessageThread.start();
		}
	}

	private Thread offlineMessageThread = new Thread() {

		private byte[] lock = new byte[0];

		private AtomicBoolean finish = new AtomicBoolean(false);

		@Override
		public void run() {
			while (!Thread.interrupted() && !finish.get()) {
				DebugLog.logd("MessageManager", "offlineMessageThread start");
				String userid = PreferenceWrapper.get("userID", "0");
				int sequence = PreferenceWrapper.get(Tool.combineStrings(
						userid, "_", PreferenceWrapper.CHAT_MESSAGE_SEQUENCE),
						0);
				int count = MessageHandler.getInstance().obtainOfflineMessage(userid, sequence, 200);
				if(count > 0){
					DebugLog.logd("MessageManager","offlineMessageThread list size:" + count);
				}else{
					//MainActivity.UpdateOfflineMsgCount();
				}
				DebugLog.logd("MessageManager", "offlineMessageThread finish");
				DebugLog.logd("MessageManager", "offlineMessageThread wait");
				synchronized (lock) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						DebugLog.logd("MessageManager", "offlineMessageThread",e);
					}
				}

			}
		}

		@Override
		public synchronized void start() {
			State state = getState();
			DebugLog.logd("MessageManager", "offlineMessageThread status:"+ getState().name());
			if (state == State.NEW) {
				this.setPriority(Thread.MIN_PRIORITY);
				this.setDaemon(true);
				super.start();
			} else if (state == State.WAITING || state == State.TIMED_WAITING) {
				synchronized (lock) {
					lock.notify();
				}
			}
		}

		// public synchronized void finish() {
		// finish.set(true);
		// start();
		// }

	};
	
	
	private PacketListener iqListener = new PacketListener(){

		@Override
		public void processPacket(Packet packet) {
			IQ iq = (IQ) packet;
			DebugLog.logd("XMPP",
					"receive iq:" + iq.getChildElementXML());
			if (iq instanceof RedirectIQ) {
				RedirectIQ redirect = (RedirectIQ) iq;
				XMPP_SERVER = redirect.getServer();
				DebugLog.logd("XMPP", "redirect to server:"
						+ XMPP_SERVER);
				ToastUtil.showMessage(SysApplication.application.getApplicationContext(),
						"redirect iq received:" + XMPP_SERVER);
				disconnect();
				login(SysApplication.application.getApplicationContext());
				return;
			}
			if (iq.getType() == Type.RESULT
					&& !TextUtils.isEmpty(iq.getPacketID())
					&& iq.getPacketID().startsWith(
							MessageMaintenanceThread.PING_ID_PREFIX))
				MessageMaintenanceThread.getInstance().receiverPong(
						packet.getPacketID());
		}

	};
	
	private PacketListener messageListener = new PacketListener(){

		@Override
		public void processPacket(Packet packet) {
			if(packet instanceof Message){
				Message message = (Message)packet;
				
				DebugLog.logd("XMPP", "hello receive message:"+message.toXML());
				MessageHandler.getInstance().handleMessage(mConnection.getUser(), message);
			}
		}
		
	};

}
