package com.misfit.androidwear;

/**
 * Created by zhongweizhou on 16/9/26.
 */

import android.app.Instrumentation;
import android.content.Intent;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.Until;

import junit.framework.Assert;

import java.io.File;
import java.io.Serializable;

public class VoiceActionUtil
{
    private static final String AUDIO_FILE_PATH = "/data/data/com.google.android.wearable.app";
    private static final String EXTRA_NAME = "EXTRA_FILE";
    private static final String HEADER_TEXT_LABEL = "header_text";
    private static final String INJECTION_INTENT = "audio.injection.ENQUEUE_FILE";
    private static final File OK_GOOGLE_FILE = new File("/data/data/com.google.android.wearable.app", "ok_google.wav");
    private static final int SHORT_TIMEOUT = 2000;
    private static final int TIMEOUT = 20000;
    private static final int TINY_TIMEOUT = 500;
    private static final String TRANSCRIPTION_ID = "transcription";
    private static final String WEARABLE_PKG = "com.google.android.wearable.app";

    public static void hotwordToPrompt(Instrumentation paramInstrumentation, boolean paramBoolean)
            throws RemoteException
    {
        Assert.assertTrue("no \"OK google\" file", OK_GOOGLE_FILE.exists());
        UiDevice.getInstance(paramInstrumentation).wakeUp();
        SystemClock.sleep(2000L);
        injectAudio(paramInstrumentation, OK_GOOGLE_FILE.getAbsolutePath());
        if (!paramBoolean) {
            return;
        }
        Assert.assertTrue("Could not reach audio input \"Speak now\" screen.", ((Boolean)UiDevice.getInstance(paramInstrumentation).wait(Until.hasObject(By.res("com.google.android.wearable.app", "transcription")), 20000L)).booleanValue());
    }

    private static void injectAudio(Instrumentation paramInstrumentation, String paramString)
            throws RemoteException
    {
        Intent localIntent = new Intent("audio.injection.ENQUEUE_FILE");
        localIntent.putExtra("EXTRA_FILE", (Serializable) paramString);
        UiDevice.getInstance(paramInstrumentation).wakeUp();
        SystemClock.sleep(500L);
        paramInstrumentation.getContext().sendBroadcast(localIntent);
    }

    public static void promptToConfirmation(Instrumentation paramInstrumentation, String paramString1, String paramString2, String paramString3, boolean paramBoolean)
            throws RemoteException
    {
        injectAudio(paramInstrumentation, paramString1);
        if (!paramBoolean) {
            return;
        }
        paramString1 = String.valueOf(UiDevice.getInstance(paramInstrumentation));
        paramInstrumentation = null;
//        if (!((Boolean)paramString1.wait(Until.hasObject(By.text(Pattern.compile(Pattern.quote(paramString2), 2))), 20000L)).booleanValue()) {
//            paramInstrumentation = " Expected transcription text: '" + paramString2 + "' never appeared";
//        }
//        Assert.assertTrue("header_text UI element never appeared." + paramInstrumentation, ((Boolean)paramString1.wait(Until.hasObject(By.res("com.google.android.wearable.app", "header_text")), 20000L)).booleanValue());
//        Assert.assertTrue(paramString3 + " never appeared." + paramInstrumentation, ((Boolean)paramString1.wait(Until.hasObject(By.text(Pattern.compile(".*" + paramString3 + ".*"))), 20000L)).booleanValue());
    }
}