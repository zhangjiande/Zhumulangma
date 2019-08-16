package com.gykj.zhumulangma.common.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gykj.zhumulangma.common.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 10719
 * on 2019/6/18
 */
public class GlobalPlay extends FrameLayout {

    private CircleImageView civAvatar;
    private ImageView ivPlay;
    private Animation mAnimation;

    public GlobalPlay(@NonNull Context context) {
        this(context,null);
    }

    public GlobalPlay(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GlobalPlay(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.common_widget_global_play,this);
        civAvatar=findViewById(R.id.civ_avatar);
        ivPlay=findViewById(R.id.iv_play);
        mAnimation = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        mAnimation.setDuration(5000);
        mAnimation.setRepeatCount(Animation.INFINITE);
        mAnimation.setRepeatMode(Animation.RESTART);
        mAnimation.setInterpolator(new LinearInterpolator());
//        civAvatar.startAnimation(mAnimation);
    }
    public void play(String avatarUrl){
        ivPlay.setImageResource(R.drawable.ic_common_widget_pause);
        Glide.with(getContext()).load(avatarUrl).into(civAvatar);
        civAvatar.startAnimation(mAnimation);
    }
    public void pause(){
        ivPlay.setImageResource(R.drawable.ic_common_widget_play_white);
        civAvatar.clearAnimation();
    }
}
