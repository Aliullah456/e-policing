package com.example.abcd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class complaintviewActivity extends AppCompatActivity {
    private  String keyy;
    CollectionReference collectionReference;
    FirebaseStorage storageReference;
    private StorageReference storee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaintview);
        final EditText trackid=findViewById(R.id.viewcomplainttrackeridpolicetxt);
        final EditText complainerid=findViewById(R.id.viewcomplaineridpolicetxt);
        final EditText crimetype=findViewById(R.id.viewcrimetypepolicetxt);
        final EditText crime=findViewById(R.id.viewcrimepolicetxt);
        final EditText crimedetail=findViewById(R.id.viewcrimedetailtypepolicetxt);
        final EditText region=findViewById(R.id.viewcrimecitypolicetxt);
        final EditText area=findViewById(R.id.viewcrimeareapolicetxt);
        final EditText address=findViewById(R.id.viewaddresspolicetxt);
        final EditText date=findViewById(R.id.viewdatepolicetxt);
        final EditText time=findViewById(R.id.viewtimepolicetxt);
        final EditText status=findViewById(R.id.viewcrimestatuspolicetxt);
        Button update=findViewById(R.id.viewcomplaintupdatebutton);
        collectionReference= FirebaseFirestore.getInstance().collection("complain");
        keyy=getIntent().getStringExtra("track_id");
        collectionReference.document(keyy).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    trackid.setText(documentSnapshot.getString("Trackid".toString()));
                    complainerid.setText(documentSnapshot.getString("complainer_id".toString()));
                    crimetype.setText(documentSnapshot.getString("crime_catogry".toString()));
                    crime.setText(documentSnapshot.getString("crime"));
                    crimedetail.setText(documentSnapshot.getString("detail"));
                    region.setText(documentSnapshot.getString("region"));
                    area.setText(documentSnapshot.getString("sector"));
                    address.setText(documentSnapshot.getString("address"));
                    date.setText(documentSnapshot.getString("date"));
                    time.setText(documentSnapshot.getString("time"));
                    status.setText(documentSnapshot.getString("status"));
                }
            }
        });
       update.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
            String trackidd=trackid.getText().toString();
            String complaineridd=complainerid.getText().toString();
            String crimetypee=crimetype.getText().toString();
            String crimee=crime.getText().toString();
            String crimedetaill=crimedetail.getText().toString();
            String regionn=region.getText().toString();
            String areaa=area.getText().toString();
            String addressss=address.getText().toString();
            String datee=date.getText().toString();
            String timee=time.getText().toString();
            String statuss=status.getText().toString();
            HashMap data=new HashMap();
            data.put("Trackid",trackidd);
            data.put("complainer_id",complaineridd);
            data.put("crime_catogry",crimetypee);
            data.put("crime",crimee);
            data.put("detail",crimedetaill);
            data.put("region",regionn);
            data.put("sector",areaa);
            data.put("address",addressss);
            data.put("date",datee);
            data.put("time",timee);
            data.put("status",statuss);
            collectionReference.document(keyy).update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(complaintviewActivity.this, "data updated successfully", Toast.LENGTH_LONG).show();
                        complaintviewActivity.this.finish();
                    }else{
                        Toast.makeText(complaintviewActivity.this, "some error occurs", Toast.LENGTH_SHORT).show();
                    }
                }
            });
           }
       });

    }
}