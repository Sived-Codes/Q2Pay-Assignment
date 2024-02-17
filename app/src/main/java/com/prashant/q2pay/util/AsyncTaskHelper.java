package com.prashant.q2pay.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;

public class AsyncTaskHelper {

    public interface AsyncTaskCallback<T> {
        void onPreExecute();

        void onPostExecute(T result);

        void onError(Exception e);
    }

    private static final Handler handler = new Handler(Looper.getMainLooper());

    public static void runInBackground(Runnable runnable) {
        executeAsyncTask(() -> {
            runnable.run();
            return null;
        }, null);
    }

    public static void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }

    private static <T> void executeAsyncTask(Callable<T> callable, AsyncTaskCallback<T> callback) {
        if (callback != null) {
            callback.onPreExecute();
        }

        try {
            T result = callable.call();
            if (callback != null) {
                runOnUiThread(() -> callback.onPostExecute(result));
            }
        } catch (Exception e) {
            if (callback != null) {
                runOnUiThread(() -> callback.onError(e));
            }
        }
    }
}
