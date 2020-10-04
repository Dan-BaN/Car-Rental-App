package com.geekdroids.carrental;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
public class book_a_car extends AppCompatActivity implements AdapterView.OnItemSelectedListener{


    Button savebtn;
    Spinner spinner;
    TextView selectcartxt;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    FirebaseUser user;
    String item;
    SelectCar selectCar;


    String[] carList = {"Choose a car", "Car1" , "Car2", "Car3", "Car4"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_book_a_car);


        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        user = firebaseAuth.getCurrentUser();

        selectcartxt = findViewById(R.id.selectCarTxt);
        savebtn = findViewById(R.id.save);
        spinner = findViewById(R.id.selectcar);

        spinner.setOnItemSelectedListener(this);

        selectCar = new SelectCar();

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, carList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(arrayAdapter);




        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save(item);

            }
        });

    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = spinner.getSelectedItem().toString();
        selectcartxt.setText(item);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    void Save(String item) {

        if(item == "Choose a car" ){
            Toast.makeText(this,"Select a car",Toast.LENGTH_SHORT).show();
        }else {
            selectCar.setCarList(item);

            item = spinner.getSelectedItem().toString();

            DocumentReference documentReference = firestore.collection("Selected car").document(user.getUid());
            final Map<String, Object> carList = new HashMap<>();
            carList.put("Car model",item);

            documentReference.set(carList).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(book_a_car.this,   "car has been added", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String error = e.getMessage();

                    Toast.makeText(book_a_car.this,"error"+ error, Toast.LENGTH_SHORT).show();
                }
            });



        }

    }
}