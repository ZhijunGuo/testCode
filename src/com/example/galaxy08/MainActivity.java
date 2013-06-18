package com.example.galaxy08;

import java.util.ArrayList;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.example.galaxy08.adapter.InvitesListAdapter;
import com.example.galaxy08.app.PreferenceWrapper;
import com.example.galaxy08.dao.GalaxyProviderInfo;
import com.example.galaxy08.dao.UserColumns;
import com.example.galaxy08.entity.Invite;
import com.example.galaxy08.entity.User;
import com.example.galaxy08.http.BaseJsonHandler;
import com.example.galaxy08.http.GalaxyApi;
import com.example.galaxy08.http.model.BaseResponse;
import com.example.galaxy08.http.response.FriendsResponse;
import com.example.galaxy08.http.response.InvitationResponse;
import com.example.galaxy08.tool.ToastUtil;

public class MainActivity extends TabActivity implements OnClickListener {

	private String TAB_MESSAGE = "tab_message";
	private String TAB_NEWS = "tab_news";
	private String TAB_CONTACT = "tab_contact";
	private String TAB_MORE = "tab_more";

	private TabHost mainHost;

	private Intent messageIntent;
	private Intent newsIntent;
	private Intent contactIntent;
	private Intent moreIntent;

	private RadioButton messageButton;
	private RadioButton newsButton;
	private RadioButton contactButton;
	private RadioButton moreButton;

	private ListView infoList;
	private TextView infoTitleText;

	private Dialog infoWindow;

	private Builder temp;

	private InvitesListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		mainHost = getTabHost();

		messageIntent = new Intent(this, MainMessageActivity.class);
		newsIntent = new Intent(this, MainFeedActivity.class);
		contactIntent = new Intent(this, MainContactActivity.class);
		
