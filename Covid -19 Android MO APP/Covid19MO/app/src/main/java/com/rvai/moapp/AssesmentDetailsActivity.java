package com.rvai.moapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AssesmentDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assesment_details);

        Intent i= getIntent();
       String Id= (String)i.getExtras().get("mob");
        RecyclerView recyclerView=findViewById(R.id.assesmentList);
        Query query = FirebaseDatabase.getInstance().getReference().child("assesment").child(Id).orderByChild("timestamp");
        FirebaseRecyclerOptions<AssesmentModel> options = new FirebaseRecyclerOptions.Builder<AssesmentModel>().setQuery(query, AssesmentModel.class)
                .setLifecycleOwner(this)
                .build();

        FirebaseRecyclerAdapter<AssesmentModel, QuarantineViewHolder> adapter = new FirebaseRecyclerAdapter<AssesmentModel, QuarantineViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull QuarantineViewHolder holder, int position, @NonNull AssesmentModel model) {
                holder.date.setText(Utils.getDateTime(model.getTimestamp()));
                holder.bodytemp.setText(Integer.toString(model.getBodytemp()));
                if (model.getCough() == 0)
                    holder.cough.setText("NO");
                else
                    holder.cough.setText("YES");

                if (model.getFever() == 0)
                    holder.fever.setText("NO");
                else
                    holder.fever.setText("YES");

                if (model.getBreathingdifficulty() == 0)
                    holder.breathingproblem.setText("NO");
                else
                    holder.breathingproblem.setText("YES");


            }

            @NonNull
            @Override
            public QuarantineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assesment_item, parent, false);
                return new QuarantineViewHolder(view);
            }

        };
        recyclerView.setAdapter(adapter);
        recyclerView.animate();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }
    class QuarantineViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView date, bodytemp, fever, cough, breathingproblem;


        public QuarantineViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.assesment_date);
            bodytemp = itemView.findViewById(R.id.body_temperature);
            fever = itemView.findViewById(R.id.fever);
            cough = itemView.findViewById(R.id.cough);
            breathingproblem = itemView.findViewById(R.id.breathingprob);

        }
    }
}
