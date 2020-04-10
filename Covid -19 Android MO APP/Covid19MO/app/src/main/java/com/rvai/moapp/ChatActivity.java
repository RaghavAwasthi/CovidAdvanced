package com.rvai.moapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.rvai.moapp.models.ChatModel;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent i = getIntent();
        String ID = (String) i.getExtras().get("ID");
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        AppCompatEditText edittext = findViewById(R.id.messageEditText);

        MaterialButton button = findViewById(R.id.sendButton);
        RecyclerView recyclerview = findViewById(R.id.chatmessage_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(false);
        recyclerview.setLayoutManager(layoutManager);

        String path = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber() + ID;
        Query query = db.getReference().child("chatmessages").child(path).orderByChild("timestamp");
        FirebaseRecyclerOptions<ChatModel> options = new FirebaseRecyclerOptions.Builder<ChatModel>().setQuery(query, ChatModel.class)
                .setLifecycleOwner(this)
                .build();



        FirebaseRecyclerAdapter<ChatModel, ChatViewHolder> adapter = new FirebaseRecyclerAdapter<ChatModel, ChatViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ChatViewHolder holder, int position, @NonNull ChatModel model) {
                holder.timestamp.setText(Utils.getDateTime(model.getTimestamp()));

                boolean isPhoto = model.getUrl() != null;
                if (isPhoto) {
                    holder.message.setVisibility(View.GONE);
                    holder.imageview.setVisibility(View.VISIBLE);
                    Glide.with(holder.imageview.getContext())
                            .load(model.getUrl())
                            .into(holder.imageview);
                } else {
                    holder.message.setVisibility(View.VISIBLE);
                    holder.imageview.setVisibility(View.GONE);
                    holder.message.setText(model.getMessage());
                }

                if (model.getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())) {
                    ViewGroup.MarginLayoutParams cardViewMarginParams = (ViewGroup.MarginLayoutParams) holder.holderview.getLayoutParams();
                    cardViewMarginParams.setMargins(40, 0, 0, 0);
                    //Dont forget this line
                    holder.holderview.setBackgroundColor(getResources().getColor(R.color.colorUserChat));
                    holder.holderview.requestLayout();

                }
            }

            @NonNull
            @Override
            public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = getLayoutInflater().inflate(R.layout.chat_layout_item, parent, false);

                return new ChatViewHolder(view);
            }
        };
        recyclerview.setAdapter(adapter);
        recyclerview.scrollToPosition(-1);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChatModel model = new ChatModel();
                model.setTimestamp(Utils.getCurrentDateTime());
                model.setMessage(edittext.getText().toString());
                model.setSender(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                db.getReference().child("chatmessages").child(path).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        edittext.getText().clear();
                    }
                });
            }
        });

    }

    class ChatViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView holderview;
        AppCompatTextView message, timestamp;
        AppCompatImageView imageview;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            timestamp = itemView.findViewById(R.id.timestamp);
            imageview = itemView.findViewById(R.id.photoImageView);
            holderview = itemView.findViewById(R.id.item_);

        }
    }
}
