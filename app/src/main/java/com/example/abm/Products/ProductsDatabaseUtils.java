package com.example.abm.Products;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.TextView;

import com.example.abm.Clients.Client;
import com.example.abm.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProductsDatabaseUtils {

    private static FirebaseAuth auth = FirebaseAuth.getInstance();
    private static final FirebaseFirestore database = FirebaseFirestore.getInstance();

    public static void databaseGetProducts(ProgressDialog progressDialog, ProductsMainActivity productsMainActivity) {
        ArrayList<Product> new_products = new ArrayList<>();
        database.collection("Products").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Product product = documentSnapshot.toObject(Product.class);
                    new_products.add(product);
                }
                progressDialog.dismiss();
                productsMainActivity.setProducts(new_products);
                productsMainActivity.buildRecyclerView();


            }
        });

    }

    public static void databaseGetClientOrManager(ProductsMainActivity productsMainActivity) {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String UserUid = user.getUid();
            database.collection("Clients").document(UserUid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        Client client = documentSnapshot.toObject(Client.class);
                        if (client != null) {
                            //check if the current user is a client or manager
                            if (client.getManager()) {
                                // remove any button which are not needed for the manager
                                productsMainActivity.SetIfManager();

                            } else {
                                // remove any button which are not needed for the client
                                productsMainActivity.SetIfClient();

                            }
                        }
                    });
        }

    }
}
