package com.example.galaxy08;

import java.util.ArrayList;
import java.util.List;

import com.example.galaxy08.adapter.MessageMainListAdapter;
import com.example.galaxy08.app.PreferenceWrapper;
import com.example.galaxy08.chat.ChatActivity;
import com.example.galaxy08.dao.ChatSessionColumns;
import com.example.galaxy08.dao.GalaxyProviderInfo;
import com.example.galaxy08.entity.ChatMessage;
import com.example.galaxy08.entity.ChatMessages;
import com.example.galaxy08.entity.ChatSession;
import com.example.galaxy08.entity.Group;
import com.example.galaxy08.entity.User;
import com.example.galaxy08.entity.Users;
import com.example.galaxy08.tool.ToastUtil;
import com.example.galaxy08.tool.Tool;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 聊天信息按照两种方式显示：组织架构/更新时间
 * @author guozhijun
 */
public class MainMessageActivity extends Activity implements OnItemClickListener,
OnChildClickListener, OnClickListener{
	
	private ExpandableListView messageList;
	
	private ExpandableListAdapter adapter;
	
	private List<List<Group>> mData;
	
	private EditText searchEdit;
	
	private ListView timeList;//按照更新时间排序
	
	private TimeListAdapter timeAdapter;
	
	private int[] mGroupArray = new int[]{
			R.array.guanliceng,R.array.guanliceng2,
			R.array.shejizu
	};
	
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_message);
		setupView();
		addListener();
	};
	
	private void setupView(){
		messageList = (ExpandableListView)findViewById(R.id.message_main_list);
		mData = new ArrayList<List<Group>>();
		//initData();
		adapter = new MessageMainListAdapter(this, mData);
		messageList.setAdapter(adapter);
		messageList.setGroupIndicator(null);
		searchEdit = (EditText)findViewById(R.id.message_search_contact);
		timeList = (ListView)findViewById(R.id.message_main_list_time);
		timeAdapter = new TimeListAdapter(this, null);
		timeList.setAdapter(timeAdapter);
	}
	
	@Override
	protected void onResume() {
		timeAdapter.updateData(getChatDataDB());
		super.onResume();
	}
	
	private void addListener(){
		messageList.setOnItemClickListener(this);
		messageList.setOnChildClickListener(this);
		findViewById(R.id.message_chat_add_button).setOnClickListener(this);
		findViewById(R.id.message_left_button).setOnClickListener(this);
		timeList.setOnItemClickListener(this);
	}

