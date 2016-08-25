package com.example.autobag;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Administrator on 2016/6/8.
 */
public class MyService extends Service {
    public final static String TAG = "MyService";
    //private MyBinder myBinder = new MyBinder();
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "MyService onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "MyService onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "MyService onDestroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "MyService onBind");
        return null;
    }
}
