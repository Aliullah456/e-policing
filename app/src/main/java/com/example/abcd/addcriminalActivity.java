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

public class addcriminalActivity extends AppCompatActivity {
private ImageView addcriminalimageview;
    private EditText addcriminalname;
    private  EditText addcriminalcnic;
    private  EditText addcriminalphone;
    private  EditText addcriminaladdress;
    private EditText addcriminalage;
    private  EditText addcriminalcomplaintid;
    private Button addcriminalbtn;
    private TextView addcriminalprogresstxt;
    private ProgressBar addcriminalprogressbar;
    private Toolbar addcriminaltoolbar;
    private static final int REQUEST_CODE_IMAGE = 101;
    private Uri imageUri;
    private boolean isImageAdded = false;
    private CollectionReference database;
    private StorageReference storageReference;
    private String imageid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcriminal);
         addcriminalimageview=findViewById(R.id.criminalpicture);
         addcriminalname=findViewById(R.id.addcriminalname);
         addcriminalcnic=findViewById(R.id.addcriminalCNIC);
         addcriminalphone=findViewById(R.id.addcriminalphone);
         addcriminaladdress=findViewById(R.id.addcriminaladdress);
         addcriminalage=findViewById(R.id.addcriminalage);
         addcriminalcomplaintid=findViewById(R.id.addcriminalcomplaint);
         addcriminalbtn=findViewById(R.id.addcriminalsavebtn);
         addcriminalprogresstxt=findViewById(R.id.addecriminaltextviewporgress);
         addcriminalprogressbar=findViewById(R.id.addcriminalprogressbar);
         addcriminaltoolbar=findViewById(R.id.addcriminaltoolbar);
         addcriminaltoolbar.setTitle("add new criminal");
        addcriminalprogresstxt.setVisibility(View.GONE);
        addcriminalprogressbar.setVisibility(View.GONE);
        storageReference= FirebaseStorage.getInstance().getReference().child("Criminals");
        database= FirebaseFirestore.getInstance().collection("criminals");
        addcriminalimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE_IMAGE);
            }
        });
addcriminalbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String addcriminalnamee=addcriminalname.getText().toString();
        String addcriminalcnicc=addcriminalcnic.getText().toString();
        String addcriminalphonee=addcriminalphone.getText().toString();
        String addcriminaladdressss=addcriminaladdress.getText().toString();
        String addcriminalagee=addcriminalage.getText().toString();
        String addcriminalcomplaintidd=addcriminalcomplaintid.getText().toString();
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
    private void uploadImage(final String addcriminalnamee, final String addcriminalcnicc, final String addcriminalphonee,
                             final String addcriminaladdressss, final String addcriminalagee, final String addcriminalcomplaintidd) {
        addcriminalprogresstxt.setVisibility(View.VISIBLE);
        addcriminalprogressbar.setVisibility(View.VISIBLE);
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
                                    pic.put("criminal_name",addcriminalnamee);
                                    pic.put("criminal_cnic",addcriminalcnicc);
                                    pic.put("criminal_phone",addcriminalphonee);
                                    pic.put("criminal_address",addcriminaladdressss);
                                    pic.put("criminal_age",addcriminalagee);
                                    pic.put("complaint_id",addcriminalcomplaintidd);
                                    FirebaseFirestore.getInstance().collection("criminals").add(pic);
                                    Toast.makeText(addcriminalActivity.this, "file Uploaded", Toast.LENGTH_SHORT).show();
                                    imageUri=null;
                                    addcriminalActivity.this.finish();
                                }
                            });

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress=(taskSnapshot.getBytesTransferred()*100)/taskSnapshot.getTotalByteCount();
                    addcriminalprogressbar.setProgress((int)progress);
                    addcriminalprogresstxt.setText(progress+" %");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(addcriminalActivity.this, "Couldnt upload", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(addcriminalActivity.this, "please first select a file", Toast.LENGTH_SHORT).show();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && data != null) {
            imageUri = data.getData();
            isImageAdded = true;
            addcriminalimageview.setImageURI(imageUri);
        }
    }

}