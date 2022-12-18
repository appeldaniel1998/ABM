package com.example.abm.AppointmentType;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abm.R;

import java.util.ArrayList;


/**
 * This class is the adapter for the recycler view in the AppointmentType Activity
 * It is used to display the appointment types in the recycler view
 * It is a subclass of RecyclerView.Adapter
 * It is a subclass of RecyclerView.ViewHolder
 * It is a subclass of View.OnClickListener
 * We used this tutorial to create this class: https://www.youtube.com/watch?v=Nw9JF55LDzE
 */
public class AppointmentTypeRecycleAdapter extends RecyclerView.Adapter<AppointmentTypeRecycleAdapter.AppointmentTypeViewHolder> {

    private ArrayList<AppointmentType> appointmentTypes;
    private OnItemClickListener clickListener; //instance of interface below

    //Interface for onclick listener
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    //This is a static class that is a subclass of RecyclerView.ViewHolder
    public static class AppointmentTypeViewHolder extends RecyclerView.ViewHolder {
        public TextView name; // Parameters of the XML item
        public TextView duration;
        public TextView price;

        public AppointmentTypeViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.typeName);
            duration = itemView.findViewById(R.id.duration);
            price = itemView.findViewById(R.id.typePrice);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public AppointmentTypeRecycleAdapter(ArrayList<AppointmentType> appointmentTypes) {
        this.appointmentTypes = appointmentTypes;
    }


    @NonNull
    @Override
    public AppointmentTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_appointment_type_item, parent, false);
        return new AppointmentTypeViewHolder(view, clickListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AppointmentTypeViewHolder holder, int position) {
        //This method is called when the recycler view is created and it binds the view holder to the data
        AppointmentType currAppointmentType = this.appointmentTypes.get(position);
        holder.name.setText(currAppointmentType.getTypeName());
        holder.duration.setText(currAppointmentType.getDuration());
        holder.price.setText(currAppointmentType.getPrice());
    }


    @Override
    public int getItemCount() {
        return this.appointmentTypes.size();
    }
}
