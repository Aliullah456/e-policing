package com.example.abcd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class addvehicle extends AppCompatActivity {
Toolbar vehicletoolbar;
EditText ownername;
EditText ownerfather;
EditText ownercity;
EditText vehicleplate;
EditText vehiclemakename;
EditText model;
EditText color;
EditText price;
EditText registrationdate;
EditText chassisnumber;
EditText enginenumber;
CollectionReference firestore;
Button addvehiclebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addvehicle);
        vehicletoolbar=findViewById(R.id.vehicletoolbar);
        vehicletoolbar.setTitle("add new vehicle");
        ownername=findViewById(R.id.vehicleownernametxt);
        ownerfather=findViewById(R.id.vehicleownerfathertxt);
        ownercity=findViewById(R.id.vehicleownercitytxt);
        vehicleplate=findViewById(R.id.vehicleplatenumbertxt);
        vehiclemakename=findViewById(R.id.vehiclemakenametxt);
        model=findViewById(R.id.vehiclemodeltxt);
        color=findViewById(R.id.vehiclecolortxt);
        price=findViewById(R.id.vehiclepricetxt);
        registrationdate=findViewById(R.id.vehicleregistration);
        chassisnumber=findViewById(R.id.vehiclechassisnumber);
        enginenumber=findViewById(R.id.vehicleenginenumber);
        addvehiclebtn=findViewById(R.id.addheviclebtn);
        firestore=FirebaseFirestore.getInstance().collection("car_registration");
        addvehiclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ownernamee=ownername.getText().toString();
                String ownnerfatherr=ownerfather.getText().toString();
                String ownercityy=ownercity.getText().toString();
                String vehicleplatee=vehicleplate.getText().toString();
                String vehiclemakenamee=vehiclemakename.getText().toString();
                String modell=model.getText().toString();
                String colorr=color.getText().toString();
                String pricee=price.getText().toString();
                String registrationdatee=registrationdate.getText().toString();
                String chassisnumberr=chassisnumber.getText().toString();
                String enginenumberr=enginenumber.getText().toString();
                if(ownernamee.isEmpty()&&ownnerfatherr.isEmpty()&&ownercityy.isEmpty()&&vehicleplatee.isEmpty()&&vehiclemakenamee.isEmpty()
                &&modell.isEmpty()&&colorr.isEmpty()&&pricee.isEmpty()&&registrationdatee.isEmpty()&&chassisnumberr.isEmpty()&&enginenumberr.isEmpty()){
                    Toast.makeText(addvehicle.this, "please fill all the fields first", Toast.LENGTH_SHORT).show();
                }else{
                    HashMap data =new HashMap();
                    data.put("plate_number",vehicleplatee);
                    data.put("owner_name",ownernamee);
                    data.put("owner_father",ownnerfatherr);
                    data.put("owner_city",ownercityy);
                    data.put("Make_Name",vehiclemakenamee);
                    data.put("Model",modell);
                    data.put("Color",colorr);
                    data.put("Vehicle_Price",pricee);
                    data.put("Registration_Date",registrationdatee);
                    data.put("Chassis_Number",chassisnumberr);
                    data.put("Engine_Number",enginenumberr);
                    firestore.add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()){
                                addvehicle.this.finish();
                            }else{
                                Toast.makeText(addvehicle.this, "vehicle not added", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}