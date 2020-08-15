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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.repairsystem.init.ActivityCollector;
import com.example.repairsystem.ratingbar.RatingBar;

import java.util.HashMap;
import java.util.Map;

public class evaluateActivity extends AppCompatActivity{

    private EditText et_evaluate;
    private TextView tv_ratingbar_detail,te_workernumber,te_worker_finish_time,t3_repair_type,t3_repair_type_item;
    private RatingBar ratingBar;
    private ImageView img_evaluate_quit;
    private RadioButton rb_1,rb_2,rb_3;
    private Button btn_send_evaluate;
    private String evaluate="";
    private int workerSatisfaction=0,satisfaction=0;
    private String ordernumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_evaluate);
        ActivityCollector.addActivity(this);//添加
        initData();

    }

    //组件初始化
    private void initData() {
        Intent intent=getIntent();

        ordernumber=intent.getStringExtra("ordernumber");     //获取相应的订单号

        te_workernumber=findViewById(R.id.tw_worker_number);
        te_workernumber.setText(intent.getStringExtra("workernumber"));

        te_worker_finish_time=findViewById(R.id.te_worker_finish_time);
        te_worker_finish_time.setText(intent.getStringExtra("endTime"));

        t3_repair_type=findViewById(R.id.t3_repair_type);
        t3_repair_type.setText(intent.getStringExtra("type"));

        t3_repair_type_item=findViewById(R.id.t3_repair_type_item);
        t3_repair_type_item.setText(intent.getStringExtra("item"));

        et_evaluate=findViewById(R.id.et_evaluate);
        tv_ratingbar_detail=findViewById(R.id.tv_ratingbar_detail);
        img_evaluate_quit=findViewById(R.id.img_evaluate_quit);
        img_evaluate_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rb_1=findViewById(R.id.rb_1);
        rb_2=findViewById(R.id.rb_2);
        rb_3=findViewById(R.id.rb_3);
        rb_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workerSatisfaction=1;  //用户选中了第一个button，非常差的评价
                rb_2.setChecked(false);
                rb_3.setChecked(false);
            }
        });
        rb_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workerSatisfaction=2;  //用户选中了第一个button，非常差的评价
                rb_1.setChecked(false);
                rb_3.setChecked(false);
            }
        });
        rb_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workerSatisfaction=3;  //用户选中了第一个button，非常差的评价
                rb_2.setChecked(false);
                rb_1.setChecked(false);
            }
        });

        ratingBar=findViewById(R.id.rab_1);
        ratingBar.setOnStarChangeListener(new RatingBar.OnStarChangeListener() {
            @Override
            public void onStarChanged(float selectedNumber, int position) {
                switch ((int) selectedNumber){
                    case 1:
                        tv_ratingbar_detail.setText("非常不满意");
                        satisfaction=1;
                        tv_ratingbar_detail.setTextColor(Color.RED);
                        break;
                    case 2:
                        tv_ratingbar_detail.setText("不满意");
                        satisfaction=2;
                        tv_ratingbar_detail.setTextColor(Color.RED);
                        break;
                    case 3:
                        tv_ratingbar_detail.setText("一般");
                        satisfaction=3;
                        tv_ratingbar_detail.setTextColor(Color.parseColor("#ffa516"));
                        break;
                    case 4:
                        tv_ratingbar_detail.setText("满意");
                        satisfaction=4;
                        tv_ratingbar_detail.setTextColor(Color.parseColor("#ffa516"));
                        break;
                    case 5:
                        tv_ratingbar_detail.setText("非常满意");
                        satisfaction=5;
                        tv_ratingbar_detail.setTextColor(Color.parseColor("#ffa516"));
                        break;
                }
            }
        });
        btn_send_evaluate=findViewById(R.id.btn_send_evaluate);
        btn_send_evaluate.setVisibility(View.VISIBLE);
        btn_send_evaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evaluate=et_evaluate.getText().toString();
                sendUserEvaluate();
            }
        });
    }

    private String status;
    //提交评价订单给服务器
    private void sendUserEvaluate() {
        String url = "https://3a142762b7.eicp.vip/user/endOpinion";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        status = s;
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
                map.put("workerSatisfaction", workerSatisfaction+"");
                map.put("satisfaction", satisfaction+"");
                map.put("opinion",evaluate);
                System.out.println("map"+map);
                return map;
            }
        };
        queue.add(request);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    if ("200".equals(status)){
                        Toast.makeText(evaluateActivity.this,"提交评价成功！",Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(evaluateActivity.this,"未知错误！",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);//清除
    }

    /*//禁用手机返回键
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