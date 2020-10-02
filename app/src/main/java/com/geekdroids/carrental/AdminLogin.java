package com.geekdroids.carrental;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AdminLogin extends AppCompatActivity {


    Button login_btn;
    ImageButton customermode;
    ImageView carimage;
    TextView welcome, signintext;
    TextInputEditText Email,Password;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_admin_login);




        carimage =  findViewById(R.id.logoImage);
        welcome =  findViewById(R.id.welcomeback);
        signintext =  findViewById(R.id.signin);
        Email =  findViewById(R.id.email_signin);
        Password =  findViewById(R.id.password);
        login_btn =  findViewById(R.id.loginbtn);
        customermode = findViewById(R.id.customermode);

        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);


        customermode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), login_signup.class));
                finish();
            }
        });


        login_btn.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             String email = Email.getText().toString().trim();
                                             String password = Password.getText().toString().trim();


                                             if (TextUtils.isEmpty(email)) {
                                                 Email.setError("Email is Required");
                                                 return;
                                             }

                                             if (TextUtils.isEmpty(password)) {
                                                 Password.setError("Password is required");
                                                 return;
                                             }

                                             if (Password.length() <= 6) {
                                                 Password.setError("Password must be 6 or longer characters");
                                                 return;
                                             }

                                             progressBar.setVisibility(View.VISIBLE);


                                             firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                 @Override
                                                 public void onComplete(@NonNull Task<AuthResult> task) {
                                                     if (task.isSuccessful()) {

                                                         Toast.makeText(AdminLogin.this, "Logged in successfully"  , Toast.LENGTH_SHORT).show();
                                                         startActivity(new Intent(getApplicationContext(), Admin_homescreen.class));
                                                     } else {

                                                         Toast.makeText(AdminLogin.this, "Error incorrect password or email" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                         progressBar.setVisibility(View.GONE);

                                                     }
                                                 }
                                             });
                                         }
                                     }
        );
    }
}