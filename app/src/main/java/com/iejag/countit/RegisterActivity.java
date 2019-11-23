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
    Button btnRegistrarse;
    EditText etNombre;
    EditText etApellido;
    EditText etUsuario;
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
        etNombre = findViewById(R.id.etname);
        etApellido = findViewById(R.id.etlastname);
        etUsuario = findViewById(R.id.etuser);
        contraseña = findViewById(R.id.etpassword);
        confirmarcontraseña = findViewById(R.id.etconfirmpassword);
        correo = findViewById(R.id.etemail);
        btnRegistrarse = findViewById(R.id.btnregistrarse);
        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
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
                            Log.d(TAG, "createUserWithEmail:success");
                            createUserInFirebase();

                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

     private void createUserInFirebase() {
        FirebaseUser user = mAuth.getCurrentUser();
        Users users = new Users(
                user.getUid(), etNombre.getText().toString(), etApellido.getText().toString(), etUsuario.getText().toString(), correo.getText().toString());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        myRef.child(user.getUid()).setValue(users);
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
    }


}

