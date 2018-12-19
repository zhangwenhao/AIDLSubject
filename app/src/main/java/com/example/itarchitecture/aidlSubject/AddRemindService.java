package com.example.itarchitecture.aidlSubject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.itarchitecture.aidlSubject.IRemindManager.Stub;

public class AddRemindService extends Service {

    private AlarmManager alarmManager;
    private PendingIntent pi;

    private final Stub mBinder=new Stub() {
        @Override
        public boolean AddAlarm(RemindEntity remindEntity) {
            try{
                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                Intent intent = new Intent();
                intent.setAction("android.intent.action.REMINDER");
                intent.setPackage("com.example.itarchitecture.aidlSubject");
                pi = PendingIntent.getBroadcast(getApplicationContext(), remindEntity.getRemindId(), intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP, remindEntity.getAlarmTime(), pi);
            }catch(Exception e){
                return false;
            }
            return true;
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

}
