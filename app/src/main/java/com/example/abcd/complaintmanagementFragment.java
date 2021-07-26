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
import android.widget.VideoView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link complaintmanagementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class complaintmanagementFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
   private FirestoreRecyclerOptions<complainrecord> options;
    private FirestoreRecyclerAdapter<complainrecord,complaintViewHolder> adapter;
    private  CollectionReference database;
    private String trackeridkey;
    private FirebaseStorage storageReference;
    private RecyclerView complaintrecyclerview;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public complaintmanagementFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment complaintmanagementFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static complaintmanagementFragment newInstance(String param1, String param2) {
        complaintmanagementFragment fragment = new complaintmanagementFragment();
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
        final View view = inflater.inflate(R.layout.fragment_complaintmanagement, container, false);

        EditText searchresult=(EditText) view.findViewById(R.id.complainsearchsearch);
         complaintrecyclerview=(RecyclerView)view.findViewById(R.id.complaintrecyclerview);
        complaintrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        complaintrecyclerview.setHasFixedSize(true);
        database = FirebaseFirestore.getInstance().collection("complain");
        storageReference= FirebaseStorage.getInstance();
        Loaddata("");
        searchresult.addTextChangedListener(new TextWatcher() {
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
        return view;
    }

    private void Loaddata(String data) {
        Query query = database.orderBy("Trackid").startAt(data).endAt(data + "\uf8ff");
        options = new FirestoreRecyclerOptions.Builder<complainrecord>().setQuery(query, complainrecord.class).build();
        adapter = new FirestoreRecyclerAdapter<complainrecord, complaintViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull complaintViewHolder holder, final int position, @NonNull final complainrecord model) {
                trackeridkey=getSnapshots().getSnapshot(position).getId();
                holder.complaintrackid.setText("track id: "+model.getTrackid());
                holder.complainerid.setText("complainer_id: " + model.getComplainer_id());
                holder.crimetype.setText("crime type: "+model.getCrime_catogry());
                holder.crime.setText("crime: "+model.getCrime());
                holder.crimedetail.setText("detail: "+model.getDetail());
                holder.crimecity.setText("city: "+model.getRegion());
                holder.crimearea.setText("area: "+model.getSector());
                holder.crimeaddress.setText("address: "+model.getAddress());
                holder.crimedate.setText("date: "+model.getDate());
                holder.crimetime.setText("time: "+model.getTime());
                holder.crimeresult.setText("status: "+model.getStatus());
                holder.publishtime.setText("posted at "+model.getCreated_date());
                holder.complaintdelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getSnapshots().getSnapshot(position).getReference().delete();
                    }
                });
                holder.vv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getActivity(),complaintviewActivity.class);
                        intent.putExtra("track_id",getSnapshots().getSnapshot(position).getId());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public complaintViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View complaintv = LayoutInflater.from(parent.getContext()).inflate(R.layout.complain_item, parent, false);
                return new complaintViewHolder(complaintv);
            }
        };
        adapter.startListening();
        complaintrecyclerview.setAdapter(adapter);
    }


    class complaintViewHolder extends RecyclerView.ViewHolder {
        private TextView complaintrackid;
        private TextView complainerid;
        private TextView crimetype;
        private TextView crime;
        private TextView crimedetail;
        private TextView crimecity;
        private TextView crimearea;
        private TextView crimeaddress;
        private TextView crimedate;
        private TextView crimetime;
        private TextView crimeresult;
        private TextView publishtime;
       private Button complaintdelete;
       private View vv;
        public complaintViewHolder(@NonNull View itemView) {
            super(itemView);

            complaintdelete=itemView.findViewById(R.id.complaintdeletebutton);
             complaintrackid=itemView.findViewById(R.id.complainttrackeridpolicetxt);
            complainerid=itemView.findViewById(R.id.complaineridpolicetxt);
            crimetype=itemView.findViewById(R.id.crimetypepolicetxt);
            crime=itemView.findViewById(R.id.crimepolicetxt);
            crimedetail=itemView.findViewById(R.id.crimedetailtypepolicetxt);
            crimecity=itemView.findViewById(R.id.crimecitypolicetxt);
            crimearea=itemView.findViewById(R.id.crimeareapolicetxt);
            crimeaddress=itemView.findViewById(R.id.addresspolicetxt);
            crimedate=itemView.findViewById(R.id.datepolicetxt);
            crimetime=itemView.findViewById(R.id.timepolicetxt);
            crimeresult=itemView.findViewById(R.id.crimestatuspolicetxt);
            publishtime=itemView.findViewById(R.id.publishedtimepolicetxt);

            vv=itemView;

        }
    }

}