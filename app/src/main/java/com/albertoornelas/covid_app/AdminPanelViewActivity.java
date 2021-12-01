package com.albertoornelas.covid_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.UUID;

public class AdminPanelViewActivity extends AppCompatActivity {

    // variables
    private EditText nameEtAdmin;
    private EditText placeEtAdmin;
    private EditText timeEtAdmin;
    private EditText dateEtAdmin;
    private Button btnSaveEventAdmin;
    private Button btnShowEventsAdmin;
    private FirebaseFirestore database;
    private String uId,uName, uPlace, uTime, uDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel_view);

        // Init variables
        nameEtAdmin = (EditText) findViewById(R.id.nameEtAdmin);
        placeEtAdmin = (EditText) findViewById(R.id.placeEtAdmin);
        timeEtAdmin = (EditText) findViewById(R.id.timeEtAdmin);
        dateEtAdmin = (EditText) findViewById(R.id.dateEtAdmin);
        btnSaveEventAdmin = (Button) findViewById(R.id.btnSaveEventAdmin);
        btnShowEventsAdmin = (Button) findViewById(R.id.btnShowEventsAdmin);
        database = FirebaseFirestore.getInstance();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            btnSaveEventAdmin.setText("Editar");
            uId = bundle.getString("uId");
            uName = bundle.getString("uName");
            uPlace = bundle.getString("uPlace");
            uTime = bundle.getString("uTime");
            uDate = bundle.getString("uDate");
            nameEtAdmin.setText(uName);
            placeEtAdmin.setText(uPlace);
            timeEtAdmin.setText(uTime);
            dateEtAdmin.setText(uDate);
        } else {
            btnSaveEventAdmin.setText("Guardar");
        }

        // Click listeners
        // Save data in db
        btnSaveEventAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get variables from view
                String name = nameEtAdmin.getText().toString();
                String place = placeEtAdmin.getText().toString();
                String time = timeEtAdmin.getText().toString();
                String date = dateEtAdmin.getText().toString();

                Bundle bundle1 = getIntent().getExtras();
                if (bundle1 != null) {
                    String id = uId;
                    updateEventToFirestore(id, name, place, time, date);
                } else {
                    // Call function to save data in firestore (database)
                    String id = UUID.randomUUID().toString();
                    saveEventToFirestore(id, name, place, time, date);
                }
            }
        });

        // Show List Events Admin View
        btnShowEventsAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminPanelViewActivity.this, ListEventAdminViewActivity.class));
            }
        });
    }

    private void saveEventToFirestore(String id, String name, String place, String time, String date) {
        if (!name.isEmpty() && !place.isEmpty() && !time.isEmpty() && !date.isEmpty()) {
            // Building HashMap to save data
            HashMap<String, Object> event = new HashMap<>();
            event.put("id", id);
            event.put("name", name);
            event.put("place", place);
            event.put("time", time);
            event.put("date", date);

            // Insert data to database
            database.collection("events").document(id).set(event).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        nameEtAdmin.setText("");
                        placeEtAdmin.setText("");
                        timeEtAdmin.setText("");
                        dateEtAdmin.setText("");
                        Toast.makeText(AdminPanelViewActivity.this, "Evento Guardado", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AdminPanelViewActivity.this, "Algo salio mal", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Campos vacios", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateEventToFirestore(String id, String name, String place, String time, String date) {
        Toast.makeText(AdminPanelViewActivity.this, id, Toast.LENGTH_SHORT).show();
        if (!name.isEmpty() && !place.isEmpty() && !time.isEmpty() && !date.isEmpty()) {
            // Building HashMap to save data
            HashMap<String, Object> event = new HashMap<>();
            event.put("id", id);
            event.put("name", name);
            event.put("place", place);
            event.put("time", time);
            event.put("date", date);

            // Update data to database
            database.collection("events").document(id)
                    .update("name", name, "place", place, "time", time, "date", date)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AdminPanelViewActivity.this, "Evento Actualizado", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AdminPanelViewActivity.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AdminPanelViewActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(this, "Campos vacios", Toast.LENGTH_SHORT).show();
        }
    }
}