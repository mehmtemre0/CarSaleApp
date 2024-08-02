package com.example.goapplication;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CarDetailActivity extends AppCompatActivity {

    private TextView carTitle, carBrand, carModel, carPaket, carYear, carHp, carCc, carPrice, carOwnerDetails;
    private ImageView carImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        carTitle = findViewById(R.id.car_title);
        carBrand = findViewById(R.id.car_brand);
        carModel = findViewById(R.id.car_model);
        carPaket = findViewById(R.id.car_paket);
        carYear = findViewById(R.id.car_year);
        carHp = findViewById(R.id.car_hp);
        carCc = findViewById(R.id.car_cc);
        carPrice = findViewById(R.id.car_price);
        carOwnerDetails = findViewById(R.id.car_owner_details);
        carImage = findViewById(R.id.car_image);

        String carId = getIntent().getStringExtra("carId");
        String carBrandName = getIntent().getStringExtra("carBrand");

        if (carId != null && carBrandName != null) {
            DatabaseReference carRef = FirebaseDatabase.getInstance().getReference("cars").child(carBrandName).child(carId);
            carRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Car car = snapshot.getValue(Car.class);
                        if (car != null) {
                            carTitle.setText(car.getTitle());
                            carBrand.setText(car.getBrand());
                            carModel.setText(car.getModel());
                            carPaket.setText(car.getPaket());
                            carYear.setText(car.getYear());
                            carHp.setText(car.getHp());
                            carCc.setText(car.getCc());
                            carPrice.setText(car.getPrice());
                            carOwnerDetails.setText(car.getOwnerDetails());

                            // Decode Base64 image
                            byte[] decodedString = Base64.decode(car.getImage(), Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            carImage.setImageBitmap(decodedByte);
                        }
                    } else {
                        carTitle.setText("Araç bilgileri yüklenemedi");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    carTitle.setText("Araç bilgileri yüklenemedi: " + error.getMessage());
                }
            });
        } else {
            carTitle.setText("Araç bilgileri yüklenemedi");
        }
    }
}