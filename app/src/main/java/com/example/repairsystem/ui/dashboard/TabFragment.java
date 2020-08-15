package com.example.repairsystem.ui.dashboard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bunny.android.library.LoadDataLayout;
import com.example.repairsystem.Main2Activity;
import com.example.repairsystem.Main3Activity;
import com.example.repairsystem.R;
import com.example.repairsystem.UserName;
import com.example.repairsystem.dialog.PickValueView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;


public class TabFragment extends Fragment implements PickValueView.onSelectedChangeListener {
    private TextView titleTv;
    private LoadDataLayout loadDataLayout;
    private String mTitle;
    private int titleCode;
    private PullToRefreshListView listView;
    private View view;

    private TextView tv1, tv4, tv_cancel, tv5, tv_userphone;
    private EditText et2, et3, et4, et6;
    private ImageView iv_sendMsg, iv_sendMsg2, iv_sendMsg3;
    private Button btn_sendMsg;
    private Dialog dialog;
    private PickValueView pickString;
    private RelativeLayout rl_type, rl_typeselection;
    private static final String[] valueStr = {"水", "电", "木工", "其他"};
    private String[] selection1 = new String[]{"冷水水龙头", "卫生间", "堵", "盆池", "漏水"};
    private String[] selection2 = new String[]{"插座", "灯", "开关", "风扇", "排气扇", "时空开关"};
    private String[] selection3 = new String[]{"寝室大门", "卫生间木门", "卫生间玻璃门", "阳台塑钢门", "衣柜",
            "书桌", "洗脸台柜子", "床", "椅子", "窗"};
    private String[] selection4 = new String[]{"热水", "空调", "网络", "饮水机"};

    //调用系统相册-选择图片
    private static final int IMAGE = 1;
    private int imgset = 0;
    private int code = 0; //提交订单的状态码 默认为0，未有维修人员处理

    private ArrayList<myUserOrderList> cData;
    private ArrayList<myUserOrderList> cData2 = new ArrayList<>();
    private MyAdapter<myUserOrderList> carAdapter;

    private TextView tv_mylist_ordertime, tv_mylist_ordernumber, tv_mylist_username, tv_order_code;
    private TextView tv_location1, tv_location2, tv_location3, tv_repair_type, tv_repair_detail;
    private LinearLayout ll_goto_detail_order, ll_goto_evaluate_order;
    private TabLnsner tabLnsner;
    private ViewPager pager;

    private Bitmap bitmap1, bitmap2, bitmap3;
    private String file1;
    private String dataUrl="";
    private String  url;
//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        tabLnsner = (TabLnsner) context;
//    }


