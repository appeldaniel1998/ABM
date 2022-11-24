package com.example.abm.Clients;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abm.BaseActivity;
import com.example.abm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class ClientsMainActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private ClientsRecycleAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private ArrayList<ClientItemRecycleView> clients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients_main);
        super.initMenuSideBar();

        ProgressDialog progressDialog;
        progressDialog = ProgressDialog.show(this, "Clients", "Loading, please wait...", true);

        recyclerViewLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recyclerViewClients);
        recyclerView.hasFixedSize();
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        clients = new ArrayList<>();

        super.getCurrDatabase().collection("Clients")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                String name = data.get("firstName") + " " + data.get("lastName");
                                String email = (String) data.get("email");
                                clients.add(new ClientItemRecycleView(name, email));
                            }
                            recyclerViewAdapter = new ClientsRecycleAdapter(clients);
                            recyclerView.setAdapter(recyclerViewAdapter);
                            progressDialog.dismiss();
                        }
                    }
                });

        recyclerViewAdapter.setOnItemClickListener(new ClientsRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });
    }
}