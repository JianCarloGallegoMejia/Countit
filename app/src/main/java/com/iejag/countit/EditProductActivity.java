package com.iejag.countit;

import androidx.appcompat.app.AppCompatActivity;
import io.realm.Realm;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditProductActivity extends AppCompatActivity {


    private EditText etName;
    private EditText etPrice;
    private EditText etDescription;
    private EditText etQuantity;
    private Button btnConfirm;
    private Realm realm;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        realm = Realm.getDefaultInstance();
        loadViews();
        id = getIntent().getStringExtra("productId");
        loadProduct(id);
    }

    private void loadProduct(String id) {
        Product product = realm.where(Product.class).equalTo("id", id).findFirst();
        if (product != null) {
            etName.setText(product.getName());
            etPrice.setText(product.getPrice());
            etDescription.setText(product.getDescription());
            etQuantity.setText(product.getQuantity());
        }
    }

    private void loadViews() {
        etName = findViewById(R.id.edtName);
        etPrice = findViewById(R.id.edtPrice);
        etDescription = findViewById(R.id.edtDescription);
        etQuantity = findViewById(R.id.edtQuantity);
        btnConfirm = findViewById(R.id.btn_save);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProduct();
            }
        });
    }

    private void updateProduct() {

        String name = etName.getText().toString();
        String description = etDescription.getText().toString();
        String quantity = etQuantity.getText().toString();
        String price = etPrice.getText().toString();
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
            int iPrice = Integer.parseInt(price);
            int iQuantity = Integer.parseInt(quantity);
            int semitotal = iPrice * iQuantity;
            String total = String.valueOf(semitotal);
            Product product = new Product(id, name, description, quantity, price, total);
            updateProductInDB(product);
            finish();
        }
    }

    private void updateProductInDB(Product product) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(product);
        realm.commitTransaction();
    }
}

