package com.example.contacts.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contacts.KEYS;
import com.example.contacts.MyDialogFragment;
import com.example.contacts.R;
import com.example.contacts.UTILS;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class profiler extends AppCompatActivity implements MyDialogFragment.MyDialogListener {
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
                Toast.makeText(profiler.this, "That didn't work", Toast.LENGTH_SHORT).show() ;
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
                MyDialogFragment dialogFragment = new MyDialogFragment();
                dialogFragment.setListener(profiler.this);
                dialogFragment.show(getSupportFragmentManager(), "MyDialogFragment");
            }
        });
    }
    private void message(View view , String s){
        Snackbar.make(view,s,Snackbar.LENGTH_LONG).show() ;
    }
    public void AlertBox(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Do you want to exit ?");
        builder.setTitle("Alert !");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            finish();
        });
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        AlertBox(profiler.this) ;
    }

    @Override
    public void onOkButtonClicked(String inputText) {
        if(inputText.equals(user_password.getText().toString().trim())) {
            Intent edit = new Intent(profiler.this, Edit.class);
            edit.putExtra("USERIDTOEDIT", user_id.getText().toString().trim());
            startActivity(edit);
        } else if (inputText.equals("")) {
            Toast.makeText(this, "PROVIDE PASSWORD TO PROCEED !", Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(this, "PASSWORD NOT MATCHED !", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(()->{
                finish() ;
            }, 1400) ;
        }
    }
}