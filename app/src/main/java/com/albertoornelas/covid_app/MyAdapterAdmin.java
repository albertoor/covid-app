package com.albertoornelas.covid_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MyAdapterAdmin extends RecyclerView.Adapter<MyAdapterAdmin.MyViewHolder> {

    // variables
    private ListEventAdminViewActivity listEventAdminViewActivity;
    private List<EventModel> eventsList;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    public MyAdapterAdmin(ListEventAdminViewActivity listEventAdminViewActivity, List<EventModel> eventsList) {
        this.listEventAdminViewActivity = listEventAdminViewActivity;
        this.eventsList = eventsList;
    }

    public void updateData (int position) {
        EventModel eventModel = eventsList.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("uId", eventModel.getId());
        bundle.putString("uName", eventModel.getName());
        bundle.putString("uPlace", eventModel.getPlace());
        bundle.putString("uTime", eventModel.getTime());
        bundle.putString("uDate", eventModel.getDate());
        Intent intent = new Intent(listEventAdminViewActivity, AdminPanelViewActivity.class);
        intent.putExtras(bundle);
        listEventAdminViewActivity.startActivity(intent);
    }

    public void deleteDate(int position) {
        EventModel eventModel = eventsList.get(position);
        database.collection("events").document(eventModel.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(listEventAdminViewActivity, "Evento eliminado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(listEventAdminViewActivity, "Error + " + task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void notifyRemoved(int position) {
        eventsList.remove(position);
        notifyItemRemoved(position);
        listEventAdminViewActivity.fetchEvents();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(listEventAdminViewActivity).inflate(R.layout.event, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nameTv.setText(eventsList.get(position).getName());
        holder.placeTv.setText(eventsList.get(position).getPlace());
        holder.timeTv.setText(eventsList.get(position).getTime());
        holder.dateTv.setText(eventsList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv, placeTv, timeTv, dateTv;

        public MyViewHolder(@NonNull View view) {
            super(view);
            // Init variables
            nameTv = (TextView) view.findViewById(R.id.nameTvAdmin);
            placeTv = view.findViewById(R.id.placeTvAdmin);
            timeTv = view.findViewById(R.id.timeTvAdmin);
            dateTv = view.findViewById(R.id.dateTvAdmin);
        }
    }
}




