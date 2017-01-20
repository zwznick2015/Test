package com.misfit.androidwear;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.Until;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

//import com.android.test.pairing.ClockworkPairingUtil;
//import com.android.test.power.AlarmsUtil;
//import com.android.test.power.FrameworkUtil;
//import com.android.test.power.LatchUtil;
//import com.android.test.power.NotificationUtil;
//import com.android.test.power.PowerTestCase;
//import com.android.test.power.ScreenUtil;
//import com.android.test.power.VoiceActionUtil;


/**
 * Created by zhongweizhou on 16/9/23.
 */
@RunWith(AndroidJUnit4.class)
public class ClockworkPairingPowerTests extends PowerTestCase{

    private static final String AUDIO_FILE_PATH = "/data/data/com.google.android.wearable.app/";
    private static final long BT_CONNECTION_WAIT_MSEC = 60000L;
    private static final String COMPANION_FINISH = "Companion Finish";
    private static final long COMPANION_START_WAIT_MSEC = 30000L;
    private static final long COMPANION_TIMEOUT_HEAVY_SECS = 3600L;
    private static final long COMPANION_TIMEOUT_TYPICAL_SECS = 7200L;
    private static final long COMPANION_TIMEOUT_VALIDATE_SECS = 720L;
    private static final long GPS_DURATION_HEAVY_SECS = 120L;
    private static final long GPS_DURATION_TYPICAL_SECS = 120L;
    private static final long GPS_INTERVAL_1SEC = 1L;
    private static final String GPS_PARAM = "gps";
    private static final long GPS_START_DELAY = 60L;
    private static final long HOTWORD_PROMPT_WAIT_MSEC = 5000L;
    private static final long IDLE_DELAY_IN_SECONDS = 15L;
    private static final String MSGTESTPATH = "ClockworkPairingPowerTests";
    private static final String MUSICPLAYBACK_PARAM = "music";
    private static final String MUSIC_PACKAGE_NAME = "com.google.android.apps.musicplaybacktest";
    private static final String MUSIC_PAUSE_ACTION = "com.google.android.apps.musicplaybacktest.pause";
    private static final long MUSIC_PLAYBACK_HEAVY_SECS = 100L;
    private static final long MUSIC_PLAYBACK_TYPICAL_SECS = 80L;
    private static final long MUSIC_PLAYBACK_VALIDATE_SECS = 10L;
    private static final String MUSIC_PLAY_ACTION = "com.google.android.apps.musicplaybacktest.play";
    private static final String MUSIC_SERVICE_NAME = "com.google.android.apps.musicplaybacktest.MusicPlaybackService";
    private static final long MUSIC_START_DELAY_SECS = 90L;
    private static final String OFFBODY_ACTIVITY_NAME = "com.google.android.clockwork.testapp.MainActivity";
    private static final long OFFBODY_DELAY_MSEC = 10000L;
    private static final String OFFBODY_INTENT_ACTION = "com.google.offbodytestapp.CHANGE_INTERVAL";
    private static final String OFFBODY_PACKAGE_NAME = "com.google.android.clockwork.testapp";
    private static final String OFFBODY_PARAM = "offbody_freq";
    private static final BySelector SELECTOR_FREQUENCY = By.res("com.google.android.clockwork.testapp:id/interval");
    private static final String TAG = ClockworkPairingPowerTests.class.getCanonicalName();
    private static final String TESTSTARTPATH = "TestStart";
    private static final long VA_INTERVAL_HEAVY_SECS = 60L;
    private static final long VA_INTERVAL_MICROBENCHMARK_SECS = 300L;
    private static final long VA_INTERVAL_TYPICAL_SECS = 120L;
    private static final long VA_INTERVAL_VALIDATE_SECS = 60L;
    private static final int VA_ITERATIONS_HEAVY = 10;
    private static final int VA_ITERATIONS_TYPICAL = 10;
    private static final int VA_ITERATIONS_VALIDATE = 1;
    private static final String VA_PARAM = "voice_actions";
    private static final String WHATTIMEISIT = "/data/data/com.google.android.wearable.app/what_time_is_it.wav";
    private static final long WIFI_CLOUDSYNC_WAIT_MSEC = 120000L;
    private static final String WIFI_ENABLE_CMD = "svc wifi enable";
    private static final String WIFI_SESSION_PARAM = "wifi";
    private static final String WIFI_SETTINGS_ACTIVITY = "com.google.android.clockwork.settings.wifi.WifiSettingsActivity";
    private static final String WIFI_SETTINGS_NAME = "com.google.android.apps.wearable.settings";
    private static MockLocationListener mLocationListener;
    private static LocationManager mLocationManager;
    private static int mOffBodyFreq;
    private static boolean mRunGPS;
    private static boolean mRunMusicPlayback;
    private static boolean mRunOffBody = false;
    private static boolean mRunVoiceActions;
    private static boolean mRunWiFiSession = false;
//    private GoogleApiClient mClient;
    private Context mContext;


