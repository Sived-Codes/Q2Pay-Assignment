package com.prashant.q2pay.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.gson.Gson;
import com.prashant.q2pay.R;
import com.prashant.q2pay.api.ApiReceiver;
import com.prashant.q2pay.databinding.ActivityProductDetailBinding;
import com.prashant.q2pay.model.ProductModel;
import com.prashant.q2pay.util.AsyncTaskHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProductDetailActivity extends BaseActivity {

    private static final String[] ADJECTIVES = {"Premium quality", "Elegantly designed", "High-performance", "Innovative and stylish", "Luxurious and durable", "Sleek and modern", "Cutting-edge technology"};


    private ActivityProductDetailBinding bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bind = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        bind.backBtn.setOnClickListener(v -> finish());

        String id = getIntent().getStringExtra("id");

        if (id != null) {
            getProductDetail(id);
        }
    }

    private void getProductDetail(String id) {

       AsyncTaskHelper.runInBackground(() -> {
           ApiReceiver receiver = new ApiReceiver(ProductDetailActivity.this, new ApiReceiver.ApiListener() {
               @Override
               public void onResponse(JSONObject object) {

                   try {
                       ProductModel model = new Gson().fromJson(object.toString(), ProductModel.class);

                       AsyncTaskHelper.runOnUiThread(() -> {
                           bind.productTitle.setText(model.getTitle());
                           bind.productDescription.setText(model.getDescription());
                           bind.productRating.setText("Rating : " + model.getRating() + "/5");
                           bind.productPrice.setText("Price : $" + model.getPrice());
                           bind.productStock.setText(model.getStock() + " Available");

                           List<String> imageUrls = model.getImages();

                           ImageSlider imageSlider = findViewById(R.id.imageSlider);
                           List<SlideModel> imageSlideModels = new ArrayList<>();

                           for (String imageUrl : imageUrls) {
                               imageSlideModels.add(new SlideModel(imageUrl, randomTitle(), null));
                           }
                           imageSlider.setImageList(imageSlideModels);

                       });


                   } catch (Exception e) {
                       Toast.makeText(ProductDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                   }

                   bind.progressBar.setVisibility(View.GONE);
                   bind.productDetailLayout.setVisibility(View.VISIBLE);
               }

               @Override
               public void onError(VolleyError error) {
                   Toast.makeText(ProductDetailActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                   bind.progressBar.setVisibility(View.GONE);
                   bind.productDetailLayout.setVisibility(View.VISIBLE);
               }
           });
           receiver.getProductView(id);
       });


    }

    public String randomTitle() {
        Random random = new Random();
        return ADJECTIVES[random.nextInt(ADJECTIVES.length)];
    }

}