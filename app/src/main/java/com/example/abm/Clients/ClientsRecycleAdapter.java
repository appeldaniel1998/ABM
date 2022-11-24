package com.example.abm.Clients;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abm.R;

import java.util.ArrayList;


/**
 * This class is the adapter for the recycler view in the ProductsMainActivity
 * It is used to display the products in the recycler view
 * It is a subclass of RecyclerView.Adapter
 * It is a subclass of RecyclerView.ViewHolder
 * It is a subclass of View.OnClickListener
 * We used this tutorial to create this class: https://www.youtube.com/watch?v=Nw9JF55LDzE
 */
public class ClientsRecycleAdapter extends RecyclerView.Adapter<ClientsRecycleAdapter.ClientViewHolder> {

    private ArrayList<ClientItemRecycleView> clients;
    private OnItemClickListener clickListener; //instance of interface below

    //Interface for onclick listener
    public interface OnItemClickListener {
        void onItemClick (int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    //This is a static class that is a subclass of RecyclerView.ViewHolder
    public static class ClientViewHolder extends RecyclerView.ViewHolder {
        public TextView name; // Parameters of the XML item
        public TextView email;

        public ClientViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                    {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public ClientsRecycleAdapter(ArrayList<ClientItemRecycleView> clients) {
        this.clients = clients;
    }

    @NonNull
    @Override
    //This method is called when the recycler view is created and it creates the view holder
    public ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_clients_item, parent, false);
        ClientViewHolder clientViewHolder = new ClientViewHolder(view, clickListener);
        return clientViewHolder;
    }

    @Override
    //This method is called when the recycler view is created and it binds the view holder to the data
    public void onBindViewHolder(@NonNull ClientViewHolder holder, int position) {
        ClientItemRecycleView currClient = this.clients.get(position);
        holder.name.setText(currClient.getName());
        holder.email.setText(currClient.getEmail());
    }

    @Override
    public int getItemCount() {
        return this.clients.size();
    }
}
