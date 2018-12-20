package com.example.itarchitecture;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * @Description: ReminderActivity
 * @Author: zwh
 * @CreateDate: 2018/12/20 16:22
 * @Version: 1.0
 */
public class ReminderActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        Intent intent = getIntent();
        String content = intent.getStringExtra("info");
        mediaPlayer=MediaPlayer.create(this,R.raw.audio);
        mediaPlayer.start();
        new AlertDialog.Builder(ReminderActivity.this)
                .setTitle("Clock")
                .setMessage(content)
                .setPositiveButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mediaPlayer.stop();
                        ReminderActivity.this.finish();
                    }
                })
                .show();
    }

}
