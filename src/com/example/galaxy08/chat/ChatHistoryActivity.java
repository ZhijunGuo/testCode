package com.example.galaxy08.chat;

import java.io.File;
import java.util.ArrayList;

import com.example.galaxy08.R;
import com.example.galaxy08.app.PreferenceWrapper;
import com.example.galaxy08.chat.ChatActivity.OnGroupButtonClickListener;
import com.example.galaxy08.dao.ChatmessageColumns;
import com.example.galaxy08.dao.GalaxyProviderInfo;
import com.example.galaxy08.entity.ChatMessage;
import com.example.galaxy08.entity.ChatMessage.MESSAGE_MEDIA_TYPE;
import com.example.galaxy08.entity.ChatMessage.MESSAGE_TYPE;
import com.example.galaxy08.entity.ChatMessages;
import com.example.galaxy08.message.audio.AudioPlayer;
import com.example.galaxy08.tool.Tool;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
/*
 * 查询聊天记录
 * 1 私聊：type=single 
 * 	 需要：targetId
 * 2 群聊：type=group
 *   需要：groupId  
 */
public class ChatHistoryActivity extends Activity implements OnClickListener,
OnItemClickListener,OnItemLongClickListener,OnEditorActionListener{
	
	private String targetId;
	private String groupId;
	
	private final int COUNT_PRE_PAGE=8;
	
	private int sumCount;
	
	private int sumPage;
	
	private ListView historyList;
	
	private EditText pageEdit;
	
	private TextView topText;
	
	private TextView sumCountText;
	
	private String myId;
	
	private boolean privateChat = false;
	
	private ChatMessage targetMessage = null;
	
	private ChatHistoryListAdapter adapter;
	
	private int currentPage = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_history);
		setUpView();
		myId = PreferenceWrapper.get(PreferenceWrapper.USER_ID, "0");
		String type = getIntent().getStringExtra("type");
		if(type.equals("single")){
			//私聊信息
			privateChat = true;
			targetId = getIntent().getStringExtra("targetId");
			topText.setText("与"+targetId+"聊天记录");
		}else{
			//群聊信息
			privateChat = false;
			groupId = getIntent().getStringExtra("groupId");
			topText.setText("群组"+groupId+"聊天记录");
		}
		sumCount = getCount();
		sumPage = sumCount/COUNT_PRE_PAGE==0?
				sumCount/COUNT_PRE_PAGE:sumCount/COUNT_PRE_PAGE+1;
		pageEdit.setText(String.valueOf(sumPage));
		currentPage = sumPage;
		sumCountText.setText("/"+currentPage);
		adapter = new ChatHistoryListAdapter(this, getData(sumPage));
		historyList.setAdapter(adapter);
		historyList.setOnItemLongClickListener(this);
		pageEdit.setOnEditorActionListener(this);
	}
	
	private void setUpView(){
		historyList = (ListView)findViewById(R.id.chat_history_list);
		historyList.setDivider(null); 
		historyList.setOnItemClickListener(this);
		findViewById(R.id.chat_history_next_button).setOnClickListener(this);
		findViewById(R.id.chat_history_pre_button).setOnClickListener(this);
		findViewById(R.id.chat_history_left_button).setOnClickListener(this);
		findViewById(R.id.chat_history_right_button).setOnClickListener(this);
		pageEdit = (EditText)findViewById(R.id.chat_history_page_edit);
		sumCountText = (TextView)findViewById(R.id.chat_history_sumcount);
		topText = (TextView)findViewById(R.id.chat_history_top_text);
	}
	
	private int getCount(){
		if(privateChat){
			ContentResolver cr = this.getContentResolver();
			Cursor cursor = cr.query(GalaxyProviderInfo.CHATMESSAGE_URI, 
					new String[]{"count(*)"}, 
					null, null, null);
			if(cursor!=null){
				cursor.moveToNext();
				return cursor.getInt(cursor.getColumnIndex("count(*)"));
			}else{
				return 0;
			}
		}else{
			return 0;
		}
	}
	
	private ArrayList<ChatMessage> getData(int page){
		ArrayList<ChatMessage> ms = null;
		ContentResolver cr;
		
		if(privateChat){
			//如果是私聊
			cr = this.getContentResolver();
			Cursor curor = cr.query(GalaxyProviderInfo.CHATMESSAGE_URI, 
					new String[]{
					ChatmessageColumns._ID,
					ChatmessageColumns.CONTENT,
					ChatmessageColumns.USERID,
					ChatmessageColumns.TIMESTAMP,
					ChatmessageColumns.TARGETID,
					ChatmessageColumns.AUDIO_TIME,
					ChatmessageColumns.MEDIATYPE,
					ChatmessageColumns.MEDIA_CACHE,
					ChatmessageColumns.TYPE}, 
					"("+ChatmessageColumns.USERID+"=? and "+ChatmessageColumns.TARGETID+"=?) or ("+ChatmessageColumns.USERID+"=? and "
							+ChatmessageColumns.TARGETID+"=?) limit "+(page-1)*COUNT_PRE_PAGE+","+COUNT_PRE_PAGE, 
					new String[]{
					myId,
					targetId,
					targetId,
					myId
					},null);
			if(curor!=null){
				ms = new ArrayList<ChatMessage>();
				while(curor.moveToNext()){
					ChatMessage oneChat = new ChatMessage();
					oneChat.setId(curor.getLong(curor.getColumnIndex(ChatmessageColumns._ID)));
					oneChat.setUserid(curor.getString(curor.getColumnIndex(ChatmessageColumns.USERID)));
					oneChat.setTargetid(curor.getString(curor.getColumnIndex(ChatmessageColumns.TARGETID)));
					oneChat.setContent(curor.getString(curor.getColumnIndex(ChatmessageColumns.CONTENT)));
					oneChat.setTimestamp(curor.getLong(curor.getColumnIndex(ChatmessageColumns.TIMESTAMP)));
					oneChat.setMediaCache(curor.getString(curor.getColumnIndex(ChatmessageColumns.MEDIA_CACHE)));
					oneChat.setAudioTime(curor.getInt(curor.getColumnIndex(ChatmessageColumns.AUDIO_TIME)));
					//oneChat.setMediatype(curor.getInt(curor.getColumnIndex(ChatmessageColumns.AUDIO_TIME)));
					switch (curor.getInt(curor.getColumnIndex(ChatmessageColumns.MEDIATYPE))) {
					case 0:
						oneChat.setMediatype(MESSAGE_MEDIA_TYPE.MEDIA_INVALID);
						break;
					case 1:
						oneChat.setMediatype(MESSAGE_MEDIA_TYPE.MEDIA_TEXT);
						break;
					case 2:
						oneChat.setMediatype(MESSAGE_MEDIA_TYPE.MEDIA_IMAGE);
						break;
					case 3:
						oneChat.setMediatype(MESSAGE_MEDIA_TYPE.MEDIA_AUDIO);
						break;

					default:
						break;
					}
					switch (curor.getInt(curor.getColumnIndex(ChatmessageColumns.TYPE))) {
					case 0:
						oneChat.setType(MESSAGE_TYPE.MESSAGE_INVALID);
						break;
					case 1:
						oneChat.setType(MESSAGE_TYPE.MESSAGE_SEND);
						break;
					case 2:
						oneChat.setType(MESSAGE_TYPE.MESSAGE_RECEIVE);
						break;
					default:
						break;
					}
					
					ms.add(oneChat);
				}
				curor.close();
			}else{
				Toast.makeText(this, "c==null", Toast.LENGTH_SHORT).show();
			}
		}else{
			
		}
		return ms;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chat_history_next_button:
			if(currentPage<sumPage){
				currentPage++;
				pageEdit.setText(String.valueOf(currentPage));
				adapter.replaceData(getData(currentPage));
			}
			break;
		case R.id.chat_history_pre_button:
			if(currentPage!=1){
				currentPage--;
				pageEdit.setText(String.valueOf(currentPage));
				adapter.replaceData(getData(currentPage));
			}
			break;
		case R.id.chat_history_left_button:
			this.finish();
			break;
		case R.id.chat_history_right_button:
			Toast.makeText(this, "操作", Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
		ChatMessage message = (ChatMessage)adapter.getItem(position);
		if(message.getMediatype()==ChatMessage.MESSAGE_MEDIA_TYPE.MEDIA_AUDIO){
			//语音-->播放
			AudioPlayer.getInstance().play(message.getMediaCache());
		}else if(message.getMediatype()==ChatMessage.MESSAGE_MEDIA_TYPE.MEDIA_IMAGE){
			//图片-->查看
			Intent it = new Intent(Intent.ACTION_VIEW);
			it.setDataAndType(Uri.fromFile(new File(message.getMediaCache())), "image/*");
			startActivity(it); 
		}
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		AudioPlayer.getInstance().stop(true);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> adapterView, View view, 
			int position,
			long arg3) {
		targetMessage = (ChatMessage)adapter.getItem(position);
		if(targetMessage.getMediatype()==ChatMessage.MESSAGE_MEDIA_TYPE.MEDIA_TEXT){
			new Builder(ChatHistoryActivity.this).setTitle("请选择相关操作")
			.setItems(new String[]{"删除","重新发送","转发","隐藏","复制"},new OnChatHistoryMessageClickListener())
			.show();
		}else{
			new Builder(ChatHistoryActivity.this).setTitle("请选择相关操作")
			.setItems(new String[]{"删除","重新发送","转发","隐藏"},new OnChatHistoryMessageClickListener())
			.show();
		}
		return false;
	}
	
	class OnChatHistoryMessageClickListener implements android.content.DialogInterface.OnClickListener{
		
		public void onClick(DialogInterface dialog, int which) {
			//"删除","重新发送","转发","隐藏","复制"
			switch (which) {
			case 0:
				//删除 Delete Record
				Toast.makeText(ChatHistoryActivity.this, "删除记录", Toast.LENGTH_SHORT).show();
				ContentResolver cr = ChatHistoryActivity.this.getContentResolver();
				cr.delete(GalaxyProviderInfo.CHATMESSAGE_URI, ChatmessageColumns._ID+"=?", 
						new String[]{String.valueOf(targetMessage.getId())});
				adapter.removeMessage(targetMessage);
				break;
			case 1:
				// 重新发送
				Intent targetIntent = new Intent(ChatHistoryActivity.this, ChatActivity.class);
				targetIntent.putExtra(ChatActivity.RESULT_COPY, targetMessage);
				ChatHistoryActivity.this.setResult(RESULT_OK,targetIntent);
				ChatHistoryActivity.this.finish();
				break;
			case 2:
				//转发
				Toast.makeText(ChatHistoryActivity.this, "转发", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				//隐藏
				Toast.makeText(ChatHistoryActivity.this, "隐藏", Toast.LENGTH_SHORT).show();
				break;
			case 4:
				//复制
				Toast.makeText(ChatHistoryActivity.this, "复制", Toast.LENGTH_SHORT).show();
				ChatActivity.copyMessage = targetMessage;
				break;
			default:
				break;
			}
			targetMessage = null;
		}
	}

	@Override
	public boolean onEditorAction(TextView arg0, int actionId, KeyEvent key) {
		/*
		 * 搜狗输入法 按下回车按键为 0
		 */
		if(actionId==EditorInfo.IME_ACTION_DONE
				||actionId==0){
			int page = Integer.valueOf(pageEdit.getText().toString().trim());
			if(page<1||page>sumPage||page==currentPage){
				return true;
			}
			adapter.replaceData(getData(page));
			Tool.hideSoftKeyBoard(ChatHistoryActivity.this);
		}
		return true;
	}
}
