package com.example.galaxy08.register_login;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.galaxy08.R;
import com.example.galaxy08.app.PreferenceWrapper;
import com.example.galaxy08.http.BaseJsonHandler;
import com.example.galaxy08.http.GalaxyApi;
import com.example.galaxy08.http.response.LoginAndRegisterResponse;
import com.example.galaxy08.tool.ToastUtil;
import com.example.galaxy08.util.StringUtils;

public class RegisterActivity extends Activity implements OnClickListener{

	private Button activateAccountButton;

	private Button backButton;

	private EditText mobileNumberEdit;
	private EditText companyAccountEdit;
	private EditText securityCodeEdit;
	
	//private TextView activateTitle;
	
	public static final int FOR_REGISTER3 = 0x1001;
	
	private String mobileNumber;
	private String companyAccount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activate);
		setupView();
		addListener();
	}

	private void setupView(){
		
		companyAccountEdit = (EditText)findViewById(R.id.activate_company_account_edit);
		mobileNumberEdit = (EditText)findViewById(R.id.activate_mobile_number_edit);
		securityCodeEdit = (EditText)findViewById(R.id.activate_sercurity_code_edit);
		activateAccountButton = (Button)findViewById(R.id.activate_next_step_button);
		backButton = (Button)findViewById(R.id.activate_back_button);
	}

	private void addListener(){
		activateAccountButton.setOnClickListener(this);
		backButton.setOnClickListener(this);
	}
	/*
	 * 处理启动 activity返回的情况
	 * （非 Javadoc）
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case FOR_REGISTER3:
			if(resultCode==RESULT_OK){
				setResult(RESULT_OK);
				this.finish();
				return;
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activate_back_button:
			this.finish();
			break;

		case R.id.activate_next_step_button:
			
			companyAccount = companyAccountEdit.getText().toString().trim();
			mobileNumber = mobileNumberEdit.getText().toString().trim();
			String securityCode = securityCodeEdit.getText().toString().trim();
			
			if(StringUtils.isNull(companyAccount)||StringUtils.isNull(mobileNumber)
					||StringUtils.isNull(securityCode)){
				Toast.makeText(this, getString(R.string.no_null), Toast.LENGTH_SHORT).show();
				return;
			}
			
			GalaxyApi.activateAccount(companyAccount,mobileNumber,securityCode, new BaseJsonHandler<LoginAndRegisterResponse>() {

				@Override
				public void onStatusFail(LoginAndRegisterResponse response) {
					super.onStatusFail(response);
					ToastUtil.showMessage(RegisterActivity.this, response.getMessage());
				}
				@Override
				public void onStatusOk(LoginAndRegisterResponse response) {
					ToastUtil.showMessage(RegisterActivity.this, "验证码正确");
					PreferenceWrapper.put(PreferenceWrapper.TOKEN, response.getToken());
					PreferenceWrapper.put(PreferenceWrapper.USER_ID, String.valueOf(response.getUserid()));
					PreferenceWrapper.commit();
					Intent intent = new Intent(RegisterActivity.this, RegisterActivity2.class);
					intent.putExtra(RegisterActivity2.TARGET_COMPANY_ACCOUNT, companyAccount);
					intent.putExtra(RegisterActivity2.TARGET_MOBILE, mobileNumber);
					startActivityForResult(intent, FOR_REGISTER3);
				}
			});
			
			break;

		default:
			break;
		}
	}
}
