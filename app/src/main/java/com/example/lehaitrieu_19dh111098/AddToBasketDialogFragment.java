package com.example.lehaitrieu_19dh111098;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AddToBasketDialogFragment extends DialogFragment implements View.OnClickListener {
    TextView tvName, tvPrice, tvQuantity;
    Button btnBuy;
    ImageView btnSubtract, btnAdd;

    App app;
    FoodBasket food;
    CartRepository cartRepository;


    public AddToBasketDialogFragment() {
    }

    @SuppressLint("ValidFragment")
    public AddToBasketDialogFragment(FoodBasket food) {
        this.food = food;
        Log.d("ABC", food.toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_to_basket_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cartRepository = new CartRepository(getActivity().getApplication());

        tvName = view.findViewById(R.id.tvName);
        tvPrice = view.findViewById(R.id.tvPrice);
        tvQuantity = view.findViewById(R.id.tvQuantity);
        btnAdd = view.findViewById(R.id.btnAdd);
        btnSubtract = view.findViewById(R.id.btnSubtract);
        btnBuy = view.findViewById(R.id.btnLogout);
        btnBuy.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnSubtract.setOnClickListener(this);

        tvName.setText(food.getName());
        tvPrice.setText(food.getPrice() + " VND");
        updateStats();
        app = (App) getActivity().getApplication();


    }

    @Override
    public void onResume() {
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        getDialog().setCancelable(true);
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btnSubtract:
                food.decrease();
                updateStats();
                break;
            case R.id.btnAdd:
                food.increase();
                updateStats();
                break;
            case R.id.btnLogout:
                if (food.quantity > 0) {
                    app.basket.addFood(food);
                }
                ((RestaurantDetailActivity) getActivity()).updateBasket();
                cartRepository.insert(new Cart(food.getFoodKey(), food.getName(), food.getPrice(), food.getImage(),food.getRate(),food.getResKey(), food.getQuantity(), food.getSum()));

                getDialog().dismiss();
                break;
        }
    }

    private void updateStats() {
        if (food.getQuantity() > 0) {
            tvQuantity.setText(String.valueOf(food.getQuantity()));
            String add = getResources().getString(R.string.add_to_basket);
            btnBuy.setText(add + " : " + food.getSum()+ " VND");
        } else {
            btnBuy.setText(getResources().getString(R.string.back_to_menu));
        }
    }
}
