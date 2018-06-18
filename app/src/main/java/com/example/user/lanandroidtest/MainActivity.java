package com.example.user.lanandroidtest;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ServerSocket mServerSocket;

    private int mLocalPort;
    private String mServiceName;
    private String mServiceType;

    private final String TAG = "NSD";

    private NsdManager.RegistrationListener mRegistrationListener;
    private NsdManager.DiscoveryListener mDiscoveryListener;

    private ArrayList<NsdServiceInfo> peers;

    private NsdManager mNsdManager;

    private LinearLayout ipLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializes the layout to hold all peer names
        ipLayout = findViewById(R.id.peerList);

        //Initializes the peer list
        peers =  new ArrayList<NsdServiceInfo>();

        initializeServerSocket();

        //Initializes the registration listener and registers the service
        initializeRegistrationListener();
        registerService(mLocalPort);
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

        //Declares the service name and type
        mServiceName = "DND";
        mServiceType = "_http._tcp.";

        serviceInfo.setServiceName(mServiceName);
        serviceInfo.setServiceType(mServiceType);
        serviceInfo.setPort(port);

        //Displays your current name
        ((TextView) findViewById(R.id.deviceName)).setText(mServiceName);

        mNsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);

        mNsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);

    }

    //Initializes the discovery listener and begins the discovery
    public void beginDiscovery(){

        initializeDiscoveryListener();
        mNsdManager.discoverServices(mServiceType, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);

    }

    //Initializes the discovery listener
    public void initializeDiscoveryListener() {

        // Instantiate a new DiscoveryListener
        mDiscoveryListener = new NsdManager.DiscoveryListener() {

            // Called as soon as service discovery begins.
            @Override
            public void onDiscoveryStarted(String regType) {
                Log.d(TAG, "Service discovery started");
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                // A service was found! Do something with it.
                Log.d(TAG, "Service discovery success" + service);
                if (!service.getServiceType().equals(mServiceType)) {
                    // Service type is the string containing the protocol and
                    // transport layer for this service.
                    Log.d(TAG, "Unknown Service Type: " + service.getServiceType());
                } else if (service.getServiceName().equals(mServiceName)) {
                    // The name of the service tells the user what they'd be
                    // connecting to. It could be "Bob's Chat App".
                    Log.d(TAG, "Same machine: " + mServiceName);
                } else if (service.getServiceName().contains("DND")){
                    //mNsdManager.resolveService(service, mResolveListener);
                    //Adds peer to list and updates list
                    Log.e(TAG, service.getServiceName());
                    peers.add(service);
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
                // When the network service is no longer available.
                // Internal bookkeeping code goes here.
                Log.e(TAG, "service lost" + service);
                //removes peer from the list and updates list
                peers.remove(service);
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.i(TAG, "Discovery stopped: " + serviceType);
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }
        };
    }

    //Initializes the registration listener
    public void initializeRegistrationListener() {
        mRegistrationListener = new NsdManager.RegistrationListener() {

            @Override
            public void onServiceRegistered(NsdServiceInfo NsdServiceInfo) {
                // Save the service name. Android may have changed it in order to
                // resolve a conflict, so update the name you initially requested
                // with the name Android actually used.
                if(NsdServiceInfo.getServiceName() != null)
                    mServiceName = NsdServiceInfo.getServiceName();
                if(NsdServiceInfo.getServiceType() != null)
                    mServiceType = NsdServiceInfo.getServiceType();

                //starts the service
                beginDiscovery();
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

    private void updatePeers(){

        ipLayout.removeAllViews();

        for(NsdServiceInfo peer: peers){

            Button peerName =  new Button(this);
            peerName.setText(peer.getServiceName());
            ipLayout.addView(peerName);

        }

    }

    //Called when the refresh button is pressed
    //Updates the peer list
    public void refreshPeers(View view){
        
        updatePeers();
    }
}
