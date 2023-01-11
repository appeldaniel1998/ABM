package com.example.abm.Utils;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.abm.BaseActivity;

import org.json.JSONArray;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainDatabaseUtils {
    private static final String url = "http://192.168.1.246:5000";

    private static RequestBody buildRequestBody(String msg) {
        MediaType mediaType = MediaType.parse("text/plain");
        return RequestBody.create(msg, mediaType);
    }

    public static void postRequestDataBase(String message, CallbackInterface callBack, BaseActivity baseActivity) {
        RequestBody requestBody = buildRequestBody(message);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().post(requestBody).url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull final Call call, @NonNull final IOException e) {
                baseActivity.postRequestOnFailure(call, e);
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) {
                baseActivity.postRequestOnSuccess(response, callBack);
            }
        });
    }
}
