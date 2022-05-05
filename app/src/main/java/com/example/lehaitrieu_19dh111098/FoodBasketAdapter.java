package com.example.lehaitrieu_19dh111098;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FoodBasketAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<FoodBasket> mFoods;


    public FoodBasketAdapter(List<FoodBasket> mFoods) {
        this.mFoods = mFoods;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView btnAdd,btnSubtract;
        TextView tvName, tvPrice,tvQuantity,tvSum;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvSum = itemView.findViewById(R.id.tvPrice);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_food_basket, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FoodBasket food = mFoods.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.tvName.setText(food.getName());
        viewHolder.tvQuantity.setText(food.quantity + "");
        String str = " VND";
        viewHolder.tvPrice.setText(food.getPrice() + str);
        viewHolder.tvSum.setText(food.sum + str);
    }

    @Override
    public int getItemCount() {
        return mFoods.size();
    }
}
