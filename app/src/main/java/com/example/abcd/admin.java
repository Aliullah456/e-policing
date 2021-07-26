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

public class admin extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mNavDrawer;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_nav_drawer_layout);
        Toolbar admintoolbar=findViewById(R.id.admintoolbar);

        admintoolbar.setTitle("epolicing system");
        setSupportActionBar(admintoolbar);
        firebaseAuth=FirebaseAuth.getInstance();
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String RegisteredUserID = currentUser.getUid();
        final DocumentReference mfirestore;
        mfirestore = FirebaseFirestore.getInstance().collection("users").document(RegisteredUserID);
        mfirestore.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String adminname=documentSnapshot.getString("fullname");
                    ((TextView)findViewById(R.id.adminname)).setText(adminname);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.getMessage();
            }
        });
        mNavDrawer=findViewById(R.id.admin_drawer_layout);
        NavigationView navigationView=findViewById(R.id.admin_navigation_view);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(
                this,mNavDrawer,admintoolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mNavDrawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.admin_fragment_container,new adminprofieFragment())
                    .commit();
            navigationView.setCheckedItem(R.id.admin_nav_profile);
        }
    }
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.admin_nav_profile:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.admin_fragment_container,new adminprofieFragment())
                        .commit();
                break;
            case R.id.nav_police:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.admin_fragment_container,new policemanagementFragment())
                        .commit();
                break;
            case R.id.nav_complainer:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.admin_fragment_container,new complainermanagementFragment())
                        .commit();
                break;
            case R.id.admin_nav_changeemail:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.admin_fragment_container,new policeemailFragment())
                        .commit();
                break;
            case R.id.admin_nav_signout:
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