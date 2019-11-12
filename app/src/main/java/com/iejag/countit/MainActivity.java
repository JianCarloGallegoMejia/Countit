package com.iejag.countit;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ProductsAdapter.UsersAdapterListener{

    private RecyclerView recyclerView;
    private Realm realm;
    private ProductsAdapter productsAdapter;
    /*
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        realm = Realm.getDefaultInstance();
        loadProducts();
        /*mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("rutas");

         */

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

    }

    private void loadProducts() {
        List<Product> products = getProducts();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        productsAdapter = new ProductsAdapter(this, products,this);
        recyclerView.setAdapter(productsAdapter);
    }
    private List<Product> getProducts() {
        List<Product> products = realm.where(Product.class).findAll();
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
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void deleteUser(String id) {
        realm.beginTransaction();
        Product product = realm.where(Product.class).equalTo("id", id).findFirst();
        if (product != null) {
            product.deleteFromRealm();
        }
        realm.commitTransaction();

        updateProducts();

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
