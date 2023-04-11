package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignIn extends AppCompatActivity {
    private EditText usernameEditText ;
    private EditText passwordEditText ;
    private Button showHidePasswordButton ;
    private Button savedetailsButton ;
    private void initialize_widgets(){
        usernameEditText = findViewById(R.id.et_username) ;
        passwordEditText = findViewById(R.id.et_password) ;
        showHidePasswordButton = findViewById(R.id.toggle_pw) ;
        savedetailsButton = findViewById(R.id.btn_login) ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

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
    }
}