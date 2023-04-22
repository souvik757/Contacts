package com.example.contacts ;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignIn extends AppCompatActivity {
    private EditText usernameEditText ;
    private EditText passwordEditText ;
    private Button showHidePasswordButton ;
    private Button savedetailsButton ;
    private Button gotosignupButton ;
    private String child = "USER" ;
    private int count ;
    private UTILS object = new UTILS() ;


    // Firebase instances
    private FirebaseFirestore DB = FirebaseFirestore.getInstance() ;
    // KEY's
    private static final String KEY_NAME = "username" ;
    private static final String KEY_PW = "password" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        setContentView(R.layout.activity_sign_in) ;

        initialize_widgets() ;
        SaveUserCount() ; // Restore preferences data's :
        OnClickListenerForButtons() ;
    }
    //                                               Widget Initializer   :::
    private void initialize_widgets(){
        usernameEditText = findViewById(R.id.et_username) ;
        passwordEditText = findViewById(R.id.et_password) ;
        showHidePasswordButton = findViewById(R.id.toggle_pw) ;
        savedetailsButton = findViewById(R.id.btn_login) ;
        gotosignupButton = findViewById(R.id.btn_signup) ;
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
                String password = passwordEditText.getText().toString() ;
                String username = usernameEditText.getText().toString() ;
                if(!object.passwordValidator(password))
                    Snackbar.make(v , "Invalid Password !" , Snackbar.LENGTH_LONG).show() ;
                else if(!object.usernameValidator(username))
                    Snackbar.make(v , "Invalid username !" , Snackbar.LENGTH_LONG).show() ;
                else {
                    IncreaseUserCount(count) ;
                    SaveDataToFireBase(v);
                }
            }
        });
        // sign up button
        gotosignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext() , signup.class) ;
                startActivity(i);
            }
        });
    }
    private void SaveDataToFireBase(View view) {
        String UserName = usernameEditText.getText().toString().trim() ;
        String Password = passwordEditText.getText().toString().trim() ;
        // Map to store data's corresponding with KEY"s of username & password ->
        Map<String , Object> datas = new HashMap<>() ;
        datas.put(KEY_NAME , UserName) ;
        datas.put(KEY_PW, Password)   ;

        child = child+String.valueOf(count) ;
        // Saving in collection ->
        DB.collection("BASICPLANUSER").document(child)
                .set(datas)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Snackbar.make(view , "Successfully logged in !" , Snackbar.LENGTH_LONG).show() ;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(view , "That didn't work !" , Snackbar.LENGTH_LONG).show() ;
                    }
                }) ;
    }
    //                                               Shared Preferences usage            :::
    //                                               Functioning Serial Count of entries to database :::
    private void IncreaseUserCount(int x){
        SharedPreferences shared = getSharedPreferences("LastCount" ,MODE_PRIVATE) ;
        SharedPreferences.Editor editor = shared.edit() ;
        editor.putInt("KEY" , x+1) ;
        editor.commit() ;
    }
    private void SaveUserCount(){
        SharedPreferences shared = getSharedPreferences("LastCount" ,MODE_PRIVATE) ;
        count = shared.getInt("KEY" , 1);
    } //             Restoring last increased Count on triggering onCreate() method :::
}