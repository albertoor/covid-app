package com.albertoornelas.covid_app;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyAdapterUser extends RecyclerView.Adapter<MyAdapterUser.MyViewHolder> {

    // variables
    private HomeActivity homeActivity;
    private List<EventModel> eventsList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public MyAdapterUser(HomeActivity homeActivity, List<EventModel> eventsList) {
        this.homeActivity = homeActivity;
        this.eventsList = eventsList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {
        public TextView idTvUser, nameTvUser, placeTvUser, timeTvUser, dateTvUser;
        public Button btnAsistUser;


        public MyViewHolder (@NonNull View view) {
            super(view);
            // Init varibales
            idTvUser = view.findViewById(R.id.idTvUser);
            nameTvUser = view.findViewById(R.id.nameTvUser);
            placeTvUser = view.findViewById(R.id.placeTvUser);
            timeTvUser = view.findViewById(R.id.timeTvUser);
            dateTvUser = view.findViewById(R.id.dateTvUser);
            btnAsistUser = (Button) view.findViewById(R.id.btnAsistUser);

            btnAsistUser.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            Map<String, Object> infected_participants = new HashMap<>();
                infected_participants.put("userId", auth.getCurrentUser().getUid());
                infected_participants.put("eventId", idTvUser.getText().toString());

            switch (view.getId()) {
                case R.id.btnAsistUser:
                    db.collection("infected_participants").document().set(infected_participants).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(btnAsistUser.getContext(), "Avisaremos a los demas", Toast.LENGTH_LONG).show();
                            btnAsistUser.setEnabled(false);
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
        View view = LayoutInflater.from(homeActivity).inflate(R.layout.event_user, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterUser.MyViewHolder holder, int position) {
        holder.idTvUser.setText(eventsList.get(position).getId());
        holder.nameTvUser.setText(eventsList.get(position).getName());
        holder.placeTvUser.setText(eventsList.get(position).getPlace());
        holder.timeTvUser.setText(eventsList.get(position).getTime());
        holder.dateTvUser.setText(eventsList.get(position).getDate());
        return;
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

}

