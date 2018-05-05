package com.lei.learningnote;


import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;

import com.lei.mylibrary.Library;
import com.zhy.autolayout.AutoLayoutActivity;

public class MainActivity extends AutoLayoutActivity {


    IMyInterface.Stub binder;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (IMyInterface.Stub) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        String string = null;
//
//        Log.e("tag",string.length()+"");
        Button button = findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    String info = binder.getInfo("1234");
//                    Log.e("tag","info = "+info);
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//
//                try {
//                    Room room = binder.getRoom();
//                    Log.e("tag","number = "+room.getNumber()+" name = "+room.getName());
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        Intent intent = new Intent(this,MyService.class);
//        bindService(intent,conn,BIND_AUTO_CREATE);
//        Intent intent = new Intent(this,TestActivity.class);
//        startActivity(intent);
        ClassB b = new ClassB();
        int[] numbers = new int[]{12,6,9,88,45,33};
        maopao(numbers);
        for(int i = 0; i < numbers.length;i++){
            Log.e("tag","int = "+numbers[i]);
        }
    }

    public void maopao(int[] numbers){
        int mid;
        for(int i = 0;i < numbers.length;i++){
            for(int j = i;j < numbers.length;j++){
                if(numbers[i] > numbers[j]){
                    if(numbers[j] < numbers[i]){
                        mid = numbers[j];
                        numbers[j] = numbers[i];
                        numbers[i] = mid;
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("tag","count = "+ Library.count);
        Library library = new Library(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }
}
