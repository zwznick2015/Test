package com.misfit.androidwear;

/**
 * Created by zhongweizhou on 16/9/23.
 */

import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.content.Intent.FLAG_RECEIVER_FOREGROUND;

//import android.app.ActivityManagerNative;

public class FrameworkUtil
{
    private static final String SD_CARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    private static final String DISCONNECT_USB = SD_CARD_PATH + "disconnectusb.log";
    private static final int IDLE_STEP_DELAY = 2000;
    private static final String IDLE_STEP_OUTPUT_PREFIX = "Stepped to:";
    private static final String POWER_OUTPUT;
    private static final String TAG = "com.android.test.power.FrameworkUtil";
    private static final String PSERVICE = "power";
    public static final String PAUDIO = "audio";

    static
    {
        POWER_OUTPUT = SD_CARD_PATH + "autotester.log";
    }

    private static Intent buildIntent(String paramString1, String paramString2, String paramString3, Uri paramUri, String paramString4)
    {
        Intent localIntent = new Intent();
        localIntent.addFlags(FLAG_RECEIVER_FOREGROUND);
        if ((paramString1 != null) && (paramString2 != null)) {
            localIntent.setComponent(new ComponentName(paramString1, paramString2));
        }
        if (paramString3 != null) {
            localIntent.setAction(paramString3);
        }
        for (;;)
        {
            if ((paramUri != null) && (paramString4 != null)) {
                localIntent.setDataAndType(paramUri, paramString4);
            }
            localIntent.setAction("android.intent.action.VIEW");
            return localIntent;

        }
    }

    public static void deleteDisconnectUsbFile()
            throws IOException
    {
//        deleteFileIfExists(DISCONNECT_USB);
    }

    public static void deleteFileIfExists(String paramString)
            throws IOException, SecurityException
    {
//        paramString = new File(paramString);
//        if (paramString.exists()) {
//            paramString.delete();
//        }
    }

    public static void disconnectUsb(Instrumentation paramInstrumentation)
            throws IOException
    {
        deleteFileIfExists(DISCONNECT_USB);
        writeToFile(DISCONNECT_USB, "readyToDisconnectUsb=true");
        int i = 0;
        while (i < 20)
        {
            if (!isDeviceCharging(paramInstrumentation))
            {
                Log.d("com.android.test.power.FrameworkUtil", "USB has been disconnected.");
                return;
            }
            Log.d("com.android.test.power.FrameworkUtil", "waiting for USB to be disconnected.");
            SystemClock.sleep(500L);
            i += 1;
        }
        Log.e("com.android.test.power.FrameworkUtil", "USB was not disconnected.");
    }

    public static void enterDozeMode(Instrumentation paramInstrumentation)
            throws IOException, InterruptedException, RemoteException
    {
        PowerManager.WakeLock localWakeLock = ((PowerManager)paramInstrumentation.getContext().getSystemService(PSERVICE)).newWakeLock(1, "com.android.test.power.FrameworkUtil");
        localWakeLock.acquire();
        disconnectUsb(paramInstrumentation);
        ScreenUtil.setScreenOff(paramInstrumentation);
        int i = 0;
        for (;;)
        {
            if (i < 10) {}
            try
            {
                Log.d("com.android.test.power.FrameworkUtil", String.format("doze mode step %d", new Object[] { Integer.valueOf(i + 1) }));
                idleStateStepNext();
                if (inDozeMode())
                {
                    if (inDozeMode()) {
                        Log.d("com.android.test.power.FrameworkUtil", "Device is now in IDLE state");
                    }
                }
                else
                {
                    Log.d("com.android.test.power.FrameworkUtil", "still not in doze mode.");
                    SystemClock.sleep(2000L);
                    i += 1;
                    continue;
                }
                Log.d("com.android.test.power.FrameworkUtil", "Device did not enter IDLE state");
                throw new IllegalStateException("Device did not enter IDLE state");
            }
            finally
            {
                localWakeLock.release();
            }
        }
    }

    public static String executeCommand(String paramString)
            throws IOException, InterruptedException
    {
        StringBuilder localStringBuilder = new StringBuilder();
        Log.d("com.android.test.power.FrameworkUtil", String.format("excecuting: %s", new Object[] { paramString }));
        try
        {
            Process localProcess = Runtime.getRuntime().exec(paramString);
            BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localProcess.getInputStream()));
            for (;;)
            {
                String str = localBufferedReader.readLine();
                if (str == null) {
                    break;
                }
                localStringBuilder.append(str);
                localStringBuilder.append("\n");
            }
            localProcess.waitFor();
        }
        catch (IOException localIOException)
        {
            throw new IOException(String.format("Fails to execute command: %s ", new Object[] { paramString }), localIOException);
        }
        //add by myself

        IOException localIOException = new IOException();
        Log.d("com.android.test.power.FrameworkUtil", String.format("output:\n%s", new Object[] { localIOException.toString() }));
        return localIOException.toString();
    }

    private static void idleStateStepNext()
            throws IOException, InterruptedException
    {
        String str = executeCommand("dumpsys deviceidle step");
        Log.d("com.android.test.power.FrameworkUtil", str);
        if (!str.startsWith("Stepped to:")) {
            throw new IllegalStateException("Incorrect response from 'dumpsys deviceidle step'");
        }
    }

    public static boolean inDozeMode()
            throws IOException, InterruptedException
    {
        String str = executeCommand("dumpsys deviceidle");
        return (str.contains("mState=IDLE\n")) || (str.contains("mState=IDLE "));
    }

    public static boolean isDeviceCharging(Instrumentation paramInstrumentation)
    {
        IntentFilter localIntentFilter = new IntentFilter("android.intent.action.BATTERY_CHANGED");
        int j = paramInstrumentation.getContext().registerReceiver(null, localIntentFilter).getIntExtra("plugged", -1);
        int i;
        if (j == 2)
        {
            i = 1;
            if (j != 1) {
//                break label55;
            }
        }
        label55:
        for (boolean bool = true;; bool = false)
        {
//            if (i != 0) {
//                break label60;
//            }
            return bool;
//            i = 0;
//            break;
        }
//        label60:
//        return true;
    }

