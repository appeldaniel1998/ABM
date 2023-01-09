package com.example.abm.Products;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abm.BaseActivity;
import com.example.abm.Cart.CartMainActivity;
import com.example.abm.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class ProductsMainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Product> products = new ArrayList<>();
    private ArrayList<Product> new_products = new ArrayList<>();
    private RecycleAdapter adapter;
    private FloatingActionButton cartButton;
    private Button addProductButton;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_main);
        super.initMenuSideBar();
        initValues();
        //getProductFromDatabase();


        //loading
        ProgressDialog progressDialog;
        progressDialog = ProgressDialog.show(this, "Product", "Loading, please wait...", true);

        //build list of product for the recycle view. get all the product from the database
        ProductsDatabaseUtils.databaseGetProducts(progressDialog, this);
    }

    // init the buttons in the activity
    private void initValues() {
        cartButton = findViewById(R.id.flaotingCartButton);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductsMainActivity.this, CartMainActivity.class));
            }
        });

        //check if the user is a client or a manager and set visibility of the buttons accordingly
        ProductsDatabaseUtils.databaseGetClientOrManager(this);
        addProductButton = findViewById(R.id.addProduct);
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductsMainActivity.this, AddNewProduct.class));
            }
        });
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    // If the current user is manager, remove any button which are not needed for the manager
    public void SetIfManager() {
        View cart = findViewById(R.id.flaotingCartButton);
        cart.setVisibility(View.GONE);

    }

    // if the user is client, remove any button which are not needed for the client
    public void SetIfClient() {
        View addProductButton = findViewById(R.id.addProduct);
        addProductButton.setVisibility(View.GONE);
    }


    public void buildRecyclerView() {
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
                intent.putExtra("Product", (CharSequence) products.get(position).getColorName());
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