package com.example.contacts ;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class SignIn extends AppCompatActivity {
    private EditText useridEditText;
    private EditText passwordEditText     ;
    private Button showHidePasswordButton ;
    private Button savedetailsButton      ;
    private Button gotosignupButton       ;
    private UTILS object = new UTILS() ; // UTILS class consists of validator methods of Inputs :::

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        setContentView(R.layout.activity_sign_in) ;

        initialize_widgets() ;
        OnClickListenerForButtons() ;
    }

    //                                               Widget Initializer   :::
    private void initialize_widgets(){
        useridEditText = findViewById( R.id.et_userid) ;
        passwordEditText       = findViewById( R.id.et_password) ;
        showHidePasswordButton = findViewById( R.id.toggle_pw)   ;
        savedetailsButton      = findViewById( R.id.btn_login)   ;
        gotosignupButton       = findViewById( R.id.btn_signup)  ;
    }
    //                                               Event Listener for Buttons  :::
    private void OnClickListenerForButtons(){
        // Set click listener for the show/hide password button
        showHidePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle password visibility
                if (passwordEditText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    // Show password
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showHidePasswordButton.setBackground(getDrawable(R.drawable.reveal));
                } else {
                    // Hide password
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    showHidePasswordButton.setBackground(getDrawable(R.drawable.hide));
                }
                // Move cursor to the end of the text
                passwordEditText.setSelection(passwordEditText.getText().length());
            }
        });
        // log in button
        savedetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"Not yet functional !",Snackbar.LENGTH_LONG).show() ;
            }
        });
        // sign up button
        gotosignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext() , signup.class) ;
                startActivity(i);
                finish() ;
            }
        });
    }
}