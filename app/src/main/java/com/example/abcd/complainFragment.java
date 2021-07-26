package com.example.abcd;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.widget.Constraints.TAG;
import static java.util.Calendar.MINUTE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link complainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class complainFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    CollectionReference firestore;
    StorageReference mStorageRef;
    private StorageTask uploadTask;
    public Uri FilePathUri;
    private String imageid;
    public String complaintid;
    public String Evidence_id;
    private RequestQueue mRequestQue;
    private ProgressDialog progressDialog;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String title="test topic";
    private String body="test body";
    private String URL="https://fcm.googleapis.com/fcm/send";
    public complainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment complainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static complainFragment newInstance(String param1, String param2) {
        complainFragment fragment = new complainFragment();
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
        final View view = inflater.inflate(R.layout.fragment_complain, container, false);
        // Inflate the layout for this fragment
        final EditText date_in = (EditText) view.findViewById(R.id.date_input);
        final EditText time_in = (EditText) view.findViewById(R.id.time_input);
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                date_in.setText(sdf.format(myCalendar.getTime()));
            }

        };

        date_in.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        time_in.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time;
                        if(selectedHour>=0 && selectedHour<12){
                            time = selectedHour + " : " + selectedMinute + " AM";
                        } else {
                            if(selectedHour == 12){
                                time = selectedHour + " : " +selectedMinute + "PM";
                            } else{
                                selectedHour = selectedHour -12;
                                time = selectedHour + " : " + selectedMinute + "PM";
                            }
                        }
                        time_in.setText( time);
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
//
        final Spinner categoryspinner = (Spinner) view.findViewById(R.id.catogoryspinner);
       // final EditText crimetext = (EditText) view.findViewById(R.id.crimetext);
        final EditText crimedetail = (EditText) view.findViewById(R.id.crimedetail);
        final Spinner regionspinner = (Spinner) view.findViewById(R.id.regionspinner);
        final Spinner distinctspinner = (Spinner) view.findViewById(R.id.distinctspinner);
        final EditText addres = (EditText) view.findViewById(R.id.Address);
        Button filebtn = (Button) view.findViewById(R.id.filebtn);
        Button uploadbtn = (Button) view.findViewById(R.id.uploadbtn);
        Button submitbtn = (Button) view.findViewById(R.id.clickbtn);
        final String[] paths = {"theft", "murder", "terrorist", "public voilence", "kidnapping"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryspinner.setAdapter(adapter);

        final String[] region = {"Islamabad"};
        ArrayAdapter<String> regionadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, region);
        regionadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regionspinner.setAdapter(regionadapter);

        final String[] distinct = {"Pakistan Secretariat", "Diplomatic Enclave", "A sector", "B sector", "C Sector", "D sector",
                "E sector", "F sector", "G sector", "H sector", "I sector"};
        ArrayAdapter<String> distinctadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, distinct);
        distinctadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distinctspinner.setAdapter(distinctadapter);
        mStorageRef= FirebaseStorage.getInstance().getReference("Files");
        firestore= FirebaseFirestore.getInstance().collection("evidence");
        final CollectionReference Ref=FirebaseFirestore.getInstance().collection("complain");
        mRequestQue= Volley.newRequestQueue(getActivity());
        FirebaseMessaging.getInstance().unsubscribeFromTopic("news");

        filebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,1);
            }
        });

        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uploadTask!=null&&uploadTask.isInProgress()){
                    Toast.makeText(getActivity(), "uploading is in progress", Toast.LENGTH_SHORT).show();
                }else{
                    FileUpload();
                }
            }
        });


        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category=categoryspinner.getSelectedItem().toString();
               // String crime=crimetext.getText().toString();
                String detail=crimedetail.getText().toString();
                final String region=regionspinner.getSelectedItem().toString();
                final String distinct=distinctspinner.getSelectedItem().toString();
                final String address=addres.getText().toString();
                String date=date_in.getText().toString();
                String time=time_in.getText().toString();

