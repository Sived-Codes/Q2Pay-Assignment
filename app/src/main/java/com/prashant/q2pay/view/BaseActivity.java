package com.prashant.q2pay.view;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.prashant.q2pay.util.NetworkReceiver;

public class BaseActivity extends AppCompatActivity {

    private final NetworkReceiver mNetworkReceiver = new NetworkReceiver();


    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkReceiver, filter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mNetworkReceiver);
    }



}
