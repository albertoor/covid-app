package com.albertoornelas.covid_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // variables
    private EditText nameText;
    private EditText emailText;
    private EditText passwordText;
    private Button btnRegister;
    private TextView loginLink;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Iniciando variables
        auth = FirebaseAuth.getInstance();
        nameText = findViewById(R.id.nameTxt);
        emailText = findViewById(R.id.emailTxt);
        passwordText = findViewById(R.id.passwordTxt);
        btnRegister = findViewById(R.id.btnRegister);
        db = FirebaseFirestore.getInstance();
        loginLink = findViewById(R.id.loginLink);

        // Listener
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Creamos el usuario
                UserModel user = new UserModel(
                        nameText.getText().toString(),
                        emailText.getText().toString(),
                        passwordText.getText().toString()
                );

                boolean everthingIsOkey = checkFields(user);

                if (everthingIsOkey) {
                    auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this.getApplicationContext(),
                                        "Error de registro " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Usuario Registrado", Toast.LENGTH_SHORT).show();
                                FirebaseUser userFb = auth.getCurrentUser();
                                saveUserCol(userFb.getUid(), user);
                                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Fallo de registro", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // funcion para hacer abrir login
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    // Funcion para comprobar campos
    public boolean checkFields(UserModel user) {
        boolean everthingIsOkey = false;
        if (user.getName().isEmpty()){
            nameText.setError("Ingrea tu nombre");
            nameText.requestFocus();
        } else if (user.getEmail().isEmpty()) {
            emailText.setError("Ingrea tu correo");
            emailText.requestFocus();
        } else if (user.getPassword().isEmpty()) {
            passwordText.setError("Ingrea tu contrasena");
            passwordText.requestFocus();
        } else if (user.getName().isEmpty() && user.getEmail().isEmpty() && user.getPassword().isEmpty()) {
            Toast.makeText(MainActivity.this, "Llena los campos", Toast.LENGTH_SHORT).show();
        } else if (!(user.getName().isEmpty() && user.getEmail().isEmpty() && user.getPassword().isEmpty())) {
            everthingIsOkey = true;
        }
        return everthingIsOkey;
    }

    // Crear usuario en la colleccion de usuarios y guardamos su usuario
    public void saveUserCol(String userUid, UserModel user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", user.getName());

        db.collection("users").document(userUid).set(userMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "Usuario gurdado", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Error de guardado", Toast.LENGTH_SHORT).show();
            }
        });
    }
}