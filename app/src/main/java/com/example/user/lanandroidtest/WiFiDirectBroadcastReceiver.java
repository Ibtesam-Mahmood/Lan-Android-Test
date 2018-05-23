package com.example.user.lanandroidtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by User on 5/23/2018.
 */

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    private  WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private MainActivity activity;


    public WiFiDirectBroadcastReceiver(WifiP2pManager tManager, WifiP2pManager.Channel tChannel, MainActivity tActivity){
        super();
        this.manager = tManager;
        this.channel = tChannel;
        this.activity = tActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {

            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                activity.setIsWifiP2pEnabled(true);
            } else {
                activity.setIsWifiP2pEnabled(false);
            }

        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

            manager.requestPeers(channel, new WifiP2pManager.PeerListListener() {
                @Override
                public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
                    ArrayList<WifiP2pDevice> deviceList = new ArrayList<>(wifiP2pDeviceList.getDeviceList());
                    activity.displayPeers(deviceList);
                }
            });

        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            //connected or disconnected

        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            //dwbi
        }
    }

    public void refreshList(View view){

        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.e("Peer", "Peers discovered");
            }

            @Override
            public void onFailure(int i) {
                Log.e("Peer", "No peers discovered");
            }
        });

    }

}
