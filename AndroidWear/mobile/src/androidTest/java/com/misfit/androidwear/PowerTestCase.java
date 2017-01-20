package com.misfit.androidwear;

import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnitRunner;
import android.support.test.uiautomator.UiDevice;
import android.util.Log;

import org.junit.Before;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

/**
 * Created by zhongweizhou on 16/9/23.
 */

public abstract class PowerTestCase extends AndroidJUnitRunner
{
    private static final long POWER_LOG_DELTA = 5000L;
    private static  String SD_CARD_PATH;
    private static final String POWER_OUTPUT = SD_CARD_PATH + "autotester.log";
    private static final String TAG = PowerTestCase.class.getCanonicalName();
    private String mActiveMeasurement = null;
    private Context mContext;
    private CountDownLatch mLatch;
    private final ArrayList<PendingIntent> mPendingIntents = new ArrayList();
    private final HashMap<String, String> mTags = new HashMap();
    UiDevice muidevice;
    static
    {
        SD_CARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    }

    private void closeActiveMeasurement(String paramString)
    {
        this.mTags.put(paramString, "stopped");
        this.mActiveMeasurement = null;
        if (this.mLatch != null) {
            this.mLatch.countDown();
        }
    }

//    private long getDurationFromParams(String paramString)
//    {
//        try
//        {
//            long l = getLong(paramString);
//            return l;
//        }
//        catch (NumberFormatException paramString)
//        {
//            throw new NumberFormatException(String.format("Error in measurement duration definition. Make sure you add a parameter like '-e %s <duration in seconds>' to your invocation", new Object[] { getTag() }));
//        }
//    }

//    private String getTag()
//    {
//        return String.format("%s-%s", new Object[] { getClass().getSimpleName(), getName() });
//    }

    private String getTag(String paramString)
    {
//        return String.format("%s-%s", new Object[] { getTag(), paramString });
          return "null";
    }

    private void setStartAlarm(final String paramString, long paramLong)
            throws IOException
    {
//        Log.d(TAG, String.format("Scheduling start for %s in %d seconds.", new Object[] { paramString, Long.valueOf(paramLong) }));
//        BroadcastReceiver local1 = new BroadcastReceiver()
//        {
//            public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
//            {
//                try
//                {
//                    PowerTestCase.-wrap0(PowerTestCase.this, paramString);
//                    return;
//                }
//                catch (Exception paramAnonymousContext)
//                {
//                    throw new RuntimeException(Log.getStackTraceString(paramAnonymousContext));
//                }
//            }
//        };
//        paramString = AlarmsUtil.setOneTimeAlarm(this.mContext, 1000L * paramLong, "AUTOTEST_TEST_BEGIN_alarm_" + paramString, local1);
//        this.mPendingIntents.add(paramString);
    }

    private void setStopAlarm(final String paramString, long paramLong)
    {
//        Log.d(TAG, String.format("Scheduling stop for %s in %d seconds.", new Object[] { paramString, Long.valueOf(paramLong) }));
//        BroadcastReceiver local2 = new BroadcastReceiver()
//        {
//            public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
//            {
//                try
//                {
//                    PowerTestCase.-wrap1(PowerTestCase.this, paramString);
//                    return;
//                }
//                catch (Exception paramAnonymousContext)
//                {
//                    throw new RuntimeException(Log.getStackTraceString(paramAnonymousContext));
//                }
//            }
//        };
//        paramString = AlarmsUtil.setOneTimeAlarm(this.mContext, 1000L * paramLong, "AUTOTEST_TEST_SUCCESS_alarm_" + paramString, local2);
//        this.mPendingIntents.add(paramString);
    }

    private void validateMeasurementStart(String paramString)
            throws IOException
    {
        if (this.mTags.containsKey(paramString)) {
            throw new UnsupportedOperationException(String.format("Can't start a measurement that was previouslystarted. Use a different suffix. (%s)", new Object[] { paramString }));
        }
        if (!FrameworkUtil.signaledForUsbDisconnection()) {
            FrameworkUtil.disconnectUsb(getInstrumentation());
        }
        this.mTags.put(paramString, "started");
    }

    private void validateMeasurementStop(String paramString)
    {
        if (!this.mTags.containsKey(paramString)) {
            throw new UnsupportedOperationException(String.format("Can't end a measurement that was never started. (%s)", new Object[] { paramString }));
        }
        if (!((String)this.mTags.get(paramString)).equals("started")) {
            throw new UnsupportedOperationException(String.format("Can't end a measurement that was already stopped or scheduled to stop. (%s)", new Object[] { paramString }));
        }
        this.mTags.put(paramString, "stop_scheduled");
    }

