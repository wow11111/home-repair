package com.example.repairsystem.workerui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.repairsystem.R;
import com.example.repairsystem.init.ActivityCollector;
import com.example.repairsystem.workerui.order.orderFragment;

public class workerMainActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
    private TextView tv_title;
    private SparseArray<Fragment> mFragmentSparseArray;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_main);
        ActivityCollector.addActivity(this);//添加
        initView();
    }

    private void initView() {
        tv_title = findViewById(R.id.text_Title_main);
        radioGroup = findViewById(R.id.tabs_rg);
        mFragmentSparseArray = new SparseArray<>();
        mFragmentSparseArray.append(R.id.One_tab, new HomeFragment());
        mFragmentSparseArray.append(R.id.order_tab, new orderFragment());
        mFragmentSparseArray.append(R.id.My_tab, new MyFragment());

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 具体的fragment切换逻辑可以根据应用调整，例如使用show()/hide()
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        mFragmentSparseArray.get(checkedId)).commit();
                switch (checkedId) {
                    case R.id.One_tab:
                        tv_title.setText("首页");
                        break;
                    case R.id.order_tab:
                        tv_title.setText("工单");
                        break;
                    case R.id.My_tab:
                        tv_title.setText("我的");
                        break;
                }
            }
        });
        // 默认显示第X个
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                mFragmentSparseArray.get(R.id.One_tab)).commit();
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
