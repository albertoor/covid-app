package com.albertoornelas.covid_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class AdminPanel extends AppCompatActivity {

    private Button btnLogoutAdmin;
    private Button btnAddEventAdminLink;
    private List<Event> eventList = new ArrayList<>();
    private RecyclerView recyclerView;
    private EventAdapterAdmin mAdapter;
    private LinearLayoutManager mLayoutManager;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        btnLogoutAdmin = (Button) findViewById(R.id.btnLogoutAdmin);
        btnAddEventAdminLink = (Button) findViewById(R.id.btnAddEventAdminLink);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        initViews();
        getEvents();

        // listeners
        btnLogoutAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(AdminPanel.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnAddEventAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminPanel.this, AddEvent.class);
                startActivity(intent);
            }
        });
    }

    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewAdmin);
        mAdapter = new EventAdapterAdmin(eventList);
        mLayoutManager = new LinearLayoutManager(AdminPanel.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    private void getEvents() {
        db.collection("events").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
                if (!snapshots.isEmpty()) {
                    Event event = new Event();
                    for (QueryDocumentSnapshot eventDoc : snapshots) {
                        event = eventDoc.toObject(Event.class);
                        event.setDocId(eventDoc.getId());
                        eventList.add(event);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}