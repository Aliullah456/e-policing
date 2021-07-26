package com.example.abcd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

public class criminalviewActivity extends AppCompatActivity {
    private ImageView updatecriminalimageview;
    private EditText updatecriminalname;
    private  EditText updatecriminalcnic;
    private  EditText updatecriminalphone;
    private  EditText updatecriminaladdress;
    private EditText updatecriminalage;
    private  EditText updatecriminalcomplaintid;
    private Button updatecriminalbtn;
    private TextView updatecriminalprogresstxt;
    private ProgressBar updatecriminalprogressbar;
    private Toolbar updatecriminaltoolbar;
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
        setContentView(R.layout.activity_criminalview);
        updatecriminalimageview=findViewById(R.id.updatecriminalpicture);
        updatecriminalname=findViewById(R.id.updatecriminalname);
        updatecriminalcnic=findViewById(R.id.updatecriminalCNIC);
        updatecriminalphone=findViewById(R.id.updatecriminalphone);
        updatecriminaladdress=findViewById(R.id.updatecriminaladdress);
        updatecriminalage=findViewById(R.id.updatecriminalage);
        updatecriminalcomplaintid=findViewById(R.id.updatecriminalcomplaint);
        updatecriminalbtn=findViewById(R.id.updatecriminalsavebtn);
        updatecriminalprogresstxt=findViewById(R.id.updatecriminaltextviewporgress);
        updatecriminalprogressbar=findViewById(R.id.updatecriminalprogressbar);
        updatecriminalprogresstxt.setVisibility(View.GONE);
        updatecriminalprogressbar.setVisibility(View.GONE);
        collectionReference= FirebaseFirestore.getInstance().collection("criminals");
        storageReference= FirebaseStorage.getInstance();
        storee=FirebaseStorage.getInstance().getReference().child("Criminals");
        keyy=getIntent().getStringExtra("criminal_key");
        collectionReference.document(keyy).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Picasso.get().load(documentSnapshot.getString("file_uri")).into(updatecriminalimageview);
                    updatecriminalname.setText(documentSnapshot.getString("criminal_name"));
                    updatecriminalcnic.setText(documentSnapshot.getString("criminal_cnic"));
                    updatecriminalphone.setText(documentSnapshot.getString("criminal_phone"));
                    updatecriminaladdress.setText(documentSnapshot.getString("criminal_address"));
                    updatecriminalage.setText(documentSnapshot.getString("criminal_age"));
                    updatecriminalcomplaintid.setText(documentSnapshot.getString("complaint_id"));
                    isImageAdded=true;
                }
            }
        });
        updatecriminalimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE_IMAGE);


            }
        });
        updatecriminalbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addcriminalnamee=updatecriminalname.getText().toString();
                String addcriminalcnicc=updatecriminalcnic.getText().toString();
                String addcriminalphonee=updatecriminalphone.getText().toString();
                String addcriminaladdressss=updatecriminaladdress.getText().toString();
                String addcriminalagee=updatecriminalage.getText().toString();
                String addcriminalcomplaintidd=updatecriminalcomplaintid.getText().toString();
                if (isImageAdded != false && addcriminalnamee != null&&addcriminalcnicc != null&&addcriminalphonee != null&& addcriminaladdressss != null
                        &&addcriminalagee!=null&&addcriminalcomplaintidd!=null) {
                    uploadImage(addcriminalnamee,addcriminalcnicc,addcriminalphonee,addcriminaladdressss,addcriminalagee,addcriminalcomplaintidd);
                }
            }
        });
    }
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }
    private void uploadImage(final String addcriminalnamee, final String addcriminalcnicc, final String addcriminalphonee, final String addcriminaladdressss, final String addcriminalagee, final String addcriminalcomplaintidd) {
        updatecriminalprogresstxt.setVisibility(View.VISIBLE);
        updatecriminalprogressbar.setVisibility(View.VISIBLE);
        if(imageUri!=null){
            collectionReference.document(keyy).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String image=documentSnapshot.getString("file_uri");
                    StorageReference ss=storageReference.getReferenceFromUrl(image);
                    ss.delete();
                }
            });
            imageid=System.currentTimeMillis()+"."+GetFileExtension(imageUri);
            final StorageReference Ref=storee.child(imageid);
            Ref.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Map<String, Object> pic = new HashMap<>();
                                    pic.put("file_uri",uri.toString());
                                    pic.put("criminal_name",addcriminalnamee);
                                    pic.put("criminal_cnic",addcriminalcnicc);
                                    pic.put("criminal_phone",addcriminalphonee);
                                    pic.put("criminal_address",addcriminaladdressss);
                                    pic.put("criminal_age",addcriminalagee);
                                    pic.put("complaint_id",addcriminalcomplaintidd);
                                    FirebaseFirestore.getInstance().collection("criminals").document(keyy).update(pic);
                                    Toast.makeText(criminalviewActivity.this, "file Uploaded", Toast.LENGTH_SHORT).show();
                                    imageUri=null;
                                    criminalviewActivity.this.finish();
                                }
                            });

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress=(taskSnapshot.getBytesTransferred()*100)/taskSnapshot.getTotalByteCount();
                    updatecriminalprogressbar.setProgress((int)progress);
                    updatecriminalprogresstxt.setText(progress+" %");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(criminalviewActivity.this, "Couldnt upload", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Map<String, Object> pic = new HashMap<>();
            pic.put("criminal_name",addcriminalnamee);
            pic.put("criminal_cnic",addcriminalcnicc);
            pic.put("criminal_phone",addcriminalphonee);
            pic.put("criminal_address",addcriminaladdressss);
            pic.put("criminal_age",addcriminalagee);
            pic.put("complaint_id",addcriminalcomplaintidd);
            FirebaseFirestore.getInstance().collection("criminals").document(keyy).update(pic).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(criminalviewActivity.this, "updated successfully", Toast.LENGTH_SHORT).show();
                        criminalviewActivity.this.finish();
                    }
                }
            }) ;     }
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && data != null) {
            imageUri = data.getData();
            isImageAdded = true;
            updatecriminalimageview.setImageURI(imageUri);
        }
    }
    }
