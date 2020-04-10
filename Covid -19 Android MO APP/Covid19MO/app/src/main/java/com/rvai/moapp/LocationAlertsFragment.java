package com.rvai.moapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class LocationAlertsFragment extends MainFragment {
    FirebaseDatabase db=FirebaseDatabase.getInstance();

    public LocationAlertsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int getTitle() {
        return R.string.patient_alerts;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alerts, container, false);
        RecyclerView recyclerview = view.findViewById(R.id.patient_list);
        Query query = FirebaseDatabase.getInstance().getReference().child("locationupdates").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        FirebaseRecyclerOptions<LocationModel> options = new FirebaseRecyclerOptions.Builder<LocationModel>().setQuery(query, LocationModel.class)
                .setLifecycleOwner(this)
                .build();

        FirebaseRecyclerAdapter<LocationModel, LocationAlertsViewHolder> adapter = new FirebaseRecyclerAdapter<LocationModel, LocationAlertsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull LocationAlertsViewHolder holder, int position, @NonNull LocationModel model) {
                holder.assesmentButton.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Reached Here", Toast.LENGTH_SHORT).show();
                if((model.getTimestamp()+800000)<Utils.getCurrentDateTime())
                {
                    db.getReference().child("users").child(getSnapshots().getSnapshot(position).getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            UserModel model=dataSnapshot.getValue(UserModel.class);
                            holder.patientname.setText(model.getName());
                            holder.patientaddress.setText(model.getAddress());
                            holder.patientmob.setText(model.getId());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else
                {
                    holder.holder_view.setVisibility(View.GONE);
                    holder.patientname.setVisibility(View.GONE);
                    holder.patientaddress.setVisibility(View.GONE);
                    holder.patientmob.setVisibility(View.GONE);
                }


            }

            @NonNull
            @Override
            public LocationAlertsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_item, parent, false);
                return new LocationAlertsViewHolder(view);
            }

        };
        recyclerview.setAdapter(adapter);

        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    class LocationAlertsViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView patientmob, patientname, patientaddress;
        AppCompatButton assesmentButton;
        MaterialCardView holder_view;

        public LocationAlertsViewHolder(@NonNull View itemView) {
            super(itemView);
            patientaddress = itemView.findViewById(R.id.patient_add);
            assesmentButton=itemView.findViewById(R.id.assesment_button);
            patientname = itemView.findViewById(R.id.patient_name);
            patientmob = itemView.findViewById(R.id.patientmob);
            holder_view=itemView.findViewById(R.id.calls_card_cell);

        }
    }
}
