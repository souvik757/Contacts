package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

public class NoConnectivity extends AppCompatActivity {

    private UTILS object = new UTILS() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_connectivity);
    }

    public void Retry(View view) {
        // Checking Internet Connectivity :::
        if (!object.IsConnected(getApplicationContext()))
            Toast.makeText(this, "Please connect to internet"+"\nthen try again", Toast.LENGTH_SHORT).show();
        else {
            Intent i = new Intent(getApplicationContext() , SignIn.class) ;
            startActivity(i) ;
            finish() ;
        }
    }
}