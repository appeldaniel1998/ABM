package com.example.abm.Appointments;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abm.R;

class CalenderViewHolder extends RecyclerView.ViewHolder{
    public final TextView dayOfMonth;


    //Constructor
    public CalenderViewHolder(@NonNull View itemView, CalenderAdapter.OnItemListener onItemListener) {
        super(itemView);
        dayOfMonth=itemView.findViewById(R.id.cellDayText);
//        this.onItemListener = onItemListener;
//        itemView.setOnClickListener(this);
    }

//    @Override
//    public void onClick(View v) {
//        onItemListener.onItemClick(getAdapterPosition(),(String) dayOfMonth.getText());
//    }
}
