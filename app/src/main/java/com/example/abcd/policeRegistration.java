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

import java.util.HashMap;
import java.util.Map;

public class policeRegistration extends AppCompatActivity {
    private EditText txt_idpolice,txt_fullnamepolice,txt_designationpolice,txt_policestationpolice,txt_emailpolice,txt_CNICpolice,
            txt_addresspolice,txt_numberpolice,txt_passwordpolice;
   private FirebaseAuth firebaseAuth;
   private FirebaseFirestore mfirestore;
    Button signuppolicebtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_registration);
        Toolbar signuptoolbarpolice=(Toolbar)findViewById(R.id.signuptoolbarpolice);
        signuptoolbarpolice.setTitle("Police Registration");
        txt_idpolice=(EditText)findViewById(R.id.txtidpolice);
        txt_fullnamepolice=(EditText)findViewById(R.id.txtnamepolice);
        txt_designationpolice=(EditText)findViewById(R.id.txtdesignationpolice);
        txt_policestationpolice=(EditText)findViewById(R.id.txtpolicestationpolice);
        txt_emailpolice=(EditText)findViewById(R.id.txtemailpolice);
        txt_CNICpolice=(EditText)findViewById(R.id.txtcnicpolice);
        txt_addresspolice=(EditText)findViewById(R.id.txtaddresspolice);
        txt_numberpolice=(EditText)findViewById(R.id.txtphonepolice);
        txt_passwordpolice=(EditText)findViewById(R.id.txtpasspolice);
        signuppolicebtn=(Button)findViewById(R.id.signupbtnpolice);
        firebaseAuth= FirebaseAuth.getInstance();
        mfirestore= FirebaseFirestore.getInstance();


        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        signuppolicebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String policeid=txt_idpolice.getText().toString();
                final String fullname = txt_fullnamepolice.getText().toString();
                final String designation=txt_designationpolice.getText().toString();
                final String policestation=txt_policestationpolice.getText().toString();
                final String email = txt_emailpolice.getText().toString();
                final String CNIC = txt_CNICpolice.getText().toString();
                final String address = txt_addresspolice.getText().toString();
                final String number = txt_numberpolice.getText().toString();
                final String password = txt_passwordpolice.getText().toString();
                final String type = "police";
                if(policeid.isEmpty()){
                    txt_idpolice.setError("id is required");
                    txt_idpolice.requestFocus();
                }
                else if (fullname.isEmpty()) {
                    txt_fullnamepolice.setError("name is required");
                    txt_fullnamepolice.requestFocus();
                }
                else if(designation.isEmpty()){
                    txt_designationpolice.setError("Designation is required");
                    txt_designationpolice.requestFocus();
                }
                else if(policestation.isEmpty()){
                    txt_policestationpolice.setError("police station is required");
                    txt_policestationpolice.requestFocus();
                }
                else if (email.isEmpty()) {
                    txt_emailpolice.setError("email is required");
                    txt_emailpolice.requestFocus();
                } else if (!email.matches(emailPattern)) {
                    txt_emailpolice.setError("valid email is required");
                    txt_emailpolice.requestFocus();
                } else if (CNIC.isEmpty()) {
                    txt_CNICpolice.setError("CNIC is required");
                    txt_CNICpolice.requestFocus();
                }
                else if(CNIC.length()<13||CNIC.length()>13){
                    txt_CNICpolice.setError("CNIC must be 13 digit");
                    txt_CNICpolice.requestFocus();
                }
                else if (address.isEmpty()) {
                    txt_addresspolice.setError("address is required");
                    txt_addresspolice.requestFocus();
                } else if (number.isEmpty()) {
                    txt_numberpolice.setError("phone number is required");
                    txt_numberpolice.requestFocus();
                } else if (number.length() < 11) {
                    txt_numberpolice.setError("11 digit is required");
                    txt_numberpolice.requestFocus();
                } else if (password.isEmpty()) {
                    txt_passwordpolice.setError("password is required");
                    txt_passwordpolice.requestFocus();
                }
                else if (password.length()<6) {
                    txt_passwordpolice.setError("password is too short");
                    txt_passwordpolice.requestFocus();
                }
                else {
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Map<String,String> data=new HashMap<>();
                                String user_id=firebaseAuth.getUid();
                                data.put("policeid",policeid);
                                data.put("fullname",fullname);
                                data.put("designation",designation);
                                data.put("policestation",policestation);
                                data.put("email",email);
                                data.put("CNIC",CNIC);
                                data.put("password",password);
                                data.put("mobilenumber",number);
                                data.put("address",address);
                                data.put("type",type);
                                data.put("user_id",user_id);
                                data.put("ispolice","");
                                mfirestore.collection("users").document(firebaseAuth.getUid()).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){

                                            firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(policeRegistration.this,"police registered email verification has been send",Toast.LENGTH_LONG).show();
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
                                Toast.makeText(policeRegistration.this,"police with this email already exists",Toast.LENGTH_SHORT).show();
                            }
                                Toast.makeText(policeRegistration.this,"police registeration failed",Toast.LENGTH_SHORT);

                            }

                        }
                    });
                }
            }
        });
    }
}