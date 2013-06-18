package com.example.galaxy08.http.service;

import java.io.IOException;
import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import com.example.galaxy08.R;
import com.example.galaxy08.http.exception.GalaxyHttpException;
import com.example.galaxy08.http.model.BaseResponse;
import com.example.galaxy08.tool.DebugLog;
import com.example.galaxy08.tool.ToastUtil;
import com.example.galaxy08.util.LifeLimitedtCache;

/**
 * 
 */
public abstract class HttpRequestCallBack
		implements RequestHandler {

	public String TAG = getClass().getSimpleName();

	//保持callback的引用，防止过快被回收
	private static LifeLimitedtCache<HttpRequestCallBack> mCache;
	
	private WeakReference<Context> mRef;

	private boolean mShowDialog;

	private boolean mCancelable;

	private Dialog mDialog;

	MyResultReceiver mReceiver;

	public HttpRequestCallBack(Context context, boolean showDialog,
			boolean cancelable) {
		this.mRef = new WeakReference<Context>(context);
		this.mShowDialog = showDialog;
		this.mCancelable = cancelable;
		this.mReceiver = new MyResultReceiver(this);
		
		createCache();
		mCache.setDefaultTimeout(60 * 1000);
		mCache.put(this);
	}

	private synchronized void createCache(){
		if(mCache == null){
			mCache = new LifeLimitedtCache<HttpRequestCallBack>();
		}
	}
	
	//释放自身的引用
	public void disposeSelf(){
		mCache.remove(this);
	}
	
	public Context getContext() {
		return mRef.get();
	}

	public ResultReceiver getResultReceiver(){
		return mReceiver;
	}
	
	public abstract void onSuccessReceive(BaseResponse resultData);

	public void onServerError(Exception e) {

	}

	public void onNetWorkInvalid() {
		DebugLog.logd(TAG, "onNetWorkError()");
//		ToastUtil.showMessage(getContext(), R.string.networkUnavailable, 2000);
		ToastUtil.showImageToast(getContext(), getContext().getString(R.string.network_isslow), R.drawable.toast_fail, 2000);
	}

	public void onFailureReceive(BaseResponse resultData) {
		DebugLog.logd(TAG, "onFailureReceive()");
	}

	/**
	 * 网络错误
	 * 
	 * @param error
	 */
	public void onIoError(Exception error) {
		DebugLog.logd(TAG, "onErrorReceive()");
		ToastUtil.showImageToast(getContext(), getContext().getString(R.string.network_isslow), R.drawable.toast_fail, 0);
	}

	public Dialog onCreateDialog() {
		ProgressDialog dlg = new ProgressDialog(mRef.get());
		dlg.setTitle(null);
		dlg.setMessage(mRef.get().getString(R.string.later));
		return dlg;
	}

	public void onPreStart() {
		if (mShowDialog && mRef.get() instanceof Activity
				&& !((Activity) mRef.get()).isFinishing()) {
			if (mDialog == null) {
				mDialog = onCreateDialog();
			}
			mDialog.show();
			mDialog.setCancelable(mCancelable);
			mDialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					// ToastUtil.showMessage(getContext(),
					// getContext().getString(R.string.hasconcelled));
					cancel();
					onCanceled();
				}
			});
		}
	}

	public void onCanceled() {

	}

	public void onFinally() {
		disMissDialog();
	}

	protected void disMissDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
	}

	@Override
	public void cancel() {
		mReceiver.cancel();
	}

	private static class MyResultReceiver extends ResultReceiver {

		boolean mCanceled;

		WeakReference<HttpRequestCallBack> mCallBack;

		public MyResultReceiver(HttpRequestCallBack callBack) {
			super(new Handler());
			this.mCallBack = new WeakReference<HttpRequestCallBack>(
					callBack);
			this.mCanceled = false;
		}

		public void cancel(){
			this.mCanceled = true;
		}
		
		@Override
		public void onReceiveResult(final int resultCode,
				final Bundle resultData) {

			if (mCanceled) {
				return;
			}
			switch (resultCode) {
			case AbstractHttpService.CODE_NEWORK_UNAVAIABLE:
				if (mCallBack.get() != null) {
					mCallBack.get().onNetWorkInvalid();
				}
				break;
			case AbstractHttpService.CODE_SUCCESS: {
				DebugLog.logd("requst  success");
				if (mCallBack.get() != null) {
					mCallBack
							.get()
							.onSuccessReceive(
									(BaseResponse) resultData
											.getSerializable(AbstractHttpService.BUNDLE_EXTRA_RESULT_DATA));
					DebugLog.logd("callback success");
				}
				break;
			}
			case AbstractHttpService.CODE_FAILURE: {
				DebugLog.logd("requst  success");
				if (mCallBack.get() != null) {
					mCallBack
							.get()
							.onFailureReceive(
									(BaseResponse) resultData
											.getSerializable(AbstractHttpService.BUNDLE_EXTRA_RESULT_DATA));
					DebugLog.logd("callback success");
				}
				break;
			}
			case AbstractHttpService.CODE_ERROR:
				if (mCallBack.get() != null) {
					Exception exception = (Exception) resultData
							.getSerializable(AbstractHttpService.BUNDLE_EXTRA_RESULT_DATA);
					if (exception instanceof IOException) {
						mCallBack.get().onIoError(exception);
					} else if (exception instanceof GalaxyHttpException) {
						if(exception.getMessage()!=null)
							DebugLog.loge(exception.getMessage());
						mCallBack.get().onServerError(exception);
					} else {
						DebugLog.loge(exception.getMessage());
						mCallBack.get().onServerError(exception);
					}
					mCallBack.get().onNetWorkInvalid();
				}
				break;
			case AbstractHttpService.CODE_START:
				if (mCallBack.get() != null) {
					mCallBack.get().onPreStart();
				}
				break;
			case AbstractHttpService.CODE_FINISHE:
				if (mCallBack.get() != null) {
					mCallBack.get().onFinally();
					mCallBack.get().disposeSelf();
					mCallBack.clear();
				}
				break;
			}
		}
	}
}
