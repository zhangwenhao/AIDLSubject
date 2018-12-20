package com.example.itarchitecture.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.List;


/**
 * @Description: RemindService
 * @Author: zwh
 * @CreateDate: 2018/12/20 15:47
 * @Version: 1.0
 */
public class RemindService extends Service {

    public final static List<InfoEntity> infoEntities=new ArrayList<>();
    public final static List<InfoEntity> cancelInfoEntities=new ArrayList<>();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IRemind.Stub mBinder=new IRemind.Stub() {
        @Override
        public void sendInfo(InfoEntity infoEntity) throws RemoteException {
           String appName=getPackageManager().getNameForUid(Binder.getCallingUid());
           appName=appName==null?"unknown App":appName;
           infoEntity.setId(appName.hashCode()+infoEntity.getId());
           infoEntity.setContent(appName+infoEntity.getContent());
           infoEntities.add(infoEntity);
        }

        @Override
        public void cancelInfo(InfoEntity infoEntity) throws RemoteException {
            String appName=getPackageManager().getNameForUid(Binder.getCallingUid());
            appName=appName==null?"unknown App":appName;
            infoEntity.setId(appName.hashCode()+infoEntity.getId());
            infoEntity.setContent(appName+infoEntity.getContent());
            cancelInfoEntities.add(infoEntity);
        }
    };
}