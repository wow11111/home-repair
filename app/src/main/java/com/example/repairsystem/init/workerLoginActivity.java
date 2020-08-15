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

public class workerLoginActivity extends AppCompatActivity {

    private ImageView img_loginquit2;

    private Button btn_loginsuccess2, btn_logintype2;
    private EditText et_namelogin2, et_psdlogin2;

    private String status = null; //服务器返回注册的状态码
    private Boolean success = false;

    private String username = "", psd = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_worker);
        ActivityCollector.addActivity(this);//添加
        try {
            username = readfile(this, "workername.txt");
            psd = readfile(this, "workerpsd.txt");
        } catch (IOException e) {
            e.printStackTrace();

        }
        System.out.println("u" + username + "p" + psd);
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
        img_loginquit2 = findViewById(R.id.img_loginquit2);
        btn_loginsuccess2 = findViewById(R.id.btn_loginsuccess2);
        btn_logintype2 = findViewById(R.id.btn_logintype2);
        et_namelogin2 = findViewById(R.id.et_namelogin2);
        et_psdlogin2 = findViewById(R.id.et_psdlogin2);

        if (username.isEmpty() || psd.isEmpty()) {
        } else {
            et_namelogin2.setText(username);
            et_psdlogin2.setText(psd);
        }

        btn_logintype2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(workerLoginActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        img_loginquit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(workerLoginActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


        //登录按钮
        btn_loginsuccess2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = et_namelogin2.getText().toString();
                psd = et_psdlogin2.getText().toString();
                if (username.isEmpty() || psd.isEmpty()) {
                    Toast.makeText(workerLoginActivity.this, "账户名或密码为空", Toast.LENGTH_SHORT).show();
                } else {
                    getStatus(); //服务器验证密码
                    getWorkerPhone();
                }


            }
        });

    }


    private String zxc;

    //账号登录验证
    public void getStatus() {
        String url = "https://3a142762b7.eicp.vip/maintainer/login";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println(s);

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
                map.put("jobNumber", username);
                map.put("password", psd);
                return map;
            }
        };
        queue.add(request);
    }

    private String workerPhone = "120";

    //查询维修工电话号码
    private void getWorkerPhone() {
        String url = "https://3a142762b7.eicp.vip/maintainer/findMaintainerPhone";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                            workerPhone = jsonObject.getString("workerPhone").toString();

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
                map.put("jobNumber", username);
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
                        writefile(workerLoginActivity.this, "workername.txt", username);
                        writefile(workerLoginActivity.this, "workerpsd.txt", psd);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println(e);
                    }
                    if (success) {
                        UserName.wokerName = username;
                        Toast.makeText(workerLoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    UserName.name = username;
                                    Thread.sleep(1000);
                                    Intent intent = new Intent(workerLoginActivity.this, com.example.repairsystem.workerui.workerMainActivity.class);
                                    startActivity(intent);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                    if (!success) {
                        Toast.makeText(workerLoginActivity.this, "账户名或者密码错误", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1:
                    UserName.workerPhone = workerPhone;
                    System.out.println(UserName.workerPhone);
                    break;
                case 2:
                    Toast.makeText(workerLoginActivity.this, "com.android.volley.TimeoutError" , Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    //禁用手机返回键
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
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
