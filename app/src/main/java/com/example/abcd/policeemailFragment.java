package com.example.abcd;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.zip.Inflater;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link policeemailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class policeemailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
FirebaseUser firebaseAuth;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public policeemailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment policeemailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static policeemailFragment newInstance(String param1, String param2) {
        policeemailFragment fragment = new policeemailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_policeemail,container,false);
        final EditText email=view.findViewById(R.id.policenewemailtxt);
        Button changeemail=view.findViewById(R.id.changepoliceemailbtn);
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        changeemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emaill=email.getText().toString();
                if(emaill.isEmpty()){
                    email.setError("type your new email please");
                    email.requestFocus();
                }
               else if(!emaill.matches(emailPattern)){
                        email.setError("type valid email address ");
                        email.requestFocus();
                }else{
                   firebaseAuth=FirebaseAuth.getInstance().getCurrentUser();
                    final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    final String RegisteredUserID = currentUser.getUid();
                    final DocumentReference mfirestore;
                    mfirestore = FirebaseFirestore.getInstance().collection("users").document(RegisteredUserID);
                    mfirestore.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String email = documentSnapshot.getString("email");
                                String password=documentSnapshot.getString("password");
                                AuthCredential credential = EmailAuthProvider
                                        .getCredential(email, password);
                                currentUser.reauthenticate(credential)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Log.d(TAG, "User re-authenticated.");
                                                //Now change your email address \\
                                                //----------------Code for Changing Email Address----------\\
                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                user.updateEmail(emaill)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Log.d(TAG, "User email address updated.");
                                                                    FirebaseFirestore.getInstance().collection("users").document(RegisteredUserID).update(
                                                                            "email",emaill);
                                                                    Toast.makeText(getActivity(), "email has been changed", Toast.LENGTH_SHORT).show();
                                                                    firebaseAuth.sendEmailVerification();
                                                                }
                                                            }
                                                        });
                                                //----------------------------------------------------------\\
                                            }
                                        });
                            }
                        }
                    });
                }
            }
        });
        return view;
    }
}