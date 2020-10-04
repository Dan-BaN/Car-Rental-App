package com.geekdroids.carrental;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
public class book_a_car extends AppCompatActivity implements AdapterView.OnItemSelectedListener{


    public static final String TAG = "TAG";
    Button savebtn;
    Spinner spinner, selectlocation;
    TextView selectcartxt;
    TextInputEditText Datetxt, Timetxt;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    FirebaseUser user;
    String item, location;
    SelectCar selectCar;
    Location selectLocation;


    String[] carList = {"Choose a car", "Car1" , "Car2", "Car3", "Car4"};
    String[] locationList = {"Choose a location", "Location1", "Location2","Location2"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_book_a_car);


        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        user = firebaseAuth.getCurrentUser();

        selectcartxt = findViewById(R.id.selectCarTxt);
        Datetxt = findViewById(R.id.setDate);
        Timetxt = findViewById(R.id.setTime);
        savebtn = findViewById(R.id.save);

        spinner = findViewById(R.id.selectcar);
        spinner.setOnItemSelectedListener(this);

        selectlocation= findViewById(R.id.selectLocation);
        selectlocation.setOnItemSelectedListener(this);

        selectCar = new SelectCar();
        selectLocation = new Location();

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, carList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        ArrayAdapter LocationAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, locationList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectlocation.setAdapter(LocationAdapter);




        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save(item);
                SaveDate();
                SaveTime();
                SaveLocation(location);



            }
        });

    }





    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = spinner.getSelectedItem().toString();
        location = selectLocation.getLocationList();
        //selectcartxt.setText(item);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    void Save(String item) {

        if (item == "Choose a car") {
            Toast.makeText(this, "Select a car", Toast.LENGTH_SHORT).show();
        } else {
            selectCar.setCarList(item);

            item = spinner.getSelectedItem().toString();

            DocumentReference documentReference = firestore.collection("Selected car").document(user.getUid());
            final Map<String, Object> carList = new HashMap<>();
            carList.put("Car model", item);

            final String finalItem = item;
            documentReference.set(carList).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(book_a_car.this, finalItem + " has been added", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String error = e.getMessage();

                    Toast.makeText(book_a_car.this, "error" + error, Toast.LENGTH_SHORT).show();
                }
            });



        }
    }

    void SaveLocation( String location){

        if (location == "Choose a location") {
            Toast.makeText(this, "Select a location", Toast.LENGTH_SHORT).show();
        } else {
            selectLocation.setLocationList(location);

            location = selectlocation.getSelectedItem().toString();

            DocumentReference documentReference = firestore.collection("Selected location").document(user.getUid());
            final Map<String, Object> locationList = new HashMap<>();
            locationList.put("Location", location);

            final String finalItem = location;
            documentReference.set(locationList).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(book_a_car.this, finalItem + " has been added", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String error = e.getMessage();

                    Toast.makeText(book_a_car.this, "error" + error, Toast.LENGTH_SHORT).show();
                }
            });


        }

    }

void SaveDate(){
    String date = Datetxt.getText().toString();
    DocumentReference documentReference = firestore.collection("Date").document(user.getUid());

    Map<String,Object> setdate = new HashMap<>();
    setdate.put("date", date);

    documentReference.set(setdate).addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
            Log.d(TAG, "Date added");
        }
    });

}

void SaveTime(){
    String time = Timetxt.getText().toString();
    DocumentReference documentReference = firestore.collection("Time").document(user.getUid());

    Map<String,Object> settime = new HashMap<>();
    settime.put("date", time);

    documentReference.set(settime).addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
            Log.d(TAG, "Time added");
        }
    });

}




}