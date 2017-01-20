package com.misfit.androidwear;

/**
 * Created by zhongweizhou on 16/10/5.
 */

public abstract interface MessageEvent {
    public abstract byte[] getData();

    public abstract String getPath();

    public abstract int getRequestId();

    public abstract String getSourceNodeId();
}
