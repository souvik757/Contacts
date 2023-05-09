package com.example.contacts.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.contacts.KEYS;
import com.example.contacts.R;
import com.example.contacts.UTILS;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignIn extends AppCompatActivity {
    private EditText useridEditText       ;
    private EditText passwordEditText     ;
    private Button showHidePasswordButton ;
    private Button savedetailsButton      ;
    private Button gotosignupButton       ;
    // Firebase instances :::
    private FirebaseFirestore DB = FirebaseFirestore.getInstance() ;
    private DocumentReference UserReference ;
    private UTILS object = new UTILS() ; // UTILS class consists of validator methods of Inputs :::
    private KEYS keys = new KEYS() ;
    // UserID :::
    private String _UserID_ = "USER1" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        setContentView(R.layout.activity_sign_in);
        initialize_widgets();
        //FetchLastUserID() ;
        //FetchDataFromDb(_UserID_ , true);
        OnClickListenerForButtons();
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
                if (object.internetIsConnected() && object.IsConnected(getApplicationContext())) {
                    String UserId = useridEditText.getText().toString().trim() ;
                    if (UserId.length() == 0)
                        message(v,"Enter UserID"+"\nand password");
                    else
                        FetchDataFromDb(UserId) ;
                }
                else{
                    new Handler().postDelayed(() -> {
                        startActivity(new Intent(getApplicationContext() , NoConnectivity.class));
                        finish() ;
                    }, 1400) ;
                }
            }
        });
        // sign up button
        gotosignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (object.IsConnected(getApplicationContext()) && object.internetIsConnected()) {
                    startActivity(new Intent(getApplicationContext(), Signup.class));
                }
                else {
                    new Handler().postDelayed(() -> {
                        startActivity(new Intent(getApplicationContext() , NoConnectivity.class));
                        finish() ;
                    }, 1400) ;
                }
            }
        });
    }
    private void FetchDataFromDb(String userID) {
        UserReference = DB.collection("BASICPLANUSER").document(userID) ;
        UserReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String password = documentSnapshot.getString(keys.PASS_KEY) ;
                    String PASSWORD = passwordEditText.getText().toString().trim() ;
                    if (!password.equals(PASSWORD))
                        Toast.makeText(SignIn.this, "password doesn't match", Toast.LENGTH_SHORT).show();
                    else {
                        Intent i = new Intent(getApplicationContext(), profiler.class);
                        i.putExtra("USERID", userID);
                        SaveUserId(userID);
                        startActivity(i);
                        finish();
                    }
                }
                else {
                    Toast.makeText(SignIn.this, "User ID doesn't exist", Toast.LENGTH_SHORT).show() ;
                    new Handler().postDelayed(()->{
                        Toast.makeText(SignIn.this, "If not signed in"+"\nplease sign up !", Toast.LENGTH_SHORT).show();;
                    } , 1000) ;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignIn.this, "That didn't work", Toast.LENGTH_SHORT).show() ;
            }
        }) ;
    }
    protected void onResume() {
        super.onResume();
        if (!object.IsConnected(getApplicationContext()) || !object.internetIsConnected()) {
            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    // Declare the onBackPressed method when the back button is pressed this method will call
    public void AlertBox(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Do you want to exit ?");
        builder.setTitle("Alert !");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            finish() ;
        });
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    public void onBackPressed() {
        AlertBox(SignIn.this) ;
    }
    private void message(View view, String message) {
        Snackbar.make(view , message,Snackbar.LENGTH_LONG).show() ;
    }
    private void SaveUserId(String x){
        Map<String , Object> data = new HashMap<>() ;
        data.put("UserID",x) ;
        DB.collection("LastSavedUserID").document("LAST_USER_ID")
                .set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        new Handler().postDelayed(()->{
                            finish() ;
                        }, 1400) ;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                }) ;
    }
}