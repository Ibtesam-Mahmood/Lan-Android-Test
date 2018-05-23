package com.example.user.lanandroidtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final IntentFilter intentFilter = new IntentFilter();

    private WifiP2pManager.Channel mChannel;
    private WifiP2pManager mManager;

    private BroadcastReceiver mReceiver;

    private boolean wifiP2pState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    //Initializes necessary code to run the application
    private void init(){

        // Indicates a change in the Wi-Fi P2P status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);

        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

        // Indicates the state of Wi-Fi P2P connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        //Used tp connect to the P2P framework
        mManager = (WifiP2pManager) getSystemService(this.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);

        //Initializes the receiver
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);
    }

    //Sets the wifi p2p state for the application
    //Prompts the user about their p2p communications state
    public void setIsWifiP2pEnabled(boolean state){
        this.wifiP2pState = state;
        if(state) {
            Toast.makeText(this, "WiFi P2P Communications Enabled", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Please Enable WiFi P2P Communications", Toast.LENGTH_SHORT).show();
        }
    }

    public void displayPeers(ArrayList<WifiP2pDevice> deviceList){

        ArrayList<String> ips = new ArrayList<>();

        for (WifiP2pDevice device : deviceList){
            ips.add(device.deviceAddress);
        }



    }

}
