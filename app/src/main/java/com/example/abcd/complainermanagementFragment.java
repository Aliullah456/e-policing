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
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link complainermanagementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class complainermanagementFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    EditText complainersearch;
    RecyclerView complainerrecyclerview;
    //FloatingActionButton policefloatingbtn;
    FirestoreRecyclerOptions<user_record> options;
    FirestoreRecyclerAdapter<user_record, complainerViewHolder> adapter;
    CollectionReference database;
    String complainerkey;
    FirebaseStorage storageReference;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public complainermanagementFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment complainermanagementFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static complainermanagementFragment newInstance(String param1, String param2) {
        complainermanagementFragment fragment = new complainermanagementFragment();
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
        final View view = inflater.inflate(R.layout.fragment_complainermanagement, container, false);
        complainersearch = view.findViewById(R.id.complainersearch);
        complainerrecyclerview = view.findViewById(R.id.complainerrecyclerview);
        complainerrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        complainerrecyclerview.setHasFixedSize(true);
        database = FirebaseFirestore.getInstance().collection("users");
        storageReference= FirebaseStorage.getInstance();

        Loaddata("");
        complainersearch.addTextChangedListener(new TextWatcher() {
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
        Query query = database.orderBy("fullname").whereEqualTo("type","complainer").startAt(data).endAt(data + "\uf8ff");
        options = new FirestoreRecyclerOptions.Builder<user_record>().setQuery(query, user_record.class).build();
        adapter = new FirestoreRecyclerAdapter<user_record, complainerViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final complainerViewHolder holder, final int position, @NonNull final user_record model) {
                complainerkey=getSnapshots().getSnapshot(position).getId();
                holder.policename.setText("complainer name:  "+model.getFullname());
                holder.policecnic.setText("CNIC:  "+model.getCNIC());
                holder.policephone.setText("phone:  "+model.getMobilenumber());
                holder.policeaddress.setText("address:  "+model.getAddress());
                holder.policeemail.setText("email:  "+model.getEmail());
                holder.policepassword.setText("password:   "+model.getPassword());
                final String picuri=getSnapshots().getSnapshot(position).getString("profileimage");
                Picasso.get().load(model.getProfileimage()).into(holder.policeimageview);
                if(picuri==null){
                    holder.policeimageview.setImageResource(R.drawable.ic_image);
                }
                holder.vv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getActivity(),complainerviewActivity.class);
                        intent.putExtra("complainer_key",getSnapshots().getSnapshot(position).getId());
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
            public complainerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View complainerv = LayoutInflater.from(parent.getContext()).inflate(R.layout.complainer_item, parent, false);
                return new complainerViewHolder(complainerv);
            }
        };
        adapter.startListening();
        complainerrecyclerview.setAdapter(adapter);
    }


    class complainerViewHolder extends RecyclerView.ViewHolder {
        private ImageView policeimageview;
        private TextView policename;
        private TextView policecnic;
        private TextView policeemail;
        private TextView policephone;
        private TextView policeaddress;
        private TextView policepassword;
        View vv;
        //Button policedelete;
        public complainerViewHolder(@NonNull View itemView) {
            super(itemView);
            policeimageview = itemView.findViewById(R.id.singlecomplainerpicture);
            policename = itemView.findViewById(R.id.singlecomplainername);
            policecnic=itemView.findViewById(R.id.singlecomplainercnic);
            policeemail=itemView.findViewById(R.id.singlecomplaineremail);
            policephone=itemView.findViewById(R.id.singlecomplainerphone);
            policeaddress=itemView.findViewById(R.id.singlecomplaineraddress);
            policepassword=itemView.findViewById(R.id.singlecomplainerpassword);
            vv=itemView;
            // policedelete=itemView.findViewById(R.id.deletepoliceaccountbtn);

        }
    }

}