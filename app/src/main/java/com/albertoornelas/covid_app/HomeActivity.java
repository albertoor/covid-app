package com.albertoornelas.covid_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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
        db.collection("events").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot events : queryDocumentSnapshots) {
                        Event event = events.toObject(Event.class);
                        eventList.add(event);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }
}