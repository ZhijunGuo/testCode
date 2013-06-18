package com.example.galaxy08.http;

import java.lang.reflect.ParameterizedType;

import org.json.JSONObject;

import com.example.galaxy08.R;
import com.example.galaxy08.SysApplication;
import com.example.galaxy08.http.model.BaseResponse;
import com.example.galaxy08.tool.ToastUtil;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;


public abstract class BaseJsonHandler<T extends BaseResponse> extends IJsonHandler{

	public String TAG = getClass().getSimpleName();
	
	private Class<T> mTClass;
	
	private DialogFragment mDialog;
	
	private FragmentActivity mActivity;
	
	private Fragment mFragment;
	
	private boolean mCancelable;
	
	@SuppressWarnings("unchecked")
	public BaseJsonHandler(){
		mTClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]; 
		initCls();
	}
	
	public BaseJsonHandler(FragmentActivity activity,boolean cancelable){
		mActivity = activity;
		mCancelable = cancelable;
		initCls();
	}
	
	public BaseJsonHandler(Fragment fragment,boolean cancelable){
		mFragment = fragment;
		mCancelable = cancelable;
		initCls();
	}
	
	@SuppressWarnings({ "unused", "unchecked" })
	private void initCls(){
		mTClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]; 
	}
	
	@Override
	public void onStart() {
		if((mActivity == null || mActivity.isFinishing()) && mFragment == null){
			return;
		}
		
		if(mDialog == null){
			mDialog = new RequestingDialog(R.string.tip_waiting);
			mDialog.setCancelable(mCancelable);
		}
		
		if(mFragment != null){
			mDialog.show(mFragment.getActivity().getSupportFragmentManager(), TAG);
		}else if(mActivity != null){
			mDialog.show(mActivity.getSupportFragmentManager(), TAG);
		}
	}

	@Override
	public void onSuccess(int statusCode, String response) {
		
	}

	@Override
	public void onFailure(Throwable error, String content) {
		Log.i("GalaxyApi", content);
		ToastUtil.showMessage(SysApplication.getAppContext(), R.string.tip_request_failed,Toast.LENGTH_SHORT);
	}

	@Override
	public void onSuccess(int statusCode, JSONObject jsonObject) {
		try {
			T response = mTClass.newInstance();
			response.parser(jsonObject);
			if(ResponseCode.SUCCESS.equals(String.valueOf(response.getStatus()))){
				onStatusOk(response);
			}else{
				onStatusFail(response);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public abstract void onStatusOk(T response);
	
	public void onStatusFail(T response){
		
	}
	
	@Override
	public void onFailure(Throwable e, JSONObject errorResponse) {
		ToastUtil.showMessage(SysApplication.getAppContext(), R.string.tip_request_failed,Toast.LENGTH_SHORT);
	}
	
	@Override
	public void onFinish() {
		if(mDialog != null && mFragment != null &&  mFragment.isVisible()){
			mDialog.dismiss();
		}
	}
	
}
