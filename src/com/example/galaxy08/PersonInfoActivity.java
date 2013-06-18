package com.example.galaxy08;

import com.example.galaxy08.app.PreferenceWrapper;
import com.example.galaxy08.entity.User;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PersonInfoActivity extends Activity implements OnClickListener{
	
	//传递 User使用
	public static final String TARGET_USER = "target_user";
	
	//目标用户
	private User targetUser;
	//UI的各部分
	private ImageView avatar;//头像
	
	private TextView nameText;//姓名
	private TextView positionText;//职位
	private TextView companyText;//公司
	
	//显示手机号码的父窗体
	private LinearLayout mobileParent;
	
	private TextView mobile;//手机号码
	private TextView address;//地址
	private TextView email;//电子邮件
	//最近的动态
	private TextView feed;
	
	//发短信 动作
	private TextView smsText;
	
	//顶部左边的button 右边的button
	private Button leftButton;
	private Button rightButton;
	
	//标题
	private TextView titleText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.person_info);
		//获取传递过来的数据
		targetUser = getIntent().getBundleExtra("data").getParcelable(TARGET_USER);
		setupView();
		addListener();
		setContent();
	}
	/**
	 * 组建View
	 */
	private void setupView() {
		
		//左右两边的button
		leftButton = (Button)findViewById(R.id.person_info_top_left_button);
		rightButton = (Button)findViewById(R.id.person_info_top_right_button);
		
		nameText = (TextView)findViewById(R.id.person_info_name);
		positionText = (TextView)findViewById(R.id.person_info_position);
		
		mobileParent = (LinearLayout)findViewById(R.id.person_info_mobile_parent);
		
		mobile = (TextView)findViewById(R.id.person_info_mobile);
		address = (TextView)findViewById(R.id.person_info_address);
		email = (TextView)findViewById(R.id.person_info_email);
		companyText = (TextView)findViewById(R.id.person_info_company);
		smsText = (TextView)findViewById(R.id.person_info_sms);
		
		titleText = (TextView)findViewById(R.id.person_info_title);
		
		avatar = (ImageView)findViewById(R.id.person_info_avatar);
		
		feed = (TextView)findViewById(R.id.person_info_feed);
	}
	/**
	 * 增加监听器
	 */
	private void addListener() {
		mobile.setOnClickListener(this);
		smsText.setOnClickListener(this);
		leftButton.setOnClickListener(this);
		rightButton.setOnClickListener(this);
	}
	/**
	 * 设置内容
	 */
	private void setContent() {
		//标题 1 是自己的资料 显示："我的信息" 2 别的资料 "xxx信息"
		String userId = PreferenceWrapper.get(PreferenceWrapper.USER_ID, "");
		if(targetUser.getUser_id().equals(userId)){
			titleText.setText(getString(R.string.my_info));
		}else{
			titleText.setText(targetUser.getUser_name()+getString(R.string.other_info));
			rightButton.setVisibility(View.GONE);
		}
		
		//头像
		if(targetUser.getImg_path()!=null){
			//下载并设置头像
		}
		//姓名
		if(targetUser.getUser_name()!=null){
			nameText.setText(targetUser.getUser_name());
		}
		//职位
		if(targetUser.getPosition()!=null){
			positionText.setText(targetUser.getPosition());
		}
		//公司
		if(targetUser.getCompany()!=null){
			companyText.setText(targetUser.getCompany());
		}
		//email
		if(targetUser.getEmail()!=null){
			email.setText(targetUser.getEmail());
		}else{
			email.setVisibility(View.GONE);
		}
		//地址
		if(targetUser.getAddress()!=null){
			address.setText(targetUser.getAddress());
		}
		//电话
		if(targetUser.getMobile()!=null){
			mobile.setText(targetUser.getMobile());
		}else{
			mobileParent.setVisibility(View.GONE);
		}
	}
	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.person_info_mobile:
			//呼叫该号码
			Uri uri = Uri.parse("tel:"+targetUser.getMobile());
			intent = new Intent(Intent.ACTION_DIAL, uri);
			startActivity(intent);
			break;
		case R.id.person_info_sms:
			//给该号码发送短信
			intent = new Intent(Intent.ACTION_VIEW);
			intent.putExtra("address", targetUser.getMobile());
			intent.putExtra("sms_body", targetUser.getUser_name()+",");
			intent.setType("vnd.android-dir/mms-sms");
			//Log.i("test", targetUser.getMobile());
			startActivity(intent);
			break;
			
		case R.id.person_info_top_left_button:
			this.finish();
			break;
		case R.id.person_info_top_right_button:
			//编辑自己的资料？？
			Toast.makeText(this, "编辑自己的资料", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}
}
