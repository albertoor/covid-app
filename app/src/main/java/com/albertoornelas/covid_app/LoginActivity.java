package com.albertoornelas.covid_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText emailText;
    private EditText passwordText;
    private FirebaseAuth auth;
    private Button btnLogin;
    private TextView registerLink;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Iniciando variables
        auth = FirebaseAuth.getInstance();
        emailText = findViewById(R.id.emailLoginText);
        passwordText = findViewById(R.id.passwordLoginTxt);
        btnLogin = findViewById(R.id.btnLogin);
        registerLink = findViewById(R.id.registerLink);
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(LoginActivity.this, "Usuario logeado", Toast.LENGTH_SHORT).show();
                    Intent I = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(I);
                } else {
                    Toast.makeText(LoginActivity.this, "Login to continue", Toast.LENGTH_SHORT).show();
                }
            }
        };

        // Listener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString();
                String pass = passwordText.getText().toString();
                boolean everthingIsOkey = checkFields(email, pass);

                if (everthingIsOkey) {
                    auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "No se pudo iniciar", Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            }
                        }
                    });
                }
            }
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    // Funcion para comprobar campos
    public boolean checkFields(String email, String password) {
        boolean everthingIsOkey = false;

         if (password.isEmpty()) {
            emailText.setError("Ingrea tu correo");
            emailText.requestFocus();
        } else if (email.isEmpty()) {
            passwordText.setError("Ingrea tu contrasena");
            passwordText.requestFocus();
        } else if ( email.isEmpty() && password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Llena los campos", Toast.LENGTH_SHORT).show();
        } else if (!(email.isEmpty() && password.isEmpty())) {
            everthingIsOkey = true;
        }
        return everthingIsOkey;
    }
}