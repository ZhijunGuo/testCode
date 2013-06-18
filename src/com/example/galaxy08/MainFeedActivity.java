package com.example.galaxy08;

import java.util.ArrayList;

import com.example.galaxy08.adapter.FeedAdapter;
import com.example.galaxy08.app.PreferenceWrapper;
import com.example.galaxy08.dao.GalaxyProviderInfo;
import com.example.galaxy08.entity.FeedSession;
import com.example.galaxy08.entity.FeedSessions;
import com.example.galaxy08.feed.FeedDetailActivity;
import com.example.galaxy08.feed.FeedPublishActivity;
import com.example.galaxy08.tool.Tool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainFeedActivity extends Activity implements OnItemClickListener,
OnClickListener{
	
	private ListView feedsessionsList;
	
	private FeedAdapter adapter;
	
	//private ArrayList<FeedSession> feedsessions;
	
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_feed);
		//插入假数据
		//insertFeedSessionDataDB();
		setUpView();
		adapter = new FeedAdapter(this, null);
		feedsessionsList.setAdapter(adapter);
		feedsessionsList.setOnItemClickListener(this);
		addListener();
	};
	
	@Override
	protected void onResume() {
		super.onResume();
		adapter.setFeedsAndNotify(FeedSessions.getFeedSessionInfoDB());
	}
	
	private void setUpView(){
		feedsessionsList = (ListView)findViewById(R.id.news_list);
	}
	
	private void addListener(){
		findViewById(R.id.main_feed_top_right_button).setOnClickListener(this);
		findViewById(R.id.main_feed_top_left_button).setOnClickListener(this);
	}
	
//	private ArrayList<FeedSession> getData(){
//		ArrayList<FeedSession> feedsessions = new ArrayList<FeedSession>();
//		FeedSession n1 = new FeedSession("销售组",null,20,"聚餐，按时参加");
//		n1.setSystemImage(R.drawable.group);
//		FeedSession n2 = new FeedSession("公司动态",null,7,"聚餐，按时参加");
//		n1.setSystemImage(R.drawable.company_feedsessions);
//		FeedSession n3 = new FeedSession("我关注的人",null,1,"聚餐，按时参加");
//		n1.setSystemImage(R.drawable.attention);
//		feedsessions.add(n1);
//		feedsessions.add(n2);
//		feedsessions.add(n3);
//		return feedsessions;
//	}
	
	/**
	 * 制造假数据 插入到数据库中
	 */
	
	private void insertFeedSessionDataDB(){
		String userid = PreferenceWrapper.get(PreferenceWrapper.USER_ID, "");
		FeedSession f1 = new FeedSession();
		f1.setFeedsession_name("公司动态");
		f1.setFeedsession_id("1001");
		f1.setLastUpdate(Tool.nowTime());
		f1.setUser_id(userid);
		getContentResolver().insert(GalaxyProviderInfo.FEEDSESSION_URI, f1.getContentValues());
		FeedSession f2 = new FeedSession();
		f2.setFeedsession_name("高级管理层分享");
		f2.setFeedsession_id("1002");
		f2.setLastUpdate(Tool.nowTime());
		f2.setUser_id(userid);
		getContentResolver().insert(GalaxyProviderInfo.FEEDSESSION_URI, f2.getContentValues());
		FeedSession f3 = new FeedSession();
		f3.setFeedsession_name("销售策略");
		f3.setFeedsession_id("1003");
		f3.setLastUpdate(Tool.nowTime());
		f3.setUser_id(userid);
		getContentResolver().insert(GalaxyProviderInfo.FEEDSESSION_URI, f3.getContentValues());
		
	}
	
//	private void getFeedSessionInfoDB(){
//		
//	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
		FeedSession feedsession = (FeedSession)adapter.getItem(position);
		Intent intent = new Intent(this, FeedDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putParcelable(FeedDetailActivity.TRAGET_FEESSESSION, feedsession);
		//bundle.putSerializable(FeedDetailActivity.TRAGET_FEESSESSION, feedsession);
		//intent.putExtra(FeedDetailActivity.TRAGET_FEESSESSION, feedsession);
		intent.putExtra("data", bundle);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_feed_top_left_button:
			
			break;
		case R.id.main_feed_top_right_button:
			Intent intent = new Intent(this, FeedPublishActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

}







