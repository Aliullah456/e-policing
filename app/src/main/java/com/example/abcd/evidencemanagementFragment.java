package com.example.abcd;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

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
 * Use the {@link evidencemanagementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class evidencemanagementFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    EditText evidencesearch;
    RecyclerView evidencerecyclerview;
    FloatingActionButton addevidencefloatingbtn;
    FirestoreRecyclerOptions<evidence_record> options;
    FirestoreRecyclerAdapter<evidence_record, evidenceViewHolder> adapter;
    CollectionReference database;
    String complaintidkey;
    FirebaseStorage storageReference;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public evidencemanagementFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment evidencemanagementFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static evidencemanagementFragment newInstance(String param1, String param2) {
        evidencemanagementFragment fragment = new evidencemanagementFragment();
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
        final View view = inflater.inflate(R.layout.fragment_evidencemanagement, container, false);
        evidencesearch = view.findViewById(R.id.evidencesearch);
        evidencerecyclerview = view.findViewById(R.id.evidencerecyclerview);
        addevidencefloatingbtn = view.findViewById(R.id.addevidencefloatingbtn);
        evidencerecyclerview.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        evidencerecyclerview.setHasFixedSize(true);
        database = FirebaseFirestore.getInstance().collection("evidence");
        storageReference= FirebaseStorage.getInstance();

        addevidencefloatingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), addevidence.class));
            }
        });
        Loaddata("");
        evidencesearch.addTextChangedListener(new TextWatcher() {
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
        Query query = database.orderBy("complaint_id").startAt(data).endAt(data + "\uf8ff");
        options = new FirestoreRecyclerOptions.Builder<evidence_record>().setQuery(query, evidence_record.class).build();
        adapter = new FirestoreRecyclerAdapter<evidence_record, evidenceViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull evidenceViewHolder holder, final int position, @NonNull final evidence_record model) {
                complaintidkey=getSnapshots().getSnapshot(position).getId();
                holder.textViewevidence.setText("complaint_id: " + model.getComplaint_id());
                String picuri=getSnapshots().getSnapshot(position).getString("file_uri");
                Uri linkaddress=Uri.parse(picuri);
                String fileExtension = MimeTypeMap.getFileExtensionFromUrl(linkaddress.toString());
                if(fileExtension.equals("jpg")||fileExtension.equals("png")||fileExtension.equals("jpeg")){
                    holder.evidence_single_videoview.setVisibility(View.GONE);
                    holder.imageViewevidence.setVisibility(View.VISIBLE);
                    Picasso.get().load(model.getFile_uri()).into(holder.imageViewevidence);
                }
                else if(fileExtension.equals("mp4")||fileExtension.equals("avi")||fileExtension.equals("mkv")){
                    holder.imageViewevidence.setVisibility(View.GONE);
                    holder.evidence_single_videoview.setVisibility(View.VISIBLE);
                    MediaController mediaController = new MediaController(getActivity());

                    holder.evidence_single_videoview.setMediaController(mediaController);
                    holder.evidence_single_videoview.setVideoURI(linkaddress);
                    holder.evidence_single_videoview.start();

                }
                holder.evidencedelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StorageReference imageref=storageReference.getReferenceFromUrl(model.getFile_uri());
                        imageref.delete();
                        getSnapshots().getSnapshot(position).getReference().delete();
                    }
                });
               /* holder.vv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getActivity(),evidenceviewActivity.class);
                        intent.putExtra("evidence_id",getSnapshots().getSnapshot(position).getId());
                        startActivity(intent);
                    }
                });*/
               holder.vv.setOnClickListener(new DoubleClickListener() {
                   @Override
                   public void onDoubleClick() {
                       Intent intent=new Intent(getActivity(),evidenceviewActivity.class);
                       intent.putExtra("evidence_id",getSnapshots().getSnapshot(position).getId());
                       startActivity(intent);
                   }

                   @Override
                   public void onSingleClick() {

                   }
               });

            }

            @NonNull
            @Override
            public evidenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View evidencev = LayoutInflater.from(parent.getContext()).inflate(R.layout.evidence_item, parent, false);
                return new evidenceViewHolder(evidencev);
            }
        };
        adapter.startListening();
        evidencerecyclerview.setAdapter(adapter);
    }


    class evidenceViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewevidence;
        private VideoView evidence_single_videoview;
        private TextView textViewevidence;
        Button evidencedelete;
        View vv;
        public evidenceViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewevidence = itemView.findViewById(R.id.evidence_single_imageview);
            evidence_single_videoview=itemView.findViewById(R.id.evidence_single_videoview);
            textViewevidence = itemView.findViewById(R.id.evidence_single_textview);
            evidencedelete=itemView.findViewById(R.id.evidencedeletebtn);
            vv=itemView;

        }
    }
    public abstract class DoubleClickListener implements View.OnClickListener {
        private static final long DEFAULT_QUALIFICATION_SPAN = 200;
        private boolean isSingleEvent;
        private long doubleClickQualificationSpanInMillis;
        private long timestampLastClick;
        private Handler handler;
        private Runnable runnable;

        public DoubleClickListener() {
            doubleClickQualificationSpanInMillis = DEFAULT_QUALIFICATION_SPAN;
            timestampLastClick = 0;
            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (isSingleEvent) {
                        onSingleClick();
                    }
                }
            };
        }

        @Override
        public void onClick(View v) {
            if((SystemClock.elapsedRealtime() - timestampLastClick) < doubleClickQualificationSpanInMillis) {
                isSingleEvent = false;
                handler.removeCallbacks(runnable);
                onDoubleClick();
                return;
            }

            isSingleEvent = true;
            handler.postDelayed(runnable, DEFAULT_QUALIFICATION_SPAN);
            timestampLastClick = SystemClock.elapsedRealtime();
        }

        public abstract void onDoubleClick();
        public abstract void onSingleClick();
    }
}