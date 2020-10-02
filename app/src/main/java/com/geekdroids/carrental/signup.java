package com.geekdroids.carrental;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class signup extends AppCompatActivity {

    public static final String TAG = "TAG";

    //variables
    TextInputEditText regName, regUserName, regNic, regPhoneNo,regPassword, regEmail;
    Button SignupBtn, regToLoginBtn;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    FirebaseFirestore firestore;
    String userID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);

        //Hooks

        regName = findViewById(R.id.username_signin);
        regUserName = findViewById(R.id.nickname_signin);
        regNic = findViewById(R.id.nic_signin);
        regPhoneNo = findViewById(R.id.phone_signin);
        regPassword = findViewById(R.id.password_signin);
        regEmail = findViewById(R.id.email_signin);
        SignupBtn = findViewById(R.id.sign_upbtn);
        regToLoginBtn = findViewById(R.id.login_btn);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore =FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBarSignup);

        if(firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), home_screen.class));
            finish();
        }



        //save data to firebase

        SignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 final String Name = regName.getText().toString();
                 final String UserName = regUserName.getText().toString();
                 final String Nic = regNic.getText().toString();
                 final String PhoneNo = regPhoneNo.getText().toString();
                 final String Password = regPassword.getText().toString();
                 final String email = regEmail.getText().toString();

                if(TextUtils.isEmpty(email)){
                    regEmail.setError("Email is Required");
                    return;
                }

                if(TextUtils.isEmpty(Password)){
                    regPassword.setError("Password is required");
                    return;
                }

                if(Password.length()<=6){
                    regPassword.setError("Password must be 6 or longer characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //register the user in firebase

                firebaseAuth.createUserWithEmailAndPassword(email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            Toast.makeText(signup.this, "User Created", Toast.LENGTH_SHORT).show();

                            userID = firebaseAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = firestore.collection("users").document(userID);
                            Map<String, Object>user = new HashMap<>();
                            user.put("fName", Name);
                            user.put("UserName", UserName);
                            user.put("Nic", Nic);
                            user.put("PhoneNo", PhoneNo);
                            user.put("Password", Password);
                            user.put("email", email);

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user profile is created for" + userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                        Log.d(TAG, "onFailure: "+ e.toString());

                                }
                            });

                            startActivity(new Intent(getApplicationContext(),home_screen.class));
                        }else{

                            Toast.makeText(signup.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }
                    }
                });
            }
    }
        );


        regToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), login_signup.class));
                finish();
            }
        });
}
}