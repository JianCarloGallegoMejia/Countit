package com.iejag.countit;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ProductsAdapter.ProductsAdapterListener {

    private RecyclerView recyclerView;
    private Realm realm;
    private ProductsAdapter productsAdapter;
    private ImageView car;
    private TextView emptycar;
    private TextView total;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.buy_list);
        recyclerView = findViewById(R.id.recycler_view);
        realm = Realm.getDefaultInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        car = findViewById(R.id.iv_car);
        emptycar = findViewById(R.id.tv_emptycar);
        total = findViewById(R.id.tv_total);

        loadProducts();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        if (user == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        updateProducts();
        List<Product> products = getProducts();
        if (products.isEmpty()) {
            car.setVisibility(View.VISIBLE);
            emptycar.setVisibility(View.VISIBLE);
        }
        if (!products.isEmpty()) {
            car.setVisibility(View.GONE);
            emptycar.setVisibility(View.GONE);
        }

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Se debe cerrar sesión", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

    private void loadProducts() {

        List<Product> products = getProducts();

        if (products.isEmpty()) {
            car.setVisibility(View.VISIBLE);
            emptycar.setVisibility(View.VISIBLE);
        }
        if (!products.isEmpty()) {
            car.setVisibility(View.GONE);
            emptycar.setVisibility(View.GONE);
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        productsAdapter = new ProductsAdapter(this, products, this);
        recyclerView.setAdapter(productsAdapter);
    }

    private List<Product> getProducts() {
        List<Product> products = realm.where(Product.class).findAll();
        DecimalFormat formateador = new DecimalFormat("###,###.##");
        int acum = 0;
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            if (product != null) {
                String cantidad = product.getQuantity();
                String precio = product.getPrice();
                int iCantidad = Integer.parseInt(cantidad);
                int iPrecio = Integer.parseInt(precio);
                acum = acum + (iCantidad * iPrecio);
            }
        }


        total.setText("$" + formateador.format(acum));
        return products;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.action_settings) {
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void deleteUser(final String id) {
        realm.beginTransaction();
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(R.string.alert_dialog);
        builder.setMessage(R.string.alert_message);
        builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Product product = realm.where(Product.class).equalTo("id", id).findFirst();
                if (product != null) {
                    product.deleteFromRealm();
                }
                realm.commitTransaction();

                updateProducts();
                List<Product> products = getProducts();
                if (products.isEmpty()) {
                    car.setVisibility(View.VISIBLE);
                    emptycar.setVisibility(View.VISIBLE);
                }
                if (!products.isEmpty()) {
                    car.setVisibility(View.GONE);
                    emptycar.setVisibility(View.GONE);
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


    }

    @Override
    public void editUser(String id) {
        Intent intent = new Intent(this, EditProductActivity.class);
        intent.putExtra("productId", id);
        startActivity(intent);

    }

    private void updateProducts() {
        productsAdapter.updateProducts(getProducts());
    }


}
