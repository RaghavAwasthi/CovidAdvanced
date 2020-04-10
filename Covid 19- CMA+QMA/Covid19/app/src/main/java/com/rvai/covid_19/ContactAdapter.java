package com.rvai.covid_19;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.rvai.covid_19.models.ContactModel;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactViewHolder> {

    ArrayList<ContactModel> list;

    public void setList(ArrayList<ContactModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.statetextView.setText(list.get(position).getCity());
        holder.mobtextView.setText(list.get(position).getMob());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class ContactViewHolder extends RecyclerView.ViewHolder {
    AppCompatTextView statetextView, mobtextView;

    public ContactViewHolder(@NonNull View itemView) {
        super(itemView);
        statetextView = itemView.findViewById(R.id.state);
        mobtextView = itemView.findViewById(R.id.mob);
    }
}
