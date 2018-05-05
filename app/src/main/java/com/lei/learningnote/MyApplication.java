package com.lei.learningnote;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Created by jianglei on 2018/4/9.
 */

public class MyApplication extends Application{


    @Override
    public void onCreate() {
        super.onCreate();

        ErrorHandler.getInstance().init(this);

    }


    public static class ErrorHandler implements Thread.UncaughtExceptionHandler{

        private static ErrorHandler instance = null;

        private ErrorHandler(){

        }

        public synchronized static ErrorHandler getInstance(){

            if(instance == null){
                instance = new ErrorHandler();
            }
            return instance;
        }

        private void init(Context context){
            Thread.setDefaultUncaughtExceptionHandler(this);
        }

        @Override
        public void uncaughtException(Thread thread, Throwable throwable) {
            Log.e("tag","error = "+throwable);
        }
    }

}
