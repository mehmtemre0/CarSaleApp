package com.example.goapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    private static final String MODEL_PATH = "model.tflite"; // Model dosyanızın ismi
    private static final int INPUT_SIZE = 224; // Modelinizin giriş boyutu (genişlik ve yükseklik)

    private Classifier classifier;
    private Map<Integer, String[]> carInfoMap;
    private Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        ImageView resultImage = findViewById(R.id.result_image);
        EditText carTitle = findViewById(R.id.car_title);
        EditText carBrand = findViewById(R.id.car_brand);
        EditText carModel = findViewById(R.id.car_model);
        EditText carPaket = findViewById(R.id.car_paket);
        EditText carYear = findViewById(R.id.car_year);
        EditText carHp = findViewById(R.id.car_hp);
        EditText carCC = findViewById(R.id.car_cc);
        EditText carPrice = findViewById(R.id.car_price);
        EditText carOwnerDetails = findViewById(R.id.car_owner_details);
        Button submitButton = findViewById(R.id.submit_button);

        // Araç bilgilerini tanımlayın
        carInfoMap = new HashMap<>();
        carInfoMap.put(0, new String[]{"BMW", "6.45", "CI", "2003-2011", "333 Hp", "4.4", ""});
        carInfoMap.put(1, new String[]{"BMW", "6.40", "Gran Coupe", "2012-2018", "320-325 HP", "3.0", ""});
        carInfoMap.put(2, new String[]{"Mercedes", "A180d", "Style", "2016-2017", "101-125 HP", "1.3-1.6", ""});
        carInfoMap.put(3, new String[]{"BMW", "3.20i", "M-Sport", "2022-2023", "170 HP", "1.6", ""});
        carInfoMap.put(4, new String[]{"BMW", "3.20i", "Sportline", "2022-2023", "170 HP", "1.6", ""});
        carInfoMap.put(5, new String[]{"Mercedes", "A180 d", "AMG", "2016-2017", "116 HP", "1.4", ""});
        carInfoMap.put(6, new String[]{"Mercedes", "CLA 200 ", "Urban", "2013-2016", "156 HP", "1.6", ""});
        carInfoMap.put(7, new String[]{"Mercedes", "CLA 200", "AMG", "2013-2016", "156 HP", "1.6", ""});
        carInfoMap.put(8, new String[]{"Fiat", "Egea", "1.3 Multijet", "2015-2020", "95 HP", "1.3", ""});
        carInfoMap.put(9, new String[]{"Fiat", "Egea", "1.3 Multijet", "2021-2023", "95 HP", "1.3", ""});
        carInfoMap.put(10, new String[]{"Fiat", "Egea", "CROSS", "2021-2022", "95 HP", "1.4", ""});
        carInfoMap.put(11, new String[]{"Hyundai", " i20", "1.4 MPI JUMP", "2015-2018", "100 HP", "1.4", ""});
        carInfoMap.put(12, new String[]{"Hyundai", "i20", "1.4 MPI JUMP", "2021-2023", "100 HP", "1.4", ""});
        carInfoMap.put(13, new String[]{"Hyundai", "i30", " Elite", "2015-2016", "136 HP", "1.6", ""});
        carInfoMap.put(14, new String[]{"Hyundai", "i30", " Elite Style", "2017-2018", "136 HP", "1.6", ""});
        // Diğer indeksler için devam edin...

        try {
            classifier = new Classifier(this, MODEL_PATH, INPUT_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Intent ile gelen veriyi al
        Intent intent = getIntent();
        imageBitmap = intent.getParcelableExtra("imageBitmap");
        if (imageBitmap != null) {
            resultImage.setImageBitmap(imageBitmap);
            classifyImage(imageBitmap, carBrand, carModel, carPaket, carYear, carHp, carCC, carPrice);
        } else if (intent.hasExtra("imageUri")) {
            Uri imageUri = Uri.parse(intent.getStringExtra("imageUri"));
            try {
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                imageBitmap = BitmapFactory.decodeStream(imageStream);
                resultImage.setImageBitmap(imageBitmap);
                classifyImage(imageBitmap, carBrand, carModel, carPaket, carYear, carHp, carCC, carPrice);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        submitButton.setOnClickListener(view -> {
            uploadData(carTitle.getText().toString(), carBrand.getText().toString(), carModel.getText().toString(),
                    carPaket.getText().toString(), carYear.getText().toString(), carHp.getText().toString(),
                    carCC.getText().toString(), carPrice.getText().toString(), carOwnerDetails.getText().toString());
        });
    }

    private void classifyImage(Bitmap image, EditText carBrand, EditText carModel, EditText carPaket, EditText carYear, EditText carHp, EditText carCC, EditText carPrice) {
        int classIndex = classifier.recognizeImage(image);
        if (carInfoMap.containsKey(classIndex)) {
            String[] carInfo = carInfoMap.get(classIndex);
            carBrand.setText(carInfo[0]);
            carModel.setText(carInfo[1]);
            carPaket.setText(carInfo[2]);
            carYear.setText(carInfo[3]);
            carHp.setText(carInfo[4]);
            carCC.setText(carInfo[5]);
            carPrice.setText(carInfo[6]);
        } else {
            carBrand.setText("Unknown");
            carModel.setText("Unknown");
            carPaket.setText("Unknown");
            carYear.setText("Unknown");
            carHp.setText("Unknown");
            carCC.setText("Unknown");
            carPrice.setText("Unknown");
        }
    }

    private void uploadData(String title, String brand, String model, String paket, String year, String hp, String cc, String price, String ownerDetails) {
        String encodedImage = encodeImageToBase64(imageBitmap);

        DatabaseReference newRef = FirebaseDatabase.getInstance().getReference("cars").child(brand).push(); // Benzersiz ID oluşturma
        String carId = newRef.getKey(); // ID'yi alma

        Map<String, Object> carData = new HashMap<>();
        carData.put("id", carId); // ID'yi ekleme
        carData.put("title", title);
        carData.put("brand", brand);
        carData.put("model", model);
        carData.put("paket", paket);
        carData.put("year", year);
        carData.put("hp", hp);
        carData.put("cc", cc);
        carData.put("price", price);
        carData.put("ownerDetails", ownerDetails);
        carData.put("image", encodedImage);

        newRef.setValue(carData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ResultActivity.this, "İlan Başarıyla Listelendi", Toast.LENGTH_SHORT).show();
                    finish(); // İlan başarılı bir şekilde listelendikten sonra aktiviteyi kapat
                } else {
                    Toast.makeText(ResultActivity.this, "İlan Listelenemedi", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ResultActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String encodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}