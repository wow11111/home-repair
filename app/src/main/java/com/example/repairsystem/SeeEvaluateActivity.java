package com.example.repairsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.repairsystem.init.ActivityCollector;
import com.example.repairsystem.ratingbar.RatingBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SeeEvaluateActivity extends AppCompatActivity {
    private EditText et_evaluate;
    private TextView tv_ratingbar_detail, te_workernumber, te_worker_finish_time, t3_repair_type, t3_repair_type_item;
    private RatingBar ratingBar;
    private ImageView img_evaluate_quit;
    private RadioButton rb_1, rb_2, rb_3;
    private Button btn_send_evaluate;
    private String evaluate = "";
    private String workerSatisfaction = "", satisfaction = "";
    private String ordernumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_evaluate);
        ActivityCollector.addActivity(this);//添加

        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);//清除
    }

    private void initData() {
        Intent intent = getIntent();

        ordernumber = intent.getStringExtra("ordernumber");     //获取相应的订单号

        te_workernumber = findViewById(R.id.tw_worker_number);
        te_workernumber.setText(intent.getStringExtra("workernumber"));

        te_worker_finish_time = findViewById(R.id.te_worker_finish_time);
        te_worker_finish_time.setText(intent.getStringExtra("endTime"));

        t3_repair_type = findViewById(R.id.t3_repair_type);
        t3_repair_type.setText(intent.getStringExtra("type"));

        t3_repair_type_item = findViewById(R.id.t3_repair_type_item);
        t3_repair_type_item.setText(intent.getStringExtra("item"));

        et_evaluate = findViewById(R.id.et_evaluate);
        tv_ratingbar_detail = findViewById(R.id.tv_ratingbar_detail);
        img_evaluate_quit = findViewById(R.id.img_evaluate_quit);
        img_evaluate_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rb_1 = findViewById(R.id.rb_1);
        rb_2 = findViewById(R.id.rb_2);
        rb_3 = findViewById(R.id.rb_3);

        ratingBar = findViewById(R.id.rab_1);

        getEvaluate();
    }

    //根据订单号获取评价
    private void getEvaluate() {
        String url = "https://3a142762b7.eicp.vip/user/findOpinion";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                            satisfaction = jsonObject.getString("satisfaction").toString();
                            workerSatisfaction = jsonObject.getString("workerSatisfaction").toString();
                            evaluate = jsonObject.getString("opinion").toString();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        handler.sendEmptyMessage(0);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println(volleyError);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("orderNumber", ordernumber);

                return map;
            }
        };
        queue.add(request);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    et_evaluate.setText(evaluate + "");
                    setCanNotEditNoClick();
                    switch (workerSatisfaction) {
                        case "1":
                            rb_1.setChecked(true);
                            rb_2.setEnabled(false);
                            rb_3.setEnabled(false);
                            break;
                        case "2":
                            rb_2.setChecked(true);
                            rb_1.setEnabled(false);
                            rb_3.setEnabled(false);
                            break;
                        case "3":
                            rb_3.setChecked(true);
                            rb_1.setEnabled(false);
                            rb_2.setEnabled(false);
                            break;
                    }
                    switch (satisfaction) {
                        case "1":
                            ratingBar.setSelectedNumber(1);
                            tv_ratingbar_detail.setText("非常不满意");
                            tv_ratingbar_detail.setTextColor(Color.RED);
                            ratingBarTouch();
                            break;
                        case "2":
                            ratingBar.setSelectedNumber(2);
                            tv_ratingbar_detail.setText("不满意");
                            tv_ratingbar_detail.setTextColor(Color.RED);
                            ratingBarTouch();
                            break;
                        case "3":
                            ratingBar.setSelectedNumber(3);
                            tv_ratingbar_detail.setText("一般");
                            tv_ratingbar_detail.setTextColor(Color.parseColor("#ffa516"));
                            ratingBarTouch();
                            break;
                        case "4":
                            ratingBar.setSelectedNumber(4);
                            tv_ratingbar_detail.setText("满意");
                            tv_ratingbar_detail.setTextColor(Color.parseColor("#ffa516"));
                            ratingBarTouch();
                            break;
                        case "5":
                            ratingBar.setSelectedNumber(5);
                            tv_ratingbar_detail.setText("非常满意");
                            tv_ratingbar_detail.setTextColor(Color.parseColor("#ffa516"));
                            ratingBarTouch();
                            break;

                    }
                    break;
            }
        }
    };

    //设置ratingbar不可被点击
    private void ratingBarTouch() {
        ratingBar.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        ratingBar.setFocusable(false);
    }

    //设置edittext不可被点击
    private void setCanNotEditNoClick() {
        et_evaluate.setFocusable(false);
        et_evaluate.setFocusableInTouchMode(false);
        // 如果之前没设置过点击事件，该处可省略
        et_evaluate.setOnClickListener(null);
    }

   /* //禁用手机返回键
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK ) {
            //do something.
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }*/
}