package com.example.user.lanandroidtest;

import android.net.nsd.NsdServiceInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;
import java.net.ServerSocket;

public class MainActivity extends AppCompatActivity {

    private ServerSocket mServerSocket;

    private int mLocalPort;

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

    }

    //Called when the refresh button is pressed
    //Updates the peer list
    public void refreshPeers(View view){


    }
}
