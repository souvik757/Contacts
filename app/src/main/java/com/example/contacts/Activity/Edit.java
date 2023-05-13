package com.example.contacts.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.contacts.EmailValidator;
import com.example.contacts.KEYS;
import com.example.contacts.ProfileGenerator;
import com.example.contacts.R;
import com.example.contacts.UTILS;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Edit extends AppCompatActivity {
    // Declaring widgets :::
//    private ImageView ProfileIcon ;
    private TextView UserId ;
    private EditText FirstName ;
    private EditText MidName ;
    private EditText LastName ;
    private EditText Email ;
    private EditText Phone ;
    private EditText Password ;
    private EditText CPassword ;
    // Declaring FireStore Instances ::
    private FirebaseFirestore DBInstances = FirebaseFirestore.getInstance() ;
    private DocumentReference DBReferences ;
    // DB keys ::
    private KEYS keys = new KEYS() ;
    private UTILS object = new UTILS() ;
    private EmailValidator emailValidator = new EmailValidator() ;
    // Icon Generator :::
    private ProfileGenerator profileGenerator = new ProfileGenerator() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Intent i = getIntent() ;
        String IDToEdit = i.getStringExtra("USERIDTOEDIT") ;

        InitializeWidgets() ;
        InitializeDataToView(IDToEdit) ;
        ButtonEventListener() ;
    }
    private void InitializeWidgets(){
//        ProfileIcon = findViewById(R.id.ProfileImageView) ;
        UserId   = findViewById(R.id.DBUserID) ;
        FirstName= findViewById(R.id.FirstName) ;
        MidName  = findViewById(R.id.MiddleName) ;
        LastName = findViewById(R.id.LastName) ;
        Email    = findViewById(R.id.Email) ;
        Phone    = findViewById(R.id.Phone) ;
        Password = findViewById(R.id.Password) ;
        CPassword= findViewById(R.id.ConfirmPassword) ;
    }
    private void SetContents(String ID ,String firstname,String midname,String lastname,
                             String email ,String ph_number,String password){
//  *      // Generate random profile pic URL
//  *      String profilePicUrl;
//  *      try {
//  *          profilePicUrl = profileGenerator.generateProfilePicUrl(256);
//  *      } catch (NoSuchAlgorithmException e) {
//  *          e.printStackTrace();
//  *          return;
//  *      }
//  *
//  *      // Load profile pic URL into ImageView using Glide
//  *      Glide.with(this)
//  *              .load(profilePicUrl)
//  *              .into(ProfileIcon);
        UserId.setText(ID);
        FirstName.setText(firstname);
        MidName.setText(midname);
        LastName.setText(lastname);
        Email.setText(email);
        Phone.setText(ph_number);
        Password.setText(password);
        VerifyVoidEntry() ;
    }
    private void VerifyVoidEntry(){
        if(FirstName.getText().toString().equals(""))
            FirstName.setHint("first name");
        if(MidName.getText().toString().equals(""))
            MidName.setHint("middle name");
        if(LastName.getText().toString().equals(""))
            LastName.setHint("last name");
        if(Email.getText().toString().equals(""))
            Email.setHint("email");
        if(Phone.getText().toString().equals(""))
            Phone.setHint("phone number");
        if(Password.getText().toString().equals(""))
            Password.setHint("password");
        if(CPassword.getText().toString().equals(""))
            CPassword.setHint("confirm password");
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
    public void OnEditButton(View v){
        String IdToEdit = UserId.getText().toString().trim() ;
        String FNAME    = FirstName.getText().toString().trim()     ;
        String MNAME    = MidName.getText().toString().trim()    ;
        String LNAME    = LastName.getText().toString().trim()      ;
        String EMAIL    = Email.getText().toString().trim()         ;
        String PH       = Phone.getText().toString().trim()   ;
        String PASS     = Password.getText().toString().trim()      ;
        String CPASS    = CPassword.getText().toString().trim()      ;
        if (object.IsConnected(getApplicationContext()) && object.internetIsConnected()) {
            if (!object.nameValidator(FNAME, MNAME, LNAME))
                AlertBox(Edit.this , "Invalid name !" , object.constraintsUSERNAME());
            else if(!emailValidator.validate(EMAIL))
                message(v , "Invalid Email address !");
            else if (!object.phoneValidator(PH))
                message(v, "Invalid Phone Number !");
            else if (!object.passwordValidator(PASS))
                AlertBox(Edit.this , "Invalid Password !" , object.constraintsPASSWORD());
            else if (!object.passwordConfirmation(PASS, CPASS))
                message(v, "Passwords are not matching !");
            else {
                // Map to store data's corresponding with KEY"s of username & password ->
                Map<String , Object> data = new HashMap<>() ;
                data.put(keys.NAME01_KEY,FNAME) ;
                data.put(keys.NAME02_KEY,MNAME) ;
                data.put(keys.NAME03_KEY,LNAME) ;
                data.put(keys.EMAIL_KEY,EMAIL)  ;
                data.put(keys.PH_KEY,PH)        ;
                data.put(keys.PASS_KEY,PASS)    ;
                DBInstances.collection("BASICPLANUSER").document(IdToEdit).update(data).
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                message(v,"Successfully updated !");
                                new Handler().postDelayed(()->{
                                    finish() ;
                                }, 1400) ;
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                message(v,"That didn't work !");
                            }
                        }) ;
            }
        }
        else{
            new Handler().postDelayed(() -> {
                startActivity(new Intent(getApplicationContext() , NoConnectivity.class));
                finish() ;
            }, 1400) ;
        }

    }
    public void DeleteButton(View view){
        String IdToEdit = UserId.getText().toString().trim() ;
//        Snackbar.make(view , "Clicked" , Snackbar.LENGTH_LONG).show() ;
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
}


// USERIDTOEDIT