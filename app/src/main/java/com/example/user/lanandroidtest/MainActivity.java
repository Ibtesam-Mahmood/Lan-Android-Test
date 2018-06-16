package com.example.user.lanandroidtest;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;
import java.net.ServerSocket;

public class MainActivity extends AppCompatActivity {

    private ServerSocket mServerSocket;

    private int mLocalPort;
    private String mServiceName;

    private NsdManager.RegistrationListener mRegistrationListener;

    private NsdManager mNsdManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Starts a socket on the next available port
    public void initializeServerSocket(){

        //Inits the socket at the next available port
        try {
            mServerSocket =  new ServerSocket(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //finds the server port
        mLocalPort = mServerSocket.getLocalPort();

    }

    //Creates an nsd object
    //registers it to listen
    public void registerService(int port){

        NsdServiceInfo serviceInfo = new NsdServiceInfo();

        serviceInfo.setServiceName("DND");
        serviceInfo.setServiceName("_dnd._tcp");
        serviceInfo.setPort(port);

        mNsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);

        mNsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);

    }

    public void beginDiscovery(){



    }

    //Initializes the discovery listener
    

    //Initializes the registration listener
    public void initializeRegistrationListener() {
        mRegistrationListener = new NsdManager.RegistrationListener() {

            @Override
            public void onServiceRegistered(NsdServiceInfo NsdServiceInfo) {
                // Save the service name. Android may have changed it in order to
                // resolve a conflict, so update the name you initially requested
                // with the name Android actually used.
                mServiceName = NsdServiceInfo.getServiceName();
            }

            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Registration failed! Put debugging code here to determine why.
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo arg0) {
                // Service has been unregistered. This only happens when you call
                // NsdManager.unregisterService() and pass in this listener.
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Unregistration failed. Put debugging code here to determine why.
            }
        };
    }

    //Called when the refresh button is pressed
    //Updates the peer list
    public void refreshPeers(View view){


    }
}
