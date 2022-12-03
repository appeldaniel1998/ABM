package com.example.abm.Products;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abm.BaseActivity;
import com.example.abm.Clients.Client;
import com.example.abm.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProductsMainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private RecycleAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Product> products = new ArrayList<>();
    private ArrayList<Product> product1 = new ArrayList<>();
    private FloatingActionButton cartButton;
    private Button addProductButton;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_main);
        super.initMenuSideBar();
        //createProductsList();
        //buildRecyclerView();

       creatProductListFromDatabase();
//        ProgressDialog progressDialog;
//        progressDialog = ProgressDialog.show(this, "Products", "Loading, please wait...", true);
        cartButton = findViewById(R.id.flaotingCartButton);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductsMainActivity.this, ProductCartActivity.class));

            }

        });

        checkClientorManger();
        addProductButton = findViewById(R.id.addProduct);
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductsMainActivity.this, AddNewProduct.class));
            }
        });

    }

    //get all the data from the database from the current from Products collection and add it to the arraylist
    private void creatProductListFromDatabase() {
        //get all the product for the database
        getCurrDatabase().collection("Products").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Product product = documentSnapshot.toObject(Product.class);
                    product1.add(product);
                }
                System.out.println("-------------------------------------");
                for (Product p : product1) {
                    System.out.println(p);
                }
                System.out.println("-------------------------------------");
                buildRecyclerView();
            }
        });

    }

    private void checkClientorManger() {
        FirebaseUser user = super.getCurrFirebaseAuth().getCurrentUser();
        if (user != null) {
            String UserUid = user.getUid();
            super.getCurrDatabase().collection("Clients").document(UserUid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        Client client = documentSnapshot.toObject(Client.class);
                        TextView name = findViewById(R.id.nameMenuHeader);
                        if (client != null) {
                            //check if the current user is a client or manager
                            if (client.getManager()) {
                                // remove any button which are not needed for the manager
                                View cart = findViewById(R.id.flaotingCartButton);
                                cart.setVisibility(View.GONE);

                            } else {
                                // remove any button which are not needed for the client
                                View addProductButton = findViewById(R.id.addProduct);
                                addProductButton.setVisibility(View.GONE);
                            }
                        }
                    });
        }

    }

    private void createProductsList() {
        //creata a list of products for the recycler view
        products.add(new Product("Red", R.drawable.canni1, "15", "15"));
        products.add(new Product("Blue", R.drawable.canni2, "15", "15"));
        products.add(new Product("Green", R.drawable.canni3, "15", "15"));
        products.add(new Product("Yellow", R.drawable.canni4, "15", "15"));
        products.add(new Product("Purple", R.drawable.canni5, "15", "15"));
        products.add(new Product("Orange", R.drawable.canni6, "15", "15"));

        //add all the products to the database
        for (Product product : products) {
            super.getCurrDatabase().collection("Products").document(product.getColorName()).set(product);
        }
    }

    private void buildRecyclerView() {
        //create the recycler view and set the adapter and layout manager for it
        recyclerView = findViewById(R.id.recycleView); //recycleView is the id of the recycleView in the xml file
        recyclerView.setHasFixedSize(true); //recycler view will not change in size
        layoutManager = new LinearLayoutManager(this); //layout manager is the way the items are displayed
        adapter = new RecycleAdapter(product1); //adapter is the way the data is displayed in the recycler view
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //when an item is clicked, open the product details activity
                product1.get(position);
                Intent intent = new Intent(ProductsMainActivity.this, ProductsClickcardActivity.class);

                intent.putExtra("Product", (CharSequence) product1.get(position).getColorName());
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