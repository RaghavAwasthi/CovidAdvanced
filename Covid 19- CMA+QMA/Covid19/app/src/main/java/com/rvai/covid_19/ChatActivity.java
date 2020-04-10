package com.rvai.covid_19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.rvai.covid_19.models.AdvisoryModel;
import com.rvai.covid_19.models.ChatModel;
import com.rvai.covid_19.models.UnreadMessageModel;
import com.rvai.covid_19.utils.Utils;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        AppCompatEditText edittext = findViewById(R.id.messageEditText);

        MaterialButton button = findViewById(R.id.sendButton);
        RecyclerView recyclerview = findViewById(R.id.chatmessage_list);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setReverseLayout(false);
        recyclerview.setLayoutManager(layoutManager);

        SharedPreferences preferences = getSharedPreferences(Constants.APP_PREFERENCE_NAME, MODE_PRIVATE);

        String moId = preferences.getString(Constants.MOID, "");
        if (moId.equals(""))
            throw new RuntimeException();

        String participantID = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        String path = moId + participantID;
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


                if(model.getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()))
                {
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


        recyclerview.scrollToPosition(adapter.getItemCount()-1);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edittext.getText() != null && edittext.getText().length() > 0) {
                    String message = edittext.getText().toString();
                    ChatModel model = new ChatModel();
                    model.setMessage(message);
                    model.setSender(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                    model.setTimestamp(Utils.getCurrentDateTime());
                    db.getReference().child("chatmessages").child(path).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            edittext.setText("");
                            UnreadMessageModel unreadMessageModel = new UnreadMessageModel();
                            unreadMessageModel.setSenderId(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                            unreadMessageModel.setReceiverId(preferences.getString(Constants.MOID, ""));
                            unreadMessageModel.setCount(1);
                            db.getReference().child("chatUsers").child(preferences.getString(Constants.MOID, ""))
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).setValue(unreadMessageModel);
                        }
                    });

                } else {
                    Toast.makeText(ChatActivity.this, "Invalid Length of Field", Toast.LENGTH_SHORT).show();
                }
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
            holderview=itemView.findViewById(R.id.item_);
        }
    }
}