    static
    {
        mRunVoiceActions = false;
        mRunMusicPlayback = false;
        mRunGPS = false;
        mRunOffBody = false;
        mOffBodyFreq = 0;
    }

    private void testDoUWear(int paramInt, long paramLong1, long paramLong2, long paramLong3, long paramLong4)
            throws Exception
    {
        System.out.print("Start test ..........ß");
//        Intent localIntent = new Intent(this.mContext, ClockworkPairingListenerService.class);
//        this.mContext.startService(localIntent);
        if (mRunOffBody) {
            launchOffBody(mOffBodyFreq, false);
        }
        LatchUtil.waitForStartMessage();
        Log.d(TAG, "Test started");
//        scheduleMeasurement(15L);
//        ScreenUtil.setScreenOff(getInstrumentation());
        if (mRunWiFiSession)
        {
            LatchUtil.awaitForCountDown(1);
            scheduleLatchCountDown("WiFi Wake Up", 90L);
            LatchUtil.awaitForCountDown(1);
            getUiDevice().wakeUp();
            FrameworkUtil.launchActivity(getInstrumentation(), "com.google.android.apps.wearable.settings", "com.google.android.clockwork.settings.wifi.WifiSettingsActivity");
            getUiDevice().executeShellCommand("svc wifi enable");
            SystemClock.sleep(2000L);
            ScreenUtil.setScreenOff(getInstrumentation());
            LatchUtil.awaitForCountDown(1);
        }
//        LatchUtil.awaitForCountDown(1);
//        if (mRunVoiceActions) {
            scheduleVoiceActions(1, paramInt, paramLong1);
//        }
//        if (mRunMusicPlayback) {
            scheduleMusicPlayback(paramLong2);
//        }
//        if (mRunGPS) {
            scheduleGPSLocationRequests(paramLong3, paramLong4);
//        }
//        waitForMeasurementToComplete();
        //通知iPhone 手机 :android wear 这边用例执行完,iPhone 手机那边的用例 可以执行下一轮用例
//        ClockworkPairingUtil.sendProceedMessageToNode(this.mClient, ClockworkPairingUtil.getFirstConnectedNode(this.mClient), "ClockworkPairingPowerTests", "Wear test finished");
    }

    private void launchOffBody(int paramInt, boolean paramBoolean)
            throws Exception
    {
        PowerManager.WakeLock localWakeLock = ((PowerManager)this.mContext.getSystemService(Context.POWER_SERVICE)).newWakeLock(1, TAG);
        localWakeLock.acquire();
        Log.d(TAG, "Launching offbody activity");
        FrameworkUtil.launchActivity(getInstrumentation(), "com.google.android.clockwork.testapp", "com.google.android.clockwork.testapp.MainActivity");
        SystemClock.sleep(10000L);
        Log.d(TAG, "Setting up offbody request using intent");
        Intent localIntent = new Intent();
        localIntent.setAction("com.google.offbodytestapp.CHANGE_INTERVAL");
        localIntent.putExtra("interval", paramInt);
        this.mContext.sendBroadcast(localIntent);
        if (paramBoolean)
        {
            Log.d(TAG, "Validating test app started properly");
            assertNotNull("Could not locate offbody app", getUiDevice().wait(Until.findObject(SELECTOR_FREQUENCY), 10000L));
            assertNotNull("Couldn't verify frequency", getUiDevice().findObject(SELECTOR_FREQUENCY.text(Integer.toString(paramInt))));
        }
        SystemClock.sleep(10000L);
        getUiDevice().pressHome();
        localWakeLock.release();
    }

