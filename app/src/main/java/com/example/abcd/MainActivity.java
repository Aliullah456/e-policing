package com.example.abcd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    DocumentReference mfirestore;

    private FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth=FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // Sign in logic here.
                    finish();
                    Intent loginn=new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(loginn);
                }
                else{
                    if(firebaseAuth.getCurrentUser().isEmailVerified()){


                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        String RegisteredUserID = currentUser.getUid();
                        mfirestore = FirebaseFirestore.getInstance().collection("users").document(RegisteredUserID);
                        mfirestore.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    String userType = documentSnapshot.getString("type");
                                    String isPolice = documentSnapshot.getString("ispolice");
                                    if (userType.equals("admin")) {

                                        finish();
                                        Intent intentResident = new Intent(MainActivity.this, admin.class);
                                        intentResident.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intentResident);

                                    } else if (userType.equals("police")) {
                                        if(isPolice.isEmpty()){
                                            finish();
                                            Intent loginn=new Intent(getApplicationContext(),LoginActivity.class);
                                            startActivity(loginn);
                                        }
                                        else {
                                            finish();
                                            Intent intentpolice = new Intent(MainActivity.this, police.class);
                                            intentpolice.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intentpolice);
                                        }

                                    } else if (userType.equals("complainer")) {

                                        finish();
                                        Intent intentMain = new Intent(MainActivity.this, complainerpage.class);
                                        intentMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intentMain);

                                    } else {

                                        Toast.makeText(MainActivity.this, "Failed to authenticate user", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }
                        });

                    }
                    else{
                        finish();
                        Intent loginn=new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(loginn);
                    }
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}
