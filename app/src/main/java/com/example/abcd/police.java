package com.example.abcd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

public class police extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mNavDrawer;
    private StorageReference storageReference;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer_layout);
        Toolbar policetoolbar=findViewById(R.id.policetoolbar);

        policetoolbar.setTitle("Police section");
        setSupportActionBar(policetoolbar);
        FirebaseAuth firebaseAuth;
        firebaseAuth=FirebaseAuth.getInstance();
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String RegisteredUserID = currentUser.getUid();
        final DocumentReference mfirestore;

        mfirestore = FirebaseFirestore.getInstance().collection("users").document(RegisteredUserID);
        mfirestore.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String Policename=documentSnapshot.getString("fullname");
                    ((TextView)findViewById(R.id.policename)).setText(Policename);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.getMessage();
            }
        });
        mNavDrawer=findViewById(R.id.drawer_layout);
        NavigationView navigationView=findViewById(R.id.navigation_view);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(
                this,mNavDrawer,policetoolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mNavDrawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container,new policeprofileFragment())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_profile);
        }
    }

    @Override
    public void onBackPressed() {
        if(mNavDrawer.isDrawerOpen(GravityCompat.START)){
            mNavDrawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,new policeprofileFragment())
                        .commit();
                break;
            case R.id.nav_plate_number:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,new PlatenumberFragment())
                        .commit();
                break;
            case R.id.nav_complaint:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,new complaintmanagementFragment())
                        .commit();
                break;
            case R.id.nav_criminal:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,new criminalmanagementFragment())
                        .commit();
                break;
            case R.id.nav_victim:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,new vehiclemanagementFragment())
                        .commit();
                break;
            case R.id.nav_evidence:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,new evidencemanagementFragment())
                        .commit();
                break;
            case R.id.nav_witness:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,new witnessmanagementFragment())
                        .commit();
                break;
            case R.id.nav_changeemail:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,new policeemailFragment())
                        .commit();
                break;
            case R.id.nav_signout:
                FirebaseAuth.getInstance().signOut();
                this.finish();
                Toast.makeText(this,"Logging out",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,LoginActivity.class));

                break;

        }
        mNavDrawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