    //这个构造方法是便于各导航同时调用一个fragment
    public TabFragment(String title) {

        mTitle = title;
        if (title.equals("我的报修")) {
            titleCode = 1;
        }
        if (title.equals("我要报修")) {
            titleCode = 2;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        switch (titleCode) {
            case 1:
                view = inflater.inflate(R.layout.fragment_tab, container, false);
                listView = view.findViewById(R.id.tabFragment_mylists);
                listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START); //上拉刷新
                listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
                    @Override
                    public void onRefresh(final PullToRefreshBase<ListView> refreshView) {
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                cData2 = new ArrayList<>();
                                listView.setAdapter(carAdapter);
                                getMyUserOrderList(); //显示所有已完成
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                refreshView.onRefreshComplete();// 刷新完成
                            }
                        });
                    }
                });
                break;
            case 2:
                view = inflater.inflate(R.layout.user_want_torepair, container, false);
                break;
        }

        if (titleCode == 1) {
            fragment1();
        }
        if (titleCode == 2) {
            fragment2();
        }

        return view;
    }

    //布局1
    private void fragment1() {

        initGif();
    }


    //布局2
    private void fragment2() {
        init();
    }


    //我的订单出事页面Gif图
    private void initGif() {

        loadDataLayout = view.findViewById(R.id.park_ldl);


        loadDataLayout.showLoading("正在努力加载中", new LoadDataLayout.SetImgCallBack() {
            @Override
            public void setImg(ImageView img) {
                Glide.with(getActivity())
                        .load(R.mipmap.loading)
                        .into(img);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    getMyUserOrderList();   //显示我的历史订单
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    //获取我的历史订单
    private void getMyUserOrderList() {
        String url = "https://3a142762b7.eicp.vip/order/findOrderByName";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println(s);
                        Gson gson = new Gson();
                        cData = gson.fromJson(s, new TypeToken<List<myUserOrderList>>() {
                        }.getType());
                        handler.sendEmptyMessage(1);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println(volleyError);
                        Toast.makeText(getContext(), "网络错误，请联系服务器", Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("username", UserName.name);
                return map;
            }
        };
        queue.add(request);

    }

    //我要报修页面初始化
    private void init() {
        et2 = view.findViewById(R.id.et2);
        et3 = view.findViewById(R.id.et3);
        et4 = view.findViewById(R.id.et4);
        tv1 = view.findViewById(R.id.tv1);
        tv4 = view.findViewById(R.id.tv4);
        tv5 = view.findViewById(R.id.tv5);
        tv_userphone = view.findViewById(R.id.tv_userphone);
        et6 = view.findViewById(R.id.et6);

        iv_sendMsg = view.findViewById(R.id.iv_sendMsg);
        iv_sendMsg2 = view.findViewById(R.id.iv_sendMsg2);
        iv_sendMsg3 = view.findViewById(R.id.iv_sendMsg3);
        btn_sendMsg = view.findViewById(R.id.btn_sendMsg);

        tv_cancel = view.findViewById(R.id.tv_cancel);
        rl_type = view.findViewById(R.id.rl_type);
        rl_typeselection = view.findViewById(R.id.rl_typeselection);

        tv1.setText(UserName.name);
        tv_userphone.setText(UserName.phone);

        rl_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择对话框
                dialog = new Dialog(getActivity(), R.style.ActionSheetDialogStyle);
                View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_cj_num, null);
                //获取组件
                tv_cancel = contentView.findViewById(R.id.tv_cancel);
                pickString = contentView.findViewById(R.id.pickString);
                //获取Dialog的监听
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                pickString.setOnSelectedChangeListener(TabFragment.this);
                String[] valueStr = new String[]{"水", "电", "木工", "其他"};
                pickString.setValueData(valueStr, valueStr[1]);
                dialog.setContentView(contentView);
                ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
                layoutParams.width = getResources().getDisplayMetrics().widthPixels;
                contentView.setLayoutParams(layoutParams);
                dialog.getWindow().setGravity(Gravity.BOTTOM);//弹窗位置
                dialog.getWindow().setWindowAnimations(R.style.ActionSheetDialogStyle);//弹窗样式
                dialog.show();//显示弹窗
            }
        });

        rl_typeselection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择对话框
                dialog = new Dialog(getContext(), R.style.ActionSheetDialogStyle);
                View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_cj_num, null);
                //获取组件
                tv_cancel = contentView.findViewById(R.id.tv_cancel);
                pickString = contentView.findViewById(R.id.pickString);
                //获取Dialog的监听
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                pickString.setOnSelectedChangeListener(TabFragment.this);

                String text = tv4.getText().toString();
                switch (text) {
                    case "水":
                        pickString.setValueData(selection1, selection1[0]);
                        break;
                    case "电":
                        pickString.setValueData(selection2, selection2[0]);
                        break;
                    case "木工":
                        pickString.setValueData(selection3, selection3[0]);
                        break;
                    case "其他":
                        pickString.setValueData(selection4, selection4[0]);
                        break;

                }

                dialog.setContentView(contentView);
                ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
                layoutParams.width = getResources().getDisplayMetrics().widthPixels;
                contentView.setLayoutParams(layoutParams);
                dialog.getWindow().setGravity(Gravity.BOTTOM);//弹窗位置
                dialog.getWindow().setWindowAnimations(R.style.ActionSheetDialogStyle);//弹窗样式
                dialog.show();//显示弹窗
            }
        });

        //提交我的报修订单按钮
        btn_sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url="https://3a142762b7.eicp.vip/order/upload";
                String filePath = file1;
                String filename = filePath.substring(filePath.lastIndexOf("."));
                try {
                    upload(url,filePath,filename);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        //添加图片按钮
        iv_sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission
                        .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    //执行启动相册的方法
                    openAlbum();
                }
            }
        });
        iv_sendMsg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlbum();
            }
        });
        iv_sendMsg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlbum();
            }
        });
    }

    //刷新页面
    private void refresh(){
        et6.setText("");
        bitmap1=null;
        bitmap2=null;
        bitmap3=null;
        imgset=0;
    }

    private String status;

    //提交用户订单给服务器
    private void sendUserOrder() {
        final String name = UserName.name;
        final String phone = UserName.phone;
        final String location1 = et2.getText().toString();
        final String location2 = et3.getText().toString();
        final String location3 = et4.getText().toString();
        final String type = tv4.getText().toString();
        final String typeSelection = tv5.getText().toString();
        final String detail = et6.getText().toString();

        String url = "https://3a142762b7.eicp.vip/order/addRepairOrder";
        RequestQueue queue = Volley.newRequestQueue(getContext());
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
                map.put("username", name);
                map.put("userPhone", phone);
                map.put("communityName", location1);
                map.put("buildingNo", location2);
                map.put("houseNumber", location3);
                map.put("repairType", type);
                map.put("repairItems", typeSelection);
                map.put("reportDescribes", detail);
                map.put("url",dataUrl);
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
                    if ("200".equals(status)) {
                        Toast.makeText(getContext(), "提交成功", Toast.LENGTH_SHORT).show();
                        refresh();
                        System.out.println("提交成功");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                    pager.setCurrentItem(0);
//                                    tabLnsner.getOne();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    break;
                case 1:
                    loadDataLayout.showSuccess();
                    if (listView!=null){
                    carAdapter = new MyAdapter<myUserOrderList>(cData, R.layout.tabfragment_mylists) {
                        @Override
                        public void bindView(ViewHolder holder, final myUserOrderList obj) {
                            System.out.println(obj.getOrderTime() + "  " + obj.getOrderNumber());
                            holder.setText(R.id.tv_mylist_ordertime, obj.getOrderTime());
                            holder.setText(R.id.tv_mylist_ordernumber, obj.getOrderNumber());
                            holder.setText(R.id.tv_mylist_username, obj.getUsername());
                            holder.setText(R.id.tv_location1, obj.getCommunityName() + "-");
                            holder.setText(R.id.tv_location2, obj.getBuildingNo() + "-");
                            holder.setText(R.id.tv_location3, obj.getHouseNumber());
                            holder.setText(R.id.tv_repair_type, "【" + obj.getRepairItems() + "】");
                            holder.setText(R.id.tv_repair_detail, obj.getReportDescribes());

                            //查看详情
                            holder.setOnClickListener(R.id.ll_goto_detail_order, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getContext(), Main3Activity.class);
                                    intent.putExtra("ordertime", obj.getOrderTime());
                                    intent.putExtra("ordernumber", obj.getOrderNumber());
                                    intent.putExtra("code", obj.getCode());
                                    intent.putExtra("location1", obj.getCommunityName() + "-");
                                    intent.putExtra("location2", obj.getBuildingNo() + "-");
                                    intent.putExtra("location3", obj.getHouseNumber());
                                    intent.putExtra("repair_type", "【" + obj.getRepairItems() + "】");
                                    intent.putExtra("repair_detail", obj.getReportDescribes());
                                    intent.putExtra("username", obj.getUsername());

                                    intent.putExtra("workerOrderTime", obj.getReceiptTime()); //维修员接受任务时间
                                    intent.putExtra("userOrderTime", obj.getOrderTime());  //用户提交维修订单时间

                                    intent.putExtra("workerNumber", obj.getJobNumber());            //维修工工号

                                    intent.putExtra("url",obj.getUrl()); //上传图片路径

                                    intent.putExtra("userPhone",obj.getUserPhone());
                                    intent.putExtra("workerPhone",obj.getWorkerPhone());
                                    startActivity(intent);


                                }
                            });

                            code = Integer.parseInt(obj.getCode());
                            switch (code) {
                                case 0:
                                    holder.setText(R.id.tv_order_code, "未受理");
                                    break;
                                case 1:
                                    holder.setText(R.id.tv_order_code, "已受理");
                                    break;
                                case 2:
                                    holder.setText(R.id.tv_order_code, "进行中");
                                    break;
                                case 3:
                                    holder.setText(R.id.tv_order_code, "已完成");
                                    break;
                                case 4:
                                    holder.setText(R.id.tv_order_code, "已评价");
                                    break;
                            }

                            if (code == 0) {
                                holder.setEnable(R.id.ll_goto_detail_order, false);

                            } else {
                                holder.setEnable(R.id.ll_goto_detail_order, true);
                            }
                            if (code != 3) {
                                holder.setVisibility(R.id.ll_goto_evaluate_order, View.GONE);
                            } else {
                                holder.setVisibility(R.id.ll_goto_evaluate_order, View.VISIBLE);
                            }
                            if (code != 4) {
                                holder.setVisibility(R.id.ll_see_evaluate_order, View.GONE);
                            } else {
                                holder.setVisibility(R.id.ll_see_evaluate_order, View.VISIBLE);
                            }

                            //去评价
                            holder.setOnClickListener(R.id.ll_goto_evaluate_order, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getContext(), com.example.repairsystem.evaluateActivity.class);
                                    intent.putExtra("ordernumber", obj.getOrderNumber());
                                    intent.putExtra("workernumber", obj.getJobNumber());
                                    intent.putExtra("endTime", obj.getEndTime());
                                    intent.putExtra("type", obj.getRepairType());
                                    intent.putExtra("item", obj.getRepairItems());
                                    startActivity(intent);
                                }
                            });

                            //查看评价
                            holder.setOnClickListener(R.id.ll_see_evaluate_order, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getContext(), com.example.repairsystem.SeeEvaluateActivity.class);
                                    intent.putExtra("ordernumber", obj.getOrderNumber());
                                    intent.putExtra("workernumber", obj.getJobNumber());
                                    intent.putExtra("endTime", obj.getEndTime());
                                    intent.putExtra("type", obj.getRepairType());
                                    intent.putExtra("item", obj.getRepairItems());
                                    startActivity(intent);
                                }
                            });
                        }
                    };
                    listView.setAdapter(carAdapter);
                    }
                    break;
                case 2:
                    if (dataUrl==null || dataUrl.equals("")){
                        Toast.makeText(getContext(),"上传图片失败",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getContext(),"上传图片成功",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };


    public interface TabLnsner {
        void getOne();
    }


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
            Cursor c = getActivity().getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            showImage(imagePath);
            c.close();
        }
    }



    //加载图片
    private void showImage(String imaePath) {
        Bitmap bm = BitmapFactory.decodeFile(imaePath);
        String file=imaePath;
        switch (imgset) {
            case 0:
                ((ImageView) view.findViewById(R.id.iv_sendMsg)).setImageBitmap(bm);
                imgset = 1;
                bitmap1 = bm;
                file1=file;
                System.out.println("file1"+file1);
                break;
            case 1:
                ((ImageView) view.findViewById(R.id.iv_sendMsg2)).setImageBitmap(bm);
                imgset = 2;
                bitmap2 = bm;
                file1=file;
                break;
            case 2:
                ((ImageView) view.findViewById(R.id.iv_sendMsg3)).setImageBitmap(bm);
                imgset = 0;
                bitmap3 = bm;
                file1=file;
                break;
        }


    }

    //上传图片
    private void upload(String url, String filePath, String fileName) throws Exception {
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
                System.out.println("dataUrl"+dataUrl);
                handler.sendEmptyMessage(2);
                sendUserOrder(); //上传维修订单
            }
        });
    }


    //自定义滚轮的显示textview
    @Override
    public void onSelected(PickValueView view, Object leftValue, Object middleValue, Object rightValue) {
        String selectedStr = (String) leftValue;
        for (int i = 0; i < valueStr.length; i++) {
            if (leftValue.equals(valueStr[i])) {
                tv4.setText(selectedStr);
                return;
            }
        }
        tv5.setText(selectedStr);
    }


}