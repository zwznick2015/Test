package com.misfit.androidwear;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by zhongweizhou on 16/9/23.
 */

public class ClockworkPairingListenerService extends Service {

    private static final String MSGTESTPATH = "ClockworkPairingPowerTests";
    private static final String TAG = ClockworkPairingListenerService.class.getCanonicalName();
    private static final String TESTSTARTPATH = "TestStart";
    private GoogleApiClient mClient;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate()
    {
        Log.d(TAG, "onCreate");
        super.onCreate();
        this.mClient = new GoogleApiClient.Builder(this).addApi(Wearable.API).build();
        this.mClient.connect();
    }
        //hello world from hometown
    public void onDestroy()
    {
        Log.d(TAG, "onDestroy");
        this.mClient.disconnect();
        super.onDestroy();
    }

    public void onMessageReceived(MessageEvent paramMessageEvent){
        Log.d(TAG,"onMessageReceived");
        if(paramMessageEvent.getPath().equals("ClockworkPairingPowerTests"));
        {
            Log.d(TAG,new String(paramMessageEvent.getData()));
            LatchUtil.countDown();
        }
        for(;;){
//            super.onMessageReceived(paramMessageEvent);
//            return;
            if(paramMessageEvent.getPath().equals("TestStart")){
                Log.d(TAG,new String(paramMessageEvent.getData()));
                LatchUtil.receivedStartMessage();
            }
        }


    }
}
