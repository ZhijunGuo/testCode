package com.example.galaxy08.http.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

/**
 *	AbstractHttpService HttpService抽象类
 *  IntentService使用队列的方式将请求的Intent加入队列，
 *  然后开启一个worker thread(线程)来处理队列中的Intent
 */
public abstract class AbstractHttpService extends IntentService {

    private static final String TAG = "HttpIntentService";

    private static final int MAX_THREADS = 3;

    public static final String INTENT_EXTRA_RECEIVER = "com.jingwei.card.extras.receiver";

    public static final String INTENT_EXTRA_RETRY = "com.jingwei.card.extras.retry";
    
    public static final String BUNDLE_EXTRA_RESULT_DATA = "com.jingwen.cn.http.bundle.resultdata";

    public static final int CODE_SUCCESS = 0;

    public static final int CODE_FAILURE = 1;

    public static final int CODE_ERROR = 2;

    public static final int CODE_NEWORK_UNAVAIABLE = 3;

    public static final int CODE_START = 4;
    
    public static final int CODE_FINISHE = 5;
    
    private ExecutorService mThreadPool;

    public AbstractHttpService() {
        super("HttpIntentService");
    }

    public void onCreate() {
        super.onCreate();
        mThreadPool = Executors.newFixedThreadPool(MAX_THREADS);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mThreadPool.submit(new IntentRunnable(intent));
    }

    protected abstract void onCustomHandleIntent(Intent intent);
    

    protected void sendSuccess(final Intent intent, final Bundle data) {
        sendResult(intent, data, CODE_SUCCESS);
    }

    protected void sendFailure(final Intent intent, final Bundle data) {
        sendResult(intent, data, CODE_FAILURE);
    }

    protected void sendResult(final Intent intent, Bundle data, final int code) {

        ResultReceiver receiver = (ResultReceiver) intent.getParcelableExtra(INTENT_EXTRA_RECEIVER);

        if (receiver != null) {
            if (data == null) {
                data = new Bundle();
            }
            receiver.send(code, data);
        }
    }

    private class IntentRunnable implements Runnable {

        private Intent mIntent;

        public IntentRunnable(final Intent intent) {
            mIntent = intent;
        }

        public void run() {
            onCustomHandleIntent(mIntent);
        }
    }
}
