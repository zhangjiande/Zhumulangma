package com.gykj.zhumulangma.player;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.SizeUtils;
import com.gykj.zhumulangma.common.AppConstants;
import com.gykj.zhumulangma.common.event.EventCode;
import com.gykj.zhumulangma.common.event.common.BaseActivityEvent;
import com.gykj.zhumulangma.common.mvvm.BaseFragment;
import com.gykj.zhumulangma.common.util.ZhumulangmaUtil;
import com.gykj.zhumulangma.common.widget.TScrollView;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import org.greenrobot.eventbus.EventBus;

import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

@Route(path = AppConstants.Router.Player.F_PLAY_TRACK)
public class PlayTrackFragment extends BaseFragment implements TScrollView.OnScrollListener, View.OnClickListener {

    private TScrollView msv;
    private CommonTitleBar ctbTrans;
    private CommonTitleBar ctbWhite;
    private View c;

    private ImageView whiteLeft;
    private ImageView whiteRight1;
    private ImageView whiteRight2;

    private ImageView transLeft;
    private ImageView transRight1;
    private ImageView transRight2;

    public PlayTrackFragment() {

    }


    @Override
    protected int onBindLayout() {
        return R.layout.player_fragment_track;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setSwipeBackEnable(false);
    }

    @Override
    protected void initView(View view) {
        msv = view.findViewById(R.id.msv);
        ctbTrans = view.findViewById(R.id.ctb_trans);
        ctbWhite = view.findViewById(R.id.ctb_white);

        c = view.findViewById(R.id.c);

        initBar();
    }

    private void initBar() {

        transLeft = ctbTrans.getLeftCustomView().findViewById(R.id.iv_left);
        transRight1 = ctbTrans.getRightCustomView().findViewById(R.id.iv1_right);
        transRight2 = ctbTrans.getRightCustomView().findViewById(R.id.iv2_right);


        transLeft.setImageResource(R.drawable.ic_common_titlebar_back);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            transLeft.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        }
        transLeft.setVisibility(View.VISIBLE);

        transRight1.setImageResource(R.drawable.ic_common_more);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            transRight1.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        }
        transRight1.setVisibility(View.VISIBLE);

        transRight2.setImageResource(R.drawable.ic_common_share);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            transRight2.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        }
        transRight2.setVisibility(View.VISIBLE);

        whiteLeft = ctbWhite.getLeftCustomView().findViewById(R.id.iv_left);
        whiteRight1 = ctbWhite.getRightCustomView().findViewById(R.id.iv1_right);
        whiteRight2 = ctbWhite.getRightCustomView().findViewById(R.id.iv2_right);
        TextView tvTitle = ctbWhite.getCenterCustomView().findViewById(R.id.tv_title);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("歌曲详情");

        whiteLeft.setImageResource(R.drawable.ic_common_titlebar_back);
        whiteLeft.setVisibility(View.VISIBLE);

        whiteRight1.setImageResource(R.drawable.ic_common_more);
        whiteRight1.setVisibility(View.VISIBLE);
        whiteRight2.setImageResource(R.drawable.ic_common_share);
        whiteRight2.setVisibility(View.VISIBLE);
    }

    @Override
    public void initListener() {
        super.initListener();
        msv.setOnScrollListener(this);
        whiteLeft.setOnClickListener(this);
        transLeft.setOnClickListener(this);

    }
    @Override
    public void initData() {

    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
          EventBus.getDefault().post(new BaseActivityEvent<>(EventCode.MainCode.HIDE_GP));
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        EventBus.getDefault().post(new BaseActivityEvent<>(EventCode.MainCode.SHOW_GP));
    }


    @Override
    protected boolean enableSimplebar() {
        return false;
    }

    @Override
    protected boolean lazyEnable() {
        return false;
    }

    @Override
    public void onScroll(int scrollY) {

        ctbTrans.setAlpha(ZhumulangmaUtil.unvisibleByScroll(scrollY, SizeUtils.dp2px(100), c.getTop() - SizeUtils.dp2px(80)));
        ctbWhite.setAlpha(ZhumulangmaUtil.visibleByScroll(scrollY, SizeUtils.dp2px(100), c.getTop() - SizeUtils.dp2px(80)));

    }

    @Override
    public void onClick(View v) {
        if(v==whiteLeft||v==transLeft){
            pop();
        }
    }
}
