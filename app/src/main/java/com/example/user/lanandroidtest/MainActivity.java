package com.example.user.lanandroidtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.peak.salut.Callbacks.SalutDataCallback;

public class MainActivity extends AppCompatActivity implements SalutDataCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
