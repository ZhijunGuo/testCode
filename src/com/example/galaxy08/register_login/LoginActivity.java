package com.example.galaxy08.register_login;


import com.example.galaxy08.MainActivity;
import com.example.galaxy08.R;
import com.example.galaxy08.app.PreferenceWrapper;
import com.example.galaxy08.http.BaseJsonHandler;
import com.example.galaxy08.http.GalaxyApi;
import com.example.galaxy08.http.MD5Util;
import com.example.galaxy08.http.model.GalaxyResponse;
import com.example.galaxy08.http.response.LoginAndRegisterResponse;
import com.example.galaxy08.tool.ToastUtil;
import com.example.galaxy08.util.StringUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener,
OnCheckedChangeListener{
	
	private EditText companyAccountEdit;
	private EditText phoneNumberEdit;
	private EditText passwordEdit;
	private Button loginButton;
	private Button activateButton;
	private CheckBox autoLoginCheckBox;
	private ProgressDialog progressDlg;
	
	private String mobile;
	private String companyAccount;
	
	public static final int FOR_REGISTER = 0x1001;
	
	private boolean isNeedQuit = true;

	//记录是否 自动登录
	private boolean isAutoLogin = false;
	
	private void tranToMain(){
		this.finish();
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		isNeedQuit = false;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.user_login);
		//首先判断是不是需要自动登录
		if(PreferenceWrapper.get(PreferenceWrapper.AUTOLOGIN, false)){
			//进行登录
			companyAccount = PreferenceWrapper.get(PreferenceWrapper.COMPANYACCOUNT, "");
			mobile = PreferenceWrapper.get(PreferenceWrapper.MOBILE, "");
			String md5Passwd = PreferenceWrapper.get(PreferenceWrapper.MD5_PASSWORD, "");
			login(companyAccount,mobile,md5Passwd,true);
		}
		setupView();
		addListener();
		setContent();
	}

	private void setupView(){
		companyAccountEdit = (EditText)findViewById(R.id.user_login_company_account_edit);
		phoneNumberEdit = (EditText)findViewById(R.id.user_login_phone_number_edit);
		passwordEdit = (EditText)findViewById(R.id.user_login_password_edit);
		activateButton = (Button)findViewById(R.id.user_login_activate_button);
		loginButton = (Button)findViewById(R.id.user_login_button);
		autoLoginCheckBox = (CheckBox)findViewById(R.id.user_login_auto_login_checkbox);
		progressDlg = ProgressDialog.show(this, "登陆", "...");
		progressDlg.setContentView(R.layout.progress);
		progressDlg.setCanceledOnTouchOutside(true);
		progressDlg.dismiss();
	}

	private void addListener(){
		activateButton.setOnClickListener(this);
		loginButton.setOnClickListener(this);
		autoLoginCheckBox.setOnCheckedChangeListener(this);
	}
	
	private void setContent() {
		boolean flag = true;
		companyAccount = PreferenceWrapper.get(PreferenceWrapper.COMPANYACCOUNT, "");
		mobile = PreferenceWrapper.get(PreferenceWrapper.MOBILE, "");
		if(!StringUtils.isNull(mobile)){
			phoneNumberEdit.setText(mobile);
		}else{
			phoneNumberEdit.requestFocus();
			flag = false;
		}
		if(!StringUtils.isNull(companyAccount)){
			companyAccountEdit.setText(companyAccount);
		}else{
			companyAccountEdit.requestFocus();
			flag = false;
		}
		if(flag){
			passwordEdit.requestFocus();
		}
	}

	/*
	 * 处理启动Activity返回的情况
	 * （非 Javadoc）
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case FOR_REGISTER:
			if(resultCode==RESULT_OK){
				tranToMain();
			}
			return;

		default:
			break;
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.user_login_activate_button:
			Intent intent = new Intent(this, RegisterActivity.class);
			startActivityForResult(intent, FOR_REGISTER);
			return;
		case R.id.user_login_button:
			
			companyAccount = companyAccountEdit.getText().toString().trim();
			mobile = phoneNumberEdit.getText().toString().trim();
			String passWord = passwordEdit.getText().toString().trim();
			if(!isValid(companyAccount)||!isValid(mobile)||!isValid(passWord)){
				Toast.makeText(this, getString(R.string.no_null), Toast.LENGTH_SHORT).show();
				return;
			}
			
			Log.i("login", "公司账号:"+companyAccount+" 手机:"+mobile+" 密码:"+passWord);
			//Toast.makeText(this,"login", "公司账号:"+companyAccount+" 手机:"+mobile+" 密码:"+passWord,Toast.LENGTH_LONG).show();
			
			if(mobile.equals("954017")||mobile.equals("954018")){
				PreferenceWrapper.put(PreferenceWrapper.USER_ID, mobile);
				PreferenceWrapper.put("token", "748826f0221e111f70c439b4a7755cf0");
				PreferenceWrapper.commit();
			}
			final String md5Passwd = MD5Util.md5(passWord);
			login(companyAccount,mobile,md5Passwd,false);
			break;

		default:
			break;
		}
	}

	private boolean isValid(String target){
		if(target!=null&&!"".equals(target))
			return true;
		else
			return false;
	}
	
	class onSetPasswdButton implements android.content.DialogInterface.OnClickListener{

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case AlertDialog.BUTTON_POSITIVE:
				Intent intent = new Intent(LoginActivity.this,RegisterActivity2.class);
				intent.putExtra(RegisterActivity2.TARGET_COMPANY_ACCOUNT, companyAccount);
				intent.putExtra(RegisterActivity2.TARGET_MOBILE, mobile);
				startActivityForResult(intent,FOR_REGISTER);
				break;
			case AlertDialog.BUTTON_NEGATIVE:
				LoginActivity.this.finish();
				break;

			default:
				break;
			}
		}
		
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		isAutoLogin = isChecked;
	}
	
	private void login(final String companyAccount,final String mobile,final String passwd,final boolean auto){
		progressDlg.show();
		GalaxyApi.userLogin(companyAccount,mobile, passwd, new BaseJsonHandler<LoginAndRegisterResponse>() {

			@Override
			public void onStatusOk(LoginAndRegisterResponse response) {
				PreferenceWrapper.put(PreferenceWrapper.TOKEN, response.getToken());
				if(!auto){
					PreferenceWrapper.put(PreferenceWrapper.USER_ID, String.valueOf(response.getUserid()));
					PreferenceWrapper.put(PreferenceWrapper.COMPANYACCOUNT, companyAccount);
					PreferenceWrapper.put(PreferenceWrapper.MOBILE, mobile);
					PreferenceWrapper.put(PreferenceWrapper.MD5_PASSWORD, passwd);
					PreferenceWrapper.put(PreferenceWrapper.AUTOLOGIN, isAutoLogin);
					PreferenceWrapper.put(PreferenceWrapper.USER_NAME, "智军");
				}
				PreferenceWrapper.commit();
				progressDlg.dismiss();
				tranToMain();
			}
			
			@Override
			public void onStatusFail(LoginAndRegisterResponse response) {
				super.onStatusFail(response);
				progressDlg.dismiss();
				ToastUtil.showMessage(LoginActivity.this, response.getMessage());
				if(response.getStatus()==8&&response.getUserStatus()==2){
					new AlertDialog.Builder(LoginActivity.this).setTitle(getString(R.string.no_password))
					.setMessage(getString(R.string.must_set_password)).setPositiveButton("确定", new onSetPasswdButton())
					.setNegativeButton("取消", new onSetPasswdButton()).show();
				}
			}
		});
	}
	
}
