package com.example.repairsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.repairsystem.init.ActivityCollector;
import com.example.repairsystem.ui.dashboard.TabFragment;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class Main3Activity extends AppCompatActivity {
    private TextView tv_mylist_ordertime, tv_mylist_ordernumber, tv_mylist_username, tv_order_code;
    private TextView tv_location1, tv_location2, tv_location3, tv_repair_type, tv_repair_detail;
    private TextView tv_user_ordertime,tv_worker_name,tv_worker_ordertime,tv_mylist_username3;
    private TextView tv_mylist_repair_type,tv_location1_2_2,tv_location2_2_2,tv_location3_2_2
            ,tv_repair_type_detail;
    private TextView tv_call_workerPhone,tv_call_userPhone;
    private Button btn_cancle_mylists;

    private int code;
    private String url;
    private ImageView iv_url;

    private String userPhone="000",workerPhone="888";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabfragment_mylists_details);
        ActivityCollector.addActivity(this);//添加
        initdata();
    }

    private void initdata() {
        //上半部分
        tv_mylist_ordertime = findViewById(R.id.tv_mylist_ordertime2);     //初始订单时间【2020-02-02】
        tv_mylist_ordernumber = findViewById(R.id.tv_mylist_ordernumber2); //订单号
        tv_mylist_username = findViewById(R.id.tv_mylist_username2);       //用户姓名
        tv_order_code = findViewById(R.id.tv_order_code2);                 //未接受，已接受，进行中，已完成
        tv_location1 = findViewById(R.id.tv_location1_2);                  //钱湖校区寝室楼
        tv_location2 = findViewById(R.id.tv_location2_2);                  //53号楼
        tv_location3 = findViewById(R.id.tv_location3_2);                  //525寝室
        tv_repair_type = findViewById(R.id.tv_repair_type2);               //维修类型（饮水机损坏）
        tv_repair_detail = findViewById(R.id.tv_repair_detail2);           //用户描述维修具体内容（饮水机不能烧热水）

        //下半部分
        tv_location1_2_2 = findViewById(R.id.tv_location1_2_2);            //钱湖校区寝室楼
        tv_location2_2_2 = findViewById(R.id.tv_location2_2_2);            //53号楼
        tv_location3_2_2= findViewById(R.id.tv_location3_2_2);              //525寝室

        tv_worker_ordertime=findViewById(R.id.tv_worker_ordertime);         //维修员接受任务时间2020-02-03 12:03:56
        tv_user_ordertime=findViewById(R.id.tv_user_ordertime);             //用户发起维修订单时间

        tv_worker_name=findViewById(R.id.tv_worker_name);                   //维修员工号
        tv_mylist_username3=findViewById(R.id.tv_mylist_username3);          //底部用户姓名

        tv_mylist_repair_type=findViewById(R.id.tv_mylist_repair_type);     //底部显示的损坏类型
        tv_repair_type_detail=findViewById(R.id.tv_repair_type_detail);     //底部维修损坏描述

        tv_call_userPhone=findViewById(R.id.tv_call_userphone);
        tv_call_workerPhone=findViewById(R.id.tv_call_workerphone);

        Intent intent = getIntent();
        userPhone=intent.getStringExtra("userPhone");
        workerPhone=intent.getStringExtra("workerPhone");
        System.out.println("userPhone+workerPhone："+userPhone+"  "+workerPhone);
        tv_call_userPhone.setText(userPhone);
        tv_call_workerPhone.setText(workerPhone);

        tv_mylist_ordertime.setText(intent.getStringExtra("ordertime"));
        tv_mylist_ordernumber.setText(intent.getStringExtra("ordernumber"));
        tv_mylist_username.setText(intent.getStringExtra("username"));
       // tv_order_code.setText(intent.getStringExtra("code"));
        tv_location1.setText(intent.getStringExtra("location1"));
        tv_location2.setText(intent.getStringExtra("location2"));
        tv_location3.setText(intent.getStringExtra("location3"));
        tv_repair_type.setText(intent.getStringExtra("repair_type"));
        tv_repair_detail.setText(intent.getStringExtra("repair_detail"));

        tv_location1_2_2.setText(intent.getStringExtra("location1"));
        tv_location2_2_2.setText(intent.getStringExtra("location2"));
        tv_location3_2_2.setText(intent.getStringExtra("location3"));

        tv_worker_ordertime.setText(intent.getStringExtra("workerOrderTime"));
        tv_user_ordertime.setText(intent.getStringExtra("userOrderTime"));

        tv_worker_name.setText(intent.getStringExtra("workerNumber"));
        tv_mylist_username3.setText(intent.getStringExtra("username"));

        tv_mylist_repair_type.setText(intent.getStringExtra("repair_type"));
        tv_repair_type_detail.setText(intent.getStringExtra("repair_detail"));

        //加载报修图片
        url=intent.getStringExtra("url");
        System.out.println("url"+url);
        iv_url=findViewById(R.id.iv_url);
        Glide.with(Main3Activity.this).load(url)
                .placeholder(R.mipmap.mylist_3)
                .error(R.mipmap.error)
                .into(iv_url);
        //图片点击事件放大
        iv_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(Main3Activity.this,ImageViewActivity.class);
                intent1.putExtra("ivUrl",url);
                startActivity(intent1);
            }
        });

        code=Integer.parseInt(intent.getStringExtra("code"));
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

         btn_cancle_mylists=findViewById(R.id.btn_cancle_mylists);
         btn_cancle_mylists.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
            finish();
             }
         });
    }
    //禁用手机返回键
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK ) {
            //do something.
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);//清除
    }

}
