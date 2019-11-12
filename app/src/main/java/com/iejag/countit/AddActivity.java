package com.iejag.countit;

import androidx.appcompat.app.AppCompatActivity;
import io.realm.Realm;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {

    private Product product;
    private TextView tvName;
    private TextView tvPrice;
    private EditText etName;
    private EditText etPrice;
    private EditText etDescription;
    private EditText etQuantity;
    private Button btnConfirm;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        realm = Realm.getDefaultInstance();
        tvName = findViewById(R.id.tv_nombre);
        tvPrice = findViewById(R.id.tv_price);
        etName = findViewById(R.id.edtName);
        etPrice = findViewById(R.id.edtPrice);
        etDescription = findViewById(R.id.edtDescription);
        etQuantity = findViewById(R.id.edtQuantity);
        btnConfirm = findViewById(R.id.btn_save);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmUser();
            }
        });

    }

    private void confirmUser() {
        Toast.makeText(this, "Producto confirmado", Toast.LENGTH_SHORT).show();
        addUserInDB(product);
    }

    private void addUserInDB(Product product) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(product);
        realm.commitTransaction();
    }
}
