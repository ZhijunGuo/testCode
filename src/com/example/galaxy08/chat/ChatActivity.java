package com.example.galaxy08.chat;

import java.io.File;
import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.galaxy08.PersonInfoActivity;
import com.example.galaxy08.R;
import com.example.galaxy08.SelectContactActivity;
import com.example.galaxy08.SysApplication;
import com.example.galaxy08.app.PreferenceWrapper;
import com.example.galaxy08.dao.ChatSessionColumns;
import com.example.galaxy08.dao.ChatmessageColumns;
import com.example.galaxy08.dao.GalaxyProviderInfo;
import com.example.galaxy08.entity.ChatMessage;
import com.example.galaxy08.entity.ChatMessage.MESSAGE_MEDIA_TYPE;
import com.example.galaxy08.entity.ChatMessage.MESSAGE_STATUS;
import com.example.galaxy08.entity.ChatMessage.MESSAGE_TYPE;
import com.example.galaxy08.entity.ChatMessages;
import com.example.galaxy08.entity.ChatSession;
import com.example.galaxy08.entity.User;
import com.example.galaxy08.entity.Users;
import com.example.galaxy08.message.MessageHandler;
import com.example.galaxy08.message.MessageManager;
import com.example.galaxy08.message.audio.AudioPlayer;
import com.example.galaxy08.message.audio.AudioPlayer2;
import com.example.galaxy08.message.audio.AudioRecorder;
import com.example.galaxy08.message.audio.AudioRecorder.RecordListener;
import com.example.galaxy08.message.chat.OnMessageSendListener;
import com.example.galaxy08.tool.Common;
import com.example.galaxy08.tool.DebugLog;
import com.example.galaxy08.tool.ToastUtil;

/*
 * 聊天类 
 */
