package com.iejag.countit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import io.realm.Realm;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

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
        loadToolbar();
        loadViews();
    }

    private void loadToolbar() {
        getSupportActionBar().setTitle(R.string.add_product);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadViews() {
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
                String name = etName.getText().toString().trim();
                String price = etPrice.getText().toString().trim();
                String description = etDescription.getText().toString().trim();
                String quantity = etQuantity.getText().toString().trim();

                boolean isSuccess = true;

                if (name.isEmpty()) {
                    etName.setError(getString(R.string.required));
                    isSuccess = false;
                }
                if (price.isEmpty()) {
                    etPrice.setError(getString(R.string.required));
                    isSuccess = false;
                }
                if (description.isEmpty()) {
                    etDescription.setError(getString(R.string.required));
                    isSuccess = false;
                }
                if (quantity.isEmpty()) {
                    etQuantity.setError(getString(R.string.required));
                    isSuccess = false;
                }
                if (isSuccess) {
                    String id = UUID.randomUUID().toString();
                    price = etPrice.getText().toString();
                    int iPrice = Integer.parseInt(price);
                    quantity = etQuantity.getText().toString();
                    int iQuantity = Integer.parseInt(quantity);
                    int semitotal = iPrice * iQuantity;
                    String total = String.valueOf(semitotal);
                    Product product = new Product(id, name, description, quantity, price, total);
                    confirmProduct(product);
                    finish();
                }
            }
        });
    }


    private void confirmProduct(Product product) {
        Toast.makeText(this, "Producto confirmado", Toast.LENGTH_SHORT).show();
        addProductInDB(product);
    }

    private void addProductInDB(Product product) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(product);
        realm.commitTransaction();
    }
}
