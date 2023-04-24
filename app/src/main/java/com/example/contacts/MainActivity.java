package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    // Declaring widgets :::
    ImageView LogoLoading ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitializeWidgets() ;
        LogoLoading.setAnimation(AnimationUtils.loadAnimation(this , R.anim.rotation));

        new Handler().postDelayed(() -> {
            startActivity(new Intent(getApplicationContext() , SignIn.class));
            finish() ;
        }, 1400) ;
    }
    private void InitializeWidgets(){
        LogoLoading = findViewById(R.id.logoImageView) ;
    }
}

// SH1 : 20:B8:5A:2D:A4:5C:A6:2C:67:DA:5A:E6:04:4E:DF:BC:0F:7B:90:55