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
 * Use the {@link criminalmanagementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class criminalmanagementFragment extends Fragment {
    EditText criminalsearch;
    RecyclerView criminalrecyclerview;
    FloatingActionButton criminalfloatingbtn;
    FirestoreRecyclerOptions<criminal_record> options;
    FirestoreRecyclerAdapter<criminal_record, criminalViewHolder> adapter;
    CollectionReference database;
    String criminalidkey;
    FirebaseStorage storageReference;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public criminalmanagementFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment criminalmanagementFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static criminalmanagementFragment newInstance(String param1, String param2) {
        criminalmanagementFragment fragment = new criminalmanagementFragment();
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
        final View view = inflater.inflate(R.layout.fragment_criminalmanagement, container, false);
        criminalsearch = view.findViewById(R.id.criminalsearch);
        criminalrecyclerview = view.findViewById(R.id.criminalrecyclerview);
        criminalfloatingbtn = view.findViewById(R.id.addcriminalbtn);
        criminalrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        criminalrecyclerview.setHasFixedSize(true);
        database = FirebaseFirestore.getInstance().collection("criminals");
        storageReference= FirebaseStorage.getInstance();
        criminalfloatingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), addcriminalActivity.class));
            }
        });
        Loaddata("");
        criminalsearch.addTextChangedListener(new TextWatcher() {
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
        Query query = database.orderBy("criminal_name").startAt(data).endAt(data + "\uf8ff");
        options = new FirestoreRecyclerOptions.Builder<criminal_record>().setQuery(query, criminal_record.class).build();
        adapter = new FirestoreRecyclerAdapter<criminal_record, criminalViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull criminalViewHolder holder, final int position,
                                            @NonNull final criminal_record model) {
                criminalidkey=getSnapshots().getSnapshot(position).getId();
                holder.criminalname.setText("criminal name:  "+model.getCriminal_name());
                holder.criminalcnic.setText("CNIC:  "+model.getCriminal_cnic());
                holder.criminalphone.setText("phone:  "+model.getCriminal_phone());
                holder.criminaladdress.setText("address:  "+model.getCriminal_address());
                holder.criminalage.setText("age:  "+model.getCriminal_age());
                holder.complaintid.setText("complaint id:  "+model.getComplaint_id());
                Picasso.get().load(model.getFile_uri()).into(holder.imageViewcriminal);
                holder.criminaldelete.setOnClickListener(new View.OnClickListener() {
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
                        Intent intent=new Intent(getActivity(),criminalviewActivity.class);
                        intent.putExtra("criminal_key",getSnapshots().getSnapshot(position).getId());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public criminalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View criminalv = LayoutInflater.from(parent.getContext()).inflate(R.layout.criminal_item, parent, false);
                return new criminalViewHolder(criminalv);
            }
        };
        adapter.startListening();
        criminalrecyclerview.setAdapter(adapter);
    }


    class criminalViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewcriminal;
        private TextView criminalname;
        private TextView criminalphone;
        private TextView criminalcnic;
        private TextView criminaladdress;
        private TextView criminalage;
        private TextView complaintid;
        Button criminaldelete;
        View vv;
        public criminalViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewcriminal = itemView.findViewById(R.id.singlecriminalpicture);
            criminalname = itemView.findViewById(R.id.singlecriminalname);
            criminalcnic=itemView.findViewById(R.id.singlecriminalCNIC);
            criminalphone=itemView.findViewById(R.id.singlecriminalphone);
            criminaladdress=itemView.findViewById(R.id.singlecriminaladdress);
            criminalage=itemView.findViewById(R.id.singlecriminalage);
            complaintid=itemView.findViewById(R.id.singlecriminalcomplaint);
            criminaldelete=itemView.findViewById(R.id.signlecriminaldeletebtn);

            vv=itemView;

        }
    }

}