package com.rvai.moapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rvai.moapp.models.UnreadMessageModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatListFragment extends MainFragment {
    FirebaseDatabase db = FirebaseDatabase.getInstance();

    public ChatListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.chat_list);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        Query query = db.getReference().child("chatUsers").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        FirebaseRecyclerOptions<UnreadMessageModel> options = new FirebaseRecyclerOptions.Builder<UnreadMessageModel>().setQuery(query, UnreadMessageModel.class)
                .setLifecycleOwner(this)
                .build();


        FirebaseRecyclerAdapter<UnreadMessageModel, ChatPatientViewHolder> adapter = new FirebaseRecyclerAdapter<UnreadMessageModel, ChatPatientViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ChatPatientViewHolder holder, int position, @NonNull UnreadMessageModel model) {
                holder.textview.setText(model.getSenderId());
                holder.textview.setTag(model.getSenderId());
                if (model.getCount() > 0) {
                    holder.dotimage.setVisibility(View.VISIBLE);
                } else
                    holder.dotimage.setVisibility(View.GONE);


                db.getReference().child("users").child(model.getSenderId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserModel um = dataSnapshot.getValue(UserModel.class);
                        String image = um.getImageurl();
                        holder.userName.setText(um.getName());
                        Glide.with(getContext()).load(image).into(holder.imageView);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                holder.textview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String target = (String) v.getTag();
                        Toast.makeText(getContext(), "" + target, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getContext(), ChatActivity.class);
                        i.putExtra("ID", model.getSenderId());
                        startActivity(i);
                    }
                });
            }

            @NonNull
            @Override
            public ChatPatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = getLayoutInflater().inflate(R.layout.chat_list_item, parent, false);
                return new ChatPatientViewHolder(view);
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public int getTitle() {
        return R.string.patient_chat;
    }

    class ChatPatientViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView textview, userName;
        AppCompatImageView imageView, dotimage;


        public ChatPatientViewHolder(@NonNull View itemView) {
            super(itemView);
            textview = itemView.findViewById(R.id.contact_name);
            imageView = itemView.findViewById(R.id.profile_image);
            dotimage = itemView.findViewById(R.id.dotimage);
            userName = itemView.findViewById(R.id.contact_text_name);

        }
    }
}
