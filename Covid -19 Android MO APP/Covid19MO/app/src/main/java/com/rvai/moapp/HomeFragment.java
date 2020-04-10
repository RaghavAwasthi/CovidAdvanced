package com.rvai.moapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class HomeFragment extends MainFragment {


    @Override
    public int getTitle() {
        return R.string.home;
    }

    public HomeFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView=root.findViewById(R.id.alert_list);
        Query query = FirebaseDatabase.getInstance().getReference().child("alerts").orderByChild("timetamp");
        FirebaseRecyclerOptions<AlertsModel> options = new FirebaseRecyclerOptions.Builder<AlertsModel>().setQuery(query, AlertsModel.class)
                .setLifecycleOwner(this)
                .build();

        FirebaseRecyclerAdapter<AlertsModel, AlertsViewHolder> adapter = new FirebaseRecyclerAdapter<AlertsModel, AlertsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AlertsViewHolder holder, int position, @NonNull AlertsModel model) {
                holder.name.setText(model.getPatientName());
                holder.address.setText(model.getPatientAddress());
                holder.patientMob.setText(model.getPatientID());
            }

            @NonNull
            @Override
            public AlertsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alert_item, parent, false);
                return new AlertsViewHolder(view);
            }

        };
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return root;
    }
    class AlertsViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView patientMob;
        AppCompatTextView name;
        AppCompatTextView address;

        public AlertsViewHolder(@NonNull View itemView) {
            super(itemView);
            patientMob = itemView.findViewById(R.id.patientmob);
            name = itemView.findViewById(R.id.patient_name);
            address = itemView.findViewById(R.id.patient_add);
        }
    }
}
