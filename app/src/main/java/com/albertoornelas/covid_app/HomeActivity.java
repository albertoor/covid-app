package com.albertoornelas.covid_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private Button bntLogout;
    private TextView currentUserTxt;
    private TextView countInfected;
    private FirebaseAuth auth;
    private RecyclerView recyclerView;
    private FirebaseFirestore database;
    private MyAdapterUser adapter;
    private List<EventModel> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Iniciando variables
        bntLogout = (Button) findViewById(R.id.btnLogout);
        auth = FirebaseAuth.getInstance();
        currentUserTxt = findViewById(R.id.currentUseTxt);
        currentUserTxt.setText("Hola, " + auth.getCurrentUser().getEmail());
        database = FirebaseFirestore.getInstance();

        countInfected = (TextView) findViewById(R.id.countInfected);

        recyclerView = findViewById(R.id.recyclerViewUser);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventList = new ArrayList<>();
        adapter = new MyAdapterUser(this, eventList);
        recyclerView.setAdapter(adapter);

        fetchEvents();

        listeningCounts();

        // listeners
        bntLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void listeningCounts() {
        database.collection("infected_participants").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())  {
                    int count = 0;
                    for (DocumentSnapshot document: task.getResult()) {
                        count++;
                        countInfected.setText("Contador de casos: " + count);
                    }
                }
            }
        });
    }

    protected void fetchEvents() {
        database.collection("events").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                eventList.clear();

                for (DocumentSnapshot snapshot : task.getResult()) {
                    EventModel eventModel = new EventModel(
                            snapshot.getString("id"), snapshot.getString("name"),
                            snapshot.getString("place"), snapshot.getString("time"),
                            snapshot.getString("date"));
                    eventList.add(eventModel);
                }
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HomeActivity.this, "Algo salio mal", Toast.LENGTH_SHORT).show();
            }
        });
    }

}