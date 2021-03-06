package com.gykj.zhumulangma.home.activity;


import android.Manifest;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.BarUtils;
import com.gykj.zhumulangma.common.Constants;
import com.gykj.zhumulangma.common.bean.SearchHistoryBean;
import com.gykj.zhumulangma.common.event.KeyCode;
import com.gykj.zhumulangma.common.mvvm.view.BaseMvvmActivity;
import com.gykj.zhumulangma.common.util.SpeechUtil;
import com.gykj.zhumulangma.common.util.ToastUtil;
import com.gykj.zhumulangma.home.R;
import com.gykj.zhumulangma.home.databinding.HomeActivitySearchBinding;
import com.gykj.zhumulangma.home.dialog.SpeechPopup;
import com.gykj.zhumulangma.home.fragment.SearchHistoryFragment;
import com.gykj.zhumulangma.home.fragment.SearchResultFragment;
import com.gykj.zhumulangma.home.fragment.SearchSuggestFragment;
import com.gykj.zhumulangma.home.mvvm.ViewModelFactory;
import com.gykj.zhumulangma.home.mvvm.viewmodel.SearchViewModel;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.jakewharton.rxbinding3.view.RxView;
import com.jakewharton.rxbinding3.widget.RxTextView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.interfaces.SimpleCallback;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wuhenzhizao.titlebar.statusbar.StatusBarUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Author: Thomas.
 * <br/>Date: 2019/9/10 8:23
 * <br/>Email: 1071931588@qq.com
 * <br/>Description:?????????
 */
