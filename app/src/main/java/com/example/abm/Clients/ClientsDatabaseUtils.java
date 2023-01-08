package com.example.abm.Clients;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

public class ClientsDatabaseUtils {
    private static final FirebaseFirestore database = FirebaseFirestore.getInstance();

    public static void uploadImageToFirebase(StorageReference storageReference, String clientUID, Uri profilePicUri, Context context) {
        // upload image to firebase storage
        StorageReference fileRef = storageReference.child("Clients").child(clientUID).child("profile.jpg");
        fileRef.putFile(profilePicUri)
                .addOnSuccessListener(taskSnapshot -> Toast.makeText(context, "Profile image uploaded successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Image upload failed!", Toast.LENGTH_SHORT).show());
    }

    public static void addClientToFirebase(Client user, String uid) {
        database.collection("Clients").document(uid).set(user); //adding user data to database
    }

    public static void databaseGetClient(ProgressDialog progressDialog, String clientUID, EditClientActivity editClientActivity) {
        database.collection("Clients").document(clientUID)
                .get()
                .addOnSuccessListener(documentSnapshot -> { // If client info retrieved successfully from the DB
                    Client client = documentSnapshot.toObject(Client.class);
                    editClientActivity.onClientGetOnSuccess(progressDialog, client);
                });
    }

    public static void databaseDeleteClient(String clientUID) {
        //need to delete all documents where the user exists (from every collection)
        database.collection("Clients").document(clientUID).delete();
        database.collection("Appointments").document(clientUID).delete();
        database.collection("Orders").document(clientUID).delete();
        database.collection("Cart").document(clientUID).delete();
    }
}
