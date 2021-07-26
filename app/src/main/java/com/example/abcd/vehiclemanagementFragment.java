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
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link vehiclemanagementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class vehiclemanagementFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirestoreRecyclerOptions<vehicle_record> options;
    private FirestoreRecyclerAdapter<vehicle_record, vehicleViewHolder> adapter;
    private CollectionReference database;
    private String vehicleidkey;
    private FirebaseStorage storageReference;
    private RecyclerView singlevehiclerecyclerview;
    private EditText singlevehiclesearch;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FloatingActionButton addvehiclebtn;

    public vehiclemanagementFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment victimmanagementFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static vehiclemanagementFragment newInstance(String param1, String param2) {
        vehiclemanagementFragment fragment = new vehiclemanagementFragment();
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
        final View view = inflater.inflate(R.layout.fragment_vehiclemanagement, container, false);
        singlevehiclesearch=(EditText) view.findViewById(R.id.vehiclesearch);
        singlevehiclerecyclerview=(RecyclerView)view.findViewById(R.id.vehiclerecyclerview);
        singlevehiclerecyclerview.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        singlevehiclerecyclerview.setHasFixedSize(true);
         addvehiclebtn=view.findViewById(R.id.addvehiclefloatbtn);
        addvehiclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newintent=new Intent(getActivity(),addvehicle.class);
                startActivity(newintent);
            }
        });
        database = FirebaseFirestore.getInstance().collection("car_registration");
        storageReference= FirebaseStorage.getInstance();
        Loaddata("");
        singlevehiclesearch.addTextChangedListener(new TextWatcher() {
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
        return view;    }
    private void Loaddata(String data) {
        Query query = database.orderBy("plate_number").startAt(data).endAt(data + "\uf8ff");
        options = new FirestoreRecyclerOptions.Builder<vehicle_record>().setQuery(query, vehicle_record.class).build();
        adapter = new FirestoreRecyclerAdapter<vehicle_record, vehicleViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull vehicleViewHolder holder, final int position, @NonNull final vehicle_record model) {
                vehicleidkey=getSnapshots().getSnapshot(position).getId();
               holder.Platenumber.setText("plate number:  "+model.getPlate_number());
               holder.ownername.setText("owner name:  "+model.getOwner_name());
               holder.ownerfather.setText("owner father:  "+model.getOwner_father());
               holder.ownercity.setText("owner city"+model.getOwner_city());
               holder.vehiclemakename.setText("make name:  "+model.getMake_Name());
               holder.vehiclemodel.setText("Model:  "+model.getModel());
               holder.vehiclecolor.setText("Color:  "+model.getColor());
               holder.vehicleprice.setText("price:  "+model.getVehicle_Price());
               holder.registationdate.setText("Registration Date:  "+model.getRegistration_Date());
               holder.chassisnumber.setText("Chassis Number:  "+model.getChassis_Number());
               holder.enginenumber.setText("Engine Number:  "+model.getEngine_Number());
                holder.vehicledelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getSnapshots().getSnapshot(position).getReference().delete();
                    }
                });
                holder.vv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getActivity(),vehicleviewActivity.class);
                        intent.putExtra("vehicle_id",getSnapshots().getSnapshot(position).getId());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public vehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View vehiclev = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_item, parent, false);
                return new vehicleViewHolder(vehiclev);
            }
        };
        adapter.startListening();
        singlevehiclerecyclerview.setAdapter(adapter);
    }


    class vehicleViewHolder extends RecyclerView.ViewHolder {
        private TextView Platenumber;
        private TextView ownername;
        private TextView ownerfather;
        private TextView ownercity;
        private TextView vehiclemakename;
        private TextView vehiclemodel;
        private TextView vehiclecolor;
        private TextView vehicleprice;
        private TextView registationdate;
        private TextView chassisnumber;
        private TextView enginenumber;
        private Button vehicledelete;
        private View vv;
        public vehicleViewHolder(@NonNull View itemView) {
            super(itemView);
            vehicledelete=itemView.findViewById(R.id.singledeleteheviclebtn);
            Platenumber=itemView.findViewById(R.id.singlevehicleplatenumbertxt);
            ownername=itemView.findViewById(R.id.singlevehicleownernametxt);
            ownerfather=itemView.findViewById(R.id.singlevehicleownerfathertxt);
            ownercity=itemView.findViewById(R.id.singlevehicleownercitytxt);
            vehiclemakename=itemView.findViewById(R.id.singlevehiclemakenametxt);
            vehiclemodel=itemView.findViewById(R.id.singlevehiclemodeltxt);
            vehiclecolor=itemView.findViewById(R.id.singlevehiclecolortxt);
            vehicleprice=itemView.findViewById(R.id.singlevehiclepricetxt);
            registationdate=itemView.findViewById(R.id.singlevehicleregistration);
            chassisnumber=itemView.findViewById(R.id.singlevehiclechassisnumber);
            enginenumber=itemView.findViewById(R.id.singlevehicleenginenumber);
            vv=itemView;

        }
    }

}