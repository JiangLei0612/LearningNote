package com.lei.learningnote;

import android.util.Log;

/**
 * Created by jianglei on 2018/4/23.
 */

public class ClassA {

    static {
        Log.e("tag","static A");
    }

    {
        Log.e("tag","代码块 A");
    }

    public ClassA(){
        Log.e("tag","构造器 A");
    }

}
