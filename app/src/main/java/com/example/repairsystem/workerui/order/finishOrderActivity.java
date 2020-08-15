package com.example.repairsystem.workerui.order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.repairsystem.R;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class finishOrderActivity extends AppCompatActivity {

    private EditText et_worekr_evaluate;
    private ImageView iv_sendMsg_worker;
    private Button btn_sendMsg_worker;

    private String imagePath;
    private String file1;
    private String dataUrl =null;

    private Boolean SEND_IMAGEVIEW = false;

    private String status;
    private String maintainerOpinion = "";
    private String orderNumber;

    //调用系统相册-选择图片
    private static final int IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_order);

        init();
        initData();
    }

    private void init() {
        et_worekr_evaluate = findViewById(R.id.et_worker_evalute);
        iv_sendMsg_worker = findViewById(R.id.iv_sendMsg_worker);
        btn_sendMsg_worker = findViewById(R.id.btn_sendMsg_worker);
    }

    //功能初始化
    private void initData() {
        Intent intent = getIntent();
        orderNumber = intent.getStringExtra("ordernumber");
        System.out.println("orderNumber"+orderNumber);
        iv_sendMsg_worker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(finishOrderActivity.this, Manifest.permission
                        .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(finishOrderActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    //执行启动相册的方法
                    openAlbum();
                }
            }
        });

        //上传图片
        btn_sendMsg_worker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maintainerOpinion = et_worekr_evaluate.getText().toString();
                    String url = "https://3a142762b7.eicp.vip/order/upload";
                    String filePath = file1;
                    String filename = filePath.substring(filePath.lastIndexOf("."));
                    try {

                        upload(url, filePath, filename);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        });

    }




    //上传维修工的订单报告
    private void sendWorkerEvaluate() {
        String url = "https://3a142762b7.eicp.vip/maintainer/endWorkOrder";
        RequestQueue queue = Volley.newRequestQueue(finishOrderActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        status = s;
                        System.out.println("s："+status);
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
                map.put("orderNumber", orderNumber);
                map.put("maintainerOpinion", maintainerOpinion);
                map.put("url2", dataUrl);
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
                    if (dataUrl == null || dataUrl.equals("")) {
                        System.out.println(dataUrl);
                        Toast.makeText(finishOrderActivity.this, "上传图片失败", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(finishOrderActivity.this, "上传图片成功", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1:
                    if ("200".equals(status)) {
                        Toast.makeText(finishOrderActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    break;
            }
        }
    };

    //打开相册的方法
    private void openAlbum() {
        //调用相册
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = finishOrderActivity.this.getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            imagePath = c.getString(columnIndex);
            showImage(imagePath);
            c.close();
        }
    }


    //加载图片
    private void showImage(String imagePath) {
        if (this.imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(this.imagePath);
            file1 = imagePath;
            System.out.println(file1);
            iv_sendMsg_worker.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "fail to set image", Toast.LENGTH_SHORT).show();
        }
    }

    //上传图片
    private void upload(String url, String filePath, String fileName) throws Exception {
        System.out.println("url:"+url+" file:"+filePath+" name:"+fileName);
        OkHttpClient client = new OkHttpClient();
        final RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileName,
                        RequestBody.create(MediaType.parse("multipart/form-data"), new File(filePath)))
                .build();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                dataUrl = response.body().string();
                System.out.println("fanhui"+dataUrl);
                handler.sendEmptyMessage(0);
                sendWorkerEvaluate(); //上传维修订单
            }
        });
    }

}