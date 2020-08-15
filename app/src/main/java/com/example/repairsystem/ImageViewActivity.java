package com.example.repairsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.repairsystem.init.ActivityCollector;

public class ImageViewActivity extends AppCompatActivity {

    private ImageView iv_large;
    private LinearLayout ll_lvLarge_cancle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        ActivityCollector.addActivity(this);//添加
        init();
    }

    private void init() {
        iv_large=findViewById(R.id.iv_large);
        ll_lvLarge_cancle=findViewById(R.id.ll_ivLarge_cancle);
        ll_lvLarge_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent=getIntent();
        String url=intent.getStringExtra("ivUrl");
        Glide.with(ImageViewActivity.this).load(url)
                .placeholder(R.mipmap.mylist_3)
                .error(R.mipmap.error)
                .into(iv_large);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);//清除
    }
}