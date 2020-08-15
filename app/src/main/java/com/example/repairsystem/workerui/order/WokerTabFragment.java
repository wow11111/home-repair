package com.example.repairsystem.workerui.order;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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
import com.example.repairsystem.dialog.PickValueView;
import com.example.repairsystem.ui.dashboard.MyAdapter;
import com.example.repairsystem.ui.dashboard.myUserOrderList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WokerTabFragment extends Fragment {
    private LoadDataLayout loadDataLayout;
    private int titleCode;
    private PullToRefreshListView listView;
    private View view;

    private ArrayList<myUserOrderList> cData, cData3, cData5;
    private ArrayList<myUserOrderList> cData2 = new ArrayList<>();
    private ArrayList<myUserOrderList> cData4 = new ArrayList<>();
    private ArrayList<myUserOrderList> cData6 = new ArrayList<>();
    private MyAdapter<myUserOrderList> carAdapter;

    //这个构造方法是便于各导航同时调用一个fragment
    public WokerTabFragment(String title) {
        if (title.equals("任务工单")) {
            titleCode = 1;
        }
        if (title.equals("进行中")) {
            titleCode = 2;
        }
        if (title.equals("历史订单")) {
            titleCode = 3;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_worker, container, false);
        loadDataLayout = view.findViewById(R.id.park_ldl2);
        listView = view.findViewById(R.id.tabFragment_mylists_worker_order);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START); //上拉刷新
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(final PullToRefreshBase<ListView> refreshView) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        switch (titleCode) {
                            case 1:
                                cData2 = new ArrayList<>();
                                listView.setAdapter(carAdapter);
                                getWorkerOrderList();
                                break;
                            case 2:
                                cData4 = new ArrayList<>();
                                listView.setAdapter(carAdapter);
                                getIngOrderList();
                                break;
                            case 3:
                                cData6 = new ArrayList<>();
                                listView.setAdapter(carAdapter);
                                getFinishOrderList();
                                break;
                        }
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
        if (titleCode == 1) {
            fragment1();
        }
        if (titleCode == 2) {
            fragment2();
        }
        if (titleCode == 3) {
            fragment3();
        }
        return view;
    }

    //接单中心
    private void fragment1() {
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
                    getWorkerOrderList();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    //正在进行中的订单
    private void fragment2() {
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
                    getIngOrderList();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    //查看已完成订单
    private void fragment3() {
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
                    getFinishOrderList();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //获取我的任务订单（code=1的订单）
    private void getWorkerOrderList() {
        String url = "https://3a142762b7.eicp.vip/maintainer/findWorkOrder";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println(s);
                        Gson gson = new Gson();
                        cData = null;
                        cData = new ArrayList<myUserOrderList>();
                        cData2 = null;
                        cData2 = new ArrayList<myUserOrderList>();
                        cData = gson.fromJson(s, new TypeToken<List<myUserOrderList>>() {
                        }.getType());
                        int a = cData.size();
                        for (int i = a - 1; i >= 0; i--) {
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
                map.put("jobNumber", UserName.wokerName);
                return map;
            }
        };
        queue.add(request);

    }


    //获取我正在进行中的订单（code=2的订单）
    private void getIngOrderList() {
        String url = "https://3a142762b7.eicp.vip/maintainer/findWorkOrder2";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println(s);
                        Gson gson = new Gson();
                        cData3 = null;
                        cData3 = new ArrayList<myUserOrderList>();
                        cData4 = null;
                        cData4 = new ArrayList<myUserOrderList>();

                        cData3 = gson.fromJson(s, new TypeToken<List<myUserOrderList>>() {
                        }.getType());
                        int a = cData3.size();
                        for (int i = a - 1; i >= 0; i--) {
                            myUserOrderList myUserOrderList = cData3.get(i);
                            cData4.add(myUserOrderList);
                        }

                        handler.sendEmptyMessage(3);
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
                map.put("jobNumber", UserName.wokerName);
                return map;
            }
        };
        queue.add(request);
    }


    //获取已完成中的订单（code=3的订单）
    private void getFinishOrderList() {
        String url = "https://3a142762b7.eicp.vip/maintainer/findWorkOrder3";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println(s);
                        Gson gson = new Gson();

                        cData6 = null;
                        cData6 = new ArrayList<myUserOrderList>();

                        cData5 = gson.fromJson(s, new TypeToken<List<myUserOrderList>>() {
                        }.getType());
                        int a = cData5.size();

                        for (int i = a - 1; i >= 0; i--) {
                            myUserOrderList myUserOrderList = cData5.get(i);
                            cData6.add(myUserOrderList);
                        }

                        handler.sendEmptyMessage(5);
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
                map.put("jobNumber", UserName.wokerName);
                return map;
            }
        };
        queue.add(request);
    }

    private String orderNumber = "";
    private String status;

    //接受维修工任务订单
    private void receiveWorkerOrder(final String aaa) {
        String url = "https://3a142762b7.eicp.vip/maintainer/receiptWorkOrder";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println(s);
                        status = s;
                        System.out.println("status：" + status);
                        handler.sendEmptyMessage(2);
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
                map.put("orderNumber", aaa);
                map.put("workerPhone", UserName.workerPhone);
                return map;
            }
        };
        queue.add(request);
    }

    private String status2;

    /*//完成维修工任务订单
    private void finishWorkerOrder(final String aaa) {
        String url = "https://3a142762b7.eicp.vip/maintainer/endWorkOrder";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println(s);
                        status2 = s;
                        System.out.println("status2：" + status2);
                        handler.sendEmptyMessage(4);
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
                map.put("orderNumber", aaa);
                return map;
            }
        };
        queue.add(request);
    }*/

    private int code;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    loadDataLayout.showSuccess();
                    carAdapter = new MyAdapter<myUserOrderList>(cData2, R.layout.worker_order_lists) {
                        @Override
                        public void bindView(ViewHolder holder, final myUserOrderList obj) {


                            holder.setText(R.id.tv_mylist_ordertime_worker_order, obj.getOrderTime());
                            holder.setText(R.id.tv_mylist_ordernumber_worker_order, obj.getOrderNumber());
                            holder.setText(R.id.tv_mylist_username_worker_order, obj.getUsername());
                            holder.setText(R.id.tv_location1_worker_order, obj.getCommunityName() + "-");
                            holder.setText(R.id.tv_location2_worker_order, obj.getBuildingNo() + "-");
                            holder.setText(R.id.tv_location3_worker_order, obj.getHouseNumber());
                            holder.setText(R.id.tv_repair_type_worker_order, "【" + obj.getRepairItems() + "】");
                            holder.setText(R.id.tv_repair_detail_worker_order, obj.getReportDescribes());

                            orderNumber = obj.getOrderNumber(); //获取订单编号
                            //接受任务
                            holder.setOnClickListener(R.id.ll_worker_receive_order, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //创建一个提示对话框的构造者
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setTitle("接受订单"); //设置标题
                                    builder.setMessage("是否接受订单？");  //提示信息
                                    builder.setIcon(R.mipmap.order);  //设置图标
                                    //正面的按钮
                                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            receiveWorkerOrder(obj.getOrderNumber()); //获取订单编号);
                                        }
                                    });
                                    builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(getContext(), "取消成功", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    builder.show();

                                }
                            });
                            code = Integer.parseInt(obj.getCode());
                            switch (code) {
                                case 0:
                                    holder.setText(R.id.tv_order_code_ing_order, "未受理");
                                    break;
                                case 1:
                                    holder.setText(R.id.tv_order_code_ing_order, "已受理");
                                    break;
                                case 2:
                                    holder.setText(R.id.tv_order_code_ing_order, "进行中");
                                    break;
                                case 3:
                                    holder.setText(R.id.tv_order_code_ing_order, "已完成");
                                    break;
                                case 4:
                                    holder.setText(R.id.tv_order_code_finish_order, "已评价");
                                    break;
                            }
                        }

                    };

                    listView.setAdapter(carAdapter);

                    break;

                case 2:
                    if ("200".equals(status)) {
                        Toast.makeText(getContext(), "接受订单成功，请前往订单页面进行查看", Toast.LENGTH_SHORT).show();
                        getWorkerOrderList();
                    } else {
                        Toast.makeText(getContext(), "接受订单失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 3:

                    loadDataLayout.showSuccess();
                    carAdapter = new MyAdapter<myUserOrderList>(cData4, R.layout.worker_ing_lists) {
                        @Override
                        public void bindView(ViewHolder holder, final myUserOrderList obj) {


                            holder.setText(R.id.tv_mylist_ordertime_ing_order, obj.getOrderTime());
                            holder.setText(R.id.tv_mylist_ordernumber_ing_order, obj.getOrderNumber());
                            holder.setText(R.id.tv_mylist_username_ing_order, obj.getUsername());
                            holder.setText(R.id.tv_location1_ing_order, obj.getCommunityName() + "-");
                            holder.setText(R.id.tv_location2_ing_order, obj.getBuildingNo() + "-");
                            holder.setText(R.id.tv_location3_ing_order, obj.getHouseNumber());
                            holder.setText(R.id.tv_repair_type_ing_order, "【" + obj.getRepairItems() + "】");
                            holder.setText(R.id.tv_repair_detail_ing_order, obj.getReportDescribes());

                            //查看详情
                            holder.setOnClickListener(R.id.ll_ing_order_details, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String url = obj.getUrl();
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

                                    intent.putExtra("url", obj.getUrl());

                                    intent.putExtra("userPhone", obj.getUserPhone());
                                    intent.putExtra("workerPhone", obj.getWorkerPhone());
                                    startActivity(intent);
                                }
                            });

                            //完成任务
                            holder.setOnClickListener(R.id.ll_ing_order_finish, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //finishWorkerOrder(obj.getOrderNumber()); //获取订单编号);
                                    Intent intent = new Intent(getContext(), finishOrderActivity.class);
                                    intent.putExtra("ordernumber", obj.getOrderNumber());
                                    System.out.println("obj.getOrderNumber()" + obj.getOrderNumber());
                                    startActivity(intent);
                                }
                            });
                            code = Integer.parseInt(obj.getCode());
                            switch (code) {
                                case 0:
                                    holder.setText(R.id.tv_order_code_ing_order, "未受理");
                                    break;
                                case 1:
                                    holder.setText(R.id.tv_order_code_ing_order, "已受理");
                                    break;
                                case 2:
                                    holder.setText(R.id.tv_order_code_ing_order, "进行中");
                                    break;
                                case 3:
                                    holder.setText(R.id.tv_order_code_ing_order, "已完成");
                                    break;
                                case 4:
                                    holder.setText(R.id.tv_order_code_finish_order, "已评价");
                                    break;
                            }
                        }

                    };

                    listView.setAdapter(carAdapter);

                    break;
                /*case 4:
                    if ("200".equals(status2)) {
                        Toast.makeText(getContext(), "结束订单", Toast.LENGTH_SHORT).show();
                        getWorkerOrderList();
                    } else {
                        Toast.makeText(getContext(), "未知错误", Toast.LENGTH_SHORT).show();
                    }
                    break;*/
                case 5:
                    loadDataLayout.showSuccess();
                    carAdapter = new MyAdapter<myUserOrderList>(cData5, R.layout.worker_finish_lists) {
                        @Override
                        public void bindView(ViewHolder holder, final myUserOrderList obj) {

                            code = Integer.parseInt(obj.getCode());
                            switch (code) {
                                case 0:
                                    holder.setText(R.id.tv_order_code_finish_order, "未受理");
                                    break;
                                case 1:
                                    holder.setText(R.id.tv_order_code_finish_order, "已受理");
                                    break;
                                case 2:
                                    holder.setText(R.id.tv_order_code_finish_order, "进行中");
                                    break;
                                case 3:
                                    holder.setText(R.id.tv_order_code_finish_order, "已完成");
                                    break;
                                case 4:
                                    holder.setText(R.id.tv_order_code_finish_order, "已评价");
                                    break;
                            }

                            orderNumber = obj.getOrderNumber(); //获取订单编号

                            holder.setText(R.id.tv_mylist_ordertime_finish_order, obj.getOrderTime());
                            holder.setText(R.id.tv_mylist_ordernumber_finish_order, obj.getOrderNumber());
                            holder.setText(R.id.tv_mylist_username_finish_order, obj.getUsername());
                            holder.setText(R.id.tv_location1_finish_order, obj.getCommunityName() + "-");
                            holder.setText(R.id.tv_location2_finish_order, obj.getBuildingNo() + "-");
                            holder.setText(R.id.tv_location3_finish_order, obj.getHouseNumber());
                            holder.setText(R.id.tv_repair_type_finish_order, "【" + obj.getRepairItems() + "】");
                            holder.setText(R.id.tv_repair_detail_finish_order, obj.getReportDescribes());

                            //查看详情
                            holder.setOnClickListener(R.id.ll_finish_order_details, new View.OnClickListener() {
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

                                    intent.putExtra("url", obj.getUrl());

                                    intent.putExtra("userPhone", obj.getUserPhone());
                                    intent.putExtra("workerPhone", obj.getWorkerPhone());
                                    System.out.println("obj.getUserPhone()+obj.getWorkerPhone()：" + obj.getUserPhone() + "  " + obj.getWorkerPhone());
                                    startActivity(intent);
                                }
                            });

                            if (code == 4) {
                                holder.setVisibility(R.id.ll_workersee_evaluate_order, View.VISIBLE);
                            } else {
                                holder.setVisibility(R.id.ll_workersee_evaluate_order, View.GONE);
                            }
                            //查看评价
                            holder.setOnClickListener(R.id.ll_workersee_evaluate_order, new View.OnClickListener() {
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
                    break;
            }
        }

    };

}





