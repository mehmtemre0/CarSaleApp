package com.example.goapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import android.widget.ImageView;

public class CarsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cars, container, false);

        ImageView btnBrand1 = view.findViewById(R.id.btn_brand1);
        ImageView btnBrand2 = view.findViewById(R.id.btn_brand2);
        ImageView btnBrand3 = view.findViewById(R.id.btn_brand3);
        ImageView btnBrand4 = view.findViewById(R.id.btn_brand4);

        btnBrand1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCarListActivity("BMW");
            }
        });

        btnBrand2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCarListActivity("Mercedes");
            }
        });

        btnBrand3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCarListActivity("Fiat");
            }
        });

        btnBrand4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCarListActivity("Hyundai");
            }
        });

        return view;
    }

    private void openCarListActivity(String brand) {
        Intent intent = new Intent(getActivity(), CarListActivity.class);
        intent.putExtra("brand", brand);
        startActivity(intent);
    }
}