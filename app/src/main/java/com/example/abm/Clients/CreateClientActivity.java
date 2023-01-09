package com.example.abm.Clients;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.abm.BaseActivity;
import com.example.abm.R;
import com.example.abm.Utils.DatePicker;

public class CreateClientActivity extends BaseActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText phoneNumber;
    private EditText address;
    private TextView birthdayDate;
    private TextView title;

    private DatePickerDialog datePickerDialog;

    private Button addClient;
    private Uri profilePicUri;
    private ImageView userIcon;
    private boolean profilePicWasChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logreg_register);
        super.initMenuSideBar();

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phoneNumber);
        address = findViewById(R.id.address);
        birthdayDate = findViewById(R.id.birthdayDatePicker);
        addClient = findViewById(R.id.registerButton);
        addClient.setText("Done");
        title = findViewById(R.id.title);
        userIcon = findViewById(R.id.personIcon);
        title.setText("Add new client");

        findViewById(R.id.password).setVisibility(View.GONE); //remove password inputs from the layout.
        findViewById(R.id.retypePassword).setVisibility(View.GONE);

        // Initiating date picks handling
        datePickerDialog = DatePicker.initDatePicker(birthdayDate, this);
        birthdayDate.setText(DatePicker.getTodayDate()); // Set initial date to today's date

        userIcon.setOnClickListener(v -> {
            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGalleryIntent, super.IMG_REQUEST_CODE_GALLERY);
        });

        addClient.setOnClickListener(v -> {
            //Converting fields to text
            String textFirstName = firstName.getText().toString();
            String textLastName = lastName.getText().toString();
            String textEmail = email.getText().toString();
            String textPhoneNumber = phoneNumber.getText().toString();
            String textAddress = address.getText().toString();
            int textBirthdayDate = DatePicker.stringToInt(birthdayDate.getText().toString());

            if (TextUtils.isEmpty(textFirstName) || TextUtils.isEmpty(textEmail)) {
                Toast.makeText(CreateClientActivity.this, "Empty email or first name!", Toast.LENGTH_SHORT).show();
            } else {
                //if the manger adds a client, we create an id.
                String uid = String.valueOf(java.util.UUID.randomUUID()); //Create a random UID for the new client
                Client userToAdd = new Client(textFirstName, textLastName, textEmail, textPhoneNumber, textAddress, textBirthdayDate, uid); //creating a new user
                ClientsDatabaseUtils.addClientToFirebase(userToAdd, uid); //adding user data to database
                if (profilePicWasChanged) {
                    ClientsDatabaseUtils.uploadImageToFirebase(super.getStorageReference(), uid, profilePicUri, CreateClientActivity.this);
                }

                Toast.makeText(CreateClientActivity.this, "Client added successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CreateClientActivity.this, ClientsMainActivity.class));
                finish();
            }
        });
    }

    /**
     * Onclick  listener when the layout (the line) of "birthday"  is pressed.
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
                this.profilePicUri = data.getData();
                userIcon.setImageURI(this.profilePicUri); // set the image view to the image received from the client's gallery

//                uploadImageToFirebase(imageUri); // upload to firebase storage
                this.profilePicWasChanged = true;
            }
        }
    }
}