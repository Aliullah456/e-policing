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
import com.google.firebase.auth.FirebaseAuth;

public class forgotpass extends AppCompatActivity {
    private Toolbar forgotpasstoolbar;
    private EditText email;
    private Button submit;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);
        forgotpasstoolbar=(Toolbar)findViewById(R.id.forgotpasstoolbar);
        forgotpasstoolbar.setTitle("Reset password");
        email=(EditText)findViewById(R.id.editText);
        submit=(Button)findViewById(R.id.button7);
        firebaseAuth=FirebaseAuth.getInstance();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eemail=email.getText().toString();
                if(eemail.isEmpty()){
                    email.setError("please type your email");
                }
                else{
                    firebaseAuth.sendPasswordResetEmail(eemail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(forgotpass.this,"please check your inbox",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(forgotpass.this,LoginActivity.class ));
                            }
                            else{
                                Toast.makeText(forgotpass.this,"sorry there is a problem please try again",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
