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

public class witnessviewActivity extends AppCompatActivity {
    private ImageView updatewitnessimageview;
    private EditText updatewitnessname;
    private  EditText updatewitnesscnic;
    private  EditText updatewitnessphone;
    private  EditText updatewitnessaddress;
    private EditText updatewitnessage;
    private  EditText updatewitnesscomplaintid;
    private Button updatewitnessbtn;
    private TextView updatewitnessprogresstxt;
    private ProgressBar updatewitnessprogressbar;
    private  String keyy;
    private static final int REQUEST_CODE_IMAGE = 101;
    private Uri imageUri;
    private StorageReference storee;
    private boolean isImageAdded = false;
    private CollectionReference collectionReference;
    private FirebaseStorage storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_witnessview);
        updatewitnessimageview=findViewById(R.id.updatewitnesspicture);
        updatewitnessname=findViewById(R.id.updatewitnessname);
        updatewitnesscnic=findViewById(R.id.updatewitnessCNIC);
        updatewitnessphone=findViewById(R.id.updatewitnessphone);
        updatewitnessaddress=findViewById(R.id.updatewitnessaddress);
        updatewitnessage=findViewById(R.id.updatewitnessage);
        updatewitnesscomplaintid=findViewById(R.id.updatewitnesscomplaint);
        updatewitnessbtn=findViewById(R.id.updatewitnesssavebtn);
        updatewitnessprogresstxt=findViewById(R.id.updatewitnesstextviewporgress);
        updatewitnessprogressbar=findViewById(R.id.updatewitnessprogressbar);
        updatewitnessprogresstxt.setVisibility(View.GONE);
        updatewitnessprogressbar.setVisibility(View.GONE);
        collectionReference= FirebaseFirestore.getInstance().collection("witness");
        storageReference= FirebaseStorage.getInstance();
        storee=FirebaseStorage.getInstance().getReference().child("Witness");
        keyy=getIntent().getStringExtra("witness_key");
        collectionReference.document(keyy).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Picasso.get().load(documentSnapshot.getString("file_uri")).into(updatewitnessimageview);
                    updatewitnessname.setText(documentSnapshot.getString("witness_name"));
                    updatewitnesscnic.setText(documentSnapshot.getString("witness_cnic"));
                    updatewitnessphone.setText(documentSnapshot.getString("witness_phone"));
                    updatewitnessaddress.setText(documentSnapshot.getString("witness_address"));
                    updatewitnessage.setText(documentSnapshot.getString("witness_age"));
                    updatewitnesscomplaintid.setText(documentSnapshot.getString("complaint_id"));
                    isImageAdded=true;
                }
            }
        });
        updatewitnessimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE_IMAGE);


            }
        });
        updatewitnessbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addcriminalnamee=updatewitnessname.getText().toString();
                String addcriminalcnicc=updatewitnesscnic.getText().toString();
                String addcriminalphonee=updatewitnessphone.getText().toString();
                String addcriminaladdressss=updatewitnessaddress.getText().toString();
                String addcriminalagee=updatewitnessage.getText().toString();
                String addcriminalcomplaintidd=updatewitnesscomplaintid.getText().toString();
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
        updatewitnessprogresstxt.setVisibility(View.VISIBLE);
        updatewitnessprogressbar.setVisibility(View.VISIBLE);
        if(imageUri!=null){
            collectionReference.document(keyy).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String image=documentSnapshot.getString("file_uri");
                    StorageReference ss=storageReference.getReferenceFromUrl(image);
                    ss.delete();
                }
            });
            String imageid = System.currentTimeMillis() + "." + GetFileExtension(imageUri);
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
                                    pic.put("witness_name",addcriminalnamee);
                                    pic.put("witness_cnic",addcriminalcnicc);
                                    pic.put("witness_phone",addcriminalphonee);
                                    pic.put("witness_address",addcriminaladdressss);
                                    pic.put("witness_age",addcriminalagee);
                                    pic.put("witness_id",addcriminalcomplaintidd);
                                    FirebaseFirestore.getInstance().collection("witness").document(keyy).update(pic);
                                    Toast.makeText(witnessviewActivity.this, "file Uploaded", Toast.LENGTH_SHORT).show();
                                    imageUri=null;
                                    witnessviewActivity.this.finish();
                                }
                            });

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress=(taskSnapshot.getBytesTransferred()*100)/taskSnapshot.getTotalByteCount();
                    updatewitnessprogressbar.setProgress((int)progress);
                    updatewitnessprogresstxt.setText(progress+" %");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(witnessviewActivity.this, "Couldnt upload", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Map<String, Object> pic = new HashMap<>();
            pic.put("witness_name",addcriminalnamee);
            pic.put("witness_cnic",addcriminalcnicc);
            pic.put("witness_phone",addcriminalphonee);
            pic.put("witness_address",addcriminaladdressss);
            pic.put("witness_age",addcriminalagee);
            pic.put("complaint_id",addcriminalcomplaintidd);
            FirebaseFirestore.getInstance().collection("witness").document(keyy).update(pic).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(witnessviewActivity.this, "updated successfully", Toast.LENGTH_SHORT).show();
                        witnessviewActivity.this.finish();
                    }
                }
            }) ;     }
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && data != null) {
            imageUri = data.getData();
            isImageAdded = true;
            updatewitnessimageview.setImageURI(imageUri);
        }
    }

    }
