package com.example.repairsystem.init;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.example.repairsystem.MainActivity;
import com.example.repairsystem.R;
import com.example.repairsystem.UserName;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private ImageView img_loginquit;
    private TextView tv_signup;
    private Button btn_loginsuccess, btn_logintype;
    private EditText et_namelogin, et_psdlogin;

    private String status = null; //服务器返回注册的状态码
    private Boolean success = false;

    private String username, psd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityCollector.addActivity(this);//添加
        try {
            username = readfile(this, "name.txt");
            psd = readfile(this, "psd.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        init();

    }

    //写入文件
    public static void writefile(Context context, String filename, String text) throws IOException {
        System.out.println("filename:" + filename + " text:" + text);
        OutputStream out = context.openFileOutput(filename, Context.MODE_PRIVATE);//覆盖
        byte[] bytes = text.getBytes();
        out.write(bytes, 0, bytes.length);
        out.close();
    }

    //读取文件
    public static String readfile(Context context, String filename) throws IOException {
        InputStream in = context.openFileInput(filename);
        byte[] bytes = new byte[1024];
        StringBuffer sb = new StringBuffer();
        int len = -1;
        while ((len = in.read(bytes)) != -1) {
            sb.append(new String(bytes, 0, len));
        }
        in.close();
        return sb.toString();
    }

    private void init() {
        img_loginquit = findViewById(R.id.img_loginquit);
        tv_signup = findViewById(R.id.tv_signup);
        btn_loginsuccess = findViewById(R.id.btn_loginsuccess);
        btn_logintype = findViewById(R.id.btn_logintype);
        et_namelogin = findViewById(R.id.et_namelogin);
        et_psdlogin = findViewById(R.id.et_psdlogin);
        if (username.isEmpty() || psd.isEmpty()) {
        } else {
            et_namelogin.setText(username);
            et_psdlogin.setText(psd);
        }

        btn_logintype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, workerLoginActivity.class);
                startActivity(intent);
            }
        });

        img_loginquit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //退出程序
                moveTaskToBack(true);
            }
        });

        //登录界面注册按钮
        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        //登录按钮
        btn_loginsuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = et_namelogin.getText().toString();
                psd = et_psdlogin.getText().toString();
                if (username.isEmpty() || psd.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "账户名或密码为空", Toast.LENGTH_SHORT).show();
                } else {
                    getStatus(); //服务器验证密码
                    getUserPhone();//获取用户的电话
                }


            }
        });

    }


    private String zxc;

    //账号登录验证
    public void getStatus() {
        String url = "https://3a142762b7.eicp.vip/user/login";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                            zxc = jsonObject.getString("status").toString();
                            System.out.println(zxc);
                            if ("200".equals(zxc)) {
                                success = true;
                            }
                            if ("500".equals(zxc)) {
                                success = false;
                            }
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
                        handler.sendEmptyMessage(2);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("username", username);
                map.put("password", psd);
                return map;
            }
        };
        queue.add(request);
    }

    private String userPhone;

    //查询用户电话号码
    private void getUserPhone() {
        String url = "https://3a142762b7.eicp.vip/user/findUserPhone";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                            userPhone = jsonObject.getString("userPhone").toString();
                            System.out.println(userPhone);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(1);
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
                map.put("username", username);
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
                    System.out.println(1233);
                    try {
                        writefile(LoginActivity.this, "name.txt", username);
                        writefile(LoginActivity.this, "psd.txt", psd);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (success) {
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    UserName.name = username;
                                    Thread.sleep(1000);
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                    if (!success) {
                        Toast.makeText(LoginActivity.this, "账户名或者密码错误", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1:
                    UserName.phone = userPhone;
                    break;
                case 2:
                    Toast.makeText(LoginActivity.this,"com.android.volley.TimeoutError",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    // 再点一次退出程序时间设置
    private long TOUCH_TIME = 0L;

    //当前时间与上次点击返回键的时间差
    private static final long WAIT_TIME = 2000L;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            //小于2s就代表用户不是误操作，则退出
            if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
                ActivityCollector.exitApp();
            } else {
                TOUCH_TIME = System.currentTimeMillis();
                Toast.makeText(this, "在按一次退出", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
