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

public class evidenceviewActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_IMAGE = 101;
    private ImageView evidenceviewimage;
    private EditText evidenceviewtext;
    private Button evidenceviewupdate;
    private  String keyy;
    CollectionReference collectionReference;
    FirebaseStorage storageReference;
    private StorageReference storee;
    private boolean isImageAdded=false;
    private Uri imageUri;
    private String imageid;
    private TextView evideceupdateprogresstext;
    private ProgressBar evidenceupdateprogressbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evidenceview);
        evidenceviewimage=findViewById(R.id.evidenceview_single_imageview);
        evidenceviewtext=findViewById(R.id.evidenceview_single_textview);
        evidenceviewupdate=findViewById(R.id.evidenceviewupdatetebtn);
        evideceupdateprogresstext=findViewById(R.id.evidenceupdatetextviewporgress);
        evidenceupdateprogressbar=findViewById(R.id.evidenceupdateprogressbar);
        evideceupdateprogresstext.setVisibility(View.GONE);
        evidenceupdateprogressbar.setVisibility(View.GONE);
        collectionReference= FirebaseFirestore.getInstance().collection("evidence");
        storageReference= FirebaseStorage.getInstance();
        storee=FirebaseStorage.getInstance().getReference().child("Files");
        keyy=getIntent().getStringExtra("evidence_id");
        collectionReference.document(keyy).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String name=documentSnapshot.getString("complaint_id").toString();
                    String image=documentSnapshot.getString("file_uri").toString();
                    evidenceviewtext.setText(name);
                    Picasso.get().load(image).into(evidenceviewimage);
                    isImageAdded=true;
                }
            }
        });

        evidenceviewimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE_IMAGE);


            }
        });
        evidenceviewupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String evidencecid = evidenceviewtext.getText().toString();
                if(evidencecid.isEmpty()){
                    evidenceviewtext.setError("please fill this first");
                }
                else if (isImageAdded != false ) {
                    uploadImage(evidencecid);
                }
                else {
                    Toast.makeText(evidenceviewActivity.this, "isimageadded is false ", Toast.LENGTH_SHORT).show();
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
        evideceupdateprogresstext.setVisibility(View.VISIBLE);
        evidenceupdateprogressbar.setVisibility(View.VISIBLE);
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
                                    pic.put("complaint_id",evidencecid);
                                    collectionReference.document(keyy).update(pic);
                                    Toast.makeText(evidenceviewActivity.this, "evidence record updated", Toast.LENGTH_SHORT).show();
                                    imageUri=null;
                                    evidenceviewActivity.this.finish();
                                }
                            });

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress=(taskSnapshot.getBytesTransferred()*100)/taskSnapshot.getTotalByteCount();
                    evidenceupdateprogressbar.setProgress((int)progress);
                    evideceupdateprogresstext.setText(progress+" %");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(evidenceviewActivity.this, "Couldnt update", Toast.LENGTH_SHORT).show();
                    e.getMessage();
                }
            });
        }else{
            Map<String, Object> pic = new HashMap<>();
            pic.put("complaint_id",evidencecid);
            collectionReference.document(keyy).update(pic).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(evidenceviewActivity.this, "id successfully updated", Toast.LENGTH_SHORT).show();
                        evidenceviewActivity.this.finish();
                    }
                }
            });
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && data != null) {
            imageUri = data.getData();
            isImageAdded = true;
            evidenceviewimage.setImageURI(imageUri);
        }

    }

}
