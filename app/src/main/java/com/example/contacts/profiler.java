package com.example.contacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class profiler extends AppCompatActivity {
    // Declaring widgets :::
    private TextView user_id ;
    private TextView user_name ;
    private TextView user_email ;
    private TextView user_phone ;
    private TextView user_password ;
    private Button edit ;
    // Firebase instances :::
    private FirebaseFirestore DB = FirebaseFirestore.getInstance() ;
    private DocumentReference UserReference ;
    // DB keys ::
    private KEYS keys = new KEYS() ;
    private UTILS object = new UTILS() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiler);
        // Initialize Widgets :::
        InitializeWidgets() ;
        // Fetching Intent content :::
        Intent i = getIntent() ;
        // Unique UserID for generated on each sign up :::
        String User_ID   = i.getStringExtra("USERID") ;
        FetchDataFromDb(User_ID) ;
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
    private void FetchDataFromDb(String userID) {
        UserReference = DB.collection("BASICPLANUSER").document(userID) ;
        UserReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String firstname = documentSnapshot.getString(keys.NAME01_KEY) ;
                    String midname   = documentSnapshot.getString(keys.NAME02_KEY) ;
                    String lastname  = documentSnapshot.getString(keys.NAME03_KEY) ;
                    String FullName  = object.ParseName(firstname,midname,lastname);
                    String email     = documentSnapshot.getString(keys.EMAIL_KEY)  ;
                    String ph_number = documentSnapshot.getString(keys.PH_KEY)     ;
                    String password  = documentSnapshot.getString(keys.PASS_KEY)   ;
                    SetContents(userID , FullName, email , ph_number, password)    ;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(profiler.this, "That didn't work", Toast.LENGTH_SHORT).show();;
            }
        }) ;
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