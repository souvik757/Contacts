package com.example.contacts.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.contacts.R;
import com.example.contacts.UTILS;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore DB = FirebaseFirestore.getInstance() ;
    private DocumentReference UserReference ;
    private UTILS object = new UTILS() ;
    // Declaring widgets :::
    ImageView LogoLoading ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitializeWidgets() ;
        LogoLoading.setAnimation(AnimationUtils.loadAnimation(this , R.anim.rotation));
        FetchDataFromDb() ;
    }
    private void FetchDataFromDb() {
        UserReference = DB.collection("LastSavedUserID").document("LAST_USER_ID") ;
        UserReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String userID = documentSnapshot.getString("UserID") ;
                    Intent i = new Intent(getApplicationContext(), profiler.class);
                    i.putExtra("USERID", userID);
                    startActivity(i);
                    finish();
                }
                else {
                    GoToSignInPage() ;
                    new Handler().postDelayed(()->{
                        Toast.makeText(MainActivity.this, "If not signed in"+"\nplease sign up !", Toast.LENGTH_SHORT).show();;
                    } , 500) ;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "That didn't work", Toast.LENGTH_SHORT).show() ;
            }
        }) ;
    }
    private void GoToSignInPage(){
        // Checking Internet Connectivity :::
        if (!object.IsConnected(getApplicationContext())) {
            new Handler().postDelayed(() -> {
                startActivity(new Intent(getApplicationContext() , NoConnectivity.class));
                finish() ;
            }, 1400) ;
        }
        else {
            new Handler().postDelayed(() -> {
                startActivity(new Intent(getApplicationContext() , SignIn.class));
                finish() ;
            }, 1400) ;
        }
    }
    private void InitializeWidgets(){
        LogoLoading = findViewById(R.id.logoImageView) ;
    }
}

// SH1 : 20:B8:5A:2D:A4:5C:A6:2C:67:DA:5A:E6:04:4E:DF:BC:0F:7B:90:55