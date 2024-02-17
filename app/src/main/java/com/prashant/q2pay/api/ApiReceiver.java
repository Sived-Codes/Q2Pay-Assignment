package com.prashant.q2pay.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class ApiReceiver {
    private static final String TAG = "ApiReceiver";
    private Context context;
    private ApiListener listener;
    private ListListener listListener;
    private RequestQueue requestQueue;

    public ApiReceiver(Context context, ApiListener listener) {
        this.context = context;
        this.listener = listener;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public ApiReceiver(Context context, ListListener listener) {
        this.context = context;
        this.listListener = listener;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    private void getData(int method, String url, JSONObject object) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, url, object,
                response -> {
                    Log.d(TAG, "onResponse: " + response);
                    listener.onResponse(response);
                },
                this::handleError);

        requestQueue.add(jsonObjectRequest);
    }

    private void getList(int method, String url, JSONObject object) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(method, url, null,
                response -> {
                    try {
                        listListener.onResponse(response);
                    } catch (Exception e) {
                        Log.d(TAG, "onResponse: " + e);
                    }
                },
                this::handleError) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                return object == null ? null : object.toString().getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public RetryPolicy getRetryPolicy() {
                return new DefaultRetryPolicy(
                        10000, // 10 seconds timeout
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                );
            }
        };

        requestQueue.add(jsonArrayRequest);
    }

    private void handleError(VolleyError error) {
        if (error != null && error.networkResponse != null) {
            Log.e(TAG, "Network Response Code: " + error.networkResponse.statusCode);
            try {
                String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                Log.e(TAG, "Network Response Body: " + responseBody);
            } catch (Exception e) {
                Log.e(TAG, "Exception while handling VolleyError: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "Unknown VolleyError");
        }
    }

    public void getAllProducts() {
        getData(Request.Method.GET, Constant.PRODUCT_LIST_URL, new JSONObject());
    }

    public void getProductView(String id) {
        getData(Request.Method.GET, Constant.PRODUCT_LIST_URL+"/"+id, new JSONObject());
    }

    public interface ApiListener {
        void onResponse(JSONObject object);
        void onError(VolleyError error);
    }

    public interface ListListener {
        void onResponse(JSONArray object);
        void onError(VolleyError error);
    }
}