//	private void initData(){
//		 for (int i = 0; i < mGroupArray.length; i++) { 
//	            List<GroupItem> list = new ArrayList<GroupItem>(); 
//	            String[] childs = getStringArray(mGroupArray[i]); 
//	            for (int j = 0; j < childs.length; j++) { 
//	                GroupItem item = new GroupItem(childs[j],"12:10","hello"); 
//	                list.add(item); 
//	            } 
//	            mData.add(list); 
//	        } 
//	}
//	  private String[] getStringArray(int resId) { 
//	        return getResources().getStringArray(resId); 
//	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int arg2, long arg3) {
		
		if(adapterView.getId()==R.id.message_main_list_time){
			ChatSession c = (ChatSession)timeAdapter.getItem(arg2);
			ArrayList<User> temp = new ArrayList<User>();
			temp.add(c.getTargetUser());
			Intent intent = new Intent(this, ChatActivity.class);
			intent.putExtra(ChatActivity.TARGET_USER, temp);
			intent.putExtra(ChatActivity.USER_COUNT, temp.size());
			intent.putExtra(ChatActivity.CHAT_NAME, c.getTargetUser().getUser_name());
			startActivity(intent);
		}else{
			CheckBox box = (CheckBox)view.findViewById(R.id.main_list_checkbox);
			if(box.isChecked()){
				box.setChecked(false);
				box.setBackgroundResource(R.drawable.right);
			}else{
				box.setChecked(true);
				box.setBackgroundResource(R.drawable.down);
			}
		}
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		Group i = mData.get(groupPosition).get(childPosition);
		String name = i.getGroupName();
		if(name.equals(PreferenceWrapper.get(PreferenceWrapper.USER_ID, "0"))){
			Toast.makeText(this, "不能和自己聊天", Toast.LENGTH_SHORT).show();
			return false;
		}
		Intent intent = new Intent(this,ChatActivity.class);
		intent.putExtra("targetId", name);
		startActivity(intent);
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.message_chat_add_button:
			Intent intent = new Intent(this, SelectContactActivity.class);
			startActivity(intent);
			break;
			
		case R.id.message_left_button:
			//在 两种显示方式之间进行切换
			ToastUtil.makeText(this, "显示方式切换", Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
	}
	
	/**
	 * 查询会话记录
	 * @return
	 */
	private ArrayList<ChatSession> getChatDataDB(){
		ArrayList<ChatSession> cs = null;
		ContentResolver cr = this.getContentResolver();
		Cursor cursor = cr.query(GalaxyProviderInfo.CHAT_URI, 
				new String[]{
				ChatSessionColumns.TARGET_ID,
				ChatSessionColumns.TYPE,
				ChatSessionColumns.LAST_UPDATE,
				ChatSessionColumns.CREATE_TIME
				}, 
				ChatSessionColumns.USER_ID+"=?", 
				new String[]{
				PreferenceWrapper.get(PreferenceWrapper.USER_ID, "")
				}, 
				ChatSessionColumns.LAST_UPDATE+" desc");
		
		if(cursor!=null){
			cs = new ArrayList<ChatSession>();
			while(cursor.moveToNext()){
				ChatSession c =new ChatSession();
				c.setTargetId(cursor.getString(cursor.getColumnIndex(ChatSessionColumns.TARGET_ID)));
				c.setlastupdate(cursor.getString(cursor.getColumnIndex(ChatSessionColumns.LAST_UPDATE)));
				c.setCreateTime(cursor.getString(cursor.getColumnIndex(ChatSessionColumns.CREATE_TIME)));
				c.setType(cursor.getInt(cursor.getColumnIndex(ChatSessionColumns.TYPE)));
				cs.add(c);
			}
			cursor.close();
		}
		/**
		 * 分类别 查询用户
		 */
		if(cs!=null){
			for(int i=0;i<cs.size();i++){
				String targetId = cs.get(i).getTargetId();
				cs.get(i).setTargetUser(Users.getUserInfoFromDB(targetId).get(0));
				ArrayList<ChatMessage> messages = ChatMessages.getMessages(
						PreferenceWrapper.get(PreferenceWrapper.USER_ID, ""), targetId, 0);
				cs.get(i).setMessage(messages.get(messages.size()-1));
			}
		}
		/**
		 * 获取最后一条信息
		 */
		return cs;
	}
	
	class TimeListAdapter extends BaseAdapter{

		private ArrayList<ChatSession> cs;
		private LayoutInflater inflater;
		
		public TimeListAdapter(Context context,ArrayList<ChatSession> cs){
			setCs(cs);
			this.inflater = LayoutInflater.from(context);
		}
		
		public void setCs(ArrayList<ChatSession> cs) {
			if(cs!=null)
				this.cs = cs;
			else{
				this.cs = new ArrayList<ChatSession>();
			}
		}
		
		@Override
		public int getCount() {
			// TODO 自动生成的方法存根
			return cs.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO 自动生成的方法存根
			return cs.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			// TODO 自动生成的方法存根
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHodler h = null;
			if(convertView==null){
				h = new ViewHodler();
				convertView=inflater.inflate(R.layout.message_main_list_item, null);
				h.avatar = (ImageView)convertView.findViewById(R.id.message_main_list_item_icon);
				h.name = (TextView)convertView.findViewById(R.id.message_main_list_item_groupname);
				h.message = (TextView)convertView.findViewById(R.id.message_main_list_item_grouplastmessage);
				h.time = (TextView)convertView.findViewById(R.id.message_main_list_item_grouptime);
				h.count = (TextView)convertView.findViewById(R.id.message_main_list_item_groupmessagecount);
				convertView.setTag(h);
			}else{
				h = (ViewHodler)convertView.getTag();
			}
			ChatSession c = cs.get(position);
			if(c!=null){
				if(c.getTargetUser().getUser_name()!=null){
					h.name.setText(c.getTargetUser().getUser_name());
				}
				if(c.getMessage()!=null){
					h.message.setText(c.getMessage().getContent());
					h.time.setText(Tool.parseDateTime(c.getMessage().getTimestamp()));
				}
			}
			
			return convertView;
		}
		
		public void updateData(ArrayList<ChatSession> cs){
			this.setCs(cs);
			this.notifyDataSetChanged();
		}
		
		class ViewHodler{
			ImageView avatar;
			TextView name;
			TextView message;
			TextView time;
			TextView count;
		}
		
	}
}
