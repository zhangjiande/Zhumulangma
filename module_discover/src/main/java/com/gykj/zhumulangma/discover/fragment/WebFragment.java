/*
package com.gykj.zhumulangma.discover.fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.gykj.zhumulangma.common.event.ActivityEvent;
import com.gykj.zhumulangma.common.event.EventCode;
import com.gykj.zhumulangma.common.event.KeyCode;
import com.gykj.zhumulangma.common.mvvm.view.BaseFragment;
import com.gykj.zhumulangma.discover.R;
import com.gykj.zhumulangma.discover.databinding.DiscoverFragmentWebBinding;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.WebIndicator;
import com.just.agentwebX5.AgentWebX5;
import com.just.agentwebX5.ChromeClientCallbackManager;
import com.just.agentwebX5.DefaultWebClient;
import com.just.agentwebX5.DownLoadResultListener;
import com.just.agentwebX5.PermissionInterceptor;
import com.just.agentwebX5.WebDefaultSettingsManager;
import com.just.agentwebX5.WebSettings;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

*/
/**
 * Author: Thomas.
 * <br/>Date: 2019/9/25 15:44
 * <br/>Email: 1071931588@qq.com
 * <br/>Description:
 *//*

public class WebFragment extends BaseFragment<DiscoverFragmentWebBinding> {
    private AgentWebX5 mAgentWeb;
    @Autowired(name = KeyCode.Discover.PATH)
    public String mPath;
    private ImageView ivClose;
    private AlertDialog mAlertDialog;

    @Override
    protected int onBindLayout() {
        return R.layout.discover_fragment_web;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView.setBackgroundColor(Color.WHITE);
    }
    @Override
    protected void initView() {
        ivClose = mSimpleTitleBar.getLeftCustomView().findViewById(R.id.iv_left);
        ivClose.setImageResource(R.drawable.ic_common_web_close);
    }

    @Override
    protected boolean enableSwipeBack() {
        return false;
    }

    @Override
    public void initListener() {
        super.initListener();
        ivClose.setOnClickListener(v -> showDialog());
    }

    @Override
    public void initData() {
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(mBinding.flContainer, new FrameLayout.LayoutParams(-1, -1))
                .setIndicatorColorWithHeight(getResources().getColor(R.color.colorPrimary), 1)
                .setWebSettings(getSettings())
                .setWebViewClient(mWebViewClient)
                .setWebChromeClient(mWebChromeClient)
                .setReceivedTitleCallback(mCallback)
                .setPermissionInterceptor(mPermissionInterceptor)
                .setNotifyIcon(R.mipmap.download)
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)
                .interceptUnkownScheme()
                .openParallelDownload()
                .setSecurityType(AgentWebX5.SecurityType.strict)
                .addDownLoadResultListener(mDownLoadResultListener)
                .createAgentWeb()
                .ready()
                .go(mPath);
    }
    public WebSettings getSettings() {
        return WebDefaultSettingsManager.getInstance();
    }


    protected com.tencent.smtt.sdk.WebViewClient mWebViewClient = new com.tencent.smtt.sdk.WebViewClient() {
        private HashMap<String, Long> timer = new HashMap<>();

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, String url) {
            //intent:// scheme????????? ????????????false ??? ????????? DefaultWebClient ?????? ??? ??????????????????Activity  ??? ??????Activity????????????????????????????????????.  true ????????????
            //???????????????????????? ???intent://play?vid=XODEzMjU1MTI4&refer=&tuid=&ua=Mozilla%2F5.0%20(Linux%3B%20Android%207.0%3B%20SM-G9300%20Build%2FNRD90M%3B%20wv)%20AppleWebKit%2F537.36%20(KHTML%2C%20like%20Gecko)%20Version%2F4.0%20Chrome%2F58.0.3029.83%20Mobile%20Safari%2F537.36&source=exclusive-pageload&cookieid=14971464739049EJXvh|Z6i1re#Intent;scheme=youku;package=com.youku.phone;end;
            //?????????????????????????????????????????? ??? ???????????????????????? true  ?????????????????? H5 ?????? ??????????????????????????????????????? ???????????? false ??? DefaultWebClient  ?????????intent ???????????? ????????? ??? ????????????????????????????????? ??????????????? ??? ????????????????????? ??? ??????????????? ??? ???????????????????????????????????? .
            if (url.startsWith("intent://"))
                return true;
            else if (url.startsWith("youku"))
                return true;
//            else if(isAlipay(view,url))  //????????????defaultWebClient?????????????????????
//                return true;


            return false;
        }

        int index=1;
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            timer.put(url, System.currentTimeMillis());
            if (url.equals(mPath)) {
                ivClose.setVisibility(View.GONE);
            } else {
                ivClose.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (timer.get(url) != null) {
                long overTime = System.currentTimeMillis();
                Long startTime = timer.get(url);
            }

        }
    };
    protected WebChromeClient mWebChromeClient = new WebChromeClient() {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }
        @Override
        public boolean onJsAlert(WebView webView, String s, String s1, JsResult jsResult) {
            return false;
        }
    };
    protected ChromeClientCallbackManager.ReceivedTitleCallback mCallback = (view, title) -> setTitle(new String[]{title});
    //AgentWeb ???????????????????????? Action ??????????????????????????? ?????????????????? ???
//?????? https//:www.baidu.com ??? Url ????????????????????? ??????false ???????????????????????????23 ??? agentWeb ????????????????????? ???true ???Url?????????????????????????????????
//??????????????????????????????????????? ??? ?????????????????????????????????????????? ???
    protected PermissionInterceptor mPermissionInterceptor = (url, permissions, action) -> {
        return false;
    };
    protected DownLoadResultListener mDownLoadResultListener = new DownLoadResultListener() {
        @Override
        public void success(String path) {
        }

        @Override
        public void error(String path, String resUrl, String cause, Throwable e) {
        }
    };

    private void showDialog() {

        if (mAlertDialog == null) {
            mAlertDialog = new AlertDialog.Builder(mActivity)
                    .setMessage("???????????????????????????????")
                    .setNegativeButton("?????????", (dialog, which) -> {
                        if (mAlertDialog != null) {
                            mAlertDialog.dismiss();
                        }
                    })
                    .setPositiveButton("??????", (dialog, which) -> {

                        if (mAlertDialog != null) {
                            mAlertDialog.dismiss();
                        }
                        pop();
                    }).create();
        }
        mAlertDialog.show();

    }


    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        EventBus.getDefault().post(new ActivityEvent(EventCode.Main.HIDE_GP));
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        EventBus.getDefault().post(new ActivityEvent(EventCode.Main.SHOW_GP));
    }

    @Override
    public void onResume() {
        if (mAgentWeb != null)
            mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mAgentWeb != null)
            mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAgentWeb != null) {
            mAgentWeb.getWebLifeCycle().onDestroy();
            mAgentWeb.getWebCreator().get().destroy();
        }
    }

    @Override
    protected boolean enableLazy() {
        return false;
    }

    @Override
    public void onSimpleBackClick() {
        if (!mAgentWeb.getWebCreator().get().canGoBack()) {
            pop();
        } else {
            mAgentWeb.back();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        if (super.onBackPressedSupport()) {
            return true;
        } else if (mAgentWeb.getWebCreator().get().canGoBack()) {
            mAgentWeb.back();
            return true;
        }
        return false;
    }

    @Override
    public Integer[] onBindBarRightIcon() {
        return new Integer[]{R.drawable.ic_common_share};
    }

    @Override
    public void onRight1Click(View v) {
        super.onRight1Click(v);
        Bitmap favicon = mAgentWeb.getWebCreator().get().getFavicon();
        String title = mAgentWeb.getWebCreator().get().getTitle();
        UMWeb web = new UMWeb(mPath);
        web.setTitle(title);//??????
        web.setThumb(new UMImage(mActivity, favicon));  //?????????
        web.setDescription("???????????????");//??????
        EventBus.getDefault().post(new ActivityEvent(EventCode.Main.SHARE, new ShareAction(mActivity).withMedia(web)));
    }
}
*/
