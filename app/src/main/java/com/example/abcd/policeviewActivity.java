package com.example.abcd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class policeviewActivity extends AppCompatActivity {
    private ImageView updatepoliceimageview;
    private EditText updatepoliceid;
    private EditText updatepolicename;
    private EditText updatedesignation;
    private EditText updatepolicestation;
    private EditText updatepoliceemail;
    private  EditText updatepolicecnic;
    private  EditText updatepolicephone;
    private  EditText updatepoliceaddress;
    private  EditText updateconfirmation;
    private Button updatepolicebtn;
    private TextView updatepoliceprogresstxt;
    private ProgressBar updatepoliceprogressbar;
    private Toolbar updatepolicetoolbar;
    private  String keyy;
    private static final int REQUEST_CODE_IMAGE = 101;
    private Uri imageUri;
    private StorageReference storee;
    private boolean isImageAdded = false;
    private CollectionReference collectionReference;
    private FirebaseStorage storageReference;
    private String imageid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policeview);
        updatepoliceimageview=findViewById(R.id.updatepolicepicture);
        updatepoliceid=findViewById(R.id.updatepoliceid);
        updatepolicename=findViewById(R.id.updatepolicename);
        updatedesignation=findViewById(R.id.updatepolicedesignation);
        updatepolicestation=findViewById(R.id.updatepolicestation);
        updatepoliceemail=findViewById(R.id.updatepoliceemail);
        updatepolicecnic=findViewById(R.id.updatepoliceCNIC);
        updatepolicephone=findViewById(R.id.updatepolicephone);
        updatepoliceaddress=findViewById(R.id.updatepoliceaddress);
        updateconfirmation=findViewById(R.id.updateconfirmation);
        updatepolicebtn=findViewById(R.id.updatepolicesavebtn);
        updatepoliceprogresstxt=findViewById(R.id.updatepolicetextviewporgress);
        updatepoliceprogressbar=findViewById(R.id.updatepoliceprogressbar);
        updatepoliceprogresstxt.setVisibility(View.GONE);
        updatepoliceprogressbar.setVisibility(View.GONE);
        collectionReference= FirebaseFirestore.getInstance().collection("users");
        storageReference= FirebaseStorage.getInstance();
        storee=FirebaseStorage.getInstance().getReference().child("users");
        keyy=getIntent().getStringExtra("police_key");
        StorageReference profileRef=FirebaseStorage.getInstance().getReference().child("users/"+ keyy+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(updatepoliceimageview);
                isImageAdded=true;
            }
        });
        collectionReference.document(keyy).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                        updatepoliceid.setText(documentSnapshot.getString("policeid"));
                        updatepolicename.setText(documentSnapshot.getString("fullname"));
                        updatedesignation.setText(documentSnapshot.getString("designation"));
                        updatepolicestation.setText(documentSnapshot.getString("policestation"));
                        updatepoliceemail.setText(documentSnapshot.getString("email"));
                        updatepolicecnic.setText(documentSnapshot.getString("CNIC"));
                        updatepolicephone.setText(documentSnapshot.getString("mobilenumber"));
                        updatepoliceaddress.setText(documentSnapshot.getString("address"));
                        updateconfirmation.setText(documentSnapshot.getString("ispolice"));

                }
            }

        });
        updatepoliceimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent OpenGallaryIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(OpenGallaryIntent,1000);
            }
        });
        updatepolicebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addpoliceidd=updatepoliceid.getText().toString();
                String addpolicenamee=updatepolicename.getText().toString();
                String addpolicedesignation=updatedesignation.getText().toString();
                String addpolicestationn=updatepolicestation.getText().toString();
                String addpoliceemaill=updatepoliceemail.getText().toString();
                String addpolicecnicc=updatepolicecnic.getText().toString();
                String addpolicephonee=updatepolicephone.getText().toString();
                String addpoliceaddressss=updatepoliceaddress.getText().toString();
                String addconfirmation=updateconfirmation.getText().toString();
                if (addpoliceidd != null&&addpolicenamee != null&&addpolicedesignation != null&&
                        addpolicestationn != null &&addpoliceemaill!=null&&addpolicecnicc!=null&&addpolicephonee!=null&&
                addpoliceaddressss!=null&&addconfirmation!=null) {
                    uploadImage(addpoliceidd,addpolicenamee,addpolicedesignation,addpolicestationn,addpoliceemaill,addpolicecnicc,addpolicephonee
                    ,addpoliceaddressss,addconfirmation);
                }else{
                    Toast.makeText(policeviewActivity.this, "didnt change due to no", Toast.LENGTH_SHORT).show();
                }
                }
        });

    }
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }
    private void uploadImage(final String addpoliceid,  final String addpolicename, final String addpolicedesignation,
                             final String addpolicestation,final String addpoliceemail, final String addpolicecnicc,
                             final String addpolicephonee, final String addpoliceaddressss, final String addconfirmation) {
        updatepoliceprogresstxt.setVisibility(View.VISIBLE);
        updatepoliceprogressbar.setVisibility(View.VISIBLE);
        if(imageUri!=null){
//            collectionReference.document(keyy).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                @Override
//                public void onSuccess(DocumentSnapshot documentSnapshot) {
//                    String image=documentSnapshot.getString("profileimage");
//                    StorageReference ss=storageReference.getReferenceFromUrl(image);
//                    ss.delete();
//                }
//            });
            final StorageReference fireRef=FirebaseStorage.getInstance().getReference()
                    .child("users/"+keyy+"/profile.jpg");
           // imageid=System.currentTimeMillis()+"."+GetFileExtension(imageUri);
            //final StorageReference Ref=storee.child(imageid);
            fireRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fireRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String image_idd=uri.toString();
                                    Map<String, Object> pic = new HashMap<>();
                                    pic.put("profileimage",image_idd);
                                    pic.put("policeid",addpoliceid);
                                    pic.put("fullname",addpolicename);
                                    pic.put("designation",addpolicedesignation);
                                    pic.put("policestation",addpolicestation);
                                    pic.put("email",addpoliceemail);
                                    pic.put("CNIC",addpolicecnicc);
                                    pic.put("mobilenumber",addpolicephonee);
                                    pic.put("address",addpoliceaddressss);
                                    pic.put("ispolice",addconfirmation);
                                    FirebaseFirestore.getInstance().collection("users").document(keyy).update(pic);
                                    Toast.makeText(policeviewActivity.this, "file Uploaded", Toast.LENGTH_SHORT).show();
                                    imageUri=null;
                                    policeviewActivity.this.finish();
                                }
                            });

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress=(taskSnapshot.getBytesTransferred()*100)/taskSnapshot.getTotalByteCount();
                    updatepoliceprogressbar.setProgress((int)progress);
                    updatepoliceprogresstxt.setText(progress+" %");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(policeviewActivity.this, "Couldnt upload", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Map<String, Object> pic = new HashMap<>();
            pic.put("policeid",addpoliceid);
            pic.put("fullname",addpolicename);
            pic.put("designation",addpolicedesignation);
            pic.put("policestation",addpolicestation);
            pic.put("email",addpoliceemail);
            pic.put("CNIC",addpolicecnicc);
            pic.put("mobilenumber",addpolicephonee);
            pic.put("address",addpoliceaddressss);
            pic.put("ispolice",addconfirmation);
            FirebaseFirestore.getInstance().collection("users").document(keyy).update(pic).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(policeviewActivity.this, "updated successfully", Toast.LENGTH_SHORT).show();
                        policeviewActivity.this.finish();
                    }
                }
            }) ;     }
    }
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            if(resultCode== Activity.RESULT_OK){
                imageUri=data.getData();
                updatepoliceimageview.setImageURI(imageUri);
                isImageAdded=true;
               // UploadImagetoFirebase(imageUri);

            }
        }
    }
}