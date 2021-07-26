package com.example.abcd;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link policemanagementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class policemanagementFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    EditText policesearch;
    RecyclerView policerecyclerview;
    FloatingActionButton policefloatingbtn;
    FirestoreRecyclerOptions<user_record> options;
    FirestoreRecyclerAdapter<user_record, policeViewHolder> adapter;
    CollectionReference database;
    String policekey;
    FirebaseStorage storageReference;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public policemanagementFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment policemanagementFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static policemanagementFragment newInstance(String param1, String param2) {
        policemanagementFragment fragment = new policemanagementFragment();
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
        final View view = inflater.inflate(R.layout.fragment_policemanagement, container, false);
        policesearch = view.findViewById(R.id.policesearch);
        policerecyclerview = view.findViewById(R.id.policerecyclerview);
        policerecyclerview.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        policerecyclerview.setHasFixedSize(true);
        database = FirebaseFirestore.getInstance().collection("users");
        storageReference= FirebaseStorage.getInstance();
        Loaddata("");
        policesearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() != null) {
                    Loaddata(s.toString());
                } else {
                    Loaddata("");
                }
            }
        });
        return  view;
    }

    private void Loaddata(String data) {
        Query query = database.orderBy("fullname").whereEqualTo("type","police").startAt(data).endAt(data + "\uf8ff");
        options = new FirestoreRecyclerOptions.Builder<user_record>().setQuery(query, user_record.class).build();
        adapter = new FirestoreRecyclerAdapter<user_record, policeViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final policeViewHolder holder, final int position,
                                            @NonNull final user_record model) {
                policekey=getSnapshots().getSnapshot(position).getId();
                holder.policeid.setText("police id: "+model.getPoliceid());
                holder.policename.setText("police name:  "+model.getFullname());
                holder.designation.setText("designation: "+model.getDesignation());
                holder.policestation.setText("policestation: "+model.getPolicestation());
                holder.policecnic.setText("CNIC:  "+model.getCNIC());
                holder.policephone.setText("phone:  "+model.getMobilenumber());
                holder.policeaddress.setText("address:  "+model.getAddress());
                holder.policeemail.setText("email:  "+model.getEmail());
                holder.policepassword.setText("password:   "+model.getPassword());
                holder.confirmation.setText("Active: "+model.getIspolice());
                final String picuri=getSnapshots().getSnapshot(position).getString("profileimage");
                    Picasso.get().load(model.getProfileimage()).into(holder.policeimageview);
                if(picuri==null){
                    holder.policeimageview.setImageResource(R.drawable.ic_image);
                }
                holder.vv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getActivity(),policeviewActivity.class);
                        intent.putExtra("police_key",getSnapshots().getSnapshot(position).getId());
                        startActivity(intent);
                    }
                });
                /*holder.policedelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(picuri!=null){
                        StorageReference imageref=storageReference.getReferenceFromUrl(model.getProfileimage());
                        imageref.delete();}
                        getSnapshots().getSnapshot(position).getReference().delete();
                    }
                });*/

            }

            @NonNull
            public policeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View policev = LayoutInflater.from(parent.getContext()).inflate(R.layout.police_item, parent, false);
                return new policeViewHolder(policev);
            }
        };
        adapter.startListening();
        policerecyclerview.setAdapter(adapter);
    }


    class policeViewHolder extends RecyclerView.ViewHolder {
        private ImageView policeimageview;
        private TextView policeid;
        private TextView policename;
        private TextView designation;
        private TextView policestation;
        private TextView policecnic;
        private TextView policeemail;
        private TextView policephone;
        private TextView policeaddress;
        private TextView policepassword;
        private TextView confirmation;
        View vv;
        public policeViewHolder(@NonNull View itemView) {
            super(itemView);
            policeimageview = itemView.findViewById(R.id.singlepolicepicture);
            policeid=itemView.findViewById(R.id.singlepoliceid);
            policename = itemView.findViewById(R.id.singlepolicename);
            designation=itemView.findViewById(R.id.singlepolicedesignation);
            policestation=itemView.findViewById(R.id.singlepolicestation);
            policecnic=itemView.findViewById(R.id.singlepolicecnic);
            policeemail=itemView.findViewById(R.id.singlepoliceemail);
            policephone=itemView.findViewById(R.id.singlepolicephone);
            policeaddress=itemView.findViewById(R.id.singlepoliceaddress);
            policepassword=itemView.findViewById(R.id.singlepolicepassword);
            confirmation=itemView.findViewById(R.id.singlepoliceconfirmation);
            vv=itemView;


        }
    }

}