//    public static boolean isForegroundApp(String paramString)
//            throws RemoteException
//    {
//        Object localObject = ActivityManagerNative.getDefault().getRunningAppProcesses();
//        if (localObject == null) {
//            return false;
//        }
//        localObject = ((Iterable)localObject).iterator();
//        while (((Iterator)localObject).hasNext())
//        {
//            ActivityManager.RunningAppProcessInfo localRunningAppProcessInfo = (ActivityManager.RunningAppProcessInfo)((Iterator)localObject).next();
//            if ((localRunningAppProcessInfo.importance == 100) && (localRunningAppProcessInfo.processName.equals(paramString))) {
//                return true;
//            }
//        }
//        return false;
//    }

    public static boolean isMusicPlaying(Instrumentation paramInstrumentation)
    {
//        return ((AudioManager)paramInstrumentation.getContext().getSystemService(PAUDIO)).isMusicActive();
        return true;
    }

    public static boolean isProcessRunning(String paramString)
            throws IOException, InterruptedException
    {
        return executeCommand("ps").contains(paramString);
    }

    public static boolean isServiceRunning(Instrumentation paramInstrumentation, String paramString)
    {
//        ActivityManager actManager = (ActivityManager) paramInstrumentation.getContext().getSystemService(Context.ACTIVITY_SERVICE);
//        Log.v("com.android.test.power.FrameworkUtil", String.format("Looking for service: %s", new Object[] { paramString }));
//        Log.v("com.android.test.power.FrameworkUtil", "List of running services:");
////        actManager = paramInstrumentation.getRunningServices(Integer.MAX_VALUE).iterator();
////        while (paramInstrumentation.hasNext())
//        {
////            ActivityManager.RunningServiceInfo localRunningServiceInfo = (ActivityManager.RunningServiceInfo)paramInstrumentation.next();
//            Log.v("com.android.test.power.FrameworkUtil", localRunningServiceInfo.service.getClassName());
//            if (paramString.equals(localRunningServiceInfo.service.getClassName()))
//            {
//                Log.v("com.android.test.power.FrameworkUtil", String.format("Found %s.", new Object[] { paramString }));
//                return true;
//            }
//        }
//        Log.v("com.android.test.power.FrameworkUtil", String.format("Couldnt't find %s.", new Object[] { paramString }));
        return false;
    }

    public static void launchActivity(Instrumentation paramInstrumentation, Intent paramIntent, String paramString)
    {
        if (paramString == null) {
            throw new UnsupportedOperationException("Package Name should not be null");
        }
        Log.d("com.android.test.power.FrameworkUtil", "Launching activity");
        Log.d("com.android.test.power.FrameworkUtil", String.format("Activity: %s", new Object[] { paramIntent.getPackage() }));
        Log.d("com.android.test.power.FrameworkUtil", String.format("Action:   %s", new Object[] { paramIntent.getAction() }));
        Log.d("com.android.test.power.FrameworkUtil", String.format("Data:     %s", new Object[] { paramIntent.getData() }));
        Log.d("com.android.test.power.FrameworkUtil", String.format("type:     %s", new Object[] { paramIntent.getType() }));
        paramInstrumentation.getContext().startActivity(paramIntent);
    }

    public static void launchActivity(Instrumentation paramInstrumentation, String paramString1, String paramString2)
            throws Exception
    {
        launchActivity(paramInstrumentation, paramString1, paramString2, null, null, null);
    }

    public static void launchActivity(Instrumentation paramInstrumentation, String paramString1, String paramString2, String paramString3)
            throws Exception
    {
        launchActivity(paramInstrumentation, paramString1, paramString2, paramString3, null, null);
    }

    public static void launchActivity(Instrumentation paramInstrumentation, String paramString1, String paramString2, String paramString3, Uri paramUri, String paramString4)
            throws Exception
    {
        launchActivity(paramInstrumentation, buildIntent(paramString1, paramString2, paramString3, paramUri, paramString4), paramString1);
    }

    public static boolean signaledForUsbDisconnection()
    {
        return new File(DISCONNECT_USB).exists();
    }

    public static void writeToFile(String paramString1, String paramString2)
            throws IOException
    {
//        paramString1 = new FileWriter(new File(paramString1), true);
//        paramString1.write(paramString2);
//        paramString1.close();
    }
}