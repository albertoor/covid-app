package com.albertoornelas.covid_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditEvent extends AppCompatActivity {
    private EditText eventNameEditText;
    private EditText eventPlaceEditText;
    private EditText eventTimestampEditText;
    private EditText eventAforoEditText;
    private Button btnEditEventAdmin;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        eventNameEditText = (EditText) findViewById(R.id.eventNameEditText);
        eventPlaceEditText = (EditText) findViewById(R.id.eventPlaceEditText);
        eventTimestampEditText = (EditText) findViewById(R.id.eventTimestampEditText);
        eventAforoEditText = (EditText) findViewById(R.id.eventAforoEditText);
        btnEditEventAdmin = (Button) findViewById(R.id.btnEditEventAdmin);
        db = FirebaseFirestore.getInstance();

        // Tomar info desde el otro activity
        String nameTxt =  getIntent().getStringExtra("name");
        String placeTxt = getIntent().getStringExtra("place");
        String timestampTxt = getIntent().getStringExtra("timestamp");
        String aforoTxt = getIntent().getStringExtra("aforo");
        String docIdTxt = getIntent().getStringExtra("docId");

        // Colocar informacion
        eventNameEditText.setText(nameTxt);
        eventPlaceEditText.setText(placeTxt);
        eventTimestampEditText.setText(timestampTxt);
        eventAforoEditText.setText(aforoTxt);

//        Toast.makeText(EditEvent.this, docIdTxt, Toast.LENGTH_LONG).show();

        // Listeners
        btnEditEventAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Event event = new Event(eventNameEditText.getText().toString(),
                        eventPlaceEditText.getText().toString(),
                        eventTimestampEditText.getText().toString(),
                        Integer.parseInt(eventAforoEditText.getText().toString()));
                db.collection("events").document(docIdTxt).set(event).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EditEvent.this, "Evento editado", Toast.LENGTH_SHORT).show();
                        eventNameEditText.setText("");
                        eventPlaceEditText.setText("");
                        eventTimestampEditText.setText("");
                        eventAforoEditText.setText("");

                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditEvent.this, "Error al editar el evento", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

}