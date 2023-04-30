package com.example.contacts.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.text.InputType;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.contacts.EmailValidator;
import com.example.contacts.KEYS;
import com.example.contacts.R;
import com.example.contacts.UTILS;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {
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
    private String child ;
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
        child = object.rand_generatedSTR() ;
        count = object.rand_generatedINT() ;
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
                        AlertBox(Signup.this , "Invalid name !" , object.constraintsUSERNAME());
                    else if(!emailValidator.validate(EMAIL))
                        message(v , "Invalid Email address !");
                    else if (!object.phoneValidator(PH))
                        message(v, "Invalid Phone Number !");
                    else if (!object.passwordValidator(PASS))
                        AlertBox(Signup.this , "Invalid Password !" , object.constraintsPASSWORD());
                    else if (!object.passwordConfirmation(PASS, CPASS))
                        message(v, "Passwords are not matching !");
                    else {
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
                        // Grant permission for SMS ::
                        if (ContextCompat.checkSelfPermission(Signup.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                            // Permission is granted, so send the SMS message
                            sendSMS(PH, child+object.bold());
                        } else {
                            // Permission is not granted, so request it
                            ActivityCompat.requestPermissions(Signup.this, new String[] { Manifest.permission.SEND_SMS }, PERMISSION_REQUEST_CODE);
                        }
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
    public void AlertBox(Context context , String alert , String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setTitle(alert);
        builder.setCancelable(true);
        builder.setNegativeButton("ok", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private static final int PERMISSION_REQUEST_CODE = 1;

    private void sendSMS(String phoneNumber, String message) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted, so send the SMS message
            try {
                SmsManager smsManager = SmsManager.getDefault();
                ArrayList<String> messageParts = smsManager.divideMessage(message);
                smsManager.sendMultipartTextMessage(phoneNumber, null, messageParts, null, null);
                Toast.makeText(getApplicationContext(), "SMS sent to " + phoneNumber, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "SMS failed to send", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            // Permission is not granted, so request it
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.SEND_SMS }, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, so send the SMS message
                sendSMS("7063721378", "working uhooo!!");
            } else {
                // Permission is not granted, so show an error message
                Toast.makeText(getApplicationContext(), "SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}