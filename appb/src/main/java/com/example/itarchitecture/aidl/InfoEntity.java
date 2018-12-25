package com.example.itarchitecture.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Description: RemindService
 * @Author: zwh
 * @CreateDate: 2018/12/20 15:42
 * @Version: 1.0
 */
public class InfoEntity implements Parcelable {

    private int id;
    private long dateTime;
    private String content;

    public InfoEntity(){}

    private InfoEntity(Parcel in){
        readFromParcel(in);
    }

    public static final Creator<InfoEntity> CREATOR = new Creator<InfoEntity>() {
        @Override
        public InfoEntity createFromParcel(Parcel in) {
            return new InfoEntity(in);
        }

        @Override
        public InfoEntity[] newArray(int size) {
            return new InfoEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeLong(dateTime);
        dest.writeString(content);
    }

    private  void readFromParcel(Parcel in){
        id=in.readInt();
        dateTime = in.readLong();
        content = in.readString();
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * For list contains function
     * @param o
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InfoEntity that = (InfoEntity) o;
        return id == that.id;
    }

}