    private void writeStartToPowerLog(String paramString)
            throws IOException
    {
        if (this.mActiveMeasurement != null) {
            throw new IllegalStateException("Tried to start a measurement when there was still another one active.");
        }
        String str = String.format("%d %s %s\n", new Object[] { Long.valueOf(System.currentTimeMillis() + 5000L), "AUTOTEST_TEST_BEGIN", paramString });
        FrameworkUtil.writeToFile(POWER_OUTPUT, str);
        Log.d(TAG, String.format("Measurement started for %s", new Object[] { paramString }));
        this.mActiveMeasurement = paramString;
    }

    private void writeStopToPowerLog(String paramString)
            throws IllegalStateException, IOException
    {
        if (this.mActiveMeasurement == null) {
            throw new IllegalStateException(String.format("Tried to end a measurement that was not active. Measurement tag %s.", new Object[] { paramString }));
        }
        if (!this.mActiveMeasurement.equals(paramString)) {
            throw new IllegalStateException(String.format("Tried to end measurement %s while %s was active.", new Object[] { paramString, this.mActiveMeasurement }));
        }
        String str = String.format("%d %s %s\n", new Object[] { Long.valueOf(System.currentTimeMillis() - 5000L), "AUTOTEST_TEST_SUCCESS", paramString });
        FrameworkUtil.writeToFile(POWER_OUTPUT, str);
        Log.d(TAG, String.format("Measurement ending for %s", new Object[] { paramString }));
        closeActiveMeasurement(paramString);
    }

//    protected long getLong(String paramString)
//    {
//        return Long.parseLong(getParams().getString(paramString));
//    }

//    public long getMeasurementDuration()
//    {
//        return getDurationFromParams(getTag());
//    }

//    public long getMeasurementDuration(String paramString)
//    {
//        return getDurationFromParams(getTag(paramString));
//    }

    protected Bundle getParams()
    {
//        return ((AndroidJUnitRunner)getInstrumentation()).getArguments();
        return InstrumentationRegistry.getArguments();
    }

    protected String getString(String paramString)
    {
        return getParams().getString(paramString);
    }
//
    protected UiDevice getUiDevice()
    {
        return UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

//    protected void scheduleMeasurement()
//            throws IOException
//    {
//        String str = getTag();
//        validateMeasurementStart(str);
//        writeStartToPowerLog(str);
//        validateMeasurementStop(str);
//        setStopAlarm(getTag(), getMeasurementDuration());
//    }

//    protected void scheduleMeasurement(long paramLong)
//            throws IOException
//    {
//        String str = getTag();
//        validateMeasurementStart(str);
//        setStartAlarm(str, paramLong);
//        validateMeasurementStop(str);
//        setStopAlarm(str, getMeasurementDuration() + paramLong);
//    }

//    protected void scheduleMeasurement(String paramString, long paramLong)
//            throws IOException
//    {
//        String str = getTag(paramString);
//        validateMeasurementStart(str);
//        setStartAlarm(str, paramLong);
//        validateMeasurementStop(str);
//        setStopAlarm(str, getMeasurementDuration(paramString) + paramLong);
//    }
    @Before
    public void setUp()
            throws Exception {
//        super.setUp();
        this.mContext = getInstrumentation().getContext();
        FrameworkUtil.deleteDisconnectUsbFile();
        muidevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        muidevice.wakeUp();
        muidevice.pressMenu();
        muidevice.pressHome();
    }

    protected void startMeasurement()
            throws IOException
    {
//        validateMeasurementStart(getTag());
//        writeStartToPowerLog(getTag());
    }

    protected void startMeasurement(long paramLong)
            throws IOException
    {
//        validateMeasurementStart(getTag());
//        setStartAlarm(getTag(), paramLong);
    }

    protected void startMeasurement(String paramString, long paramLong)
            throws IOException
    {
        validateMeasurementStart(getTag(paramString));
        setStartAlarm(getTag(paramString), paramLong);
    }

    protected void stopMeasurement()
            throws IOException
    {
//        validateMeasurementStop(getTag());
//        writeStopToPowerLog(getTag());
    }

    protected void stopMeasurement(long paramLong)
            throws IOException
    {
//        validateMeasurementStop(getTag());
//        setStopAlarm(getTag(), paramLong);
    }

    protected void stopMeasurement(String paramString, long paramLong)
            throws IOException
    {
        validateMeasurementStop(getTag(paramString));
        setStopAlarm(getTag(paramString), paramLong);
    }

    public void tearDown()
            throws Exception
    {
        FrameworkUtil.deleteDisconnectUsbFile();
        Iterator localIterator = this.mPendingIntents.iterator();
        while (localIterator.hasNext()) {
            ((PendingIntent)localIterator.next()).cancel();
        }
    }

    protected void waitForMeasurementToComplete()
            throws InterruptedException
    {
        this.mLatch = new CountDownLatch(1);
        Log.d(TAG, "Waiting for measurements to complete.");
        this.mLatch.await();
    }
}