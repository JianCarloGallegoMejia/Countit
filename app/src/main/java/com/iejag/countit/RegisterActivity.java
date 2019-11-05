package com.iejag.countit;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    Button registrarse;
    EditText nombre;
    EditText apellido;
    EditText usuario;
    EditText contraseña;
    EditText confirmarcontraseña;
    EditText correo;
    String TAG = "RegisterActivity";
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        nombre = findViewById(R.id.etname);
        apellido = findViewById(R.id.etlastname);
        usuario = findViewById(R.id.etuser);
        contraseña = findViewById(R.id.etpassword);
        confirmarcontraseña = findViewById(R.id.etconfirmpassword);
        correo = findViewById(R.id.etemail);
        registrarse = findViewById(R.id.btnregistrarse);
        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebasecreateuser();
            }
        });
    }

    private void firebasecreateuser() {
        mAuth.createUserWithEmailAndPassword(correo.getText().toString(), contraseña.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            createUserInFirebase();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

     private void createUserInFirebase() {
        FirebaseUser user = mAuth.getCurrentUser();
        Users users = new Users(
                user.getUid(), nombre.getText().toString(), apellido.getText().toString(), usuario.getText().toString(), correo.getText().toString());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        myRef.child(user.getUid()).setValue(users);
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
    }


}

