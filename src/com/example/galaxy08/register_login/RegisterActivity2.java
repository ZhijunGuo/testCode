package com.example.galaxy08.register_login;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView.CommaTokenizer;

import com.example.galaxy08.R;
import com.example.galaxy08.app.PreferenceWrapper;
import com.example.galaxy08.http.BaseJsonHandler;
import com.example.galaxy08.http.GalaxyApi;
import com.example.galaxy08.http.response.LoginAndRegisterResponse;
import com.example.galaxy08.tool.ToastUtil;
import com.example.galaxy08.util.StringUtils;

public class RegisterActivity2 extends Activity implements OnClickListener{
	//返回按键
	private Button back;
	//设置密码Edit
	private EditText setPasswdEditText;
	//确认密码
	private EditText confirmPasswdEditText;
	//开始使用按钮
	private Button startButton;

	private String mobileNumber;
	private String companyAccount;
	
	private String passwd;
	//注册动作，服务器的返回

	public static final String TARGET_SECURITY_CODE = "target_security_code";
	public static final String TARGET_MOBILE = "target_mobile";
	public static final String TARGET_COMPANY_ACCOUNT = "target_company_account";

	private Dialog waitForRegisterDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activate3);
		mobileNumber = getIntent().getStringExtra(TARGET_MOBILE);
		companyAccount = getIntent().getStringExtra(TARGET_COMPANY_ACCOUNT);
		setupView();
		addListener();
	}

	private void setupView(){
		back = (Button)findViewById(R.id.activate3_back_button);
		startButton = (Button)findViewById(R.id.activate3_start_button);
		setPasswdEditText = (EditText)findViewById(R.id.activate3_set_passwd_edit);
		confirmPasswdEditText = (EditText)findViewById(R.id.activate3_confirm_passwd_edit);
		waitForRegisterDialog = new ProgressDialog(this);
		waitForRegisterDialog.setTitle(getString(R.string.wait_for_register));
		waitForRegisterDialog.setCancelable(false);
	}

	private void addListener(){
		back.setOnClickListener(this);
		startButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.activate3_back_button:
			setResult(RESULT_CANCELED);
			this.finish();
			return;
		case R.id.activate3_start_button:
			String passwd1 = setPasswdEditText.getText().toString().trim();
			String passwd2 = confirmPasswdEditText.getText().toString().trim();
			if(StringUtils.isNull(passwd1)||StringUtils.isNull(passwd2)){
				ToastUtil.showMessage(this, getString(R.string.no_null));
			}
			if(!passwd1.equals(passwd2)){
				ToastUtil.showMessage(this, getString(R.string.different));
				return;
			}
			passwd = passwd1;
			
			GalaxyApi.setPasswd(companyAccount,mobileNumber,
					PreferenceWrapper.get(PreferenceWrapper.TOKEN, "")
					, passwd,new BaseJsonHandler<LoginAndRegisterResponse>() {

				@Override
				public void onStatusOk(LoginAndRegisterResponse response) {
					ToastUtil.showMessage(RegisterActivity2.this, "密码设置成功");
					PreferenceWrapper.put(PreferenceWrapper.MOBILE, mobileNumber);
					PreferenceWrapper.put(PreferenceWrapper.COMPANYACCOUNT, companyAccount);
					PreferenceWrapper.commit();
					setResult(RESULT_OK);
					RegisterActivity2.this.finish();
				}
				
				@Override
				public void onStatusFail(LoginAndRegisterResponse response) {
					super.onStatusFail(response);
					ToastUtil.showMessage(RegisterActivity2.this, response.getMessage());
				}
				
			});
			
			break;

		default:
			break;
		}
	}

}