package com.example.galaxy08.tool;



import com.example.galaxy08.R;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ToastUtil {

	private static Handler handler = new Handler(Looper.getMainLooper());

	private static Toast toast = null;
	private static Toast imageToast = null;

	private static Object synObj = new Object();

	private static String message;
	private static int len;

	public static Toast makeText(Context context, String str, int length) {
		return getToast(context,str,length);
	}
	public static Toast makeText(Context context, int resId, int length) {
		return makeText(context,context.getString(resId),length);
	}

	public static void showMessage(final Context act, final String msg) {
		showMessage(act, msg, Toast.LENGTH_SHORT);
	}
	
	public static void showMessageLong(final Context act, final String msg) {
		showMessage(act, msg, Toast.LENGTH_LONG);
	}


	/*
	 * public static void showMessage(final Context act, final int msg) {
	 * showMessage(act, msg, Toast.LENGTH_SHORT); }
	 */

	public static void showMessage(final Context act, final String msg,
			final int len) {
		new Thread(new Runnable() {
			public void run() {
				handler.post(new Runnable() {
					@Override
					public void run() {
						synchronized (synObj) {
							getToast(act, msg, len).show();
						}
					}
				});
			}
		}).start();
	}

	public static void showMessage(final Context act, final int msg,
			final int len) {
		new Thread(new Runnable() {
			public void run() {
				handler.post(new Runnable() {
					@Override
					public void run() {
						//DebugLog.logd("synObj not in");
						synchronized (synObj) {
							//DebugLog.logd("synObj in");
							//DebugLog.logd("toast =" + toast);
							getToast(act,act.getString(msg),len).show();
						}
					}
				});
			}
		}).start();
	}

	private static Toast getToast(Context ctx, String msg, int len) {
		if (toast == null) {
			toast = Toast.makeText(ctx, msg, len);
			// toast.setGravity(Gravity.CENTER, 0, 0);
			TextView tv = (TextView) toast.getView().findViewById(
					android.R.id.message);
			tv.setTextSize(20);
		}
		toast.setText(msg);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(len);
		return toast;
	}
	

	/**
	 * 显示带图片的和文字的toast
	 * ToastUtil.showImageToast(LoginActivity.this,getResources().getString(R.string.finished),R.drawable.toast_ok, 0);
	 * @param ctx
	 * @param msg   toast的内容
	 * @param resId   toast图片的resourceId
	 * @param len     toast显示时长
	 */
	public static void showImageToast(Context ctx, String msg, int resId, int len ) {
//		LayoutInflater inflater = LayoutInflater.from(ctx);
//		View toastView = inflater.inflate(R.layout.toast_success_image,
//				(ViewGroup) null);
//		ImageView toastImage = (ImageView) toastView.findViewById(R.id.Ivtoast_success_image);
//		TextView toastMessage = (TextView) toastView.findViewById(R.id.Tvtoast_message);
//		toastImage.setImageResource(resId);
//		toastMessage.setText(msg);
//		
//		imageToast = getImageToast(ctx);
//		imageToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//		imageToast.setDuration(len);
//		imageToast.setView(toastView);
//		imageToast.show();
	}
	
	/**
	 * show “已完成”的带图片的toast
	 * @param ctx
	 */
	public static void showSucceeImageToast(Context ctx ) {
		//showImageToast(ctx, ctx.getResources().getString(R.string.finished), R.drawable.toast_ok, 0);
	}
	
	/**
	 * show 成功对勾 图片的 toast
	 * @param ctx
	 * @param msg  “显示的文字提示”
	 */
	public static void showSucceeImageToast(Context ctx , String msg) {
		//showImageToast(ctx, msg, R.drawable.toast_ok, 0);
	}
	
	/**
	 * show “网络不给力”的带相应图片的toast
	 * ToastUtil.showImageToast(context, context.getString(R.string.network_isslow), R.drawable.toast_fail, 0);
	 * @param ctx
	 */
	public static void showNetworkSlowImageToast(Context context ) {
		showImageToast(context, context.getString(R.string.network_isslow),  R.drawable.toast_fail, 0);
	}
	
	/**
	 * show 哭脸图片的toast
	 * ToastUtil.showImageToast(context, context.getString(R.string.network_isslow), R.drawable.toast_fail, 0);
	 * @param ctx
	 */
	public static void showErrorImageToast(Context context , String messge) {
		showImageToast(context, messge,  R.drawable.toast_fail, 0);
	}

	private static synchronized Toast getImageToast(Context context) {
		if(imageToast == null){
			imageToast = new Toast(context);
		}
		return imageToast;
	}


}