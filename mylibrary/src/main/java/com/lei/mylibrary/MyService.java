package com.lei.mylibrary;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by jianglei on 2018/4/18.
 */

public class MyService extends Service{

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("tag","MyService create");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.e("tag","MyService start");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("tag","MyService bind");
        MyBinder binder = new MyBinder();
        return binder;
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
        Log.e("tag","MyService unBind");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("tag","MyService onUnbind");
        return super.onUnbind(intent);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("tag","MyService destroy");
    }

    public class MyBinder extends Binder{
        public void method1(){
            Log.e("tag","run method1");
        }
    }

}
