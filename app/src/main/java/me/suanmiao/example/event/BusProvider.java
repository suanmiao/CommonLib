package me.suanmiao.example.event;

import com.squareup.otto.Bus;

/**
 * Created by suanmiao on 14/12/29.
 */
public class BusProvider {
    private static Bus mBus;

    public static Bus getInstance() {
        if (mBus == null) {
            mBus = new Bus();
        }
        return mBus;
    }
}
