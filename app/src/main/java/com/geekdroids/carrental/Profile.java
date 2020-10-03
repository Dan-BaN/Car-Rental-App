package com.geekdroids.carrental;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity {


    public static final String TAG = "TAG";
    Button changeProfileImage, updateInformation;
    TextInputEditText profileFullName, profileEmail, profileNic;
    ImageView profileImage;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    StorageReference storageReference;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);


        Intent data = getIntent();
        String fullName = data.getStringExtra("fullname");
        String email= data.getStringExtra("email");
        String Nic = data.getStringExtra("nic");

        profileFullName = findViewById(R.id.updateFullName);
        profileEmail = findViewById(R.id.updateEmail);
        profileNic = findViewById(R.id.updateNic);

        profileFullName.setText(fullName);
        profileEmail.setText(email);
        profileNic.setText(Nic);


        Log.d(TAG, "onCreate: " +fullName+ " " + email+ " " + Nic);


        profileImage = findViewById(R.id.profile_image);
        changeProfileImage = findViewById(R.id.changeProfileimage);
        updateInformation= findViewById(R.id.Update);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("users/" +firebaseAuth.getCurrentUser().getUid()+ "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });


        userId = firebaseAuth.getCurrentUser().getUid();




        changeProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open gallery

                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,1000);
            }
        });


        updateInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();

               // profileImage.setImageURI(imageUri);


                uploadImageToFirebase(imageUri);


            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        //upload image to Firebase storage

        final StorageReference fileRef = storageReference.child("users/" +firebaseAuth.getCurrentUser().getUid()+ "/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImage);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Profile.this,"Image uploading failed", Toast.LENGTH_SHORT).show();
            }
        });



    }
}