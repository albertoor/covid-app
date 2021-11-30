package com.albertoornelas.covid_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

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
//                    Map<String, Object> assists = new HashMap<>();
//                    assists.put("userId", auth.getCurrentUser().getUid());
//                    assists.put("eventId", docId.getText().toString());
//                    assists.put("isInfected", false);

                    // Buscar los campos
//                    Query queryFindUserId = db.collection("assists").whereEqualTo(FieldPath.of("userId"), auth.getCurrentUser()).startAt();
//                    System.out.println(queryFindUserId);
//                    Query queryFindEventId = db.collection("assists").whereEqualTo("eventId", docId.getText().toString());

                    // Comprobar si el usuario esta inscrito en el evento

                    // AAgregados su asistencia en el evento
//                    db.collection("assists").document().set(assists).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void unused) {
//                            Toast.makeText(btnAsist.getContext(), "Te has inscrito en el evento", Toast.LENGTH_LONG).show();
//                            btnAsist.setText("YA ESTAS INSCRITO!!!");
//                        }
//                    });
                    break;
                default:
                    break;
            }
        }
    }

    public EventAdapter (List<Event> eventList) {
        this.eventsList = eventList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
