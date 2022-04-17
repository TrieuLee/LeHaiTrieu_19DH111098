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

public class TopFoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Food> mFoods;
    private OnFoodItemClickListener mListener;
    private int TYPE_LAYOUT;

    public TopFoodAdapter(List<Food> foods, OnFoodItemClickListener listener){
        mFoods = foods;
        mListener = listener;

    }



    public class ViewHolderFood extends RecyclerView.ViewHolder{
        TextView tvFName, tvFRate, tvFPrice;
        ImageView imgFood;

        public ViewHolderFood(@NonNull View itemView) {
            super(itemView);
            tvFName = itemView.findViewById(R.id.tvFname);
            tvFRate = itemView.findViewById(R.id.tvFRate);
            tvFPrice = itemView.findViewById(R.id.tvfprice);
            imgFood = itemView.findViewById(R.id.imgFood);
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_food, parent, false);
        return new ViewHolderFood(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Food food = mFoods.get(position);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        ViewHolderFood viewHolderFood = (ViewHolderFood) holder;
        StorageReference foodRef = storageReference.child("food/"+ food.getImage());
        foodRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(viewHolderFood.imgFood);
            }
        });
        viewHolderFood.tvFName.setText(food.getName());
        viewHolderFood.tvFRate.setText("Rate:".concat(String.valueOf(food.getRate())));
        viewHolderFood.tvFPrice.setText(String.valueOf(food.getPrice()));
        viewHolderFood.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFoodItemClick(food);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFoods.size();
    }

    public interface OnFoodItemClickListener {
        void onFoodItemClick(Food food);
    }
}
