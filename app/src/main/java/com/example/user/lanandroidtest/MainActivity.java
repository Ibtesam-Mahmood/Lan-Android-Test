package com.example.user.lanandroidtest;

import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adroitandroid.near.connect.NearConnect;
import com.adroitandroid.near.discovery.NearDiscovery;
import com.adroitandroid.near.model.Host;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final long DISCOVERABLE_TIMEOUT_MILLIS = 60000;
    private static final long DISCOVERY_TIMEOUT_MILLIS = 10000;
    private static final long DISCOVERABLE_PING_INTERVAL_MILLIS = 5000;

    private NearDiscovery mNearDiscovery;
    private NearConnect mNearConnect;

    private LinearLayout ipLayout;
    private MainActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNearDiscovery = new NearDiscovery.Builder()
                .setContext(this)
                .setDiscoverableTimeoutMillis(DISCOVERABLE_TIMEOUT_MILLIS)
                .setDiscoveryTimeoutMillis(DISCOVERY_TIMEOUT_MILLIS)
                .setDiscoveryListener(getNearDiscoveryListener(), Looper.getMainLooper())
                .build();

        mNearConnect = new NearConnect.Builder()
                .fromDiscovery(mNearDiscovery)
                .setContext(this)
                .setListener(getNearConnectListener(), Looper.getMainLooper())
                .build();

        ipLayout = findViewById(R.id.peerList);

        mNearDiscovery.makeDiscoverable(android.os.Build.MODEL);
        ( (TextView) findViewById(R.id.deviceName)).setText(android.os.Build.MODEL);

        activity = this;
    }

    //Called when the refresh button is pressed
    //Updates the peer list
    public void refreshPeers(View view){

        ipLayout.removeAllViews();
        mNearDiscovery.makeDiscoverable(android.os.Build.MODEL);
        mNearDiscovery.startDiscovery();
        ((Button) findViewById(R.id.refresh)).setText("Discovering...");

    }

    //Displays the list of peers available to connect to
    public void displayPeers(ArrayList<Host> peers){

        ipLayout.removeAllViews();

        for(int i = 0; i < peers.size(); i++){

            Button peer = new Button(this);

            peer.setText(peers.get(i).getName());

            ipLayout.addView(peer);

        }

    }

    //Creates a listener used by NearDiscovery
    private NearDiscovery.Listener getNearDiscoveryListener() {
        return new NearDiscovery.Listener() {
            @Override
            public void onPeersUpdate(Set<Host> hosts) {
                //Obtains the list of peers and calls a method to display them
                Log.e("Peer", "Peers discovered");
                ArrayList<Host> availablePeers = new ArrayList<>(hosts);
                displayPeers(availablePeers);
            }

            @Override
            public void onDiscoveryTimeout() {
                mNearDiscovery.stopDiscovery();
                ((Button) findViewById(R.id.refresh)).setText("Refresh");
                Toast.makeText(activity, "Discovery timeout", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDiscoveryFailure(Throwable e) {
                Log.e("Peer", "Something went wrong while searching for participants");
            }

            @Override
            public void onDiscoverableTimeout() {
                Log.e("Peer", "You're not discoverable anymore");
            }

        };
    }

    //Creates a listener used by NearConnect
    private NearConnect.Listener getNearConnectListener() {
        return new NearConnect.Listener() {
            @Override
            public void onReceive(byte[] bytes, final Host sender) {
                // Process incoming data here
            }

            @Override
            public void onSendComplete(long jobId) {
                // jobId is the same as the return value of NearConnect.send(), an approximate epoch time of the send
            }

            @Override
            public void onSendFailure(Throwable e, long jobId) {
                // handle failed sends here
            }

            @Override
            public void onStartListenFailure(Throwable e) {
                // This tells that the NearConnect.startReceiving() didn't go through properly.
                // Common cause would be that another instance of NearConnect is already listening and it's NearConnect.stopReceiving() needs to be called first
            }
        };
    }
}
