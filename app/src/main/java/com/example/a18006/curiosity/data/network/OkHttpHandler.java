package com.example.a18006.curiosity.data.network;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.example.a18006.curiosity.data.db.DBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpHandler extends AsyncTask<String, String, String> {

    private Context context;
    private Handler handler;

    private DBHelper dbHelper;

    private final OkHttpClient client = new OkHttpClient();
    private static final String API_URL = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=1000&api_key=DEMO_KEY";
    private static final int MAX_PHOTOS = 20;

    public static final int MSG_FINISHED = 1;

    public OkHttpHandler(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    @Override
    protected void onPreExecute() {
        dbHelper = new DBHelper(context);
    }

    @Override
    protected String doInBackground(String...params) {
        try {
            Request request = new Request.Builder()
                    .url(API_URL)
                    .build();

            Response response = client.newCall(request).execute();

            JSONObject jsonObj = new JSONObject(response.body().string());
            String result = jsonObj.get("photos").toString();
            JSONArray posts = new JSONArray(result);

            for (int i = 0; i < MAX_PHOTOS; i++) {
                JSONObject post = posts.getJSONObject(i);

                dbHelper.put(post.getString("id"), post.getString("img_src"));
            }
            dbHelper.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if (s != null) {
            super.onPostExecute(s);
        }
        handler.sendEmptyMessage(MSG_FINISHED);
    }
}
