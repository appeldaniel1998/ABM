package com.example.abm.Cart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abm.Products.RecycleAdapter;
import com.example.abm.R;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private ArrayList<Cart> cart;
    private OnItemClickListener mListener;

    //Card click listener
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onAddClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {

        mListener = listener;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        //attributes for single item in the recycler view
        public ImageView productimage;
        public TextView Productcolor;
        public TextView quantity;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            productimage = itemView.findViewById(R.id.productImage);
            Productcolor = itemView.findViewById(R.id.ProductColor);
            quantity = itemView.findViewById(R.id.PolishQuantity);
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

    //This is the constructor for the CartAdapter class
    public CartAdapter(ArrayList<Cart> cart) {
        this.cart = cart;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //init the recycler view cart layout with the single cart card view layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_product_cart_cardview, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cart cartItem = cart.get(position);
        holder.productimage.setImageResource(cartItem.getProductImage());
        holder.Productcolor.setText(cartItem.getProductColor());
        //cast the quantity to string
        holder.quantity.setText("Quantity: "+ String.valueOf(cartItem.getQuantity()));

    }

    @Override
    public int getItemCount() {
        return cart.size();
    }

    }