public class ChatActivity extends Activity implements OnClickListener,
OnItemClickListener, OnLongClickListener, OnItemLongClickListener,
OnScrollListener {

	private final int CHAT_HISTORY = 0x1001;
	private final int IMAGE_CAMERA = 0x1100;
	private final int IMAGE_STORE = 0x1101;
	// 操作码 添加联系人到当前聊天
	private final int REQUEST_CODE_ADD_CONTACT = 0x1002;

	public final static String RESULT_COPY = "RESULT_COPY";
	public final static String TARGET_USER = "target_user";
	public final static String CHAT_NAME = "chat_name";
	public final static String USER_COUNT = "user_count";// 参与聊天的人数(除掉自己)
	protected static final int UPDATE_LISTVIEW = 0x5;

	private BroadcastReceiver myReceiver;

	private EditText contentText;

	private Button sendButton;
	private Button audioButton;
	private Button otherButton;
	private Button pictureButton;

	private ListView chatList;

	private ChatListAdapter adapter;

	private ArrayList<ChatMessage> cs;

	private TextView top;

	private com.example.galaxy08.message.chat.Chat targetChat;

	private LinearLayout chatOtherLayout;

	private ContentResolver cr;

	private String myId;
	// private String targetId;
	// 与当前用户聊天的联系人
	private ArrayList<User> targetUsers;
	// 如果是 私聊

	private ChatSession chatSession;

	private User targetUser;

	private boolean isNone = false;

	private Boolean voiceLong = false;

	private long mStartTime;
	private long endTime;

	private PopupWindow pasteWindow;

	private PopupWindow moreWinddow;

	private AsyncMultiMediaDownloader loader;

	private AtomicBoolean isRecordFinish = new AtomicBoolean(true);

	// private Timer timer;

	public boolean isCamera = false;

	public static ChatMessage copyMessage;

	private AudioRecorder audioRecorder;
	int i = 0;

	private boolean isRunning = false;

	private int scrollStateTemp;

	private int count = 0;

	private Thread queryThread = new Thread() {
		public void run() {
			ArrayList<ChatMessage> messages_temp = ChatMessages.getMessages(
					myId, targetUser.getUser_id(), count);
			Log.i("thread", "queryThread--end--size:" + messages_temp.size());

			count += messages_temp.size();
			// 主线程更新界面
			Message message = Message.obtain();
			message.what = UPDATE_LISTVIEW;
			// message.obj = messages_temp;
			Bundle bundle = new Bundle();
			bundle.putParcelableArrayList("kao", messages_temp);
			message.setData(bundle);
			handler.sendMessage(message);

			isRunning = false;
			Log.i("thread", "isRunning = false;");
		};
	};

	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:// 开始发送消息

				break;
			case 1:// 发送结束(成功或者失败)
				adapter.notifyDataSetChanged();
				chatList.setSelection(adapter.getCount());
				break;
			case 3:
				Toast.makeText(ChatActivity.this, "录音...", Toast.LENGTH_SHORT)
				.show();
				break;
			case 4:
				Toast.makeText(ChatActivity.this, "录音开始", Toast.LENGTH_SHORT)
				.show();
				break;

			case UPDATE_LISTVIEW:
				ArrayList<ChatMessage> is = msg.getData()
				.getParcelableArrayList("kao");
				adapter.addArrayListAtFrist(is);
				break;
			default:
				break;
			}
		};
	};
	private DisplayMetrics displayMetrics;

	private void setupView() {

		chatList = (ListView) findViewById(R.id.chat_list);
		chatList.setOnItemClickListener(this);
		chatList.setOnItemLongClickListener(this);
		chatList.setDivider(null);
		contentText = (EditText) findViewById(R.id.chat_edit_text);
		contentText.setOnLongClickListener(this);
		sendButton = (Button) findViewById(R.id.chat_send_button);
		sendButton.setOnClickListener(this);
		audioButton = (Button) findViewById(R.id.chat_audio_button);
		// audioButton.setOnClickListener(this);
		audioButton.setOnTouchListener(new VoiceClickListener());
		// audioButton.setOnLongClickListener(this);
		otherButton = (Button) findViewById(R.id.chat_other_button);
		otherButton.setOnClickListener(this);
		// pictureButton = (Button) findViewById(R.id.chat_picture_button);
		// pictureButton.setOnClickListener(this);
		top = (TextView) findViewById(R.id.chat_top);
		chatOtherLayout = (LinearLayout) findViewById(R.id.chat_footer_send_more);
		findViewById(R.id.chat_top_back_button).setOnClickListener(this);
		findViewById(R.id.chat_top_group_button).setOnClickListener(this);
		// receiveImage = (ImageView)findViewById(R.id.chat_item_image_target);
		// sendImage = (ImageView)findViewById(R.id.chat_item_image_my);
		LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

		View moreView = (View) mLayoutInflater.inflate(R.layout.chat_send_more,
				null, true);
		moreView.findViewById(R.id.chat_send_more_face)
		.setOnClickListener(this);
		moreView.findViewById(R.id.chat_send_more_image).setOnClickListener(
				this);
		moreView.findViewById(R.id.chat_send_more_video).setOnClickListener(
				this);

		moreWinddow = new PopupWindow(moreView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, true);

		View menuView = (View) mLayoutInflater.inflate(R.layout.paste, null,
				true);
		menuView.findViewById(R.id.paste_button).setOnClickListener(this);
		pasteWindow = new PopupWindow(menuView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, true);
		pasteWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					pasteWindow.dismiss();
					return true;
				}
				return false;
			}
		});
		pasteWindow.setOutsideTouchable(true);
		moreWinddow.setOutsideTouchable(true);
		moreWinddow.setFocusable(false);
		moreWinddow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					moreWinddow.dismiss();
					return true;
				}
				return false;
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chat);

		loader = new AsyncMultiMediaDownloader();

		setupView();
		myId = PreferenceWrapper.get(PreferenceWrapper.USER_ID, "0");
		// 与当前用户聊天的联系人
		targetUsers = getIntent().getParcelableArrayListExtra(TARGET_USER);
		String chatName = getIntent().getStringExtra(CHAT_NAME);
		top.setText(chatName);

		if (targetUsers.size() == 1) {
			targetUser = targetUsers.get(0);
			targetChat = MessageManager.getInstance().chatWith(
					targetUser.getUser_id());
		} else {
			// 创建群组id 通知服务器
		}

		/**
		 * 将会话记录 写入数据库
		 * 如果数据库中存在 本用户与目标用户的会话记录 不插入新纪录
		 */
		if(!isExist()){
			chatSession = new ChatSession();
			chatSession.setCreateTime(String.valueOf(System.currentTimeMillis()));
			chatSession.setTargetId(targetUser.getUser_id());
			chatSession.setlastupdate(String.valueOf(System.currentTimeMillis()));
			chatSession.setUserid(myId);
			ContentResolver cr = this.getContentResolver();
			cr.insert(GalaxyProviderInfo.CHAT_URI, chatSession.getContentValues());
		}

		adapter = new ChatListAdapter(this, null);
		// 获取聊天的历史消息
		// SysApplication.application.threadPool.submit(queryThread);
		chatList.setAdapter(adapter);

		chatList.setOnScrollListener(this);

		sendButton.setOnClickListener(this);

		// 需要注册一个广播接收器
		myReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				ArrayList<ChatMessage> list = intent
						.getParcelableArrayListExtra(MessageHandler.INTENT_KEY_MESSAGE);
				adapter.addArrayListMessage(list);
				// 如果接收到了语音或者图片消息 需要进行下载
				// for(ChatMessage msg:list){
				// if(msg.getMediatype()==MESSAGE_MEDIA_TYPE.MEDIA_AUDIO||
				// msg.getMediatype()==MESSAGE_MEDIA_TYPE.MEDIA_IMAGE){
				// loader.loadFile(msg);
				// }
				// }
			}
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction(MessageHandler.BROADCAST_ACTION_NEW_MESSAGE);
		filter.setPriority(Integer.MAX_VALUE);
		registerReceiver(myReceiver, filter);
		displayMetrics = this.getResources().getDisplayMetrics();
	}

	/**
	 * 查询数据库中是否存在该会话记录
	 */
	private boolean isExist(){
		ContentResolver cr = this.getContentResolver();
		Cursor cursor = cr.query(GalaxyProviderInfo.CHAT_URI, 
				new String[]{
				ChatSessionColumns.TARGET_ID,
				ChatSessionColumns.CREATE_TIME,
				ChatSessionColumns.LAST_UPDATE,
				ChatSessionColumns.TYPE
		}, 
		ChatSessionColumns.TARGET_ID+"=?", 
		new String[]{targetUser.getUser_id()}, null);
		if(cursor!=null&&cursor.moveToNext()){
			chatSession = new ChatSession();
			chatSession.setCreateTime(cursor.getString(cursor.getColumnIndex(ChatSessionColumns.CREATE_TIME)));
			chatSession.setlastupdate(cursor.getString(cursor.getColumnIndex(ChatSessionColumns.LAST_UPDATE)));
			chatSession.setTargetId(cursor.getString(cursor.getColumnIndex(ChatSessionColumns.TARGET_ID)));
			chatSession.setType(cursor.getInt(cursor.getColumnIndex(ChatSessionColumns.TYPE)));
			cursor.close();
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chat_send_more_face:
			moreWinddow.dismiss();
			break;
		case R.id.chat_send_more_image:
			new Builder(ChatActivity.this)
			.setTitle("请选择图片来源")
			.setItems(new String[] { "相机", "相册" },
					new OnCameraClickListener()).show();
			moreWinddow.dismiss();
			break;
		case R.id.chat_send_more_video:
			moreWinddow.dismiss();
			break;
		case R.id.paste_button:
			pasteWindow.dismiss();
			if (copyMessage != null) {
				contentText.setText(copyMessage.getContent());
			}
			break;
		case R.id.chat_send_button:
			String content = contentText.getText().toString().trim();
			if (content == null || "".equals(content)) {
				Toast.makeText(this, "不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			contentText.setText("");
			ChatMessage message = targetChat.sendMessage(content,
					defaultOnMessageSendListener);
			adapter.addMessage(message);
			// 插入数据库
			this.getContentResolver().insert(
					GalaxyProviderInfo.CHATMESSAGE_URI,
					message.getContentValues());
			contentText.setText("");
			if(!content.equals(getString(R.string.i_konow))){
				// 制造 假回复信息
				ChatMessage messageTemp = new ChatMessage();
				messageTemp.setContent(getString(R.string.no_time));
				messageTemp.setType(MESSAGE_TYPE.MESSAGE_RECEIVE);
				messageTemp.setStatus(MESSAGE_STATUS.STATUS_SEND_SUCCESS);// (MESSAGE_TYPE.MESSAGE_RECEIVE);
				messageTemp.setMediatype(MESSAGE_MEDIA_TYPE.MEDIA_TEXT);// (MESSAGE_TYPE.MESSAGE_RECEIVE);
				messageTemp.setTargetid(targetUser.getUser_id());// (MESSAGE_MEDIA_TYPE.MEDIA_TEXT);//(MESSAGE_TYPE.MESSAGE_RECEIVE);
				messageTemp.setName(targetUser.getUser_name());
				messageTemp.setUserid(PreferenceWrapper.get(
						PreferenceWrapper.USER_ID, ""));
				messageTemp.setTimestamp(System.currentTimeMillis());

				adapter.addMessage(messageTemp);

				// 插入数据库
				this.getContentResolver().insert(
						GalaxyProviderInfo.CHATMESSAGE_URI,
						messageTemp.getContentValues());
			}

			//chatSession.setLastUpdate(String.valueOf(System.currentTimeMillis()));

			ContentValues values = new ContentValues();
			values.put(ChatSessionColumns.LAST_UPDATE, String.valueOf(System.currentTimeMillis()));

			//更新最后消息的时间
			this.getContentResolver().update(GalaxyProviderInfo.CHAT_URI, 
					values, 
					ChatSessionColumns.TARGET_ID+"=? and "+ChatSessionColumns.USER_ID+"=?", new String[]{targetUser.getUser_id(),myId});
			break;

		case R.id.chat_other_button:
			if (chatOtherLayout.isShown()) {
				chatOtherLayout.setVisibility(View.GONE);
			} else {
				chatOtherLayout.setVisibility(View.VISIBLE);
			}
			// int[] location = new int[2];
			// contentText.getLocationOnScreen(location);
			// if (moreWinddow.isShowing()) {
			// moreWinddow.dismiss();
			// } else {
			// moreWinddow.showAtLocation(contentText, Gravity.TOP
			// | Gravity.LEFT, location[0], location[1]);
			// }
			break;
			// case R.id.chat_face_button:
			// break;
			// case R.id.chat_picture_button:
			// new Builder(ChatActivity.this).setTitle("请选择图片来源")
			// .setItems(new String[]{"相机","相册"}, new OnCameraClickListener())
			// .show();
			// break;

		case R.id.chat_top_back_button:
			this.finish();
			break;
		case R.id.chat_top_group_button:
			/**
			 * 暂时跳转到 个人详情页面
			 */
//			User u = null;
//			ArrayList<User> us = Users.getUserInfoFromDB(f.getPublisher_id());
//			if(us!=null&&us.size()>0){
//				u = us.get(0);
//			}else{
//				//联网获取用户信息
//			}
			Intent intent = new Intent(this,PersonInfoActivity.class);
			Bundle bundle = new Bundle();
			bundle.putParcelable(PersonInfoActivity.TARGET_USER, targetUser);
			intent.putExtra("data", bundle);
			startActivity(intent);
			
			// new Builder(ChatActivity.this)
			// .setTitle("请选择相关操作")
			// .setItems(new String[] { "查看聊天记录", "......" },
			// new OnGroupButtonClickListener()).show();
			// 跳转到 选择联系人
			// Intent intent = new Intent(this, SelectContactActivity.class);
			// intent.putExtra(SelectContactActivity.FIRST_CONTACT,
			// targetUser.getUser_id());
			// startActivityForResult(intent, REQUEST_CODE_ADD_CONTACT);

			break;
		default:
			break;
		}
	}

	private OnMessageSendListener defaultOnMessageSendListener = new OnMessageSendListener() {

		@Override
		public void onMessageSendStart(ChatMessage message) {
			DebugLog.logd("Chat",
					"onMessageSendStart content:" + message.getContent()
					+ ", status:" + message.getStatus().name());
			Message msg = Message.obtain();
			msg.obj = message;
			msg.what = 0;
			handler.sendMessage(msg);
		}

		@Override
		public void onMessageSendFinish(ChatMessage message, boolean success) {
			DebugLog.logd("Chat",
					"onMessageSendFinish content:" + message.getContent()
					+ ", result:" + success + ", status:"
					+ message.getStatus().name());
			// Message msg = Message.obtain();
			// if(!success){
			// message.setStatus(MESSAGE_STATUS.STATUS_SEND_FAIL);
			// }else{
			//
			// }
			// msg.obj = message;
			// msg.what = 1;
			// handler.sendMessage(msg);
			handler.sendEmptyMessage(1);
		}

	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		// 在聊天记录activity返回
		case CHAT_HISTORY:
			if (resultCode == Activity.RESULT_OK) {
				ChatMessage message = data.getParcelableExtra(RESULT_COPY);
				message.setTimestamp(System.currentTimeMillis());
				switch (message.getMediatype()) {
				case MEDIA_TEXT:
					// Toast.makeText(ChatActivity.this, "文字信息",
					// Toast.LENGTH_SHORT).show();
					targetChat.sendMessage(message,
							defaultOnMessageSendListener);
					adapter.addMessage(message);
					this.getContentResolver().insert(
							GalaxyProviderInfo.CHATMESSAGE_URI,
							message.getContentValues());
					break;
				case MEDIA_IMAGE:
					// Toast.makeText(ChatActivity.this, "图片信息",
					// Toast.LENGTH_SHORT).show();
					targetChat.sendImage(message.getMediaCache(),
							defaultOnMessageSendListener);
					adapter.addMessage(message);
					this.getContentResolver().insert(
							GalaxyProviderInfo.CHATMESSAGE_URI,
							message.getContentValues());
					break;
				case MEDIA_AUDIO:
					// Toast.makeText(ChatActivity.this, "语音信息",
					// Toast.LENGTH_SHORT).show();
					targetChat.sendAudio(message.getMediaCache(),
							message.getAudioTime(),
							defaultOnMessageSendListener);
					adapter.addMessage(message);
					this.getContentResolver().insert(
							GalaxyProviderInfo.CHATMESSAGE_URI,
							message.getContentValues());
					break;
				default:
					break;
				}

			}
			break;
			// 相机
		case IMAGE_CAMERA:
			if (resultCode == Activity.RESULT_OK) {
				Cursor c = getApplicationContext().getContentResolver().query(
						Media.EXTERNAL_CONTENT_URI,
						new String[] { Images.Media._ID, Images.Media.DATA },
						null, null, Images.Media._ID + " desc limit 1");
				if (c != null) {
					c.moveToFirst();
				}
				ChatMessage message = targetChat.sendImage(
						c.getString(c.getColumnIndex(Images.Media.DATA)),
						defaultOnMessageSendListener);

				adapter.addMessage(message);
				getApplication()
				.getApplicationContext()
				.getContentResolver()
				.insert(GalaxyProviderInfo.CHATMESSAGE_URI,
						message.getContentValues());
			}
			break;
			// 相册
		case IMAGE_STORE:

			if (resultCode == Activity.RESULT_OK) {
				// Toast.makeText(this, "here", Toast.LENGTH_SHORT).show();
				// Uri imageUri = data.getData();
				Uri uri = data.getData();

				String[] proj = { MediaStore.Images.Media.DATA };

				Cursor actualimagecursor = managedQuery(uri, proj, null, null,
						null);

				int actual_image_column_index = actualimagecursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

				actualimagecursor.moveToFirst();

				String img_path = actualimagecursor
						.getString(actual_image_column_index);

				// final File file = new File(img_path);
				// if(!file.exists()){
				// try {
				// file.createNewFile();
				// } catch (IOException e) {
				// e.printStackTrace();
				// }
				// }
				// Log.i("file", img_path);
				ChatMessage cm = targetChat.sendImage(img_path,
						defaultOnMessageSendListener);
				adapter.addMessage(cm);
				getApplication()
				.getApplicationContext()
				.getContentResolver()
				.insert(GalaxyProviderInfo.CHATMESSAGE_URI,
						cm.getContentValues());
				// Bitmap map = BitmapFactory.decodeFile(img_path);
				// sendImage.setImageBitmap(map);

				// //File file = new File(imageUri.getPath());
				// try {
				// SmackFile.sendFile(Smacks.conn, targetContact.getUserJID(),
				// file);
				// } catch (XMPPException e) {
				// e.printStackTrace();
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }
				//
				//
				// Presence presence =
				// Smacks.conn.getRoster().getPresence(targetContact.getUser_name());//获得用户状态
				// if(presence.getType() == Presence.Type.available)
				// {
				// final String JID = presence.getFrom();//这里获得的是完整JID
				// new Thread(new Runnable() {
				//
				// @Override
				// public void run() {
				// try {
				// SmackFile.sendFile(Smacks.conn, JID, file);
				// Log.i("file", targetContact.getUser_id());
				// } catch (XMPPException e) {
				// e.printStackTrace();
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }
				// }
				// }).run();
				// }

				// 开启子线程

				// Log.i("uri", m.imageUri.toString());
				// ContentResolver cr = m.getContentResolver();
				// try {
				// Bitmap bitmap =
				// BitmapFactory.decodeStream(cr.openInputStream(m.imageUri));
				// /* 将Bitmap设定到ImageView */
				// if(m.isBus)
				// m.busImage.setImageBitmap(bitmap);
				// else
				// m.perImage.setImageBitmap(bitmap);
				// } catch (FileNotFoundException e) {
				// Log.e("Exception", e.getMessage(),e);
				// }
			}
			break;
		case REQUEST_CODE_ADD_CONTACT:
			if (resultCode == RESULT_OK) {
				// 提取用户 添加到当前对话

			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(myReceiver);
		AudioPlayer.getInstance().stop(true);
		loader.quit();
	}

	class OnCameraClickListener implements
	android.content.DialogInterface.OnClickListener {

		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case 0:
				isCamera = true;
				Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(imageIntent, IMAGE_CAMERA);
				break;
			case 1:
				isCamera = false;
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
				startActivityForResult(intent, IMAGE_STORE);
				break;
			default:
				break;
			}
		}
	}

	class OnGroupButtonClickListener implements
	android.content.DialogInterface.OnClickListener {

		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case 0:
				// 查看聊天的历史记录
				// Toast.makeText(ChatActivity.this, "历史记录",
				// Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(ChatActivity.this,
						ChatHistoryActivity.class);
				intent.putExtra("type", "single");
				intent.putExtra("targetId", targetUser.getUser_id());
				startActivityForResult(intent, CHAT_HISTORY);
				break;
			case 1:
				Toast.makeText(ChatActivity.this, "其他操作", Toast.LENGTH_SHORT)
				.show();
				// 其他操作
				break;
			default:
				break;
			}
		}
	}

	class OnMessageItemLongClickListener implements
	android.content.DialogInterface.OnClickListener {

		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case 0:
				// 复制
				//
				Toast.makeText(ChatActivity.this, "转发", Toast.LENGTH_SHORT)
				.show();
				break;
			case 1:
				Toast.makeText(ChatActivity.this, "复制", Toast.LENGTH_SHORT)
				.show();
				// 转发
				break;
			default:
				break;
			}
		}
	}

	class VoiceClickListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// mCard =
			// IndexCardList.getInstance(mUserID).getCardByCardid(cardId);
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// if(myAdapter==null){
				// myAdapter = new ChatMsgViewAdapter(ChatActivity.this,
				// list,otherphotourl,myphotourl,mTargetID);
				// }
				// myAdapter.conplaying=false;
				// myAdapter.playing=false;
				// myAdapter.notifyDataSetChanged();
				// 停止播放录音
				AudioPlayer.getInstance().stop(true);

				// if(!Cards.isContact(ChatActivity.this,mUserID , mTargetID)){
				// addContact();
				// return false;
				// }
				// i=0;
				// mVoiceButton.setBackgroundResource(R.drawable.press_button_press);
				// mVoiceButton.setTextColor(Color.WHITE);
				// mVoiceButton.setText(R.string.upend);
				voiceLong = true;
				// //开始录音
				// if(timer!=null){
				// timer.cancel();
				// timer.purge();
				// timer = null;
				// }
				// timer = new Timer();
				// isRecordFinish.set(false);
				mStartTime = System.currentTimeMillis();
				if (audioRecorder != null
						&& audioRecorder.getState() == State.RUNNABLE) {
					audioRecorder.finish();
					audioRecorder = null;
				}
				audioRecorder = new AudioRecorder(Common.getMP3File(
						ChatActivity.this).getAbsolutePath(),
						new RecordListener() {

					@Override
					public void onRecordStart() {
						handler.sendEmptyMessage(4);
					}

					@Override
					public void onRecordProcess(int volumn) {
						// handler.sendEmptyMessage(what);
						// Message msg = new Message();
						// msg.what = 3;
						// msg.obj = volumn;
						// handler.sendMessage(msg);
					}

					@Override
					public void onRecordFinish(boolean isCancel,
							int second) {
						endTime = System.currentTimeMillis();
						int time = (int) (endTime - mStartTime) / 1000;
						ChatMessage cm = targetChat.sendAudio(
								audioRecorder.getPath(), time,
								defaultOnMessageSendListener);
						adapter.addMessage(cm);
						getApplication()
						.getApplicationContext()
						.getContentResolver()
						.insert(GalaxyProviderInfo.CHATMESSAGE_URI,
								cm.getContentValues());
						// this.getContentResolver().insert(GalaxyProviderInfo.CHATMESSAGE_URI,
						// message.getContentValues());
						// if (!isCancel) {
						// if (second >= 1 && i>=1) {
						// ChatMessage cm =
						// targetChat.sendAudio(audioRecorder.getPath(),i,defaultOnMessageSendListener);
						// //list.add(cm);
						// adapter.addMessage(cm);
						// //handler.sendEmptyMessage(0);
						// } else {
						// // 小于1秒抬起取消发送
						// ToastUtil.showMessage(
						// ChatActivity.this, "时间太短");
						// }
						// }
						// if(menuWindow!=null){
						// menuWindow.dismiss();
						// }
						audioButton.setEnabled(true);
					}

				});
				audioRecorder.start();
				// ViewGroup root = (ViewGroup) findViewById(R.id.chat_root);
				// view =
				// LayoutInflater.from(ChatActivity.this).inflate(R.layout.audio_recorder_ring,
				// null);
				// if(menuWindow!=null){
				// menuWindow.dismiss();
				// menuWindow = null;
				// }

				// menuWindow = new PopupWindow(view,
				// displayMetrics.widthPixels/2, displayMetrics.widthPixels/2);
				// micImageView = (ImageView)
				// view.findViewById(R.id.chatting_image);
				// micImageView.setBackgroundResource(R.drawable.record01);
				// view.findViewById(R.id.pls_delete_rl).setVisibility(View.GONE);
				// view.setBackgroundResource(R.drawable.pls_talk);
				// menuWindow.showAtLocation(root, Gravity.CENTER_VERTICAL |
				// Gravity.CENTER_HORIZONTAL, 0, 0);
				break;
			case MotionEvent.ACTION_UP:
				ToastUtil.showMessage(ChatActivity.this, "录音结束");
				audioRecorder.finish();
				audioButton.setEnabled(true);
				// if(timer!=null){
				// isRecordFinish.set(true);
				// timer.cancel();
				// timer=null;
				// }

				// if(!Cards.isContact(ChatActivity.this,mUserID , mTargetID)){
				// mVoiceButton.setEnabled(true);
				// return false;
				// }
				/*
				 * if(mCard!=null&&mCard.store.equals("false")){ return false; }
				 */

				// mVoiceButton.setBackgroundResource(R.drawable.press_button);
				// mVoiceButton.setText(R.string.downstart);
				// mVoiceButton.setTextColor(ChatActivity.this.getResources().getColor(R.color.lightgrey));
				// int x = (int) event.getRawX();
				// int y = (int) event.getRawY();
				// if(voiceLong){
				// // mEndTime = System.currentTimeMillis();
				// //
				// //手指移动到屏幕中间区域抬起取消发送
				// if(x>=displayMetrics.widthPixels/2-displayMetrics.widthPixels/4&&x<=displayMetrics.widthPixels/2+displayMetrics.widthPixels/4&&y>=displayMetrics.heightPixels/2-displayMetrics.widthPixels/4&&y<=displayMetrics.heightPixels/2+displayMetrics.widthPixels/4){
				// audioRecorder.cancel();
				// audioButton.setEnabled(true);
				// }else{
				// audioRecorder.finish();
				// voiceLong = false;
				// }
				// }else{
				// audioButton.setEnabled(true);
				// }
				break;
			case MotionEvent.ACTION_MOVE:
				// if(!Cards.isContact(ChatActivity.this,mUserID , mTargetID)){
				// return false;
				// }
				/*
				 * if(mCard!=null&&mCard.store.equals("false")){ return false; }
				 */
				// int mx = (int) event.getRawX();
				// int my = (int) event.getRawY();
				// //手指移动到中间区域
				// if(mx>=displayMetrics.widthPixels/2-displayMetrics.widthPixels/4&&mx<=displayMetrics.widthPixels/2+displayMetrics.widthPixels/4&&my>=displayMetrics.heightPixels/2-displayMetrics.widthPixels/4&&my<=displayMetrics.heightPixels/2+displayMetrics.widthPixels/4){
				// if(view==null){
				// view =
				// LayoutInflater.from(ChatActivity.this).inflate(R.layout.audio_recorder_ring,
				// null);
				// }
				// view.findViewById(R.id.pls_talk_rl).setVisibility(View.GONE);
				// view.findViewById(R.id.pls_delete_rl).setVisibility(View.VISIBLE);
				// view.setBackgroundDrawable(null);
				// view.setBackgroundResource(R.drawable.plstalk_deletebg);
				// }else{
				// if(view==null){
				// view =
				// LayoutInflater.from(ChatActivity.this).inflate(R.layout.audio_recorder_ring,
				// null);
				// }
				// view.findViewById(R.id.pls_talk_rl).setVisibility(View.VISIBLE);
				// view.findViewById(R.id.pls_delete_rl).setVisibility(View.GONE);
				// if(i>=55&&i<60){
				// view.setBackgroundResource(R.drawable.plstalk_lastbg);
				// }else{
				// view.setBackgroundResource(R.drawable.pls_talk);
				// }
				// // view.setBackgroundResource(R.drawable.pls_talk);
				// }
				break;
			}
			return false;
		}
	}

	// private OnMessageSendListener defaultOnMessageSendListener = new
	// OnMessageSendListener(){
	//
	// @Override
	// public void onMessageSendStart(ChatMessage message) {
	// DebugLog.logd("Chat",
	// "onMessageSendStart content:"+message.getContent()+", status:"+message.getStatus().name());
	// handler.sendEmptyMessage(0);
	// }
	//
	// @Override
	// public void onMessageSendFinish(ChatMessage message, boolean success) {
	// DebugLog.logd("Chat",
	// "onMessageSendFinish content:"+message.getContent()+", result:"+success+", status:"+message.getStatus().name());
	// handler.sendEmptyMessage(0);
	// }
	//
	// };

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long arg3) {
		// Toast.makeText(this, "开始播放", Toast.LENGTH_SHORT).show();
		ChatMessage message = (ChatMessage) adapter.getItem(position);
		if (message.getMediatype() == ChatMessage.MESSAGE_MEDIA_TYPE.MEDIA_AUDIO) {
			// 语音-->播放
			// AudioPlayer.getInstance().play(message.getMediaCache());
			new AudioPlayer2().play(message.getMediaCache());
		} else if (message.getMediatype() == ChatMessage.MESSAGE_MEDIA_TYPE.MEDIA_IMAGE) {
			// 图片-->查看
			Intent it = new Intent(Intent.ACTION_VIEW);
			it.setDataAndType(Uri.fromFile(new File(message.getMediaCache())),
					"image/*");
			startActivity(it);
		}
	}

	@Override
	public boolean onLongClick(View view) {
		// Toast.makeText(this, "...", Toast.LENGTH_SHORT).show();
		if (copyMessage == null)
			return false;
		int[] location = new int[2];
		contentText.getLocationOnScreen(location);
		pasteWindow.showAtLocation(contentText, Gravity.LEFT, location[0],
				location[1]);
		return true;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View view,
			int position, long arg3) {
		copyMessage = (ChatMessage) adapter.getItem(position);
		if (copyMessage.getMediatype() == ChatMessage.MESSAGE_MEDIA_TYPE.MEDIA_TEXT) {
			new Builder(ChatActivity.this)
			.setTitle("操作")
			.setItems(new String[] { "转发", "复制" },
					new OnMessageItemLongClickListener()).show();
		} else {
			new Builder(ChatActivity.this)
			.setTitle("操作")
			.setItems(new String[] { "转发" },
					new OnMessageItemLongClickListener()).show();
		}
		return true;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (scrollStateTemp != SCROLL_STATE_FLING)
			Log.i("thread", "firstVisibleItem:" + firstVisibleItem + " "
					+ isRunning);

		// 滑到最顶端 继续加载
		if (firstVisibleItem == 0 && scrollStateTemp != SCROLL_STATE_FLING
				&& !isRunning) {
			isRunning = true;
			SysApplication.application.threadPool.submit(queryThread);
			Log.i("thread", "add~~queryThread:firstVisibleItem:"
					+ firstVisibleItem);
		}

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		scrollStateTemp = scrollState;
	}

}
