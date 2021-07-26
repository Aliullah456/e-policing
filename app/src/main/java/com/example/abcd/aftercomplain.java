package com.example.abcd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

public class aftercomplain extends AppCompatActivity {
    public String trackID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aftercomplain);
        EditText text=findViewById(R.id.trackidtext);
        Button mapbutton=findViewById(R.id.mapbutton);
        Toolbar aftercomplaintoolbar=(Toolbar)findViewById(R.id.aftercomplaintoolbar);
        aftercomplaintoolbar.setTitle("FIR Information");
        Bundle bundle = getIntent().getExtras();
        trackID = bundle.getString("trackid");
        text.setText(trackID);
        mapbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(aftercomplain.this,MapsActivity.class));
            }
        });


    }
}