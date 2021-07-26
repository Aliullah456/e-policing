package com.example.abcd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity {
    private EditText txt_fullname,txt_email,txt_CNIC,txt_address,txt_number,txt_password;
    private Button regbtn;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore mfirestore;
    FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar signuptoolbar=(Toolbar)findViewById(R.id.signuptoolbar);
        signuptoolbar.setTitle("User Signup");
        txt_fullname=(EditText)findViewById(R.id.txtname);
        txt_email=(EditText)findViewById(R.id.txtemail);
        txt_CNIC=(EditText)findViewById(R.id.txtcnic);
        txt_address=(EditText)findViewById(R.id.txtaddress);
        txt_number=(EditText)findViewById(R.id.txtphone);
        txt_password=(EditText)findViewById(R.id.txtpass);
        regbtn=(Button)findViewById(R.id.signupbtn);
        firebaseAuth=FirebaseAuth.getInstance();
        mfirestore=FirebaseFirestore.getInstance();


        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fullname = txt_fullname.getText().toString();
                final String email = txt_email.getText().toString();
                final String CNIC = txt_CNIC.getText().toString();
                final String address = txt_address.getText().toString();
                final String number = txt_number.getText().toString();
                final String password = txt_password.getText().toString();
                final String type = "complainer";
                if (fullname.isEmpty()) {
                    txt_fullname.setError("name is required");
                    txt_fullname.requestFocus();
                } else if (email.isEmpty()) {
                    txt_email.setError("email is required");
                    txt_email.requestFocus();
                } else if (!email.matches(emailPattern)) {
                    txt_email.setError("valid email is required");
                    txt_email.requestFocus();
                } else if (CNIC.isEmpty()) {
                    txt_CNIC.setError("CNIC is required");
                    txt_CNIC.requestFocus();
                }
                else if(CNIC.length()<13||CNIC.length()>13){
                    txt_CNIC.setError("CNIC must be 13 digit");
                    txt_CNIC.requestFocus();
                }
                else if (address.isEmpty()) {
                    txt_address.setError("address is required");
                    txt_address.requestFocus();
                } else if (number.isEmpty()) {
                    txt_number.setError("number is required");
                    txt_number.requestFocus();
                } else if (number.length() < 11) {
                    txt_number.setError("11 digit is required");
                    txt_number.requestFocus();
                } else if (password.isEmpty()) {
                    txt_password.setError("password is required");
                    txt_password.requestFocus();
                }
                else if (password.length()<6) {
                    txt_password.setError("password is too short");
                    txt_password.requestFocus();
                }
                else {
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Map<String,String> data=new HashMap<>();
                                String user_id=firebaseAuth.getUid();
                                data.put("fullname",fullname);
                                data.put("email",email);
                                data.put("CNIC",CNIC);
                                data.put("password",password);
                                data.put("mobilenumber",number);
                                data.put("address",address);
                                data.put("type",type);
                                data.put("user_id",user_id);
                                mfirestore.collection("users").document(firebaseAuth.getUid()).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){

                                            firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(signup.this,"user registered email verification has been send",Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                            //Toast.makeText(signup.this,"user registered successfully",Toast.LENGTH_SHORT);
                                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                            finish();
                                        }

                                    }
                                });
                            }
                            else{if(task.getException()instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(signup.this,"User with this email already exists",Toast.LENGTH_SHORT).show();
                            }
                                Toast.makeText(signup.this,"user registeration fail",Toast.LENGTH_SHORT);

                            }

                        }
                    });
                }
            }
        });
    }
}