package com.example.abcd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class vehicleviewActivity extends AppCompatActivity {
    EditText updateownername;
    EditText updateownerfather;
    EditText updateownercity;
    EditText updatevehicleplate;
    EditText updatevehiclemakename;
    EditText updatemodel;
    EditText updatecolor;
    EditText updateprice;
    EditText updateregistrationdate;
    EditText updatechassisnumber;
    EditText updateenginenumber;
    Task<DocumentSnapshot> firestore;
    Button updatevehiclebtn;
    String keyy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicleview);
        updateownername=findViewById(R.id.updatevehicleownernametxt);
        updateownerfather=findViewById(R.id.updatevehicleownerfathertxt);
        updateownercity=findViewById(R.id.updatevehicleownercitytxt);
        updatevehicleplate=findViewById(R.id.updatevehicleplatenumbertxt);
        updatevehiclemakename=findViewById(R.id.updatevehiclemakenametxt);
        updatemodel=findViewById(R.id.updatevehiclemodeltxt);
        updatecolor=findViewById(R.id.updatevehiclecolortxt);
        updateprice=findViewById(R.id.updatevehiclepricetxt);
        updateregistrationdate=findViewById(R.id.updatevehicleregistration);
        updatechassisnumber=findViewById(R.id.updatevehiclechassisnumber);
        updateenginenumber=findViewById(R.id.updatevehicleenginenumber);
        updatevehiclebtn=findViewById(R.id.updateheviclebtn);
        keyy=getIntent().getStringExtra("vehicle_id");
        firestore= FirebaseFirestore.getInstance().collection("car_registration").document(keyy).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    updatevehicleplate.setText(documentSnapshot.getString("plate_number"));
                    updateownername.setText(documentSnapshot.getString("owner_name"));
                    updateownerfather.setText(documentSnapshot.getString("owner_father"));
                    updateownercity.setText(documentSnapshot.getString("owner_city"));
                    updatevehiclemakename.setText(documentSnapshot.getString("Make_Name"));
                    updatemodel.setText(documentSnapshot.getString("Model"));
                    updatecolor.setText(documentSnapshot.getString("Color"));
                    updateprice.setText(documentSnapshot.getString("Vehicle_Price"));
                    updateregistrationdate.setText(documentSnapshot.getString("Registration_Date"));
                    updatechassisnumber.setText(documentSnapshot.getString("Chassis_Number"));
                    updateenginenumber.setText(documentSnapshot.getString("Engine_Number"));
                }
            }
        });
        updatevehiclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ownernamee=updateownername.getText().toString();
                String ownnerfatherr=updateownerfather.getText().toString();
                String ownercityy=updateownercity.getText().toString();
                String vehicleplatee=updatevehicleplate.getText().toString();
                String vehiclemakenamee=updatevehiclemakename.getText().toString();
                String modell=updatemodel.getText().toString();
                String colorr=updatecolor.getText().toString();
                String pricee=updateprice.getText().toString();
                String registrationdatee=updateregistrationdate.getText().toString();
                String chassisnumberr=updatechassisnumber.getText().toString();
                String enginenumberr=updateenginenumber.getText().toString();
                if(ownernamee.isEmpty()&&ownnerfatherr.isEmpty()&&ownercityy.isEmpty()&&vehicleplatee.isEmpty()&&vehiclemakenamee.isEmpty()
                        &&modell.isEmpty()&&colorr.isEmpty()&&pricee.isEmpty()&&registrationdatee.isEmpty()&&chassisnumberr.isEmpty()&&enginenumberr.isEmpty()){
                    Toast.makeText(vehicleviewActivity.this, "please fill all the fields first", Toast.LENGTH_SHORT).show();
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
                    FirebaseFirestore.getInstance().collection("car_registration").document(keyy).update(data).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                       if(task.isSuccessful()){
                           Toast.makeText(vehicleviewActivity.this, "data updated successfullt", Toast.LENGTH_SHORT).show();
                           vehicleviewActivity.this.finish();
                       }else{
                           Toast.makeText(vehicleviewActivity.this, "could'nt update ", Toast.LENGTH_SHORT).show();
                       }
                        }
                    });
                }
            }
        });
    }
}