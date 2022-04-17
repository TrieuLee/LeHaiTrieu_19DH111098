package com.example.lehaitrieu_19dh111098;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private List<Restaurant> mRes;
    private OnOrderItemClickListener mListener;

    public OrderAdapter(List<Restaurant> restaurants, OnOrderItemClickListener listener){
        mRes = restaurants;
        mListener =listener;
    }


    public class ViewHolderOrder extends RecyclerView.ViewHolder{
        TextView tvOrName, tvOrAddress, tvOrPrice, tvOrResKey;
        ImageView imgOImage;
        public ViewHolderOrder(@NonNull View itemView) {
            super(itemView);
            tvOrName = itemView.findViewById(R.id.tvOName);
            tvOrAddress = itemView.findViewById(R.id.tvOAddress);
            tvOrPrice = itemView.findViewById(R.id.tvOPrice);
            imgOImage = itemView.findViewById(R.id.ivOImage);
            tvOrResKey =itemView.findViewById(R.id.tvOReskey);
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_order, parent, false);
        return new ViewHolderOrder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Restaurant restaurant =mRes.get(position);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        ViewHolderOrder viewHolderOrder = (ViewHolderOrder) holder;
        StorageReference resRef = storageReference.child("restaurants/"+restaurant.getLogo());
        resRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(viewHolderOrder.imgOImage);

            }
        });
        viewHolderOrder.tvOrName.setText(restaurant.name);
        viewHolderOrder.tvOrAddress.setText(restaurant.address);
        viewHolderOrder.tvOrResKey.setText(restaurant.resKey);

        viewHolderOrder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onOrderItemClick(restaurant);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRes.size();
    }

    public interface OnOrderItemClickListener {
        void onOrderItemClick(Restaurant orderFinished);
    }
}
