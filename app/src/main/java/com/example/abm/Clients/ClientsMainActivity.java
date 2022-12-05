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
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

public class ClientsMainActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private ClientsRecycleAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private Button addClientButton;
    private ArrayList<ClientItemRecycleView> clients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients_main);
        super.initMenuSideBar();

        ProgressDialog progressDialog;
        progressDialog = ProgressDialog.show(this, "Clients", "Loading, please wait....", true);


        addClientButton = findViewById(R.id.addClient);
        recyclerViewLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recyclerViewClients);
        recyclerView.hasFixedSize();
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        clients = new ArrayList<>();

        addClientButton.setOnClickListener(v -> ClientsMainActivity.this.startActivity(new Intent(ClientsMainActivity.this, CreateClientActivity.class)));

        super.getCurrDatabase().collection("Clients")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> data = document.getData();
                            String name = data.get("firstName") + " " + data.get("lastName");
                            String email = (String) data.get("email");
                            String uid = (String) data.get("uid");
                            clients.add(new ClientItemRecycleView(name, email, uid));
                        }
                        recyclerViewAdapter = new ClientsRecycleAdapter(clients);
                        recyclerView.setAdapter(recyclerViewAdapter);
                        progressDialog.dismiss();

                        recyclerViewAdapter.setOnItemClickListener(position -> {
                            Intent myIntent = new Intent(ClientsMainActivity.this, SingleClientViewActivity.class);
                            myIntent.putExtra("clientUID", clients.get(position).getUID()); //Optional parameters
                            ClientsMainActivity.this.startActivity(myIntent);
                        });
                    }
                });
    }
}