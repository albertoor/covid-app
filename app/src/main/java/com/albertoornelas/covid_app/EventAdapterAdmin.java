package com.albertoornelas.covid_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class EventAdapterAdmin extends RecyclerView.Adapter<EventAdapterAdmin.MyViewHolder>  {

    private List<Event> eventsList;
    public Event event;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    // Contructor
    public EventAdapterAdmin (List<Event> eventList) {
        this.eventsList = eventList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, place, timestamp, aforo, docId;
        public Button btnDeleteAdmin;
        public Button btnEditAdminLink;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.nameAdmin);
            place = (TextView) view.findViewById(R.id.placeAdmin);
            timestamp = (TextView) view.findViewById(R.id.timestampAdmin);
            aforo = (TextView) view.findViewById(R.id.aforoAdmin);
            docId = (TextView) view.findViewById(R.id.idTxtAdmin);
            btnDeleteAdmin = (Button) view.findViewById(R.id.btnDeleteAdmin);
            btnEditAdminLink = (Button) view.findViewById(R.id.btnEditAdminLink);

            btnDeleteAdmin.setOnClickListener(this);
            btnEditAdminLink.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnDeleteAdmin:
                    // Eliminar document en la base de datos
                    db.collection("events").document(docId.getText().toString()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            eventsList.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            notifyItemChanged(getAdapterPosition());
                            notifyDataSetChanged();
                            Toast.makeText(btnDeleteAdmin.getContext(),"Se borro el evento", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(btnDeleteAdmin.getContext(),"Ocurrio un problema", Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_row_admin, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        event = eventsList.get(position);
        holder.docId.setText(event.getDocId());
        holder.name.setText(event.getName());
        holder.place.setText(event.getPlace());
        holder.timestamp.setText(event.getTimestamp());
        holder.aforo.setText("" + event.getAforo());

        holder.btnEditAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EditEvent.class);
                intent.putExtra("docId", holder.docId.getText().toString());
                intent.putExtra("name", holder.name.getText().toString());
                intent.putExtra("place", holder.place.getText().toString());
                intent.putExtra("timestamp", holder.timestamp.getText().toString());
                intent.putExtra("aforo", holder.aforo.getText().toString());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }
}
