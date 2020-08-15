package com.example.repairsystem.init;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.repairsystem.R;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private ImageView img_signupquit;
    private EditText et_namesignup, et_psdsignup, et_psd2signup,et_userphone;
    private Button btn_signup;

    private String username, psd, psd2,userphone;
    private String status = null; //服务器返回注册的状态码
    private Boolean success=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ActivityCollector.addActivity(this);//添加
        init();
    }

    private void init() {
        et_namesignup = findViewById(R.id.et_namesignup);
        et_psdsignup = findViewById(R.id.et_psdsignup);
        et_psd2signup = findViewById(R.id.et_psd2signup);
        et_userphone=findViewById(R.id.et_userphone);
        btn_signup = findViewById(R.id.btn_singupsuccess);
        img_signupquit = findViewById(R.id.img_signupquit);

        img_signupquit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = et_namesignup.getText().toString();
                psd = et_psdsignup.getText().toString();
                psd2 = et_psd2signup.getText().toString();
                userphone=et_userphone.getText().toString();
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(SignupActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                }
                if (userphone.isEmpty()){
                    Toast.makeText(SignupActivity.this, "请输入电话号码", Toast.LENGTH_SHORT).show();
                }
                if (psd.isEmpty()){
                    return;
                }
                if (!psd.equals(psd2)) {
                    Toast.makeText(SignupActivity.this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
                } else {
                    System.out.println("getstatus");
                    getStatus();
                }
            }
        });
    }


   //账号的注册,获得数据库注册的返回值
    public void getStatus() {
        String url = "https://3a142762b7.eicp.vip/user/register";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println(s);
                            if ("200".equals(s)) {
                                success = true;
                            }
                            if ("500".equals(s)) {
                                success = false;
                            }
                        handler.sendEmptyMessage(0);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println(volleyError);
                        handler.sendEmptyMessage(1);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("username", username);
                map.put("password", psd);
                map.put("userPhone",userphone);
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
                    if (success) {
                        Toast.makeText(SignupActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                         new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 try {
                                     Thread.sleep(1000);
                                     Intent intent=new Intent(SignupActivity.this, LoginActivity.class);
                                     startActivity(intent);
                                 } catch (InterruptedException e) {
                                     e.printStackTrace();
                                 }
                             }
                         }).start();
                    }
                    if (!success) {
                        Toast.makeText(SignupActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1:
                    Toast.makeText(SignupActivity.this,"com.android.volley.TimeoutError",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);//清除
    }
}
