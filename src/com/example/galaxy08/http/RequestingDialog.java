package com.example.galaxy08.http;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class RequestingDialog extends DialogFragment{

	private  Activity mActivity;
	
	public RequestingDialog(int msg){
		Bundle args = new Bundle();
        args.putInt("message", msg);
        setArguments(args);
	}
	
	@Override
    public void onAttach(Activity activity) {
        mActivity = activity;
        super.onAttach(activity);
    }
	
	@Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String message = mActivity.getString(getArguments().getInt("message"));
		ProgressDialog dialog = new ProgressDialog(mActivity);
		dialog.setMessage(message);
		return dialog;
	}
	
}
