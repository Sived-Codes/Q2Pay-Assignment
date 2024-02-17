package com.prashant.q2pay.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prashant.q2pay.R;
import com.prashant.q2pay.api.ApiReceiver;
import com.prashant.q2pay.databinding.ActivityMainBinding;
import com.prashant.q2pay.model.ProductModel;
import com.prashant.q2pay.util.AsyncTaskHelper;
import com.prashant.q2pay.util.MyAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding bind;

    MyAdapter<ProductModel> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        bind.bottomNavigation.setItemSelected(R.id.menu_home, true);
        bind.bottomNavigation.showBadge(R.id.menu_account);

        getProductData();


    }

    private void getProductData() {

        AsyncTaskHelper.runInBackground(() -> {
            ApiReceiver receiver = new ApiReceiver(MainActivity.this, new ApiReceiver.ApiListener() {
                @Override
                public void onResponse(JSONObject object) {
                    bind.progressBar.setVisibility(View.GONE);
                    bind.productRecyclerview.setVisibility(View.VISIBLE);

                    try {
                        JSONArray productsArray = object.getJSONArray("products");

                        Type productListType = new TypeToken<ArrayList<ProductModel>>() {
                        }.getType();
                        ArrayList<ProductModel> productList = new Gson().fromJson(productsArray.toString(), productListType);

                        AsyncTaskHelper.runOnUiThread(() -> setRecyclerView(productList));

                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onError(VolleyError error) {
                    bind.progressBar.setVisibility(View.GONE);
                    bind.productRecyclerview.setVisibility(View.VISIBLE);
                }
            });
            receiver.getAllProducts();
        });

    }

    private void setRecyclerView(ArrayList<ProductModel> productList) {
        bind.productRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter<>(productList, (holder, position) -> {
            ProductModel model = productList.get(position);

            ImageView productImage = holder.itemView.findViewById(R.id.productImageView);
            TextView productTitle = holder.itemView.findViewById(R.id.productTitleTextView);
            TextView productDescription = holder.itemView.findViewById(R.id.productDetailTextView);
            TextView productPrice = holder.itemView.findViewById(R.id.productPriceTextView);
            TextView productDiscount = holder.itemView.findViewById(R.id.productDiscountTextView);
            TextView productRating = holder.itemView.findViewById(R.id.productRatingTextView);

            Picasso.get().load(model.getThumbnail()).into(productImage);

            productTitle.setText(model.getTitle());
            productDescription.setText(model.getDescription());
            productPrice.setText("$ " + model.getPrice());
            productDiscount.setText(model.getDiscountPercentage() + " % discounted");
            productRating.setText(model.getRating() + "/5 Rating");

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("id", String.valueOf(model.getId()));
                startActivity(intent);
            });

        }, R.layout.cl_product_layout);
        bind.productRecyclerview.setAdapter(adapter);

    }
}
