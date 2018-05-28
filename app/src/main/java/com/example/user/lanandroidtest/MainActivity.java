package com.example.user.lanandroidtest;

import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.adroitandroid.near.discovery.NearDiscovery;
import com.adroitandroid.near.model.Host;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final long DISCOVERABLE_TIMEOUT_MILLIS = 60000;
    private static final long DISCOVERY_TIMEOUT_MILLIS = 10000;
    private static final long DISCOVERABLE_PING_INTERVAL_MILLIS = 5000;
    private NearDiscovery mNearDiscovery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNearDiscovery = new NearDiscovery.Builder()
                .setContext(this)
                .setDiscoverableTimeoutMillis(DISCOVERABLE_TIMEOUT_MILLIS)
                .setDiscoveryTimeoutMillis(DISCOVERY_TIMEOUT_MILLIS)
                .setDiscoverablePingIntervalMillis(DISCOVERABLE_PING_INTERVAL_MILLIS)
                .setDiscoveryListener(getNearDiscoveryListener(), Looper.getMainLooper())
                .build();
    }

    //Called when the refresh button is pressed
    //Updates the peer list
    public void refreshPeers(View view){


    }

    private NearDiscovery.Listener getNearDiscoveryListener() {
        return new NearDiscovery.Listener() {
            @Override
            public void onPeersUpdate(Set<Host> hosts) {
                // Handle updates of peer list here - some peer might have got removed if it wasn't reachable anymore or some new peer might have been added
            }

            @Override
            public void onDiscoveryTimeout() {
                // This is called after the discovery timeout (specified in the builder) from starting discovery using the startDiscovery()
            }

            @Override
            public void onDiscoveryFailure(Throwable e) {
                // This is called if discovery could not be started
            }

            @Override
            public void onDiscoverableTimeout() {
                // This is called after the discoverable timeout (specified in the builder) from becoming discoverable by others using the makeDiscoverable()
            }
        };
    }
}
