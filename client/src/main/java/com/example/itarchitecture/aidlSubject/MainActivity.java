package com.example.itarchitecture.aidlSubject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private IRemindManager iRemindManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindService();
        findViewById(R.id.btn_b1).setOnClickListener(this);
    }


    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iRemindManager=IRemindManager.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iRemindManager=null;
        }
    };

    private void bindService() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.REMINDER");
        intent.setPackage("com.example.itarchitecture.aidlSubject");
        bindService(intent,mConnection,Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unbindService(mConnection);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        try{
            RemindEntity re=new RemindEntity(System.currentTimeMillis()+1000,"测试",1,"com.example.itarchitecture.aidlsubject.MainActivity");
            iRemindManager.AddAlarm(re);
            Toast.makeText(getApplicationContext(), "success" , Toast.LENGTH_SHORT).show();
        }catch (Exception e){

        }
    }
}
