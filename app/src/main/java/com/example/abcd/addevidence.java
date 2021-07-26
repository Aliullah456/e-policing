package com.example.abcd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class addevidence extends AppCompatActivity {
    private static final int REQUEST_CODE_IMAGE = 101;
    private ImageView addevidenceimageview;
    private EditText addevidencecid;
    private TextView addevidencetextviewporgress;
    private ProgressBar addevidenceprogressbar;
    private Button addevidencesavebtn;
    private Toolbar addevidencetoolbar;
    private Uri imageUri;
    private boolean isImageAdded = false;
    private CollectionReference database;
    private StorageReference storageReference;
    private String imageid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addevidence);
        addevidenceimageview = findViewById(R.id.addevidenceimageview);
        addevidencecid = findViewById(R.id.addevidencecid);
        addevidencetextviewporgress = findViewById(R.id.addevidencetextviewporgress);
        addevidenceprogressbar = findViewById(R.id.addevidenceprogressbar);
        addevidencesavebtn = findViewById(R.id.addevidencesavebtn);
        addevidencetextviewporgress.setVisibility(View.GONE);
        addevidenceprogressbar.setVisibility(View.GONE);
        addevidencetoolbar=findViewById(R.id.addevidencetoolbar);
        addevidencetoolbar.setTitle("add new Evidence Record");
        storageReference= FirebaseStorage.getInstance().getReference().child("Files");
        database= FirebaseFirestore.getInstance().collection("evidence");
        addevidenceimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE_IMAGE);
            }
        });
        addevidencesavebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String evidencecid = addevidencecid.getText().toString();
                if (isImageAdded != false && evidencecid != null) {
                    uploadImage(evidencecid);
                }
            }
        });
    }
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }
    private void uploadImage(final String evidencecid) {
        addevidencetextviewporgress.setVisibility(View.VISIBLE);
        addevidenceprogressbar.setVisibility(View.VISIBLE);
        if(imageUri!=null){
            imageid=System.currentTimeMillis()+"."+GetFileExtension(imageUri);
            final StorageReference Ref=storageReference.child(imageid);

            Ref.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Map<String, Object> pic = new HashMap<>();
                                    pic.put("file_uri",uri.toString());
                                    pic.put("complaint_id",evidencecid);
                                    FirebaseFirestore.getInstance().collection("evidence").add(pic);
                                    Toast.makeText(addevidence.this, "file Uploaded", Toast.LENGTH_SHORT).show();
                                    imageUri=null;
                                    addevidence.this.finish();
                                }
                            });

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress=(taskSnapshot.getBytesTransferred()*100)/taskSnapshot.getTotalByteCount();
                    addevidenceprogressbar.setProgress((int)progress);
                    addevidencetextviewporgress.setText(progress+" %");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(addevidence.this, "Couldnt upload", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(addevidence.this, "please first select a file", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && data != null) {
            imageUri = data.getData();
            isImageAdded = true;
            addevidenceimageview.setImageURI(imageUri);
        }
    }
}

