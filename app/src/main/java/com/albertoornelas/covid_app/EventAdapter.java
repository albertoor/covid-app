package com.albertoornelas.covid_app;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder>  {

    private List<Event> eventsList;
    public Event event;

    private String currentEvntId;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, place, timestamp, aforo, docId;
        public Button btnAsist;
        private static final String TAG = "Get Event id";


        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            place = (TextView) view.findViewById(R.id.place);
            timestamp = (TextView) view.findViewById(R.id.timestamp);
            aforo = (TextView) view.findViewById(R.id.aforo);
            docId = (TextView) view.findViewById(R.id.idTxt);
            docId.setVisibility(View.INVISIBLE);
            btnAsist = (Button) view.findViewById(R.id.btnAsist);

            btnAsist.setOnClickListener(this);
//            db = FirebaseFirestore.getInstance();
//            auth = FirebaseAuth.getInstance();
        }

//        Map<String, Object> attended = new HashMap<>();
//                    attendee.put("eventId", docId.getText().toString());
//                    db.collection("users")
//                            .document(auth.getCurrentUser().getUid())
//                .collection("attended")
//                            .document().set(attended);

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnAsist:
                    Map<String, Object> attendee = new HashMap<>();
                    attendee.put("userId", auth.getCurrentUser().getUid());

                    // Add data in events
                    db.collection("events")
                            .document(docId.getText().toString())
                            .collection("attendees")
                            .document().set(attendee);
                    break;
                default:
                    break;
            }
        }
    }


//    Event event = new Event(nameTxt.getText().toString(),
//            placeTxt.getText().toString(),
//            timestampTxt.getText().toString(),
//            Integer.parseInt(aforoNum.getText().toString()));
//
//                db.collection("events").document().set(event).addOnSuccessListener(new OnSuccessListener<Void>() {
//        @Override
//        public void onSuccess(Void unused) {
//            Log.d(TAG, "Evento agreado");
//            nameTxt.setText("");
//            placeTxt.setText("");
//            timestampTxt.setText("");
//            aforoNum.setText("");
//        }
//    }).addOnFailureListener(new OnFailureListener() {
//        @Override
//        public void onFailure(@NonNull Exception e) {
//            Log.w(TAG, "Error al agregar el evento", e);
//        }
//    });

    public EventAdapter (List<Event> eventList) {
        this.eventsList = eventList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        eventsList.get(0);
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        event = eventsList.get(position);
        holder.docId.setText(event.getDocId());
        holder.name.setText("Evento: " + event.getName());
        holder.place.setText("Lugar: " + event.getPlace());
        holder.timestamp.setText("Fecha y hora: "  + event.getTimestamp());
        holder.aforo.setText("Aforo: " + event.getAforo());
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

}