		moreIntent = new Intent(this, MainMoreActivity.class);
		initRadios();
		setupIntent();
		mainHost.setCurrentTabByTag(TAB_MESSAGE);
		initInfoWindow();
	}

	private void initInfoWindow() {
		// infoWindow
		temp = new Builder(this);
		View view = LayoutInflater.from(this).inflate(R.layout.info_window,
				null);
		infoList = (ListView) view.findViewById(R.id.info_window_list);
		infoTitleText = (TextView) view.findViewById(R.id.info_window_title);
		temp.setView(view);

		view.findViewById(R.id.info_window_ok).setOnClickListener(this);
		view.findViewById(R.id.info_window_cancel).setOnClickListener(this);
		/**
		 * 获取该用户的所有邀请
		 */
		GalaxyApi.getInvitations(
				PreferenceWrapper.get(PreferenceWrapper.MOBILE, "0"),
				PreferenceWrapper.get(PreferenceWrapper.TOKEN, "0"),
				new BaseJsonHandler<InvitationResponse>() {
					@Override
					public void onStatusOk(InvitationResponse response) {
						if (response.getInvites() != null
								&& response.getInvites().size() > 0) {
							adapter = new InvitesListAdapter(MainActivity.this,
									response.getInvites());
							infoList.setAdapter(adapter);
							infoTitleText.setText("公司邀请("
									+ response.getInvites().size() + ")");
							infoWindow = temp.show();
						}

					}

					@Override
					public void onStatusFail(InvitationResponse response) {
						super.onStatusFail(response);
						ToastUtil.showMessage(MainActivity.this,
								response.getMessage());
					}
				});
		/**
		 * 获取该用户的通讯录
		 */
		GalaxyApi.getFriends(new BaseJsonHandler<FriendsResponse>() {

			@Override
			public void onStatusOk(FriendsResponse response) {
				ArrayList<Long> deletes = response.getDeletes();
				if(deletes!=null&&deletes.size()>0){
					//开启线程 删除数据库中 指定id的联系人
					SysApplication.application.threadPool
					.submit(new DeleteThread(deletes));
				}
				ArrayList<User> users = response.getFriends();
				if(users!=null&&users.size()>0){
					//开启线程  增加联系人到本地数据库
					/**
					 * 1  返回的数据是全部联系人，还是新增联系人？
					 * 2 如果联系人的信息发生改变，怎么办？
					 */
				}
			}
			
			@Override
			public void onStatusFail(FriendsResponse response) {
				super.onStatusFail(response);
				ToastUtil.showMessage(MainActivity.this, response.getMessage());
			}
		});
		
		/**
		 * 获取离线消息
		 */
		//GalaxyApi.obtainOfflineMessage(, handler);
		
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private void initRadios() {
		messageButton = ((RadioButton) findViewById(R.id.radio_message));
		messageButton.setOnClickListener(this);
		newsButton = ((RadioButton) findViewById(R.id.radio_news));
		newsButton.setOnClickListener(this);
		//taskButton = ((RadioButton) findViewById(R.id.radio_task));
		contactButton = ((RadioButton) findViewById(R.id.radio_contact));
		contactButton.setOnClickListener(this);
		//taskButton.setOnClickListener(this);
		moreButton = ((RadioButton) findViewById(R.id.radio_more));
		moreButton.setOnClickListener(this);
	}

	private void setupIntent() {

		mainHost.addTab(buildTabSpec(TAB_MESSAGE, R.string.main_message,
				R.drawable.message_unselect, messageIntent));
		mainHost.addTab(buildTabSpec(TAB_NEWS, R.string.main_news,
				R.drawable.news_unselect, newsIntent));
		mainHost.addTab(buildTabSpec(TAB_CONTACT, R.string.main_contact,
				R.drawable.tab_address_normal, contactIntent));
		mainHost.addTab(buildTabSpec(TAB_MORE, R.string.main_more,
				R.drawable.more_unselect, moreIntent));
	}

	private TabSpec buildTabSpec(String tag, int resLabel, int resIcon,
			final Intent content) {
		return mainHost
				.newTabSpec(tag)
				.setIndicator(getString(resLabel),
						getResources().getDrawable(resIcon))
				.setContent(content);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.radio_message:
			mainHost.setCurrentTabByTag(TAB_MESSAGE);
			break;
		case R.id.radio_news:
			mainHost.setCurrentTabByTag(TAB_NEWS);
			break;
		case R.id.radio_contact:
			mainHost.setCurrentTabByTag(TAB_CONTACT);
			break;
		case R.id.radio_more:
			mainHost.setCurrentTabByTag(TAB_MORE);
			break;
		case R.id.info_window_ok:
			// 用户点击了 接受邀请
			joinCompany();
			// infoWindow.
			break;
		case R.id.info_window_cancel:
			// 用户点击了 忽略邀请
			infoWindow.dismiss();
			break;

		default:
			break;
		}
	}

	private void joinCompany() {

		boolean isNoneChecked = true;

		for (Invite in : adapter.getInvites()) {
			if (in.isChecked()) {
				GalaxyApi.joinCompany(
						PreferenceWrapper.get(PreferenceWrapper.MOBILE, "0"),
						in.getCid(), new BaseJsonHandler<BaseResponse>() {

							@Override
							public void onStatusOk(BaseResponse response) {
								infoWindow.dismiss();
								ToastUtil
										.showMessage(MainActivity.this, "加入成功");
							}

							public void onStatusFail(BaseResponse response) {
								infoWindow.dismiss();
								ToastUtil.showMessage(MainActivity.this,
										response.getMessage());
							};
						});
				isNoneChecked = false;
			}
		}
		if (isNoneChecked) {
			Toast.makeText(this, "没有选中公司", Toast.LENGTH_SHORT).show();
		} else {
			infoWindow.dismiss();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		SysApplication.application.threadPool.shutdown();
	}
	
	class DeleteThread extends Thread{
		ArrayList<Long> deletes;
		public DeleteThread(ArrayList<Long> deletes){
			this.deletes = deletes;
		}
		@Override
		public void run() {
			ContentResolver cr = MainActivity.this.getContentResolver();
			if(this.deletes!=null&&this.deletes.size()>0){
				for(int i = 0;i<this.deletes.size();i++){
					cr.delete(GalaxyProviderInfo.USER_URI, UserColumns.USER_ID, 
							new String[]{String.valueOf(this.deletes.get(i))});
				}
			}
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(1, 0, 1, "关于");
		menu.add(1, 0, 2, "退出");
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case 1:
			Toast.makeText(this, "关于", Toast.LENGTH_SHORT).show();
			break;
		case 2:
			Toast.makeText(this, "退出", Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
