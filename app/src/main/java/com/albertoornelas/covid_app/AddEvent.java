package com.albertoornelas.covid_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class AddEvent extends AppCompatActivity {

    private TextView nameTxt;
    private TextView placeTxt;
    private TextView timestampTxt;
    private TextView aforoNum;
    private Button btnAddEvent;
    private FirebaseFirestore db;

    private static final String TAG = "Add Event";
    public static final String DOCUMENT_ID = "document_id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        nameTxt = findViewById(R.id.nameTxtAdd);
        placeTxt = findViewById(R.id.placeTxtAdd);
        timestampTxt = findViewById(R.id.placeTxtAdd);
        aforoNum = findViewById(R.id.aforoTxtAdd);
        btnAddEvent = (Button) findViewById(R.id.btnAddEvent);
        db = FirebaseFirestore.getInstance();

        btnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Event event = new Event(nameTxt.getText().toString(),
                        placeTxt.getText().toString(),
                        timestampTxt.getText().toString(),
                        Integer.parseInt(aforoNum.getText().toString()));

                db.collection("events").document().set(event).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Evento agreado");
                        nameTxt.setText("");
                        placeTxt.setText("");
                        timestampTxt.setText("");
                        aforoNum.setText("");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error al agregar el evento", e);
                    }
                });
            }
        });

    }
}