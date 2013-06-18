package com.example.galaxy08;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.galaxy08.app.PreferenceWrapper;
import com.example.galaxy08.dao.GalaxyProviderInfo;
import com.example.galaxy08.entity.Department;
import com.example.galaxy08.entity.User;
import com.example.galaxy08.entity.Users;

public class MainContactActivity extends Activity implements OnClickListener,OnItemClickListener{
	
	private ListView contactList;
	
	private ContactListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_contact);
		//插入假数据
		//insertUserData();
		setupView();
		addListener();
		setContent();
	}
	
	private void setupView(){
		contactList = (ListView)findViewById(R.id.contact_list);
		adapter = new ContactListAdapter(this, Users.getUserInfoFromDB());
		contactList.setAdapter(adapter);
	}
	
	private void addListener(){
		findViewById(R.id.contact_top_left_button).setOnClickListener(this);
		findViewById(R.id.contact_top_right_button).setOnClickListener(this);
		contactList.setOnItemClickListener(this);
	}
	
	private void setContent(){
		//getUserFromDB();
	}
	
	/**
	 * 在数据库中读取 联系人信息
	 * @return
	 */
//	private ArrayList<User> getUserFromDB(){
//		ArrayList<User> userInfo = null;
//		
//		ContentResolver cr = this.getContentResolver();
//		Cursor cursor = cr.query(GalaxyProviderInfo.USER_URI, 
//				new String[]{
//				UserColumns.USER_ID,
//				UserColumns.USER_NAME,
//				UserColumns.IMG_PATH,
//				UserColumns.POSITION,
//				UserColumns.DEPARTMENT,
//				UserColumns.MOBILE,
//				UserColumns.TELEPHONY,
//				UserColumns.EMAIL,
//				UserColumns.ADDRESS,
//				UserColumns.GROUP_ID,
//				UserColumns.LEVEL,
//				UserColumns.TYPE
//		}, UserColumns.OWNERID+"=?", 	
//		new String[]{PreferenceWrapper.get(PreferenceWrapper.USER_ID, "")},null);
//		
//		if(cursor!=null){
//			userInfo = new ArrayList<User>();
//			while(cursor.moveToNext()){
//				User u = new User();
//				u.setUser_id(cursor.getString(cursor.getColumnIndex(UserColumns.USER_ID)));
//				u.setUser_name(cursor.getString(cursor.getColumnIndex(UserColumns.USER_NAME)));
//				u.setPosition(cursor.getString(cursor.getColumnIndex(UserColumns.POSITION)));
//				u.setDepartment(cursor.getString(cursor.getColumnIndex(UserColumns.DEPARTMENT)));
//				u.setAddress(cursor.getString(cursor.getColumnIndex(UserColumns.ADDRESS)));
//				u.setEmail(cursor.getString(cursor.getColumnIndex(UserColumns.EMAIL)));
//				u.setImg_path(cursor.getString(cursor.getColumnIndex(UserColumns.IMG_PATH)));
//				u.setMobile(cursor.getString(cursor.getColumnIndex(UserColumns.MOBILE)));
//				u.setTelephony(cursor.getString(cursor.getColumnIndex(UserColumns.TELEPHONY)));
//				u.setType(cursor.getInt(cursor.getColumnIndex(UserColumns.TYPE)));
//				u.setLevel(cursor.getInt(cursor.getColumnIndex(UserColumns.LEVEL)));
//				//u.set
//				userInfo.add(u);
//			}
//			cursor.close();
//		}
//		
//		return userInfo;
//	}
//	
	/**
	 * 准备几条数据 插到数据库中
	 * @return
	 */
	private void insertUserData(){
		
		/**
		 * 模拟 群组 (28,29,34)
		 * groupid 0 : 全公司
		 * groupid  29: 高级管理 (王大拿，大海，智军)
		 * groupid  34: 销售部(张晴)
		 */
		String userid = PreferenceWrapper.get(PreferenceWrapper.USER_ID, "");
		
		ArrayList<Department> ds = new ArrayList<Department>();
		
		Department g1 = new Department();
		g1.setDepartment_id(28);
		g1.setDepartment_name("公司");
		g1.setOwner_id(userid);
		g1.setLevel(0);
		
		Department g2 = new Department();
		g2.setDepartment_id(29);
		g2.setDepartment_name("高级管理层");
		g2.setOwner_id(userid);
		g2.setLevel(1);
		
		Department g3 = new Department();
		g3.setDepartment_id(34);
		g3.setDepartment_name("销售部");
		g3.setOwner_id(userid);
		g3.setLevel(1);
		
		ds.add(g1);
		ds.add(g2);
		ds.add(g3);
		
		insertDepartMentInfoToDB(ds);
		
		ArrayList<User> myUser = new ArrayList<User>();
		User u = new User();
		u.setUser_id("36");
		u.setMobile("18253415371");
		u.setUser_name("王大拿");
		u.setPosition("经理");
		u.setDepartment("高级管理层");
		u.setAddress("北京朝阳");
		u.setGender(1);
		u.setOwner_id(userid);
		u.setGroup_id(29);
		
		User u1 = new User();
		u1.setUser_id("34");
		u1.setEmail("guozj2005@163.com");
		u1.setUser_name("大海");
		u1.setPosition("行政主管");
		u1.setDepartment("高级管理层");
		u1.setAddress("北京海淀");
		u1.setGender(0);
		u1.setOwner_id(userid);
		u1.setGroup_id(29);
		
		User u2 = new User();
		u2.setUser_id("37");
		u2.setEmail("zhangqing@163.com");
		u2.setUser_name("张晴");
		u2.setPosition("主管");
		u2.setDepartment("销售部");
		u2.setAddress("北京海淀");
		u2.setGender(0);
		u2.setOwner_id(userid);
		u2.setGroup_id(34);
		
		User u3 = new User();
		u3.setUser_id("35");
		u3.setUser_name("智军");
		u3.setPosition("经理");
		u3.setDepartment("高级管理层");
		u3.setAddress("北京丰台");
		u3.setGender(1);
		u3.setOwner_id(userid);
		u3.setGroup_id(29);
		
		myUser.add(u);
		myUser.add(u1);
		myUser.add(u2);
		myUser.add(u3);
		
		insertUserInfoToDB(myUser);
	}

	/**
	 * 将获取的联系人信息插入到数据库中
	 * 
	 */
	
	private void insertUserInfoToDB(ArrayList<User> userInfos){
		ContentResolver cr = this.getContentResolver();
		for(User u:userInfos){
			cr.insert(GalaxyProviderInfo.USER_URI, u.getContentValues());
		}
	}
	
	/**
	 * 将群组信息插入到数据库中
	 */
	private void insertDepartMentInfoToDB(ArrayList<Department> ds){
		ContentResolver cr = this.getContentResolver();
		for(Department d:ds){
			cr.insert(GalaxyProviderInfo.DEPARTMENT_URI, d.getContentValues());
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.contact_top_left_button:
			
			break;
		case R.id.contact_top_right_button:
			
			break;
			
		case R.id.contact_item_call:
			
			break;
			
		case R.id.contact_item_attention_button:
			//有两种情况：加关注/取消关注
			User user = (User)v.getTag();
			Button attButton = (Button)v;//.getTag();
			String text = attButton.getText().toString().trim();
			//如果是加关注 button 转换为 取消关注
			if(text.equals(getString(R.string.add_attention))){
				attButton.setBackgroundResource(R.drawable.cancel_attention_button);
				attButton.setText(getString(R.string.cancel_attention));
			}else{
				attButton.setBackgroundResource(R.drawable.add_attention_button);
				attButton.setText(getString(R.string.add_attention));
			}
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		User targetUser = (User)adapter.getItem(position);
		Intent intent = new Intent(this,PersonInfoActivity.class);
		//intent.putExtra(PersonInfoActivity.TARGET_USER, targetUser);
		Bundle bundle = new Bundle();
		bundle.putParcelable(PersonInfoActivity.TARGET_USER, targetUser);
		intent.putExtra("data", bundle);
		startActivity(intent);
	}
	
	class ContactListAdapter extends BaseAdapter{
		
		private ArrayList<User> myUsers;
		private LayoutInflater inflater;
		
		public ContactListAdapter(Context context,ArrayList<User> myUsers){
			inflater = LayoutInflater.from(context);
			setMyUsers(myUsers);
		}

		public void setMyUsers(ArrayList<User> myUsers) {
			if(myUsers!=null)
				this.myUsers = myUsers;
			else
				this.myUsers = new ArrayList<User>();
		}
		
		@Override
		public int getCount() {
			return myUsers.size();
		}

		@Override
		public Object getItem(int position) {
			return myUsers.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			User u = myUsers.get(position);
			Viewhodler h = null;
			if(convertView==null){
				convertView = inflater.inflate(R.layout.contact_item,null);
				h = new Viewhodler();
				h.avatar = (ImageView)convertView.findViewById(R.id.contact_item_avatar);
				h.name = (TextView)convertView.findViewById(R.id.contact_item_name);
				h.position = (TextView)convertView.findViewById(R.id.contact_item_position);
				h.department = (TextView)convertView.findViewById(R.id.contact_item_department);
				h.callButton = (ImageView)convertView.findViewById(R.id.contact_item_call);
				h.attentionButton = (Button)convertView.findViewById(R.id.contact_item_attention_button);
				convertView.setTag(h);
			}else{
				h = (Viewhodler)convertView.getTag();
			}
			//h.avatar
			h.name.setText(u.getUser_name());
			h.position.setText(u.getPosition());
			h.department.setText(u.getDepartment());
			h.callButton.setOnClickListener(MainContactActivity.this);
			h.callButton.setTag(u);
			h.attentionButton.setOnClickListener(MainContactActivity.this);
			h.attentionButton.setTag(u);
			return convertView;
		}
		
		class Viewhodler {
			ImageView avatar;
			TextView name;
			TextView position;
			TextView department;
			
			Button attentionButton;
			ImageView callButton;
			
		}
		
	}
	
}
