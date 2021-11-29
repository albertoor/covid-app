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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private List<Event> eventList = new ArrayList<>();
    private RecyclerView recyclerView;
    private EventAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private FirebaseFirestore db;
    private static final String TAG = "Events";
    private TextView addEventLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Iniciando variables
        bntLogout = (Button) findViewById(R.id.btnLogout);
        auth = FirebaseAuth.getInstance();
        currentUserTxt = findViewById(R.id.currentUseTxt);
        currentUserTxt.setText("Hola, " + auth.getCurrentUser().getEmail());
        db = FirebaseFirestore.getInstance();
        addEventLink = findViewById(R.id.addEventLink);

        // functiones para deplegar info
        initViews();
        getEvents();

        // listeners
        bntLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        addEventLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(HomeActivity.this, AddEvent.class);
                startActivity(intent);
            }
        });
    }

    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAdapter = new EventAdapter(eventList);
        mLayoutManager = new LinearLayoutManager(HomeActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    private void getEvents() {
        db.collection("events").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
                if (!snapshots.isEmpty()) {
                    for (QueryDocumentSnapshot eventDoc : snapshots) {
                        Event event = eventDoc.toObject(Event.class);
                        event.setDocId(eventDoc.getId());

                        eventList.add(event);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}