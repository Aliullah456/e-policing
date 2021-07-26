package com.example.abcd;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
public class Result extends AppCompatActivity {
    Intent intent;
    String value;
    TextView text;
    private CollectionReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        intent = getIntent();
        value = intent.getStringExtra("result"); //if it's a string you stored.
        database = FirebaseFirestore.getInstance().collection("car_registration");
        text = (TextView) findViewById(R.id.textView);

        final String result = null;
        database.whereEqualTo("plate_number", value).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    if (task.getResult().isEmpty()) {
                        text.setText("No result found for plate: "+value);
                        text.setTextColor(Color.RED);
                    } else {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            text.setText("Plate Number: " + document.getString("plate_number"));
                            text.append("\n\nowner name: " + document.getString("owner_name"));
                            text.append("\n\nowner father name: " + document.getString("owner_father"));
                            text.append("\n\nowner city: " + document.getString("owner_city"));
                            text.append("\n\nChassis number: " + document.getString("Chassis_Number"));
                            text.append("\n\nEngine number: " + document.getString("Engine_Number"));
                            text.append("\n\nModel: " + document.getString("Model"));
                            text.append("\n\nRegistration Date: " + document.getString("Registration_Date"));
                            text.append("\n\nVehicle Price: " + document.getString("Vehicle_Price"));
                            text.append("\n\nColor: " + document.getString("Color"));
                            text.append("\n\nMake Name: " + document.getString("Make_Name"));

                        }
                    }
                }

            }
        });

    }
}