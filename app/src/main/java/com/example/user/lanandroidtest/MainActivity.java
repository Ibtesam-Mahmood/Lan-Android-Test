package com.example.user.lanandroidtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Callbacks.SalutDataCallback;
import com.peak.salut.Salut;
import com.peak.salut.SalutDataReceiver;
import com.peak.salut.SalutServiceData;

public class MainActivity extends AppCompatActivity implements SalutDataCallback {

    private SalutDataReceiver  mDataReceiver;
    private SalutServiceData mServiceData;

    private Salut network;

    private boolean isHosting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializes data receiver and port
        mDataReceiver = new SalutDataReceiver(this, this);
        mServiceData =  new SalutServiceData("p2pTest", 25011, "APP");

        //Defines the network
        network =  new Salut(mDataReceiver, mServiceData, new SalutCallback() {
            @Override
            public void call() {
                Log.e("P2P", "Sorry, but this device does not support WiFi Direct.");
            }
        });

    }

    @Override
    public void onDataReceived(Object o) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    //Called when the refresh button is pressed
    //Updates the peer list
    public void refreshPeers(View view){


    }
}
