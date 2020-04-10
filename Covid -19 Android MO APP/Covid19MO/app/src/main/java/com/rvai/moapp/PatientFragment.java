package com.rvai.moapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class PatientFragment extends MainFragment {


    public PatientFragment() {
        // Required empty public constructor
    }

    @Override
    public int getTitle() {
        return R.string.patient;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_patient, container, false);
        RecyclerView recyclerview = view.findViewById(R.id.patient_list);
        Query query = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("moId").equalTo(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        FirebaseRecyclerOptions<UserModel> options = new FirebaseRecyclerOptions.Builder<UserModel>().setQuery(query, UserModel.class)
                .setLifecycleOwner(this)
                .build();
        FirebaseRecyclerAdapter<UserModel, PatientViewHolder> adapter = new FirebaseRecyclerAdapter<UserModel, PatientViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PatientViewHolder holder, int position, @NonNull UserModel model) {
                holder.patientname.setText(model.getName());
                holder.patientaddress.setText(model.getAddress());
                holder.patientmob.setText(model.getId());
                holder.assesmentButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), getSnapshots().get(position).id, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getContext(), AssesmentDetailsActivity.class);
                        i.putExtra("mob", model.getId());
                        startActivity(i);
                    }
                });

            }

            @NonNull
            @Override
            public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_item, parent, false);
                return new PatientViewHolder(view);
            }

        };
        recyclerview.setAdapter(adapter);

        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    class PatientViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView patientmob, patientname, patientaddress;
        AppCompatButton assesmentButton;

        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            patientaddress = itemView.findViewById(R.id.patient_add);
            assesmentButton=itemView.findViewById(R.id.assesment_button);
            patientname = itemView.findViewById(R.id.patient_name);
            patientmob = itemView.findViewById(R.id.patientmob);

        }
    }
}
