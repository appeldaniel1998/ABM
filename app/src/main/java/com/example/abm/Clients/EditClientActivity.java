package com.example.abm.Clients;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.abm.BaseActivity;
import com.example.abm.R;
import com.example.abm.Utils.DatePicker;
import com.google.firebase.storage.StorageReference;

public class EditClientActivity extends BaseActivity {

    Client client;
    String clientUID;
    private TextView titleName;
    private TextView firstName;
    private TextView lastName;
    private TextView email;
    private TextView birthday;
    private TextView phoneNumber;
    private TextView address;
    private ImageView userIcon;
    private Button doneEditingButton;
    private Button deleteClientButton;
    private DatePickerDialog datePickerDialog;
    private StorageReference storageReference;


    // Same as single client view but with editable fields
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logreg_register);

        storageReference = super.getStorageReference();

        ProgressDialog progressDialog;
        progressDialog = ProgressDialog.show(this, "Edit Client", "Loading, please wait....", true);

        doneEditingButton = findViewById(R.id.registerButton);
        titleName = findViewById(R.id.title);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phoneNumber);
        address = findViewById(R.id.address);
        birthday = findViewById(R.id.birthdayDatePicker);
        userIcon = findViewById(R.id.personIcon);
        deleteClientButton = findViewById(R.id.deleteButton);
        findViewById(R.id.password).setVisibility(View.GONE);
        findViewById(R.id.retypePassword).setVisibility(View.GONE);

        userIcon.setOnClickListener(v -> {
            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGalleryIntent, super.IMG_REQUEST_CODE_GALLERY);
        });

        // Initiating date picks handling
        datePickerDialog = DatePicker.initDatePicker(birthday, this);

        Intent intent = getIntent();
        clientUID = intent.getStringExtra("clientUID");

        handleDeleteButton(clientUID); //TODO verify that not needed


        ProgressDialog imageProgressDialog = ProgressDialog.show(this, "Edit Client", "Loading, please wait....", true);
        StorageReference profilePicReference = storageReference.child("Clients").child(this.clientUID).child("profile.jpg");

        //Connecting with Firebase storage and retrieving image
        profilePicReference.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(EditClientActivity.this).load(uri).into(userIcon);
            imageProgressDialog.dismiss();
        }).addOnFailureListener(e -> {
            //failed probably due to the profile pic not existing (was not uploaded)
            imageProgressDialog.dismiss();
        });

        ClientsDatabaseUtils.databaseGetClient(progressDialog, clientUID, this);

    }

    public void onClientGetOnSuccess(ProgressDialog progressDialog, Client newClient) {
        this.client = newClient;
        initValuesOfLayout();
        progressDialog.dismiss();

        doneEditingButton.setOnClickListener(v -> {
            Client userToAdd = new Client(firstName.getText().toString(), lastName.getText().toString(), email.getText().toString(),
                    phoneNumber.getText().toString(), address.getText().toString(),
                    DatePicker.stringToInt(birthday.getText().toString()), clientUID); //creating a new user
            ClientsDatabaseUtils.addClientToFirebase(userToAdd, clientUID);

            Intent myIntent = new Intent(EditClientActivity.this, SingleClientViewActivity.class);
            myIntent.putExtra("clientUID", clientUID); //Optional parameters
            EditClientActivity.this.startActivity(myIntent);
            finish();
        });
    }

    @SuppressLint("SetTextI18n")
    private void initValuesOfLayout() {
        String fullName = client.getFirstName() + " " + this.client.getLastName();
        titleName.setText(fullName);
        firstName.setText(client.getFirstName());
        lastName.setText(client.getLastName());
        email.setText(client.getEmail());
        birthday.setText(DatePicker.intToString(client.getBirthdayDate()));
        phoneNumber.setText(client.getPhoneNumber());
        address.setText(client.getAddress());
        doneEditingButton.setText("Done");
    }

    /**
     * Onclick listener when the layout (the line) of "birthday"  is pressed.
     */
    public void onClickBirthdayLinearLayout(View view) {
        datePickerDialog.show();
    }

    /**
     * The function allows the user to choose an image from their phones gallery, which is then uploaded as their profile pic into Firebase Storage
     *
     * @param requestCode Request code with which the request is sent (determined by us)
     * @param resultCode  returns ok if data retrieved successfully
     * @param data        represents the URI of the retrieved image
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQUEST_CODE_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                Uri imageUri = data.getData();
                userIcon.setImageURI(imageUri); // set the image view to the image received from the client's gallery

                uploadImageToFirebase(imageUri); // upload to firebase storage
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        // upload image to firebase storage
        StorageReference fileRef = storageReference.child("Clients").child(this.clientUID).child("profile.jpg");
        fileRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> Toast.makeText(EditClientActivity.this, "Profile image uploaded successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(EditClientActivity.this, "Image upload failed!", Toast.LENGTH_SHORT).show());
    }

    private void handleDeleteButton(String clientUID) {
        deleteClientButton.setVisibility(View.VISIBLE);
        deleteClientButton.setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(EditClientActivity.this); //verification dialog to confirm deletion
            alert.setTitle("Delete");
            alert.setMessage("Are you sure you want to delete?");
            alert.setPositiveButton("Yes", (dialog, which) -> { //if yes was clicked
                if (!clientUID.equals(EditClientActivity.super.getCurrFirebaseAuth().getUid())) {
                    ClientsDatabaseUtils.databaseDeleteClient(clientUID);
                    Toast.makeText(EditClientActivity.this, "The client was deleted!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditClientActivity.this, ClientsMainActivity.class));
                    finish();
                    dialog.dismiss();
                } else { // if no was clicked
                    Toast.makeText(EditClientActivity.this, "You cannot delete yourself!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            alert.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            alert.show();
        });
    }
}