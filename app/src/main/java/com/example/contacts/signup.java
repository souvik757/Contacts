package com.example.contacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity {
    // Declaring Widgets :::
    //                             ::: EDITTEXT :::
    private EditText FirstName     ;
    private EditText MiddleName    ;
    private EditText LastName      ;
    private EditText Email         ;
    private EditText PhoneNumber   ;
    private EditText Password      ;
    private EditText FinalPassword ;
    //                             ::: BUTTON :::
    private Button BTNSIGNUP ;
    private Button TOGGLEPW  ;
    //
    private ImageView load   ;
    private EmailValidator emailValidator = new EmailValidator() ;
    //                                                  Firebase instances :::
    private FirebaseFirestore _ReferenceDB_ = FirebaseFirestore.getInstance() ;
    //                                                  Document reference name in FireStore Database :::
    private String child = "USER" ;
    //                                                  Document counter (serial number that will concatenate with USER for better DB management) :::
    private int count ;
    private KEYS key = new KEYS() ;
    //                                                  UTILS class instance ::
    private UTILS object = new UTILS() ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        InitializeWidgets()     ;
        SaveUserCount()         ;
        ButtonOnClickListener() ;

    }
    //                                           Initializing Widgets :::
    private void InitializeWidgets() {
        FirstName     = findViewById( R.id.etFirstName       ) ;
        MiddleName    = findViewById( R.id.etMiddleName      ) ;
        LastName      = findViewById( R.id.etLastName        ) ;
        Email         = findViewById( R.id.etEmail           ) ;
        PhoneNumber   = findViewById( R.id.etPhoneNumber     ) ;
        Password      = findViewById( R.id.etPassword        ) ;
        FinalPassword = findViewById( R.id.etConfirmPassword ) ;
        BTNSIGNUP     = findViewById( R.id.btnSignUp         ) ;
        TOGGLEPW      = findViewById( R.id.toggle_pw_sign_up ) ;
        load          = findViewById( R.id.loading           ) ;
    }
    //                                           Event Listener for Buttons :::
    private void ButtonOnClickListener() {
    //                                           Set click listener for the show/hide password button :::
        TOGGLEPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle password visibility
                if (Password.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    // Show password
                    Password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    TOGGLEPW.setBackground(getDrawable(R.drawable.reveal));
                } else {
                    // Hide password
                    Password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    TOGGLEPW.setBackground(getDrawable(R.drawable.hide));
                }
                // Move cursor to the end of the text
                Password.setSelection(Password.getText().length());
            }
        }) ;
        BTNSIGNUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (object.IsConnected(getApplicationContext()) && object.internetIsConnected()) {
                    String FNAME = FirstName.getText().toString().trim();
                    String MNAME = MiddleName.getText().toString().trim();
                    String LNAME = LastName.getText().toString().trim();
                    String EMAIL = Email.getText().toString().trim()         ;
                    String PH = PhoneNumber.getText().toString().trim();
                    String PASS = Password.getText().toString().trim();
                    String CPASS = FinalPassword.getText().toString().trim();
                    if (!object.nameValidator(FNAME, MNAME, LNAME))
                        message(v, "Invalid Name ! ");
                    else if(!emailValidator.validate(EMAIL))
                        message(v , "Invalid Email address !");
                    else if (!object.phoneValidator(PH))
                        message(v, "Invalid Phone Number !");
                    else if (!object.passwordValidator(PASS))
                        message(v, "Invalid Password !");
                    else if (!object.passwordConfirmation(PASS, CPASS))
                        message(v, "Passwords are not matching !");
                    else {
                        IncreaseUserCount(count);
                        SAVETOFIREBASE(v);
                    }
                }
                else{
                    new Handler().postDelayed(() -> {
                        startActivity(new Intent(getApplicationContext() , NoConnectivity.class));
                        finish() ;
                    }, 1400) ;
                }
            }
        });
    }
    //                                           Saving data to FireStore :::
    private void SAVETOFIREBASE(View view) {
        String FNAME = FirstName.getText().toString().trim()     ;
        String MNAME = MiddleName.getText().toString().trim()    ;
        String LNAME = LastName.getText().toString().trim()      ;
        String EMAIL = Email.getText().toString().trim()         ;
        String PH    = PhoneNumber.getText().toString().trim()   ;
        String PASS  = Password.getText().toString().trim()      ;
        // Map to store data's corresponding with KEY"s of username & password ->
        Map<String , Object> data = new HashMap<>() ;
        data.put(key.NAME01_KEY,FNAME) ;
        data.put(key.NAME02_KEY,MNAME) ;
        data.put(key.NAME03_KEY,LNAME) ;
        data.put(key.EMAIL_KEY,EMAIL)  ;
        data.put(key.PH_KEY,PH)        ;
        data.put(key.PASS_KEY,PASS)    ;
        // increase count and concatenate it with USER :::
        child = child + String.valueOf(count) ;
        _ReferenceDB_.collection("BASICPLANUSER").document(child)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        load.setVisibility(View.VISIBLE) ;
                        load.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotation));
                        Snackbar.make(view , "Successfully signed up !" , Snackbar.LENGTH_LONG).show() ;
                        new Handler().postDelayed(()->{
                            finish() ;
                        }, 1400) ;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(view , "That didn't work !" , Snackbar.LENGTH_LONG).show() ;
                    }
                }) ;
    }
    //                                           Shared Preferences usage            :::
    //                                           Functioning Serial Count of entries to database :::
    private void IncreaseUserCount(int x){
        SharedPreferences shared = getSharedPreferences("LastCount" ,MODE_PRIVATE) ;
        SharedPreferences.Editor editor = shared.edit() ;
        editor.putInt("KEY" , x+1) ;
        editor.commit() ;
    }
    //                                           Restoring last increased Count on triggering onCreate() method :::
    private void SaveUserCount(){
        SharedPreferences shared = getSharedPreferences("LastCount" ,MODE_PRIVATE) ;
        count = shared.getInt("KEY" , 1);
    }
    @Override
    public void onBackPressed() {
        finish() ;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!object.IsConnected(getApplicationContext()) || !object.internetIsConnected()) {
            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void message(View view, String message) {
        Snackbar.make(view , message,Snackbar.LENGTH_LONG).show() ;
    }
}