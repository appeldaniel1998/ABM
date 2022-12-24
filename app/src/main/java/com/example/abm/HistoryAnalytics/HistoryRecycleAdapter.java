package com.example.abm.HistoryAnalytics;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abm.Clients.ClientsRecycleAdapter;
import com.example.abm.R;
import com.example.abm.Utils.DatePicker;

import java.util.ArrayList;

/**
 * This class  is the adapter for the recycler view in the ClientsMainActivity
 * It is used to display the clients in the recycler view
 * It is a subclass of RecyclerView.Adapter
 * It is a subclass of RecyclerView.ViewHolder
 * It is a subclass of View.OnClickListener
 * We used this tutorial to create this class: https://www.youtube.com/watch?v=Nw9JF55LDzE
 */
public class HistoryRecycleAdapter extends RecyclerView.Adapter<HistoryRecycleAdapter.ClientActivityViewHolder>{

    private ArrayList<ClientActivities> clientActivities;
    private ClientsRecycleAdapter.OnItemClickListener clickListener; //instance of interface below

    //Interface for onclick listener
    public interface OnItemClickListener {
        void onItemClick (int position);
    }

    public void setOnItemClickListener(ClientsRecycleAdapter.OnItemClickListener listener) {
        this.clickListener = listener;
    }

    //This is a static class that is a subclass of RecyclerView.ViewHolder
    public static class ClientActivityViewHolder extends RecyclerView.ViewHolder {
        public TextView activityName; // Parameters of the XML item
        public TextView activityDate;
        public TextView activityPrice;
        public TextView clientName;

        public ClientActivityViewHolder(@NonNull View itemView, ClientsRecycleAdapter.OnItemClickListener listener) {
            super(itemView);
            activityName = itemView.findViewById(R.id.activityName);
            activityDate = itemView.findViewById(R.id.activityDate);
            activityPrice = itemView.findViewById(R.id.activityPrice);
            clientName = itemView.findViewById(R.id.clientName);

            itemView.setOnClickListener(v -> {
                if (listener != null)
                {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                    {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

    public HistoryRecycleAdapter(ArrayList<ClientActivities> clientActivities) {
        this.clientActivities = clientActivities;
    }

    @NonNull
    @Override
    //This method is called when the recycler view is created and it creates the view holder
    public HistoryRecycleAdapter.ClientActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_client_activity_item, parent, false);
        HistoryRecycleAdapter.ClientActivityViewHolder clientActivityViewHolder = new HistoryRecycleAdapter.ClientActivityViewHolder(view, clickListener);
        return clientActivityViewHolder;
    }

    @Override
    //This method is called when the recycler view is created and it binds the view holder to the data
    public void onBindViewHolder(@NonNull HistoryRecycleAdapter.ClientActivityViewHolder holder, int position) {
        ClientActivities currActivity = this.clientActivities.get(position);
        holder.activityName.setText(currActivity.getActivityName());
        holder.activityDate.setText(DatePicker.intToString(currActivity.getDate()));
        holder.activityPrice.setText((Math.round(Double.parseDouble(currActivity.getPrice()) * 100) / 100.0) + "");
        holder.clientName.setText(currActivity.getClientName());
    }

    @Override
    public int getItemCount() {
        return this.clientActivities.size();
    }
}
