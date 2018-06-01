package com.example.user.lanandroidtest;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Callbacks.SalutDataCallback;
import com.peak.salut.Callbacks.SalutDeviceCallback;
import com.peak.salut.Salut;
import com.peak.salut.SalutDataReceiver;
import com.peak.salut.SalutDevice;
import com.peak.salut.SalutServiceData;

public class MainActivity extends AppCompatActivity implements SalutDataCallback {

    private SalutDataReceiver  mDataReceiver;
    private SalutServiceData mServiceData;

    private Salut network;

    private String deviceName;

    private ToggleButton hostButton;
    private ToggleButton discoverButton;
    private LinearLayout ipLayout;

    private MainActivity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Sets the layout fields
        discoverButton = findViewById(R.id.button);
        hostButton = findViewById(R.id.host);
        ipLayout =  findViewById(R.id.peerList);

        //Device Name set
        deviceName = Build.MODEL;
        ((TextView) findViewById(R.id.deviceName)).setText(deviceName);

        //Initializes data receiver and port
        mDataReceiver = new SalutDataReceiver(activity, activity);
        mServiceData =  new SalutServiceData("p2pTest", 25011, deviceName);

        //Defines the network
        network =  new Salut(mDataReceiver, mServiceData, new SalutCallback() {
            @Override
            public void call() {
                Log.e("P2P", "Sorry, but this device does not support WiFi Direct.");
            }
        });

        //Sets the on check listener for the host and discover buttons
        hostButton.setOnCheckedChangeListener(host());
        discoverButton.setOnCheckedChangeListener(discover());

    }

    //When the host button is toggled the application enters host mode
    public CompoundButton.OnCheckedChangeListener host(){

        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked){ //Hosting

                    //Starts the network
                    network.startNetworkService(new SalutDeviceCallback() {
                        @Override
                        public void call(SalutDevice device) {
                            Toast.makeText(activity, device.deviceName + " Has connected", Toast.LENGTH_SHORT).show();
                        }
                    });
                    discoverButton.setClickable(false); //Ensures that the discover button cannot be clicked

                }
                else{ //Not Hosting

                    network.stopNetworkService(false); //Disables the network
                    discoverButton.setClickable(true); //Ensures that the discover button can be clicked

                }
            }
        };

    }

    //Called when the discover button is toggled, application enters client mode
    public CompoundButton.OnCheckedChangeListener discover(){

        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked){ //Discovering

                    //Starts the discovering for networks
                    network.discoverNetworkServices(new SalutDeviceCallback() {
                        @Override
                        //Adds the host to the list
                        public void call(SalutDevice device) {
                            TextView host =  new TextView(activity);
                            host.setText(device.deviceName);
                            ipLayout.addView(host);
                        }
                    }, false);
                    hostButton.setClickable(false); //Ensures that the host button cannot be clicked

                }
                else{ //Not Discovering

                    network.stopServiceDiscovery(true); //Disables the search
                    hostButton.setClickable(true); //Ensures that the host button can be clicked

                }
            }
        };

    }

    @Override
    public void onDataReceived(Object o) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
