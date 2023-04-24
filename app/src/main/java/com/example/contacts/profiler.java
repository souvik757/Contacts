package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class profiler extends AppCompatActivity {
    // Declaring widgets :::
    private TextView user_id ;
    private TextView user_name ;
    private TextView user_email ;
    private TextView user_phone ;
    private TextView user_password ;
    private Button edit ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiler);
        // Initialize Widgets :::
        InitializeWidgets() ;
        // Fetching Intent content :::
        Intent i = getIntent() ;
        // Unique UserID for generated on each sign up :::
        String User_ID   = i.getStringExtra("USERID")       ;
        String FullName  = i.getStringExtra("USERNAME")     ;
        String UserEmail = i.getStringExtra("USERMAIL")     ;
        String UserPhone = i.getStringExtra("USERPH")       ;
        String UserPass  = i.getStringExtra("USERPASSWORD") ;
        // Set Contents to Textview :::
        SetContents(User_ID,FullName,UserEmail,UserPhone,UserPass);
        // Button Event Listener :::
        ButtonEventListener() ;
    }
    private void InitializeWidgets() {
        user_id       = findViewById(R.id.DBUserName) ;
        user_name     = findViewById(R.id.FullName)   ;
        user_email    = findViewById(R.id.Email)      ;
        user_phone    = findViewById(R.id.Phone)      ;
        user_password = findViewById(R.id.Password)   ;
        edit          = findViewById(R.id.Edit)       ;
    }

    private void SetContents(String id, String name, String mail, String ph, String pw){
        user_id.setText(id)       ;
        user_name.setText(name)   ;
        user_email.setText(mail)  ;
        user_phone.setText(ph)    ;
        user_password.setText(pw) ;
    }

    private void ButtonEventListener() {
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message(v,"come back later 👀 ") ;
            }
        });
    }
    @Override
    public void onBackPressed() {
        finish() ;

    }
    private void message(View view , String s){
        Snackbar.make(view,s,Snackbar.LENGTH_LONG).show() ;
    }
}