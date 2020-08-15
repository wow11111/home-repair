package com.example.repairsystem.workerui;



import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bunny.android.library.LoadDataLayout;
import com.example.repairsystem.Main3Activity;
import com.example.repairsystem.R;
import com.example.repairsystem.UserName;
import com.example.repairsystem.ui.dashboard.MyAdapter;
import com.example.repairsystem.ui.dashboard.myUserOrderList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    private ArrayList<myUserOrderList> cData;
    private ArrayList<myUserOrderList> cData2 = new ArrayList<>();
    private MyAdapter<myUserOrderList> carAdapter;
    private LoadDataLayout loadDataLayout;
    private String mTitle;
    private int titleCode;
    private PullToRefreshListView listView;
    private int code = 0; //提交订单的状态码 默认为0，未有维修人员处理
    private View view;
    private TextView tv_mylist_ordertime, tv_mylist_ordernumber, tv_mylist_username, tv_order_code;
    private TextView tv_location1, tv_location2, tv_location3, tv_repair_type, tv_repair_detail;
    private LinearLayout ll_goto_detail_order;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_worker, container, false);
        initGif();
        listView = view.findViewById(R.id.tabFragment_mylists_worker_order);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START); //上拉刷新
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(final PullToRefreshBase<ListView> refreshView) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        cData2 = new ArrayList<>();
                        listView.setAdapter(carAdapter);
                        getAllFinishOrderList(); //显示所有已完成
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

        return view;
    }





    //我的订单出事页面Gif图
    private void initGif() {
        loadDataLayout = view.findViewById(R.id.park_ldl2);

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
                    getAllFinishOrderList(); //显示所有已完成
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    //获取全部历史订单
    private void getAllFinishOrderList() {
        String url = "https://3a142762b7.eicp.vip/order/findOrderByCode";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        Gson gson = new Gson();
                        cData = gson.fromJson(s, new TypeToken<List<myUserOrderList>>() {
                        }.getType());
                        int a = cData.size();
                        for (int i = a - 1; i > 0; i--) {
                            myUserOrderList myUserOrderList = cData.get(i);
                            cData2.add(myUserOrderList);
                        }
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
                map.put("code", "4");
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
                case 1:
                    loadDataLayout.showSuccess();

                    if (listView!=null){
                    carAdapter = new MyAdapter<myUserOrderList>(cData2, R.layout.tabfragment_mylists) {
                        @Override
                        public void bindView(ViewHolder holder, final myUserOrderList obj) {
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

                                    intent.putExtra("url",obj.getUrl());

                                    intent.putExtra("userPhone",obj.getUserPhone());
                                    intent.putExtra("workerPhone",obj.getWorkerPhone());
                                    startActivity(intent);
                                }
                            });
                            code = Integer.parseInt(obj.getCode());
                            if (code == 4) {
                                holder.setText(R.id.tv_order_code, "已评价");
                                holder.setVisibility(R.id.ll_see_evaluate_order, View.VISIBLE);  //可以去查看评价
                            }
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
            }
        }
    };
}
