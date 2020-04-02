package com.rvai.covid_19.quarantine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.rvai.covid_19.Constants;
import com.rvai.covid_19.MainActivity;
import com.rvai.covid_19.MainFragment;
import com.rvai.covid_19.R;
import com.rvai.covid_19.advisory.AdvisoryFragment;
import com.rvai.covid_19.models.AdvisoryModel;
import com.rvai.covid_19.models.AssesmentModel;
import com.rvai.covid_19.utils.Utils;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuarantineFragment extends MainFragment {
    Chip assesment_button;
    RecyclerView recyclerView;
    AppCompatTextView nextAssesment;
    SharedPreferences preferences;

    public QuarantineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quarantine, container, false);
        assesment_button = view.findViewById(R.id.assesment_button);
        preferences = getContext().getSharedPreferences(Constants.APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
        nextAssesment = view.findViewById(R.id.nextAssesment);

        if (!preferences.getBoolean(Constants.P, false)) {
            nextAssesment.setText("Your next Assesment is at 10:00 AM");
        } else if (!preferences.getBoolean(Constants.Q, false)) {

            nextAssesment.setText("Your next Assesment is at 12:00 PM");
        } else if (!preferences.getBoolean(Constants.R, false)) {

            nextAssesment.setText("Your next Assesment is at 14:00 PM");
        } else if(!preferences.getBoolean(Constants.S, false)){

            nextAssesment.setText("Your next Assesment is at 16:00 PM");
        }
        recyclerView = view.findViewById(R.id.assesment_list);
        assesment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AssesmentActivity.class);
                startActivity(i);
            }
        });
        Query query = FirebaseDatabase.getInstance().getReference().child("assesment").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).orderByChild("timestamp");
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        return view;
    }

    @Override
    public int getTitle() {
        return R.string.title_quarantine;
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
