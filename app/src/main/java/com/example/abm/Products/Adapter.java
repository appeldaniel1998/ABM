package com.example.abm.Products;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abm.R;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    Context context;
    ArrayList<Product> products;

    public Adapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.single_polish_image_view,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.color_name.setText(product.getColor_name());
        holder.image.setImageResource(product.getImage());




    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView color_name;
        ImageView image;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            color_name = itemView.findViewById(R.id.color_textView);
            image = itemView.findViewById(R.id.color_image);
        }
    }
}