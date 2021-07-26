package com.example.abcd;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link policeprofileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class policeprofileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_CODE_IMAGE = 1001;
    public ImageView policeprofile;
    private StorageReference firebaseStorage;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseAuth firebaseAuth;
    public policeprofileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment policeprofileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static policeprofileFragment newInstance(String param1, String param2) {
        policeprofileFragment fragment = new policeprofileFragment();
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
        View view=inflater.inflate(R.layout.fragment_policeprofile,container,false);
        final EditText pass=(EditText)view.findViewById(R.id.policecpasss);
        final EditText passagain=(EditText)view.findViewById(R.id.policecpasssagain);
        Button changepassbtn=(Button)view.findViewById(R.id.policechangepassbtn);
       policeprofile=(ImageView)view.findViewById(R.id.policeprofilepic);
        Button changepoliceprofilepicbtn=(Button)view.findViewById(R.id.changepoliceprofilebtn);
        final Button cpasschangebtn=(Button)view.findViewById(R.id.policecpasschangebtn);
        Button cdeletebtn=(Button)view.findViewById(R.id.policecdeletebtn);
        final TextView policeemailshowtxt=(TextView)view.findViewById(R.id.policeemailtextshow);
        //final TextInputLayout textfield2=view.findViewById(R.id.textField2);
        //final TextInputLayout textfield3=view.findViewById(R.id.textField3);
        firebaseStorage=FirebaseStorage.getInstance().getReference();

        pass.setVisibility(view.GONE);
        passagain.setVisibility(view.GONE);
        cpasschangebtn.setVisibility(view.GONE);
       // textfield2.setVisibility(view.GONE);
      //  textfield3.setVisibility(view.GONE);
        changepassbtn.setBackgroundDrawable(null);
        //if change password button is clicked
        changepassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pass.getVisibility()==view.GONE||passagain.getVisibility()==view.GONE||cpasschangebtn.getVisibility()==view.GONE
               // textfield2.getVisibility()==view.GONE||textfield3.getVisibility()==view.GONE
                ) {

                    pass.setVisibility(view.VISIBLE);
                    passagain.setVisibility(view.VISIBLE);
                    cpasschangebtn.setVisibility(view.VISIBLE);
                   // textfield2.setVisibility(view.VISIBLE);
                   // textfield3.setVisibility(view.VISIBLE);
                }
                else{
                    pass.setVisibility(view.GONE);
                    passagain.setVisibility(view.GONE);
                    cpasschangebtn.setVisibility(view.GONE);
                    //textfield2.setVisibility(view.GONE);
                    //textfield3.setVisibility(view.GONE);
                    pass.setText("");
                    passagain.setText("");
                }
            }
        });
        FirebaseAuth firebaseAuth;
        firebaseAuth=FirebaseAuth.getInstance();
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String RegisteredUserID = currentUser.getUid();
        final DocumentReference mfirestore;
        mfirestore = FirebaseFirestore.getInstance().collection("users").document(RegisteredUserID);
        mfirestore.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String name = documentSnapshot.getString("email");
                    policeemailshowtxt.setText(name);
                }
            }
        });
        firebaseAuth=FirebaseAuth.getInstance();
        changepoliceprofilepicbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent OpenGallaryIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(OpenGallaryIntent,1000);
            }
        });
        StorageReference profileRef=firebaseStorage.child("users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
           Picasso.get().load(uri).into(policeprofile);
            }
        });
        //when user press change button to change password
        cpasschangebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password=pass.getText().toString();
                String passwordagain=passagain.getText().toString();
                if(password.isEmpty())
                {
                    pass.setError("password is required");
                    pass.requestFocus();

                }
                else if(passwordagain.isEmpty())
                {
                    passagain.setError("please type new password again");
                    passagain.requestFocus();
                }
                else{
                    if(passwordagain.equals(password)){
                        currentUser.updatePassword(password);
                        Toast.makeText(getActivity(),"Password is changed successfully",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getActivity(),"password not matched",Toast.LENGTH_SHORT).show();
                    }
                }}
        });

        //when delete button is clicked
        cdeletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        FirebaseFirestore.getInstance().collection("users").document(RegisteredUserID).delete();
                        currentUser.delete();
                        StorageReference profileRef=firebaseStorage.child("users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profile.jpg");
                        profileRef.delete();
                        getActivity().finish();
                        startActivity(new Intent(getActivity(),LoginActivity.class));

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setMessage("Do you want to delete your account");
                builder.setTitle("Deleting user");
                builder.setIcon(R.drawable.ic_warning);
                AlertDialog d = builder.create();
                d.show();
            }
        });
        return view;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            if(resultCode==Activity.RESULT_OK){
                assert data != null;
                Uri imageUri=data.getData();
                //policeprofile.setImageURI(imageUri);
                UploadImagetoFirebase(imageUri);

            }
        }
    }
    private void UploadImagetoFirebase(Uri imageUri) {
        final StorageReference fireRef=firebaseStorage.child("users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profile.jpg");
    fireRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
        fireRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
               // Picasso.get().load(uri).into(policeprofile);
                String image_idd=uri.toString();
                HashMap d=new HashMap();
                d.put("profileimage",image_idd);
                FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid()).update(d).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(getActivity(), "image uploaded", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(getActivity(),"Image Upload failed", Toast.LENGTH_SHORT).show();

        }
    });
    }
}