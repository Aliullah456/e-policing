package com.example.abcd;

import android.content.Intent;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link witnessmanagementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class witnessmanagementFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    EditText witnesssearch;
    RecyclerView witnessrecyclerview;
    FloatingActionButton witnessfloatingbtn;
    FirestoreRecyclerOptions<witness_record> options;
    FirestoreRecyclerAdapter<witness_record, witnessViewHolder> adapter;
    CollectionReference database;
    String witnessidkey;
    FirebaseStorage storageReference;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public witnessmanagementFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment witnessmanagementFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static witnessmanagementFragment newInstance(String param1, String param2) {
        witnessmanagementFragment fragment = new witnessmanagementFragment();
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
        final View view = inflater.inflate(R.layout.fragment_witnessmanagement, container, false);
        witnesssearch = view.findViewById(R.id.witnesssearch);
        witnessrecyclerview = view.findViewById(R.id.witnessrecyclerview);
        witnessfloatingbtn = view.findViewById(R.id.addwitnessbtn);
        witnessrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        witnessrecyclerview.setHasFixedSize(true);
        database = FirebaseFirestore.getInstance().collection("witness");
        storageReference= FirebaseStorage.getInstance();
        witnessfloatingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), addwintness.class));
            }
        });
        Loaddata("");
        witnesssearch.addTextChangedListener(new TextWatcher() {
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
        return  view;    }

    private void Loaddata(String data) {
        Query query = database.orderBy("witness_name").startAt(data).endAt(data + "\uf8ff");
        options = new FirestoreRecyclerOptions.Builder<witness_record>().setQuery(query, witness_record.class).build();
        adapter = new FirestoreRecyclerAdapter<witness_record, witnessViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull witnessViewHolder holder, final int position, @NonNull final witness_record model) {
                witnessidkey=getSnapshots().getSnapshot(position).getId();
                holder.witnessname.setText("witness name:  "+model.getWitness_name());
                holder.witnesscnic.setText("CNIC:  "+model.getWitness_cnic());
                holder.witnessphone.setText("phone:  "+model.getWitness_phone());
                holder.witnessaddress.setText("address:  "+model.getWitness_address());
                holder.witnessage.setText("age:  "+model.getWitness_age());
                holder.complaintid.setText("complaint id:  "+model.getComplaint_id());
                Picasso.get().load(model.getFile_uri()).into(holder.imageViewwitness);
                holder.witnessdelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StorageReference imageref=storageReference.getReferenceFromUrl(model.getFile_uri());
                        imageref.delete();
                        getSnapshots().getSnapshot(position).getReference().delete();
                    }
                });
                holder.vv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getActivity(),witnessviewActivity.class);
                        intent.putExtra("witness_key",getSnapshots().getSnapshot(position).getId());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public witnessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View witnessv = LayoutInflater.from(parent.getContext()).inflate(R.layout.witness_item, parent, false);
                return new witnessViewHolder(witnessv);
            }
        };
        adapter.startListening();
        witnessrecyclerview.setAdapter(adapter);
    }


    class witnessViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewwitness;
        private TextView witnessname;
        private TextView witnessphone;
        private TextView witnesscnic;
        private TextView witnessaddress;
        private TextView witnessage;
        private TextView complaintid;
        Button witnessdelete;
        View vv;
        public witnessViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewwitness = itemView.findViewById(R.id.singlewitnesspicture);
            witnessname = itemView.findViewById(R.id.singlewitnessname);
            witnesscnic=itemView.findViewById(R.id.singlewitnessCNIC);
            witnessphone=itemView.findViewById(R.id.singlewitnessphone);
            witnessaddress=itemView.findViewById(R.id.singlewitnessaddress);
            witnessage=itemView.findViewById(R.id.singlewitnessage);
            complaintid=itemView.findViewById(R.id.singlewitnesscomplaint);
            witnessdelete=itemView.findViewById(R.id.signlewitnessdeletebtn);

            vv=itemView;

        }
    }
}