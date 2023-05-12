package com.example.contacts.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contacts.KEYS;
import com.example.contacts.R;
import com.example.contacts.UTILS;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Edit extends AppCompatActivity {
    // Declaring widgets :::
    private TextView UserId ;
    private EditText FirstName ;
    private EditText MidName ;
    private EditText LastName ;
    private EditText Email ;
    private EditText Phone ;
    private EditText Password ;
    // Declaring FireStore Instances ::
    private FirebaseFirestore DBInstances = FirebaseFirestore.getInstance() ;
    private DocumentReference DBReferences ;
    // DB keys ::
    private KEYS keys = new KEYS() ;
    private UTILS object = new UTILS() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Intent i = getIntent() ;
        String IDToEdit = i.getStringExtra("USERIDTOEDIT") ;

        InitializeWidgets() ;
        InitializeDataToView(IDToEdit) ;
        ButtonEventListener();
    }
    private void InitializeWidgets(){
        UserId   = findViewById(R.id.DBUserID) ;
        FirstName= findViewById(R.id.FirstName) ;
        MidName  = findViewById(R.id.MiddleName) ;
        LastName = findViewById(R.id.LastName) ;
        Email    = findViewById(R.id.Email) ;
        Phone    = findViewById(R.id.Phone) ;
        Password = findViewById(R.id.Password) ;
    }
    private void SetContents(String ID ,String firstname,String midname,String lastname,
                             String email ,String ph_number,String password){
        UserId.setText(ID);
        FirstName.setText(firstname);
        MidName.setText(midname);
        LastName.setText(lastname);
        Email.setText(email);
        Phone.setText(ph_number);
        Password.setText(password);
    }
    private void ButtonEventListener(){
        UserId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v , "User ID is uniquely generated for each user"+
                        "\ncann't be changed !" ,Snackbar.LENGTH_LONG).show() ;
            }
        });
    }
    private void InitializeDataToView(String ID){
        DBReferences = DBInstances.collection("BASICPLANUSER").document(ID) ;
        DBReferences.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String firstname = documentSnapshot.getString(keys.NAME01_KEY) ;
                    String midname   = documentSnapshot.getString(keys.NAME02_KEY) ;
                    String lastname  = documentSnapshot.getString(keys.NAME03_KEY) ;
                    String email     = documentSnapshot.getString(keys.EMAIL_KEY)  ;
                    String ph_number = documentSnapshot.getString(keys.PH_KEY)     ;
                    String password  = documentSnapshot.getString(keys.PASS_KEY)   ;
                    SetContents(ID , firstname ,midname,lastname, email , ph_number, password)        ;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Edit.this, "That didn't work !", Toast.LENGTH_SHORT).show();
            }
        }) ;
    }
}
















// USERIDTOEDIT