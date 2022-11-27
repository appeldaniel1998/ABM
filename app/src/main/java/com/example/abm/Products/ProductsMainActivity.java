package com.example.abm.Products;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abm.Appointments.AppointmentsMainActivity;
import com.example.abm.BaseActivity;
import com.example.abm.Cart.CartMainActivity;
import com.example.abm.Clients.ClientsMainActivity;
import com.example.abm.HistoryAnalytics.AnalyticsMainActivity;
import com.example.abm.LoginAndRegistration.LogReg_LoginOrRegisterActivity;
import com.example.abm.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ProductsMainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private RecycleAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Product> products = new ArrayList<>();



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_main);
        super.initMenuSideBar();
        createProductsList();
        buildRecyclerView();


    }

    private void createProductsList() {
        //creata a list of products for the recycler view
        products.add(new Product("Red",R.drawable.canni1));
        products.add(new Product("Blue",R.drawable.canni2));
        products.add(new Product("Green",R.drawable.canni3));
        products.add(new Product("Yellow",R.drawable.canni4));
        products.add(new Product("Purple",R.drawable.canni5));
        products.add(new Product("Orange",R.drawable.canni6));

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
//                startActivity(new Intent(ProductsMainActivity.this, ProductsClickcardActivity.class));
//                adapter.notifyItemChanged(position);

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