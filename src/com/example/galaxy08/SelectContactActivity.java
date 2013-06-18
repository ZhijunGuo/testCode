package com.example.galaxy08;

import java.util.ArrayList;
import java.util.List;

import com.example.galaxy08.adapter.SelectContactMainListAdapter;
import com.example.galaxy08.app.PreferenceWrapper;
import com.example.galaxy08.chat.ChatActivity;
import com.example.galaxy08.dao.DepartmentColumns;
import com.example.galaxy08.dao.GalaxyProviderInfo;
import com.example.galaxy08.entity.Department;
import com.example.galaxy08.entity.Departments;
import com.example.galaxy08.entity.SelectUser;
import com.example.galaxy08.entity.User;
import com.example.galaxy08.entity.Users;
import com.example.galaxy08.tool.ToastUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 选择联系人 activity
 * 
 * 选择联系人-->创建临时会话组 
 */
public class SelectContactActivity extends Activity implements OnClickListener,
OnCheckedChangeListener{

	public static final String FIRST_CONTACT = "first_contact";

	//群组会话第一个参与者(不包含当前用户)
	private User firstUser;	

	private ExpandableListView contactList;

	private Gallery contactGallery;

	private ContactAdapter contactAdapter;

	private List<List<SelectUser>> contacts;

	private SelectContactMainListAdapter mainAdapter;

	//private String[] dementNames;
	//private ArrayList<String> dementNames;
	private ArrayList<Department> ds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.select_contact);
		firstUser = (User)getIntent().getSerializableExtra(FIRST_CONTACT);
		setupView();
		addListener();
	}

	@SuppressWarnings("deprecation")
	private void setupView(){
		contactList = (ExpandableListView)findViewById(R.id.select_contact_list);

		int width = getWindowManager().getDefaultDisplay().getWidth();
		contactList.setIndicatorBounds(width-40, width-10);

		contactAdapter = new ContactAdapter(this, null);
		contactGallery = (Gallery)findViewById(R.id.select_contact_gallery);//.setAdapter(contactAdapter);
		contactGallery.setAdapter(contactAdapter);
	}

	private void addListener(){
		findViewById(R.id.select_contact_back_btn).setOnClickListener(this);
		findViewById(R.id.select_contact_true_btn).setOnClickListener(this);
		List<List<SelectUser>> su = getData();
		mainAdapter = new SelectContactMainListAdapter(this, su,this,ds);
		contactList.setAdapter(mainAdapter);
	}

	private List<List<SelectUser>> getData(){

		contacts = new ArrayList<List<SelectUser>>();
		/**
		 * 查询数据库
		 * 1 查询部门信息 列出部门
		 * 2 查询员工信息 将员工归入部门中
		 */
		//step 1 查询部门信息
		ds = Departments.getDepartMentInfoDB();
		//step 2 查询用户信息
		ArrayList<User> us = Users.getUserInfoFromDB();

		for(Department d:ds){
			//dementNames[i++] = d.getDepartment_name();
			ArrayList<SelectUser> tempData = new ArrayList<SelectUser>(); 
			for(User u:us){
				if(u.getDepartment().equals(d.getDepartment_name())){
					//us.remove(u);
					SelectUser su = new SelectUser();
					su.setContact(u);
					su.setSelect(false);
					tempData.add(su);
					Log.i("user", "u:"+u.getDepartment()+" :"+u.getUser_name());
				}
			}
			contacts.add(tempData);
		}
		return contacts;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.select_contact_main_list_item_check:
			SelectUser user = (SelectUser)v.getTag();
			CheckBox check = (CheckBox)v;
			user.setSelect(check.isChecked());
			if(check.isChecked()){
				contactAdapter.addUser(user);
				contactGallery.setSelection(contactAdapter.getCount()-1);
			}else{
				contactAdapter.removeUser(user);
			}

			break;
		
		case R.id.select_contact_back_btn:
			this.finish();
			return;
		case R.id.select_contact_true_btn:
			//获取选中的联系人
			ArrayList<SelectUser> su = contactAdapter.getContacts();
			ArrayList<User> us = new ArrayList<User>();
			for(SelectUser u:su){
				us.add(u.getContact());
			}
			if(us.size()>1){
				Toast.makeText(this, "暂时不支持群聊", Toast.LENGTH_SHORT).show();
				return;
//				View view = LayoutInflater.from(this).inflate(R.layout.set_chat_name, null);
//				EditText nameEdit = (EditText)view.findViewById(R.id.set_chat_name_edit);
//				SetChatNameListener l = new SetChatNameListener(nameEdit,us);
//				new AlertDialog.Builder(this).setView(view)
//				.setPositiveButton(getString(R.string.sure), l)
//				.setNegativeButton(getString(R.string.default_name),l)
//				.show();
			}else{
				//选中的联系人之后 ， 创建和目标联系人的会话
				Intent intent = new Intent(this,ChatActivity.class);
				intent.putExtra(ChatActivity.TARGET_USER, us);
				intent.putExtra(ChatActivity.USER_COUNT, us.size());
				intent.putExtra(ChatActivity.CHAT_NAME, us.get(0).getUser_name());
				startActivity(intent);
				this.finish();
			}
			break;

		case R.id.select_contact_gallery_item_del:
			SelectUser contact = (SelectUser)v.getTag();
			contact.setSelect(false);
			mainAdapter.notifyDataSetChanged();
			contactAdapter.removeUser(contact);

		default:
			break;
		}
	}

	class SetChatNameListener implements android.content.DialogInterface.OnClickListener{

		private EditText nameEdit;

		private String name;

		private ArrayList<User> us;


		public SetChatNameListener(EditText nameEdit, ArrayList<User> us) {
			this.nameEdit = nameEdit;
			this.us = us;
		}

		public String getName() {
			return name;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {

			//Intent intent = new
			//选中的联系人之后 ， 创建和目标联系人的会话
			Intent intent = new Intent(SelectContactActivity.this,ChatActivity.class);

			switch (which) {
			case AlertDialog.BUTTON_POSITIVE:
				//确定使用名称
				name = nameEdit.getText().toString().trim();
				break;
			case AlertDialog.BUTTON_NEGATIVE:
				//使用默认名称 类似  "群组5" "群组12" 等等
				int num = PreferenceWrapper.get(PreferenceWrapper.GROUP_NUM, 0);
				name = getString(R.string.group)+num;
				num++;
				PreferenceWrapper.put(PreferenceWrapper.GROUP_NUM, num);
				PreferenceWrapper.commit();
				break;

			default:
				break;
			}
			intent.putExtra(ChatActivity.TARGET_USER, us);
			intent.putExtra(ChatActivity.USER_COUNT, us.size());
			intent.putExtra(ChatActivity.CHAT_NAME, name);
			startActivity(intent);
			SelectContactActivity.this.finish();
		}

	}

	class ContactAdapter extends BaseAdapter{

		private ArrayList<SelectUser> contacts;

		private LayoutInflater inflater;

		public ContactAdapter(Context context,ArrayList<SelectUser> contacts){
			inflater = LayoutInflater.from(context);
			setContacts(contacts);
		}

		public void setContacts(ArrayList<SelectUser> contacts) {
			if(contacts!=null)
				this.contacts = contacts;
			else
				this.contacts = new ArrayList<SelectUser>();
		}

		public ArrayList<SelectUser> getContacts() {
			return contacts;
		}

		@Override
		public int getCount() {
			return contacts.size();
		}

		@Override
		public Object getItem(int position) {
			return contacts.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public void addUser(SelectUser u){
			this.contacts.add(u);
			this.notifyDataSetChanged();
		}

		public void removeUser(SelectUser u){
			this.contacts.remove(u);
			this.notifyDataSetChanged();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHodler h = null;
			SelectUser item = contacts.get(position);
			User contact = item.getContact();
			if(convertView==null){
				h = new ViewHodler();
				convertView = inflater.inflate(R.layout.select_contact_gallery_item, null);
				h.image = (ImageView)convertView.findViewById(R.id.select_contact_gallery_item_image);
				h.name = (TextView)convertView.findViewById(R.id.select_contact_gallery_item_name);
				h.delBtn = (ImageView)convertView.findViewById(R.id.select_contact_gallery_item_del);
				convertView.setTag(h);
			}else{
				h = (ViewHodler)convertView.getTag();
			}
			if(h.name!=null){
				h.name.setText(contact.getUser_name());
			}
			h.delBtn.setTag(item);
			h.delBtn.setOnClickListener(SelectContactActivity.this);
			return convertView;
		}

		class ViewHodler {
			ImageView image;
			TextView name;
			ImageView delBtn;
		}

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		SelectUser contact = (SelectUser)buttonView.getTag();
		contact.setSelect(isChecked);
		if(isChecked){
			contactAdapter.addUser(contact);
			contactGallery.setSelection(contactAdapter.getCount()-1);
		}else{
			contactAdapter.removeUser(contact);
		}
	}
}
