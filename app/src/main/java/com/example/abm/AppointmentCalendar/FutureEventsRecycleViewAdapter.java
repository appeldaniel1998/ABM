package com.example.abm.AppointmentCalendar;//package com.example.abm.AppointmentCalendar;
//
//public class FutureEventsRecycleViewAdapter {
//}

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abm.AppointmentCalendar.Event;
import com.example.abm.AppointmentType.AppointmentType;
import com.example.abm.Clients.ClientsRecycleAdapter;
import com.example.abm.R;
import com.example.abm.Utils.DatePicker;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class  is the adapter for the recycler view in the ClientsMainActivity
 * It is used to display the clients in the recycler view
 * It is a subclass of RecyclerView.Adapter
 * It is a subclass of RecyclerView.ViewHolder
 * It is a subclass of View.OnClickListener
 * We used this tutorial to create this class: https://www.youtube.com/watch?v=Nw9JF55LDzE
 */
public class FutureEventsRecycleViewAdapter extends RecyclerView.Adapter<FutureEventsRecycleViewAdapter.ClientActivityViewHolder> {

    private ArrayList<Event> clientFutureEvents;
    private ClientsRecycleAdapter.OnItemClickListener clickListener; //instance of interface below


    //Interface for onclick listener
    public interface OnItemClickListener {
        void onItemClick(int position);
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
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

    public FutureEventsRecycleViewAdapter(ArrayList<Event> clientFutureEvents) {
        this.clientFutureEvents = clientFutureEvents;
    }

    @NonNull
    @Override
    //This method is called when the recycler view is created and it creates the view holder
    public FutureEventsRecycleViewAdapter.ClientActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_client_activity_item, parent, false);
        FutureEventsRecycleViewAdapter.ClientActivityViewHolder clientActivityViewHolder = new FutureEventsRecycleViewAdapter.ClientActivityViewHolder(view, clickListener);
        return clientActivityViewHolder;
    }

    @Override
    //This method is called when the recycler view is created and it binds the view holder to the data
    public void onBindViewHolder(@NonNull FutureEventsRecycleViewAdapter.ClientActivityViewHolder holder, int position) {
        Event currEvent = this.clientFutureEvents.get(position);
        holder.activityName.setText(currEvent.getActivityName());
        holder.activityDate.setText(DatePicker.intToString(currEvent.getDate()));
        holder.clientName.setText(currEvent.getClientName());
        holder.activityPrice.setText(currEvent.getPriceToFutureEvents());

    }

    @Override
    public int getItemCount() {
        return this.clientFutureEvents.size();
    }


}