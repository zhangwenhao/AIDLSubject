package com.example.itarchitecture.aidlSubject;


import android.os.Parcel;
import android.os.Parcelable;

public class RemindEntity implements Parcelable {

    private long alarmTime;

    private String content;

    private int remindId;

    private String registerAction;

    public RemindEntity(long alarmTime, String content,int remindId,String registerAction) {
        this.alarmTime = alarmTime;
        this.content = content;
        this.remindId=remindId;
        this.registerAction=registerAction;
    }

    public RemindEntity(Parcel source){
        this.alarmTime=source.readLong();
        this.content=source.readString();
        this.remindId=source.readInt();
        this.registerAction=source.readString();
    }

    public long getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(long alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRemindId() {
        return remindId;
    }

    public void setRemindId(int remindId) {
        this.remindId = remindId;
    }

    public String getRegisterAction() {
        return registerAction;
    }

    public void setRegisterAction(String registerAction) {
        this.registerAction = registerAction;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.alarmTime);
        dest.writeString(this.content);
        dest.writeInt(this.remindId);
        dest.writeString(this.registerAction);
    }

    public static  final Creator<RemindEntity> CREATOR=new Creator<RemindEntity>() {
        @Override
        public RemindEntity createFromParcel(Parcel source) {
            return new RemindEntity(source);
        }

        @Override
        public RemindEntity[] newArray(int size) {
            return new RemindEntity[size];
        }
    };
}