//                if(crime.isEmpty()){
//                    crimetext.setError("to be filled by user");
//                    crimetext.requestFocus();
//                }
                 if(detail.isEmpty()){
                    crimedetail.setError("please give detail");
                    crimedetail.requestFocus();
                }
                else if (address.isEmpty()){
                    addres.setError("Yes or no ");
                    addres.requestFocus();
                }
                else if (date.isEmpty()){
                    date_in.setError("Yes or no ");
                    date_in.requestFocus();
                }
                else if (time.isEmpty()){
                    time_in.setError("Yes or no ");
                    time_in.requestFocus();
                }
                else{
                    String result="";
                    final String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
                    //complain complain=new complain(name,gender,cnic,father,cell,secondarycell,
                    //      province,address,category,crime,detail,region,distinct,crimeactin,result,hashMap);
                    Map<String, String> data=new HashMap<>();
                    data.put("crime_catogry",category);
                    //data.put("crime",crime);
                    data.put("detail",detail);
                    data.put("region",region);
                    data.put("sector",distinct);
                    data.put("address",address);
                    data.put("date",date);
                    data.put("time",time);
                    data.put("status","");
                    data.put("created_date",currentDateTimeString);
                    data.put("complainer_id", FirebaseAuth.getInstance().getUid());
                    Ref.add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            complaintid = documentReference.getId();
                            Toast.makeText(getActivity(), "complain registered", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), aftercomplain.class);
                            intent.putExtra("trackid", complaintid);
                            startActivity(intent);
                            title = "City in Danger";
                            body = "a Crime has been happened in " + address + " " + distinct + " " + region + " at "
                                    + currentDateTimeString + "/n please open app to see details of crime";
                            sendNotification();
                            //crimetext.setText("");
                            crimedetail.setText("");
                            addres.setText("");
                            date_in.setText("");
                            time_in.setText("");
                            FirebaseFirestore.getInstance().collection("complain").document(complaintid).update("Trackid",complaintid);
                            FirebaseFirestore.getInstance().collection("evidence").document(Evidence_id).update("complaint_id",complaintid);
                            final CollectionReference dr = FirebaseFirestore.getInstance().collection("complain");
                            final int time;
                            time = 30;
                            final long timecountinMillisecond = time * 60 * 1000;
                            new android.os.Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Query query = dr.whereEqualTo("status", "").whereEqualTo("Trackid", complaintid);
                                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (DocumentSnapshot document : task.getResult()) {
                                                    dr.document(document.getId()).delete();
                                                    if(FilePathUri!=null){
                                                    final StorageReference Ref=mStorageRef.child(imageid);
                                                    Ref.delete();}
                                                    Toast.makeText(getActivity(), "data deleted", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Log.d(TAG, "Error getting documents: ", task.getException());

                                            }
                                        }
                                    });
                                }
                            }, timecountinMillisecond);
                        }
                    });
                }
            }
        });
        return view;
    }

    private void sendNotification() {
        JSONObject mainObj=new JSONObject();
        try{

            mainObj.put("to","/topics/"+"news");
            JSONObject notificationObj=new JSONObject();
            notificationObj.put("title",title);
            notificationObj.put("body",body);
            mainObj.put("notification",notificationObj);
            JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, URL
                    , mainObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header=new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAAoXN8Hf0:APA91bGonZzDgYChK_x5Yd3zyAofep7hb6Zar-cbjxMYyKJlTYpPuXDqb4RwXz7MYtL6taCqi-DcmY-LcCDj6b2OWT-c1JkZugyn5BzLY2GPsy_3DVtUSnK4q-rHPwXqg7TR-rT2bdTF");
                    return header;
                }
            };
            mRequestQue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void FileUpload() {
        if(FilePathUri!=null){
            imageid=System.currentTimeMillis()+"."+GetFileExtension(FilePathUri);
            final StorageReference Ref=mStorageRef.child(imageid);
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading please wait");
            progressDialog.setIcon(R.drawable.ic_account);
            progressDialog.show();
            Ref.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Map<String, Object> pic = new HashMap<>();
                                    pic.put("file_uri",uri.toString());
                                    //pic.put("complaint_id","");
                                    FirebaseFirestore.getInstance().collection("evidence").add(pic).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Evidence_id = documentReference.getId();
                                        }
                                    });
                                    progressDialog.hide();
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), "file Uploaded", Toast.LENGTH_SHORT).show();

                                    FilePathUri=null;
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.hide();
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Couldnt upload", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            progressDialog.hide();
            Toast.makeText(getActivity(), "please first select a file", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

        }
    }


    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }
    public interface OnFragmentInteractionListener {
    }
}