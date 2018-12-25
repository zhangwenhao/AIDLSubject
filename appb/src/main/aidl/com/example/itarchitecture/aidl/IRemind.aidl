// IRemind.aidl
package com.example.itarchitecture.aidl;

// Declare any non-default types here with import statements
import com.example.itarchitecture.aidl.InfoEntity;

interface IRemind {

    /**
     * The oneway keyword modifies the behavior of remote calls.
     * When used, a remote call does not block.
     * If oneway is used with a local call, there is no impact and
     * the call is still synchronous.
     *
     * 'in' represents the input parameter
     * Server can obtain the data passed by the Client,
     * but can not modify the data of the client
     */
    //add,update remind info
    oneway void sendInfo(in InfoEntity infoEntity);
    //delete remind info
    oneway void cancelInfo(in InfoEntity infoEntity);
}
