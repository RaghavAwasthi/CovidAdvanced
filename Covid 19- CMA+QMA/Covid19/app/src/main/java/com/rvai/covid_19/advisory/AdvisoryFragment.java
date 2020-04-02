package com.rvai.covid_19.advisory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.rvai.covid_19.MainFragment;
import com.rvai.covid_19.R;
import com.rvai.covid_19.models.AdvisoryModel;

public class AdvisoryFragment extends MainFragment {
    public AdvisoryFragment() {
        // Required empty public constructor
    }

    @Override
    public int getTitle() {
        return R.string.title_adversory;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_advisory, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.advisoryList);

        Query query = FirebaseDatabase.getInstance().getReference().child("advisory").orderByChild("timestamp");
        FirebaseRecyclerOptions<AdvisoryModel> options = new FirebaseRecyclerOptions.Builder<AdvisoryModel>().setQuery(query, AdvisoryModel.class)
                .setLifecycleOwner(this)
                .build();

        FirebaseRecyclerAdapter<AdvisoryModel, AdvisoryViewHolder> adapter = new FirebaseRecyclerAdapter<AdvisoryModel, AdvisoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdvisoryViewHolder holder, int position, @NonNull AdvisoryModel model) {
                holder.title.setText(model.getTitle());
                holder.description.setText(model.getDesc());
                Glide.with(getContext()).load(model.getUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imageView);
            }

            @NonNull
            @Override
            public AdvisoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.advisory_item, parent, false);
                return new AdvisoryViewHolder(view);
            }

        };
        recyclerView.setAdapter(adapter);
        recyclerView.animate();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    class AdvisoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        TextView description;

        public AdvisoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.advisory_image);
            title = itemView.findViewById(R.id.advisory_title);
            description = itemView.findViewById(R.id.advisory_description);
        }
    }


}
