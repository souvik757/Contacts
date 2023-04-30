package com.example.contacts.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.contacts.Activity.SignIn;
import com.example.contacts.R;
import com.example.contacts.UTILS;

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