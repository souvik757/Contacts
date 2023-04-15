package com.example.contacts ;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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


    // Firebase instances
    private FirebaseFirestore DB = FirebaseFirestore.getInstance() ;
    // KEY's
    private static final String KEY_NAME = "username" ;
    private static final String KAY_PW   = "password" ;
    private void initialize_widgets(){
        usernameEditText = findViewById(R.id.et_username) ;
        passwordEditText = findViewById(R.id.et_password) ;
        showHidePasswordButton = findViewById(R.id.toggle_pw) ;
        savedetailsButton = findViewById(R.id.btn_login) ;
        gotosignupButton = findViewById(R.id.btn_signup) ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        setContentView(R.layout.activity_sign_in) ;

        initialize_widgets() ;

        OnClickListenerForButtons() ;
    }

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
                if(!passwordValidator(password))
                    Snackbar.make(v , "Invalid Password !" , Snackbar.LENGTH_LONG).show() ;
                else
                    SaveDataToFireBase(v) ;
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
        datas.put(KAY_PW , Password)   ;

        // Saving in collection ->
        DB.collection("Registers").document("BasicUsers")
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

    private boolean passwordValidator(String password){
        //1. Min Length : 8
        //2. Max Length : 30
        if(password.length() < 8 || password.length() > 30) return false ;
        //3. At least   :  3  lowercase 'a' to 'z'
        boolean HasLowerCase = false ;
        int CountHasLowerCase = 0 ;
        for(int i = 0 ; i < password.length() ; i++){
            if(Character.isLowerCase(password.charAt(i))) {
                CountHasLowerCase++ ;
            }
        }
        HasLowerCase = CountHasLowerCase == 3 ;
        //4. At least   :  3  Uppercase 'A' to 'Z'
        boolean HasUpperCase = false ;
        int CountHasUpperCase = 0 ;
        for(int i = 0 ; i < password.length() ; i++){
            if(Character.isUpperCase(password.charAt(i))) {
                CountHasUpperCase++ ;
            }
        }
        HasUpperCase = CountHasUpperCase == 3 ;
        //5. At least   :  1  Numeric
        boolean HasNumeric = false ;
        for(int i = 0 ; i < password.length() ; i++){
            if(Character.isDigit(password.charAt(i)))
            {
                HasNumeric = true ;
                break ;
            }
        }
        //6. At least   :  1  special character '#' '@' '_' '*' '!' '$' '%' '&' '^'
        boolean HasSpecial = false ;
        for(int i = 0 ; i < password.length() ; i++){
            if(password.charAt(i) == '!' || password.charAt(i) == '@' ||
               password.charAt(i) == '#' || password.charAt(i) == '$' ||
               password.charAt(i) == '%' || password.charAt(i) == '^' ||
               password.charAt(i) == '&' || password.charAt(i) == '*' ||
               password.charAt(i) == '_'){
                HasSpecial = true ;
                break ;
            }
        }

        return HasLowerCase && HasUpperCase && HasNumeric && HasSpecial ;
    }
}