package com.misfit.androidwear;

import android.util.Log;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhongweizhou on 16/9/23.
 */

public class LatchUtil {
    private static final String TAG = LatchUtil.class.getCanonicalName();
    private static CountDownLatch mLatch;
    private static CountDownLatch mTestStart;

    public static void await()
            throws InterruptedException
    {
        if (mLatch != null)
        {
            Log.d(TAG, "Latch is held");
            mLatch.await();
            Log.d(TAG, "Latch released");
            mLatch = null;
        }
    }

    public static void awaitForCountDown(int paramInt)
            throws InterruptedException
    {
        newLatch(paramInt);
        await();
    }

    public static void awaitForCountDownWithTimeOut(int paramInt, long paramLong)
            throws InterruptedException
    {
        newLatch(paramInt);
        awaitWithTimeOut(paramLong);
    }

    public static void awaitWithTimeOut(long paramLong)
            throws InterruptedException
    {
        if (mLatch != null)
        {
            Log.d(TAG, String.format("Latch is held for %d secs", new Object[] { Long.valueOf(paramLong) }));
            mLatch.await(paramLong, TimeUnit.SECONDS);
            Log.d(TAG, "Latch released");
            mLatch = null;
        }
    }

    public static void countDown()
    {
        if (mLatch != null)
        {
            Log.d(TAG, "Latch countdown");
            mLatch.countDown();
        }
    }

    public static void newLatch(int paramInt)
    {
        if (mLatch == null) {
            mLatch = new CountDownLatch(paramInt);
        }
    }

    public static void receivedStartMessage()
    {
        if (mTestStart != null) {
            mTestStart.countDown();
        }
    }

    public static void waitForStartMessage()
            throws InterruptedException
    {
        if (mTestStart == null)
        {
            mTestStart = new CountDownLatch(1);
            Log.d(TAG, "Waiting for test start message");
            mTestStart.await();
            mTestStart = null;
        }
    }
}