package com.example.abm.Products;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abm.BaseActivity;
import com.example.abm.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class ProductsMainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private RecycleAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Product> products = new ArrayList<>();
    private FloatingActionButton cartButton;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_main);
        super.initMenuSideBar();
        createProductsList();
        buildRecyclerView();
        cartButton = findViewById(R.id.flaotingCartButton);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductsMainActivity.this, ProductCartActivity.class));

            }

        });


    }

    private void createProductsList() {
        //creata a list of products for the recycler view
        products.add(new Product("Red", R.drawable.canni1));
        products.add(new Product("Blue", R.drawable.canni2));
        products.add(new Product("Green", R.drawable.canni3));
        products.add(new Product("Yellow", R.drawable.canni4));
        products.add(new Product("Purple", R.drawable.canni5));
        products.add(new Product("Orange", R.drawable.canni6));

        //add all the products to the database
        for (Product product : products) {
            super.getCurrDatabase().collection("Products").document(product.getColor_name()).set(product);
        }
    }

    private void buildRecyclerView() {
        //create the recycler view and set the adapter and layout manager for it
        recyclerView = findViewById(R.id.recycleView); //recycleView is the id of the recycleView in the xml file
        recyclerView.setHasFixedSize(true); //recycler view will not change in size
        layoutManager = new LinearLayoutManager(this); //layout manager is the way the items are displayed
        adapter = new RecycleAdapter(products); //adapter is the way the data is displayed in the recycler view
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //when an item is clicked, open the product details activity
                products.get(position);
                Intent intent = new Intent(ProductsMainActivity.this, ProductsClickcardActivity.class);

                intent.putExtra("Product", (CharSequence) products.get(position).getColor_name());
                startActivity(intent);

            }

            @Override
            public void onAddClick(int position) {
                //when the add button is clicked, add the product to the cart
                startActivity(new Intent(ProductsMainActivity.this, ProductsClickcardActivity.class));
                //Todo: add the product to the FabAcctionButton
            }
        });
    }


}