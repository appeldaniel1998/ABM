package com.example.abm.Clients;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abm.BaseActivity;
import com.example.abm.R;
import com.example.abm.Utils.BackendHandling;
import com.example.abm.Utils.CallbackInterface;

public class ClientsMainActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private ClientsRecycleAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private Button addClientButton;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients_main);
        super.initMenuSideBar();

        // loading screen
        progressDialog = ProgressDialog.show(this, "Clients", "Loading, please wait....", true);


        addClientButton = findViewById(R.id.addClient);
        recyclerViewLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recyclerViewClients);
        recyclerView.hasFixedSize();
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        // start new activity where a new clients info is to be entered
        addClientButton.setOnClickListener(v -> ClientsMainActivity.this.startActivity(new Intent(ClientsMainActivity.this, CreateClientActivity.class)));

        // get clients from database
//        super.postRequest("getAllClients", new CallbackClass());
        super.postRequest("{\"query\":\"getAllClients\"}", new CallbackClass());
    }

    private class CallbackClass implements CallbackInterface {
        @Override
        public void onCall() { // initialize the UI of activity
            recyclerViewAdapter = new ClientsRecycleAdapter(BackendHandling.clients);
            recyclerView.setAdapter(recyclerViewAdapter);

            progressDialog.dismiss(); // disable loading screen

            //onclick of each item in the recycle view (client in the list)
            recyclerViewAdapter.setOnItemClickListener(position -> {
                Intent myIntent = new Intent(ClientsMainActivity.this, SingleClientViewActivity.class);
                myIntent.putExtra("clientUID", BackendHandling.clients.get(position).getUid()); //Optional parameters
                ClientsMainActivity.this.startActivity(myIntent);
            });
        }
    }
}