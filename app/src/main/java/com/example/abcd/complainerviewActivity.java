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

public class complainerviewActivity extends AppCompatActivity {
    private ImageView updatecomplainerimageview;
    private EditText updatecomplainername;
    private EditText updatecomplaineremail;
    private  EditText updatecomplainercnic;
    private  EditText updatecomplainerphone;
    private  EditText updatecomplaineraddress;
    private Button updatecomplainerbtn;
    private TextView updatecomplainerprogresstxt;
    private ProgressBar updatecomplainerprogressbar;
    private Toolbar updatecomplainertoolbar;
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
        setContentView(R.layout.activity_complainerview);
        updatecomplainerimageview=findViewById(R.id.updatecomplainerpicture);
        updatecomplainername=findViewById(R.id.updatecomplainername);
        updatecomplaineremail=findViewById(R.id.updatecomplaineremail);
        updatecomplainercnic=findViewById(R.id.updatecomplainerCNIC);
        updatecomplainerphone=findViewById(R.id.updatecomplainerphone);
        updatecomplaineraddress=findViewById(R.id.updatecomplaineraddress);
        updatecomplainerbtn=findViewById(R.id.updatecomplainersavebtn);
        updatecomplainerprogresstxt=findViewById(R.id.updatecomplainertextviewporgress);
        updatecomplainerprogressbar=findViewById(R.id.updatecomplainerprogressbar);
        updatecomplainerprogresstxt.setVisibility(View.GONE);
        updatecomplainerprogressbar.setVisibility(View.GONE);
        collectionReference= FirebaseFirestore.getInstance().collection("users");
        storageReference= FirebaseStorage.getInstance();
        storee=FirebaseStorage.getInstance().getReference().child("users");
        keyy=getIntent().getStringExtra("complainer_key");
        StorageReference profileRef=FirebaseStorage.getInstance().getReference().child("users/"+ keyy+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(updatecomplainerimageview);
                isImageAdded=true;
            }
        });
        collectionReference.document(keyy).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    updatecomplainername.setText(documentSnapshot.getString("fullname"));
                    updatecomplaineremail.setText(documentSnapshot.getString("email"));
                    updatecomplainercnic.setText(documentSnapshot.getString("CNIC"));
                    updatecomplainerphone.setText(documentSnapshot.getString("mobilenumber"));
                    updatecomplaineraddress.setText(documentSnapshot.getString("address"));
                }
            }

        });
        updatecomplainerimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent OpenGallaryIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(OpenGallaryIntent,1000);
            }
        });
        updatecomplainerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addcomplainernamee=updatecomplainername.getText().toString();
                String addcomplaineremaill=updatecomplaineremail.getText().toString();
                String addcomplainercnicc=updatecomplainercnic.getText().toString();
                String addcomplainerphonee=updatecomplainerphone.getText().toString();
                String addcomplaineraddressss=updatecomplaineraddress.getText().toString();
                if (addcomplainernamee != null && addcomplaineremaill!=null&&addcomplainercnicc!=null&&addcomplainerphonee!=null&&
                        addcomplaineraddressss!=null) {
                    uploadImage(addcomplainernamee,addcomplaineremaill,addcomplainercnicc,addcomplainerphonee,addcomplaineraddressss);
                }else{
                    Toast.makeText(complainerviewActivity.this, "didnt change due to no", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }
    private void uploadImage( final String addcomplainername,final String addcomplaineremail, final String addcomplainercnicc,
                             final String addcomplainerphonee, final String addcomplaineraddressss) {
        updatecomplainerprogresstxt.setVisibility(View.VISIBLE);
        updatecomplainerprogressbar.setVisibility(View.VISIBLE);
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
                                    pic.put("fullname",addcomplainername);
                                    pic.put("email",addcomplaineremail);
                                    pic.put("CNIC",addcomplainercnicc);
                                    pic.put("mobilenumber",addcomplainerphonee);
                                    pic.put("address",addcomplaineraddressss);
                                    FirebaseFirestore.getInstance().collection("users").document(keyy).update(pic);
                                    Toast.makeText(complainerviewActivity.this, "file Uploaded", Toast.LENGTH_SHORT).show();
                                    imageUri=null;
                                    complainerviewActivity.this.finish();
                                }
                            });

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress=(taskSnapshot.getBytesTransferred()*100)/taskSnapshot.getTotalByteCount();
                    updatecomplainerprogressbar.setProgress((int)progress);
                    updatecomplainerprogresstxt.setText(progress+" %");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(complainerviewActivity.this, "Couldnt upload", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Map<String, Object> pic = new HashMap<>();
            pic.put("fullname",addcomplainername);
            pic.put("email",addcomplaineremail);
            pic.put("CNIC",addcomplainercnicc);
            pic.put("mobilenumber",addcomplainerphonee);
            pic.put("address",addcomplaineraddressss);
            FirebaseFirestore.getInstance().collection("users").document(keyy).update(pic).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(complainerviewActivity.this, "updated successfully", Toast.LENGTH_SHORT).show();
                        complainerviewActivity.this.finish();
                    }
                }
            }) ;     }
    }
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            if(resultCode== Activity.RESULT_OK){
                imageUri=data.getData();
                updatecomplainerimageview.setImageURI(imageUri);
                isImageAdded=true;
                // UploadImagetoFirebase(imageUri);

            }
        }
    }
}