    private void scheduleLatchCountDown(String paramString, long paramLong)
    {
        BroadcastReceiver local1 = new BroadcastReceiver()
        {
            public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
            {
                try
                {
                    LatchUtil.countDown();
                    return;
                }
                catch (Exception e)
                {
                    throw new RuntimeException(Log.getStackTraceString(e));
                }
            }
        };
        Log.d(TAG, String.format("Scheduling \"%s\" in %d secs", new Object[] { paramString, Long.valueOf(paramLong) }));
//        AlarmsUtil.setOneTimeAlarm(this.mContext, 1000L * paramLong, paramString, local1);
    }
    private void scheduleVoiceActions(int paramInt1, int paramInt2, long paramLong)
            throws Exception
    {
//        int i = paramInt1;
        int i = 0;
//        while (i < paramInt1 + paramInt2)
            while (i < 2)
        {     getUiDevice().wakeUp();
//            scheduleLatchCountDown(String.format("Voice Action #%d", new Object[] { Integer.valueOf(i) }), paramLong);
//            LatchUtil.awaitForCountDown(1);
//            Log.d(TAG, String.format("Performing Voice Action #%d", new Object[] { Integer.valueOf(i) }));
            VoiceActionUtil.hotwordToPrompt(getInstrumentation(), false);
//            SystemClock.sleep(5000L);
            VoiceActionUtil.promptToConfirmation(getInstrumentation(), "/data/data/com.google.android.wearable.app/what_time_is_it.wav", "what time is it", "", false);
            i += 1;
        }
        SystemClock.sleep(20000);
        getUiDevice().pressHome();
    }
    private void scheduleMusicPlayback(long paramLong)
            throws Exception
    {
        Log.d(TAG, String.format("Scheduling music playback for %d secs", new Object[] { Long.valueOf(paramLong) }));
        scheduleLatchCountDown("MUSIC START", 90L);
//        LatchUtil.awaitForCountDown(1);
        Log.d(TAG, "Launching music player");
        getUiDevice().wakeUp();
        Intent localIntent = new Intent();
        localIntent.setAction("com.google.android.apps.musicplaybacktest.play");
        localIntent.setComponent(new ComponentName("com.google.android.apps.musicplaybacktest", "com.google.android.apps.musicplaybacktest.MusicPlaybackService"));
        this.mContext.startService(localIntent);
        SystemClock.sleep(20000);
//        scheduleLatchCountDown("MUSIC CHECK", 10L);
//        LatchUtil.awaitForCountDown(1);
//        assertTrue("Music app is not running", FrameworkUtil.isServiceRunning(getInstrumentation(), "com.google.android.apps.musicplaybacktest.MusicPlaybackService"));
//        assertTrue(TAG + ": Music playback didn't start", FrameworkUtil.isMusicPlaying(getInstrumentation()));
        Log.d(TAG, "Music playback started");
        ScreenUtil.setScreenOff(getInstrumentation());
//        scheduleLatchCountDown("MUSIC STOP", paramLong);
//        LatchUtil.awaitForCountDown(1);
        getUiDevice().wakeUp();
        localIntent = new Intent();
        localIntent.setAction("com.google.android.apps.musicplaybacktest.pause");
        localIntent.setComponent(new ComponentName("com.google.android.apps.musicplaybacktest", "com.google.android.apps.musicplaybacktest.MusicPlaybackService"));
        this.mContext.startService(localIntent);
        Log.d(TAG, "Music playback stopped");
        ScreenUtil.setScreenOff(getInstrumentation());
    }

    private void scheduleGPSLocationRequests(long paramLong1, long paramLong2)
            throws Exception
    {
        scheduleLatchCountDown("GPS START", 60L);
        LatchUtil.awaitForCountDown(1);
        mLocationListener = new MockLocationListener();
        mLocationManager = (LocationManager)this.mContext.getSystemService(Context.LOCATION_SERVICE);
        assertTrue("Location provider is not enabled", mLocationManager.isProviderEnabled("gps"));
//        mLocationManager.requestLocationUpdates("gps", 1000L * paramLong1, 0.0F, mLocationListener);
        Log.d(TAG, String.format("Location requests at %d sec interval started for %d seconds", new Object[] { Long.valueOf(paramLong1), Long.valueOf(paramLong2) }));
        scheduleLatchCountDown("GPS STOP", paramLong2);
        LatchUtil.awaitForCountDown(1);
//        mLocationManager.removeUpdates(mLocationListener);
        Log.d(TAG, "Location requests stopped");
    }

    public void setUp()
            throws Exception
    {
        super.setUp();
        this.mContext = InstrumentationRegistry.getInstrumentation().getContext();
//        this.mClient = new GoogleApiClient.Builder(this.mContext).addApi(Wearable.API).build();
//        this.mClient.connect();
        String str = getString("offbody_freq");
        if (str != null)
        {
            mRunOffBody = true;
            mOffBodyFreq = Integer.parseInt(str);
        }
        str = getString("wifi");
        if ((str != null) && (Boolean.parseBoolean(str))) {
            mRunWiFiSession = true;
        }
        str = getString("music");
        if ((str != null) && (Boolean.parseBoolean(str))) {
            mRunMusicPlayback = true;
        }
        str = getString("voice_actions");
        if ((str != null) && (Boolean.parseBoolean(str))) {
            mRunVoiceActions = true;
        }
        str = getString("gps");
        if ((str != null) && (Boolean.parseBoolean(str))) {
            mRunGPS = true;
        }
    }

    public void tearDown()
            throws Exception
    {
//        this.mClient.disconnect();
        super.tearDown();
    }
    @Test
    public void testTypicalUsageWear()
            throws Exception
    {
        testDoUWear(10, 120L, 80L, 1L, 120L);
    }



    private static class MockLocationListener
            implements LocationListener
    {
        public void onLocationChanged(Location paramLocation)
        {
            Log.i(ClockworkPairingPowerTests.TAG, "Location Change: " + paramLocation.toString());
        }

        public void onProviderDisabled(String paramString) {}

        public void onProviderEnabled(String paramString) {}

        public void onStatusChanged(String paramString, int paramInt, Bundle paramBundle) {}
    }
}

