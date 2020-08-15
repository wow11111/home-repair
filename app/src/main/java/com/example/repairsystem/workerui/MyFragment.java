package com.example.repairsystem.workerui;

import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.repairsystem.R;
import com.example.repairsystem.UserName;
import com.example.repairsystem.ui.notifications.MyOneLineView;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MyFragment extends Fragment  implements MyOneLineView.OnRootClickListener{
    private TextView user_name,phone;
    MyOneLineView oneItem, twoItem, thereItem, fourItem, fiveItem, sixItem,sevenItem;

    private ImageView blurImageView;
    private ImageView avatarImageView;

    private Button btn_quit_to_login;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_worker_notifications, container, false);

        return root;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Glide.get(getContext()).clearMemory();
        init(); //各组件初始化
        initData(); //背景以及头像初始化

        super.onActivityCreated(savedInstanceState);
    }

    private void initData() {

        blurImageView = getActivity().findViewById(R.id.iv_blur);
        avatarImageView = getActivity().findViewById(R.id.iv_avatar);
        btn_quit_to_login=getActivity().findViewById(R.id.btn_quitto_login_worker);

        // btn_myquit=getActivity().findViewById(R.id.btn_myquit);

        Glide.with(getContext()).load(R.mipmap.head2)
                .skipMemoryCache(true)//跳过内存缓存

                .diskCacheStrategy(DiskCacheStrategy.NONE)//不要在disk硬盘缓存

                .bitmapTransform(new BlurTransformation(getContext(), 25), new CenterCrop(getContext()))

                .into(blurImageView);

        Glide.with(getContext()).load(R.mipmap.head)

                .skipMemoryCache(true)//跳过内存缓存

                .diskCacheStrategy(DiskCacheStrategy.NONE)//不要在disk硬盘缓存

                .bitmapTransform(new CropCircleTransformation(getContext()))

                .into(avatarImageView);

        btn_quit_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), com.example.repairsystem.init.workerLoginActivity.class);
                startActivity(intent);
            }
        });

    }


    private void init() {

        user_name=getActivity().findViewById(R.id.user_name);
        user_name.setText(UserName.wokerName);

        phone=getActivity().findViewById(R.id.user_phone);
        phone.setText(UserName.workerPhone);

        //在xml布局中使用MyOneLineView
        oneItem = getActivity().findViewById(R.id.one_item2);
        twoItem = getActivity().findViewById(R.id.two_item2);
        thereItem = getActivity().findViewById(R.id.three_item2);
        fourItem = getActivity().findViewById(R.id.four_item2);
        //fiveItem = getActivity().findViewById(R.id.five_item2);
        sixItem = getActivity().findViewById(R.id.six_item2);
        sevenItem = getActivity().findViewById(R.id.seven_item2);

        oneItem.initMine(R.mipmap.myusername, "基础信息", "", true);
        twoItem.initMine(R.mipmap.mymsg2, "我的消息", "", true);
        thereItem.initMine(R.mipmap.mycharge, "我的工单","", true);
        fourItem.initMine(R.mipmap.mypsd, "修改密码", "", true);
        //fiveItem.initMine(R.mipmap.mylocation, "我的地址", "", true);
        sixItem.initMine(R.mipmap.mycontact, "联系我们", "", true);
        sevenItem.initMine(R.mipmap.myset, "设置", "", true);

        thereItem.setOnRootClickListener(this,1);
    }

    @Override
    public void onRootClick(View view) {

    }
}

