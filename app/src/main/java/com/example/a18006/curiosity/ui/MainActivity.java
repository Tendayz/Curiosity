package com.example.a18006.curiosity.ui;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.a18006.curiosity.R;
import com.example.a18006.curiosity.data.db.DBHelper;
import com.example.a18006.curiosity.data.network.OkHttpHandler;

public class MainActivity extends AppCompatActivity{

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new ActivityHandler();

        OkHttpHandler okHttpHandler = new OkHttpHandler(this, handler);
        okHttpHandler.execute();
    }

    private class ActivityHandler extends Handler {

        @Override
        public void handleMessage(android.os.Message msg) {
            if (msg.what == OkHttpHandler.MSG_FINISHED) {
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2);
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_images);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);

                DBHelper dbHelper = new DBHelper(MainActivity.this);
                String[] images = dbHelper.getImages();

                if (images != null) {
                    ImageGalleryAdapter adapter = new ImageGalleryAdapter(MainActivity.this, SpacePhoto.getSpacePhotos(images));
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Изображения не найдены", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        }
    }

}