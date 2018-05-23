package com.example.user.lanandroidtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;

/**
 * Created by User on 5/23/2018.
 */

public class WifiDirectBroadcastReciever extends BroadcastReceiver {

    WifiP2pManager manager;
    WifiP2pManager.Channel channel;
    MainActivity activity;


    public WifiDirectBroadcastReciever(WifiP2pManager tManager, WifiP2pManager.Channel tChannel, MainActivity tActivity){
        super();
        this.manager = tManager;
        this.channel = tChannel;
        this.activity = tActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {

        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {


        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {


        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {

        }
    }

    
}
