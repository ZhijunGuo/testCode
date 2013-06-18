package com.example.galaxy08;

import java.util.ArrayList;

import com.example.galaxy08.app.PreferenceWrapper;
import com.example.galaxy08.entity.User;
import com.example.galaxy08.entity.Users;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainMoreActivity extends Activity 
implements OnClickListener{
	
	private TextView userName;
	
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_more);
		setupView();
	};
	
	private void setupView(){
		findViewById(R.id.more_my_name).setOnClickListener(this);
		findViewById(R.id.more_logout).setOnClickListener(this);
		userName = (TextView)findViewById(R.id.main_more_my_name);
		userName.setText(PreferenceWrapper.get(PreferenceWrapper.USER_NAME, ""));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.more_my_name:
			User u = null;
			ArrayList<User> us = Users.getUserInfoFromDB(PreferenceWrapper.get(PreferenceWrapper.USER_ID, ""));
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
			break;
		case R.id.more_logout:
			PreferenceWrapper.put(PreferenceWrapper.AUTOLOGIN, false);
			PreferenceWrapper.commit();
			this.finish();
			break;
		default:
			break;
		}
	}

}
