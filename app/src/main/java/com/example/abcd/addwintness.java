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

public class addwintness extends AppCompatActivity {
    private ImageView addwitnessimageview;
    private EditText addwitnessname;
    private  EditText addwitnesscnic;
    private  EditText addwitnessphone;
    private  EditText addwitnessaddress;
    private EditText addwitnessage;
    private  EditText addwitnesscomplaintid;
    private Button addwitnessbtn;
    private TextView addwitnessprogresstxt;
    private ProgressBar addwitnessprogressbar;
    private Toolbar addwitnesstoolbar;
    private static final int REQUEST_CODE_IMAGE = 101;
    private Uri imageUri;
    private boolean isImageAdded = false;
    private CollectionReference database;
    private StorageReference storageReference;
    private String imageid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addwintness);
        addwitnessimageview=findViewById(R.id.witnesspicture);
        addwitnessname=findViewById(R.id.addwitnessname);
        addwitnesscnic=findViewById(R.id.addwitnessCNIC);
        addwitnessphone=findViewById(R.id.addwitnessphone);
        addwitnessaddress=findViewById(R.id.addwitnessaddress);
        addwitnessage=findViewById(R.id.addwitnessage);
        addwitnesscomplaintid=findViewById(R.id.addwitnesscomplaint);
        addwitnessbtn=findViewById(R.id.addwitnesssavebtn);
        addwitnessprogresstxt=findViewById(R.id.addwitnesstextviewporgress);
        addwitnessprogressbar=findViewById(R.id.addwitnessprogressbar);
        addwitnesstoolbar=findViewById(R.id.witnesstoolbar);
        addwitnesstoolbar.setTitle("add new witness");
        addwitnessprogresstxt.setVisibility(View.GONE);
        addwitnessprogressbar.setVisibility(View.GONE);
        storageReference= FirebaseStorage.getInstance().getReference().child("Witness");
        database= FirebaseFirestore.getInstance().collection("witness");
        addwitnessimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE_IMAGE);
            }
        });
        addwitnessbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addwitnessnamee=addwitnessname.getText().toString();
                String addwitnesscnicc=addwitnesscnic.getText().toString();
                String addwitnessphonee=addwitnessphone.getText().toString();
                String addwitnessaddressss=addwitnessaddress.getText().toString();
                String addwitnessagee=addwitnessage.getText().toString();
                String addwitnesscomplaintidd=addwitnesscomplaintid.getText().toString();
                if (isImageAdded != false && addwitnessnamee != null&&addwitnesscnicc != null&&addwitnessphonee != null&& addwitnessaddressss != null
                        &&addwitnessagee!=null&&addwitnesscomplaintidd!=null) {
                    uploadImage(addwitnessnamee,addwitnesscnicc,addwitnessphonee,addwitnessaddressss,addwitnessagee,addwitnesscomplaintidd);
                }
            }
        });
    }
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }
    private void uploadImage(final String addwitnessnamee, final String addwitnesscnicc, final String addwitnessphonee, final String addwitnessaddressss, final String addwitnessagee, final String addwitnesscomplaintidd) {
        addwitnessprogresstxt.setVisibility(View.VISIBLE);
        addwitnessprogressbar.setVisibility(View.VISIBLE);
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
                                    pic.put("witness_name",addwitnessnamee);
                                    pic.put("witness_cnic",addwitnesscnicc);
                                    pic.put("witness_phone",addwitnessphonee);
                                    pic.put("witness_address",addwitnessaddressss);
                                    pic.put("witness_age",addwitnessagee);
                                    pic.put("complaint_id",addwitnesscomplaintidd);
                                    FirebaseFirestore.getInstance().collection("witness").add(pic);
                                    Toast.makeText(addwintness.this, "file Uploaded", Toast.LENGTH_SHORT).show();
                                    imageUri=null;
                                    addwintness.this.finish();
                                }
                            });

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress=(taskSnapshot.getBytesTransferred()*100)/taskSnapshot.getTotalByteCount();
                    addwitnessprogressbar.setProgress((int)progress);
                    addwitnessprogresstxt.setText(progress+" %");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(addwintness.this, "Couldnt upload", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(addwintness.this, "please first select a file", Toast.LENGTH_SHORT).show();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && data != null) {
            imageUri = data.getData();
            isImageAdded = true;
            addwitnessimageview.setImageURI(imageUri);
        }
    }

}