@Route(path = Constants.Router.Home.F_SEARCH)
public class SearchActivity extends BaseMvvmActivity<HomeActivitySearchBinding, SearchViewModel> implements
        View.OnClickListener, SearchHistoryFragment.onSearchListener, View.OnFocusChangeListener,
        TextView.OnEditorActionListener, SearchSuggestFragment.onSearchListener {

    @Autowired(name = KeyCode.Home.HOTWORD)
    public String mHotword;
    private SearchSuggestFragment mSuggestFragment;
    private SearchHistoryFragment mHistoryFragment;
    private SearchResultFragment mResultFragment;

    private Observable<CharSequence> suggestObservable;
    private Disposable suggestDisposable;
    private SpeechRecognizer mIat;
    private SpeechPopup mSpeechPopup;
    private final HashMap<String, String> mIatResults = new LinkedHashMap<>();

    private View vDialog;

    public SearchActivity() {

    }

    @Override
    public int onBindLayout() {
        return R.layout.home_activity_search;
    }


    @Override
    public void initView() {

        mHistoryFragment = new SearchHistoryFragment();
        mHistoryFragment.setSearchListener(this);
        mSuggestFragment = new SearchSuggestFragment();
        mSuggestFragment.setSearchListener(this);
        beginTransaction().add(R.id.fl_container, mHistoryFragment).commitAllowingStateLoss();
        showSoftInput(mBinding.etKeyword);

        if (StatusBarUtils.supportTransparentStatusBar()) {
            mBinding.clTitlebar.setPadding(0, BarUtils.getStatusBarHeight(), 0, 0);
        }
        //?????????x86
        mIat = SpeechRecognizer.createRecognizer(this, mInitListener);
        try {
            //??????json??????
            mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        } catch (Exception e) {
            Log.w(TAG, "??????????????????????????????x86 so?????????????????????????????????");
        }
        mSpeechPopup = new SpeechPopup(this);
    }

    @Override
    public void initListener() {
        super.initListener();
        suggestObservable = RxTextView.textChanges(mBinding.etKeyword)
                .debounce(300, TimeUnit.MILLISECONDS)
                .skip(1)
                .doOnSubscribe(d -> {
                    suggestDisposable = d;
                    accept(d);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(charSequence -> {
                    if (TextUtils.isEmpty(charSequence.toString().trim())) {
                        beginTransaction().show(mHistoryFragment).remove(mSuggestFragment).commitAllowingStateLoss();
                    } else {
                        mSuggestFragment.setKeyword(charSequence.toString());
                        if (getTopFragment() != mSuggestFragment) {
                            beginTransaction().hide(mHistoryFragment).add(R.id.fl_container, mSuggestFragment).commitAllowingStateLoss();
                        }
                    }
                });
        mBinding.ivPop.setOnClickListener(this);
        mBinding.ivSpeech.setOnClickListener(this);
        mBinding.etKeyword.setOnFocusChangeListener(this);
        suggestObservable.subscribe();
        mBinding.etKeyword.setOnEditorActionListener(this);
        RxView.clicks(mBinding.tvSearch)
                .doOnSubscribe(this)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(unit -> preSearch());
    }

    @Override
    public void initData() {
        if (mHotword != null) {
            mBinding.etKeyword.setHint(mHotword);
        } else {
            mViewModel.getHotWords();
        }
    }

    @Override
    public boolean enableSimplebar() {
        return false;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_pop) {
            finish();
        } else if (id == R.id.iv_speech) {
            new RxPermissions(this).request(new String[]{Manifest.permission.RECORD_AUDIO})
                    .subscribe(granted -> {
                        if (granted) {
                            try {
                                mIat.startListening(mRecognizerListener);
                            } catch (Exception e) {
                                ToastUtil.showToast(ToastUtil.LEVEL_E, "??????????????????????????????PC?????????");
                            }
                        } else {
                            ToastUtil.showToast("????????????????????????????????????");
                        }
                    });
        }
    }

    @Override
    public void onSearch(String keyword) {
        hideSoftInput();
        suggestDisposable.dispose();
        mBinding.etKeyword.setText(keyword);
        suggestObservable.subscribe();
        search(keyword);
    }

    private void search(String keyword) {
        SearchHistoryBean searchHistoryBean = new SearchHistoryBean();
        searchHistoryBean.setKeyword(keyword);
        searchHistoryBean.setDatatime(System.currentTimeMillis());
        mViewModel.insertHistory(searchHistoryBean);
        //??????????????????????????????
        mBinding.etKeyword.clearFocus();

        mResultFragment = (SearchResultFragment) mRouter.build(Constants.Router.Home.F_SEARCH_RESULT)
                .withString(KeyCode.Home.KEYWORD, keyword).navigation();
        if(mSuggestFragment.isResumed()){
            beginTransaction().remove(mSuggestFragment).add(R.id.fl_container, mResultFragment).commitAllowingStateLoss();
        }else {
            beginTransaction().hide(mHistoryFragment).add(R.id.fl_container, mResultFragment).commitAllowingStateLoss();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus && hasInit) {
            beginTransaction().show(mHistoryFragment).remove(mResultFragment).commitAllowingStateLoss();
        }
    }


    @Override
    public ViewModelProvider.Factory onBindViewModelFactory() {
        return ViewModelFactory.getInstance(getApplication());
    }

    @Override
    public void initViewObservable() {
        mViewModel.getHotWordsEvent().observe(this, hotWords ->
                mBinding.etKeyword.setHint(hotWords.get(0).getSearchword()));
        mViewModel.getInsertHistoryEvent().observe(this, bean -> mHistoryFragment.refreshHistory());
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            preSearch();
        }
        return false;
    }

    private void preSearch() {
        if (getTopFragment() instanceof SearchResultFragment) {
            return;
        }
        if (mBinding.etKeyword.getText().toString().trim().length() != 0) {
            onSearch(mBinding.etKeyword.getText().toString());
        } else if (mBinding.etKeyword.getHint().toString().length() != 0) {
            mBinding.etKeyword.setText(mBinding.etKeyword.getHint());
            onSearch(mBinding.etKeyword.getHint().toString());
        } else {
            ToastUtil.showToast("??????????????????????????????");
        }
    }


    /**
     * ?????????????????????
     */
    private InitListener mInitListener = code -> {
        //???????????????https://www.xfyun.cn/document/error-code??????????????????
        Log.d(TAG, "SpeechRecognizer init() code = " + code);
        if (code != ErrorCode.SUCCESS) {
            ToastUtil.showToast("??????????????????????????????" + code);
        }
    };

    /**
     * ??????????????????
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // ??????????????????sdk??????????????????????????????????????????????????????????????????
            hideSoftInput();
            new XPopup.Builder(SearchActivity.this).popupAnimation(PopupAnimation.NoAnimation)
                    .dismissOnTouchOutside(false).setPopupCallback(new SimpleCallback() {
                @Override
                public void onCreated(BasePopupView popupView) {
                    super.onCreated(popupView);
                    vDialog = mSpeechPopup.getPopupImplView();
                }

                @Override
                public void onShow(BasePopupView popupView) {
                    super.onShow(popupView);
                    vDialog.findViewById(R.id.lav_speech).setVisibility(View.VISIBLE);
                    vDialog.findViewById(R.id.lav_loading).setVisibility(View.GONE);
                }

                @Override
                public void onDismiss(BasePopupView popupView) {
                    super.onDismiss(popupView);
                    mIat.cancel();
                }
            }).asCustom(mSpeechPopup).show();
        }

        @Override
        public void onError(SpeechError error) {
            Log.d(TAG, "onError: " + error);
            // Tips???
            // ????????????10118(???????????????)????????????????????????????????????????????????????????????????????????????????????
            if (error.getErrorCode() == 10118) {
                ToastUtil.showToast(error.getErrorDescription());
            }
            mSpeechPopup.dismiss();
        }

        @Override
        public void onEndOfSpeech() {
            // ??????????????????????????????????????????????????????????????????????????????????????????????????????
            vDialog.findViewById(R.id.lav_speech).setVisibility(View.GONE);
            vDialog.findViewById(R.id.lav_loading).setVisibility(View.VISIBLE);
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            printResult(results);
            mSpeechPopup.dismiss();
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {

        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }
    };

    private void printResult(RecognizerResult results) {
        String text = SpeechUtil.parseIatResult(results.getResultString());
        String sn = null;
        // ??????json????????????sn??????
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mIatResults.put(sn, text);
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : mIatResults.keySet()) {
            stringBuilder.append(mIatResults.get(key));
        }
        mBinding.etKeyword.setText(stringBuilder.toString());
        mBinding.etKeyword.setSelection(mBinding.etKeyword.length());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            mIat.destroy();
        } catch (Exception e) {
            Log.w(TAG, "??????????????????????????????x86 so?????????????????????????????????");
        }
    }
}
