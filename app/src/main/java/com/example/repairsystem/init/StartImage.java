package com.example.repairsystem.init;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.example.repairsystem.R;

public class StartImage extends AppCompatActivity {
    private Button btnSkip;
    private int i = 4;
    private boolean isStart=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_image);
        ActivityCollector.addActivity(this);//添加
        initView();
        initEvent();

        thread.start();
       /* //延迟
        handler.postDelayed(runnableToLogin, 500);*/



    }

    //初始化组件
    public void initView() {
        btnSkip = findViewById(R.id.splash_btn_skip);
    }

    //监听事件
    public void initEvent() {
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toLoginActivity();
            }
        });
    }

   private Thread thread=new Thread(new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                try {
                    handler.sendEmptyMessage(0);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (isStart){
            toLoginActivity();
            }
        }
    });
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    btnSkip.setText("跳过" + " "+i);
                    i--;
                    break;
            }
        }
    };

    /**
     * 跳转到登录界面
     */
    private void toLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        isStart=false;
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);//清除
    }
}
