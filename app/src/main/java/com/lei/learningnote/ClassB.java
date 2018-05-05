package com.lei.learningnote;

import android.util.Log;

/**
 * Created by jianglei on 2018/4/23.
 */

public class ClassB extends ClassA{


    static {
        Log.e("tag","static B");

    }

    {
        Log.e("tag","代码块 B");
    }

    public ClassB(){
        Log.e("tag","构造器 B");
    }


}
