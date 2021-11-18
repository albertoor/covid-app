package com.albertoornelas.covid_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private Button bntLogout;
    private TextView currentUserTxt;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Iniciando variables
        bntLogout = (Button) findViewById(R.id.btnLogout);
        auth = FirebaseAuth.getInstance();
        currentUserTxt = findViewById(R.id.currentUseTxt);

        currentUserTxt.setText("Hola, " + auth.getCurrentUser().getEmail());

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
}