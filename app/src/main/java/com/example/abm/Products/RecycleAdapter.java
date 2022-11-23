package com.example.abm.Products;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abm.R;

import java.util.ArrayList;


/**
    This class is the adapter for the recycler view in the ProductsMainActivity
    It is used to display the products in the recycler view
    It is a subclass of RecyclerView.Adapter
    It is a subclass of RecyclerView.ViewHolder
    It is a subclass of View.OnClickListener
    We use this tutorial to create this class: https://www.youtube.com/watch?v=Nw9JF55LDzE

 */
public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {
    private ArrayList<Product> products;

    //This is a static class that is a subclass of RecyclerView.ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.Polish1);
            textView = itemView.findViewById(R.id.Polish1Color);
        }
    }

    public RecycleAdapter(ArrayList<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    //This method is called when the recycler view is created and it creates the view holder
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_products_polish_cardview, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    //This method is called when the recycler view is created and it binds the view holder to the data
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.imageView.setImageResource(product.getImage());
        holder.textView.setText(product.getColor_name());

    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
