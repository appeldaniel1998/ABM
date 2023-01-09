package com.example.abm.Products;

import android.app.ProgressDialog;

import com.example.abm.Clients.Client;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProductsClickCardDatabaseUtils {

    private static FirebaseAuth auth = FirebaseAuth.getInstance();
    private static final FirebaseFirestore database = FirebaseFirestore.getInstance();



    public static void databaseSetSingleProduct(String productPositon, ProgressDialog progressDialog, ProductsClickcardActivity productsClickcardActivity){

        database.collection("Products").document(productPositon)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Product product = new Product();
                        product = documentSnapshot.toObject(Product.class);
                        productsClickcardActivity.SetProduct(product);
                        productsClickcardActivity.initValuesOfLayout();
                        progressDialog.dismiss();
                    }
                });


    }


    public static void databaseGetClientOrManager(ProductsClickcardActivity productsClickcardActivity) {
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
                                productsClickcardActivity.SetIfManager();

                            } else {
                                // remove any button which are not needed for the client
                                productsClickcardActivity.SetIfClient();

                            }
                        }
                    });
        }

    }


}
