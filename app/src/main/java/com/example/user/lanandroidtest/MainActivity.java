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
    private static final String ADD_NUMBER = "add_number";

    private NearDiscovery mNearDiscovery;
    private NearConnect mNearConnect;

    private ArrayList<Host> peers;

    private LinearLayout ipLayout;
    private TextView number;
    private Button add;
    private Button connectButton;
    private MainActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //discovery builder
        mNearDiscovery = new NearDiscovery.Builder()
                .setContext(this)
                .setDiscoverableTimeoutMillis(DISCOVERABLE_TIMEOUT_MILLIS)
                .setDiscoveryTimeoutMillis(DISCOVERY_TIMEOUT_MILLIS)
                .setDiscoveryListener(getNearDiscoveryListener(), Looper.getMainLooper())
                .build();

        //conencion builder
        mNearConnect = new NearConnect.Builder()
                .fromDiscovery(mNearDiscovery)
                .setContext(this)
                .setListener(getNearConnectListener(), Looper.getMainLooper())
                .build();

        //Initializes all view components
        ipLayout = findViewById(R.id.peerList);
        connectButton = findViewById(R.id.connect);
        add = findViewById(R.id.add);
        number = findViewById(R.id.number);

        //makes the app discoverable
        mNearDiscovery.makeDiscoverable(android.os.Build.MODEL);
        ( (TextView) findViewById(R.id.deviceName)).setText(android.os.Build.MODEL);

        activity = this;
        peers = new ArrayList<>();

        //sets the connect/add button to un-pressable initially
        connectButton.setEnabled(false);
        //add.setEnabled(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mNearDiscovery.stopDiscovery();
        mNearDiscovery.makeNonDiscoverable();
        mNearConnect.stopReceiving(true);
    }

    //Called when the refresh button is pressed
    //Starts the discovery for peers
    public void refreshPeers(View view){

        ipLayout.removeAllViews();
        mNearDiscovery.makeDiscoverable(android.os.Build.MODEL);
        mNearDiscovery.startDiscovery();
        ((Button) findViewById(R.id.refresh)).setText("Discovering...");

    }

    //When the connect button is pressed
    //Connects to all peers
    public void connectPeers(View view){

        mNearConnect.startReceiving();

        //enables the add button
        //add.setEnabled(true);

    }

    //Sends add command to all peers and adds 1 to the counter
    public void addButton(View view){

        addNumber();

        for(Host peer: peers){
           // mNearConnect.send(ADD_NUMBER.getBytes(), peer);
        }


    }

    //Adds 1 to the counter
    public void addNumber(){

        int num = Integer.parseInt( number.getText().toString() );
        num++;
        number.setText(num + "");

    }

    //Displays the list of peers available to connect to
    public void displayPeers(ArrayList<Host> peers){

        ipLayout.removeAllViews();

        this.peers = peers;

        for(int i = 0; i < peers.size(); i++){

            TextView peer = new TextView(this);

            peer.setText(peers.get(i).getName());

            ipLayout.addView(peer);

        }

        // enables/disables connect button dep[ending on if there are possible peers
        if(peers.size() > 0)
            connectButton.setEnabled(true);
        else
            connectButton.setEnabled(false);

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
                if(bytes != null){
                    //handles all recive cases
                    switch(new String(bytes)){

                        case ADD_NUMBER:
                            addNumber();
                            break;

                    }

                }
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
