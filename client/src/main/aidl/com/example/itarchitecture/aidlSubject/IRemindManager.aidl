// IRemindManager.aidl
package com.example.itarchitecture.aidlSubject;

import com.example.itarchitecture.aidlSubject.RemindEntity;
// Declare any non-default types here with import statements

interface IRemindManager {

    boolean AddAlarm(in RemindEntity remindEntity);
}
