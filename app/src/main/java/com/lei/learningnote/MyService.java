package com.lei.learningnote;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by jianglei on 2018/4/18.
 */

public class MyService extends Service{

    private IBinder binder = new IMyInterface.Stub() {
        @Override
        public String getInfo(String s) throws RemoteException {
            return s;
        }

        @Override
        public Room getRoom() throws RemoteException {
            return new Room("111","大床房");
        }
    };
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
        Log.e("tag","unbindService");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
