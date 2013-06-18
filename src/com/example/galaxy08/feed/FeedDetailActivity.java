package com.example.galaxy08.feed;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.galaxy08.PersonInfoActivity;
import com.example.galaxy08.R;
import com.example.galaxy08.adapter.FeedDetailAdapter;
import com.example.galaxy08.app.PreferenceWrapper;
import com.example.galaxy08.entity.Feed;
import com.example.galaxy08.entity.FeedSession;
import com.example.galaxy08.entity.FeedDetailItem;
import com.example.galaxy08.entity.Feeds;
import com.example.galaxy08.entity.User;
import com.example.galaxy08.entity.Users;

public class FeedDetailActivity extends Activity implements OnClickListener{
	
	private ListView newContentList;
	
	//private ArrayList<Feed> feeds;
	
	private FeedDetailAdapter adapter;
	
	public static final String TRAGET_FEESSESSION = "target_feedsession";
	
	private FeedSession feedSession;
	
	private TextView title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.feed_detail);
		feedSession = getIntent().getBundleExtra("data").getParcelable(TRAGET_FEESSESSION);
		setUpView();
		//getData();
		adapter = new FeedDetailAdapter(this, null,this);
		newContentList.setAdapter(adapter);
	}
	
	private void setUpView(){
		newContentList = (ListView)findViewById(R.id.new_content_list);
		findViewById(R.id.feed_detail_left_button).setOnClickListener(this);
		findViewById(R.id.feed_detail_right_button).setOnClickListener(this);
		title = (TextView)findViewById(R.id.feed_detail_title);
		title.setText(feedSession.getFeedsession_name());
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		ArrayList<Feed> targetFs = Feeds.getFeedInfoDB(feedSession.getFeedsession_id(),
				PreferenceWrapper.get(PreferenceWrapper.USER_ID, ""));
		adapter.setDataAndNotify(targetFs);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.feed_detail_left_button:
			this.finish();
			break;
		case R.id.feed_detail_right_button:
			Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
			break;

		case R.id.feed_item_avatar:
			Feed f = (Feed)v.getTag();
			User u = null;
			ArrayList<User> us = Users.getUserInfoFromDB(f.getPublisher_id());
			if(us!=null&&us.size()>0){
				u = us.get(0);
			}else{
				//联网获取用户信息
			}
			Intent intent = new Intent(this,PersonInfoActivity.class);
			Bundle bundle = new Bundle();
			bundle.putParcelable(PersonInfoActivity.TARGET_USER, u);
			intent.putExtra("data", bundle);
			startActivity(intent);
			
		default:
			break;
		}
	}
}
