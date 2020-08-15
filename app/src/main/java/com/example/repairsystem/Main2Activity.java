package com.example.repairsystem;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class Main2Activity extends AppCompatActivity {

    private TextView tv_mylist_ordertime, tv_mylist_ordernumber, tv_mylist_username, tv_order_code;
    private TextView tv_location1, tv_location2, tv_location3, tv_repair_type, tv_repair_detail;
    private LinearLayout ll_goto_detail_order;

    private int code = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabfragment_mylists);

        initdata(); //初始化数据
        lock(); //判断查看详情是否可以被点击
    }

    private void initdata() {
        tv_mylist_ordertime = findViewById(R.id.tv_mylist_ordertime);
        tv_mylist_ordernumber = findViewById(R.id.tv_mylist_ordernumber);
        tv_mylist_username = findViewById(R.id.tv_mylist_username);
        tv_order_code = findViewById(R.id.tv_order_code);
        tv_location1 = findViewById(R.id.tv_location1);
        tv_location2 = findViewById(R.id.tv_location2);
        tv_location3 = findViewById(R.id.tv_location3);
        tv_repair_type = findViewById(R.id.tv_repair_type);
        tv_repair_detail = findViewById(R.id.tv_repair_detail);

        ll_goto_detail_order = findViewById(R.id.ll_goto_detail_order);

        /*final String ordertime=tv_mylist_ordertime.getText().toString();
        String ordernumber=tv_mylist_ordernumber.getText().toString();
        String username=tv_mylist_username.getText().toString();
        String code= tv_order_code.getText().toString();
        String location1=tv_location1.getText().toString();
        String location2=tv_location2.getText().toString();
        String location3=tv_location3.getText().toString();
        String repair_type=tv_repair_type.getText().toString();
        String repair_detail=tv_repair_detail.getText().toString();*/

        ll_goto_detail_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(1);
                Intent intent = new Intent(Main2Activity.this, Main3Activity.class);
                intent.putExtra("ordertime", tv_mylist_ordertime.getText().toString());
                intent.putExtra("ordernumber", tv_mylist_ordernumber.getText().toString());
                intent.putExtra("code", tv_order_code.getText().toString());
                intent.putExtra("location1", tv_location1.getText().toString());
                intent.putExtra("location2", tv_location2.getText().toString());
                intent.putExtra("location3", tv_location3.getText().toString());
                intent.putExtra("repair_type", tv_repair_type.getText().toString());
                intent.putExtra("repair_detail", tv_repair_detail.getText().toString());
                intent.putExtra("username", tv_mylist_username.getText().toString());

                intent.putExtra("workerOrderTime", "2020-06-03 14:25:36"); //维修员接受任务时间
                intent.putExtra("userOrderTime", "2020-06-01 14:25:36");  //用户提交维修订单时间

                intent.putExtra("workerNumber", "2017010777");            //维修工工号
                startActivity(intent);
            }
        });
    }

    //通过code状态码判断（未受理，已受理，进行中，已完成=>0,1,2,3
    private void initCode() {
        switch (code) {
            case 0:
                tv_order_code.setText("未受理");
                break;
            case 1:
                tv_order_code.setText("已受理");
                break;
            case 2:
                tv_order_code.setText("进行中");
                break;
            case 3:
                tv_order_code.setText("已完成");
                break;
        }
    }

    //通过tv_order_code的值来判断，查看详情是否可以被点击
    private void lock() {
        initCode();
        String code = tv_order_code.getText().toString();
        if ("未受理".equals(code)) {
            ll_goto_detail_order.setEnabled(false);
        } else {
            ll_goto_detail_order.setEnabled(true);
        }
    }


    //第一次点击事件发生的时间
    private long mExitTime;

    /**
     * 点击两次返回退出app
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Object mHelperUtils;
                Toast.makeText(this, "再按一次退出APP", Toast.LENGTH_SHORT).show();
                //System.currentTimeMillis()系统当前时间
                mExitTime = System.currentTimeMillis();
            } else {
                moveTaskToBack(true);  //false表示退出当前页面，其他后台仍在运行；true表示退出activity，并清除后台
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
