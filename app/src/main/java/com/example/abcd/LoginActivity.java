package com.example.abcd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText txt_email;
    private EditText txt_pass;
    Button signin;
    Button register;
    Button forgot;
    private ProgressDialog mLoginProgress;
    FirebaseAuth firebaseAuth;
    DocumentReference mfirestore;
    CollectionReference firestore;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("e-policing App");
        mLoginProgress = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance().collection("users");
        //if user is already logged in

//binding variables with their elements
        txt_email = (EditText) findViewById(R.id.nametext);
        txt_pass = (EditText) findViewById(R.id.passtext);
        signin = (Button) findViewById(R.id.button);
        register = (Button) findViewById(R.id.button3);
        forgot = (Button) findViewById(R.id.button2);

        //when forgot button is clicked
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), forgotpass.class));
            }

        });
        //when register button is clicked
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), signup.class));
            }
        });

        // when register new police officer
        TextView policeregisterpage=(TextView)findViewById(R.id.textView4);
        policeregisterpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),policeRegistration.class));
            }
        });

        //when signin button is clicked
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txt_email.getText().toString();
                String password = txt_pass.getText().toString();

                if (email.isEmpty()) {
                    txt_email.setError("correct email is required");
                    txt_email.requestFocus();
                } else if (password.isEmpty()) {
                    txt_pass.setError("correct password is required");
                    txt_pass.requestFocus();
                } else {
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                    mLoginProgress.setTitle("Logging in user");
                                    mLoginProgress.setMessage("Please wait while we log you in...");
                                    mLoginProgress.setCanceledOnTouchOutside(false);
                                    mLoginProgress.show();
                                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                    final String RegisteredUserID = currentUser.getUid();
                                    final String devicetoken= FirebaseInstanceId.getInstance().getToken();

                                    mfirestore = FirebaseFirestore.getInstance().collection("users").document(RegisteredUserID);
                                    mfirestore.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {
                                                String userType = documentSnapshot.getString("type");
                                                String isPolice = documentSnapshot.getString("ispolice");
                                                if (userType.equals("admin")) {
                                                    Map<String,String> data=new HashMap<>();
                                                    data.put("devicetoken",devicetoken);
                                                firestore.document(RegisteredUserID).set(data, SetOptions.merge());
                                                    mLoginProgress.dismiss();
                                                    finish();
                                                    Intent intentResident = new Intent(LoginActivity.this, admin.class);
                                                    intentResident.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intentResident);

                                                } else if (userType.equals("police")) {
                                                    if(isPolice.isEmpty()) {
                                                        Toast.makeText(LoginActivity.this, "this police account is not verified by admin", Toast.LENGTH_SHORT).show();
                                                        mLoginProgress.dismiss();
                                                    }else{
                                                        FirebaseMessaging.getInstance().subscribeToTopic("news");
                                                        Map<String, String> data = new HashMap<>();
                                                        data.put("devicetoken", devicetoken);
                                                        firestore.document(RegisteredUserID).set(data, SetOptions.merge());
                                                        mLoginProgress.dismiss();
                                                        finish();
                                                        Intent intentpolice = new Intent(LoginActivity.this, police.class);
                                                        intentpolice.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(intentpolice);
                                                    }

                                                } else if (userType.equals("complainer")) {
                                                    Map<String,String> data=new HashMap<>();
                                                    data.put("devicetoken",devicetoken);
                                                    firestore.document(RegisteredUserID).set(data,SetOptions.merge());
                                                    mLoginProgress.dismiss();
                                                    Intent intentMain = new Intent(LoginActivity.this, complainerpage.class);
                                                    intentMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intentMain);
                                                    finish();

                                                } else {

                                                    mLoginProgress.hide();
                                                    Toast.makeText(LoginActivity.this, "Failed to authenticate user", Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        }
                                    });

                                }else{
                                    Toast.makeText(LoginActivity.this,"first verfiy your account",Toast.LENGTH_LONG).show();
                                }


                            } else {
                                Toast.makeText(LoginActivity.this, "incorrect email or password!", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });
    }
}