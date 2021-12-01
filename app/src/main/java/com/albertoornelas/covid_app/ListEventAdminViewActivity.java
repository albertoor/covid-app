package com.albertoornelas.covid_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListEventAdminViewActivity extends AppCompatActivity {

    // variables
    private RecyclerView recyclerView;
    private FirebaseFirestore database;
    private MyAdapterAdmin adapter;
    private List<EventModel> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_event_admin_view);

        // Init variables
        recyclerView = findViewById(R.id.recyclerViewUser);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        database = FirebaseFirestore.getInstance();
        eventList = new ArrayList<>();
        adapter = new MyAdapterAdmin(this, eventList);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper touchHelper = new ItemTouchHelper(new TouchHelper(adapter));
        touchHelper.attachToRecyclerView(recyclerView);
        fetchEvents();
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
                Toast.makeText(ListEventAdminViewActivity.this, "Algo salio mal", Toast.LENGTH_SHORT).show();
            }
        });
    }
}