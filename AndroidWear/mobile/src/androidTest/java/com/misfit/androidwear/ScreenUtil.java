package com.misfit.androidwear;

import android.app.Instrumentation;
import android.os.Build;
import android.os.RemoteException;
import android.support.test.uiautomator.UiDevice;
import android.util.Log;

/**
 * Created by zhongweizhou on 16/9/23.
 */

public class ScreenUtil {
    private static final String AMBIENT_DREAM_SERVICE = "com.google.android.wearable.ambient.AmbientDream";
    private static final String TAG = "com.android.test.power.ScreenUtil";
    UiDevice muidevice;

    public static boolean inAmbientMode(Instrumentation paramInstrumentation)
    {
        return FrameworkUtil.isServiceRunning(paramInstrumentation, "com.google.android.wearable.ambient.AmbientDream");
    }

//    public static void setFullScreen(Instrumentation paramInstrumentation)
//            throws RemoteException
//    {
//       paramInstrumentation= UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
////        paramInstrumentation = UiDevice.getInstance(paramInstrumentation);
//        Point localPoint = paramInstrumentation.getDisplaySizeDp();
//        if (localPoint.x < localPoint.y) {
//            paramInstrumentation.setOrientationLeft();
//        }
//    }

    public static void setScreenOff(Instrumentation paramInstrumentation)
            throws RemoteException
    {
        Log.i("com.android.test.power.ScreenUtil", "Setting Screen Off");
        int i;
        if (Build.VERSION.SDK_INT >= 20)
        {
            UiDevice.getInstance(paramInstrumentation).pressKeyCode(223);
            i = 0;
        }
//        for (;;)
//        {
//            if (i >= 40) {
//                break label68;
//            }
//            if (!UiDevice.getInstance().isScreenOn())
//            {
//                return;
//                UiDevice.getInstance(paramInstrumentation).sleep();
//                break;
//            }
//            SystemClock.sleep(250L);
//            i += 1;
//        }
//        label68:
//        throw new IllegalStateException("Couldn't set screen off");
    }
}
