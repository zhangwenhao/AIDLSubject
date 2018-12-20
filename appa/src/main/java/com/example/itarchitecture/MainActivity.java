package com.example.itarchitecture;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.itarchitecture.aidl.InfoEntity;
import com.example.itarchitecture.aidl.RemindService;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private  AlarmManager alarmManager;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    //save the current reminders
    private List<InfoEntity> reminders=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //init view
        recyclerView=findViewById(R.id.recycle_view);
        //init the alarmManager service
        alarmManager= (AlarmManager) getSystemService( Service.ALARM_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //check alarm info to remove from list if it was expired
        checkAlarmInfo();
        //delete alarm info if other apps deleted the info
        cancelAlarm();
        //remind current alarm info from other apps
        showItemsInfo();
        setData();
    }

    /**
     * show the alarm info from other apps use MultiChoiceAlertDialog
     */
    public void showItemsInfo(){
        if(!RemindService.infoEntities.isEmpty()) {
            final String[] items = new String[RemindService.infoEntities.size()];
            final boolean chosen[] = new boolean[RemindService.infoEntities.size()];
            int cnt=0;
            for (InfoEntity infoEntity : RemindService.infoEntities) {
                items[cnt]=infoEntity.getContent();
                cnt++;
            }
            final AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("Please Check to Remind")
                    .setIcon(R.mipmap.ic_launcher)
                    .setMultiChoiceItems(items, chosen, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                            chosen[i] = b;
                        }
                    })
                    .setPositiveButton("Complete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            for (int j = 0; j < chosen.length; j++) {
                                if (chosen[j]) {
                                    addAlarm(RemindService.infoEntities.get(j));
                                }
                            }
                            RemindService.infoEntities.clear();
                            Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            RemindService.infoEntities.clear();
                        }
                    })
                    .setNeutralButton("Later", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .create();
            alertDialog.show();
        }
    }

    /**
     * check info and remove when it is expired
     */
    private void checkAlarmInfo(){
        for (InfoEntity infoEntity : RemindService.infoEntities) {
            long currentTime=System.currentTimeMillis();
            if(currentTime>=infoEntity.getDateTime()){
                RemindService.infoEntities.remove(infoEntity);
            }
        }
        for (InfoEntity infoEntity : RemindService.cancelInfoEntities) {
            long currentTime=System.currentTimeMillis();
            if(currentTime>=infoEntity.getDateTime()){
                RemindService.cancelInfoEntities.remove(infoEntity);
            }
        }
        for (InfoEntity infoEntity : reminders) {
            long currentTime=System.currentTimeMillis();
            if(currentTime>infoEntity.getDateTime()){
                reminders.remove(infoEntity);
            }
        }
    }

    /**
     * remove alarm timed task
     */
    private void cancelAlarm(){
        if(!RemindService.cancelInfoEntities.isEmpty()){
            for (InfoEntity infoEntity : RemindService.cancelInfoEntities){
                Intent intent = new Intent(MainActivity.this,ReminderActivity.class);
                PendingIntent pi = PendingIntent.getActivity(MainActivity.this, infoEntity.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pi);
                reminders.remove(infoEntity);
            }
        }
    }

    /**
     * and alarm timed task
     * @param infoEntity
     */
    private void addAlarm(InfoEntity infoEntity){
        Intent intent = new Intent(MainActivity.this,ReminderActivity.class);
        intent.putExtra("info",infoEntity.getContent());
        PendingIntent pi = PendingIntent.getActivity(MainActivity.this, infoEntity.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP,infoEntity.getDateTime(), pi);
        if(!reminders.contains(infoEntity)){
            reminders.add(infoEntity);
        }
    }

    /**
     * init view and set scroll event
     */
    private void setData(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter=new RecyclerAdapter(this,reminders);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.setOnScrollListener(new RecyclerAdapter.OnScrollListener() {
            @Override
            public void scrollTo(int pos) {
            recyclerView.scrollToPosition(pos);
            }
        });

    }
}
