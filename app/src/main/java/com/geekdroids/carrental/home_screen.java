package com.geekdroids.carrental;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class home_screen extends AppCompatActivity {

    public static final String TAG = "tag";
    TextView fullname, email, nic;
    ImageView profileImage;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    StorageReference storageReference;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home_screen);


        fullname = findViewById(R.id.fname);
        email = findViewById(R.id.profileemail);
        nic = findViewById(R.id.nic);
        profileImage = findViewById(R.id.profile_image);

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

        DocumentReference documentReference = firestore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>(){

            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if(documentSnapshot.exists()){
                    fullname.setText(documentSnapshot.getString("fName"));
                    email.setText(documentSnapshot.getString("email"));
                    nic.setText(documentSnapshot.getString("Nic"));
                }else{
                    Log.d(TAG, "onEvent: Document does not exist" );
                }


            }
        });



    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), login_signup.class));
        finish();
    }

    public void ManageProfile(View view) {

        Intent intent = new Intent(view.getContext(), Profile.class);
        intent.putExtra("fullname", "aaaa");
        intent.putExtra("email", "bbbb");
        intent.putExtra("nic", "abc123");
        startActivity(intent);

    }
}