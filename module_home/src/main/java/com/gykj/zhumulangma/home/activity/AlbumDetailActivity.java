package com.gykj.zhumulangma.home.activity;


import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.CollectionUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gykj.zhumulangma.common.Constants;
import com.gykj.zhumulangma.common.adapter.TPagerAdapter;
import com.gykj.zhumulangma.common.adapter.TabNavigatorAdapter;
import com.gykj.zhumulangma.common.dialog.TrackPagerPopup;
import com.gykj.zhumulangma.common.event.KeyCode;
import com.gykj.zhumulangma.common.extra.ViewPagerHelper;
import com.gykj.zhumulangma.common.mvvm.view.BaseRefreshActivity;
import com.gykj.zhumulangma.common.mvvm.view.status.DetailSkeleton;
import com.gykj.zhumulangma.common.util.RouteHelper;
import com.gykj.zhumulangma.common.util.ToastUtil;
import com.gykj.zhumulangma.common.util.ZhumulangmaUtil;
import com.gykj.zhumulangma.common.widget.PlayingIconView;
import com.gykj.zhumulangma.home.R;
import com.gykj.zhumulangma.home.adapter.AlbumTagAdapter;
import com.gykj.zhumulangma.home.adapter.AlbumTrackAdapter;
import com.gykj.zhumulangma.home.databinding.HomeActivityAlbumDetailBinding;
import com.gykj.zhumulangma.home.databinding.HomeLayoutAlbumDetailBinding;
import com.gykj.zhumulangma.home.databinding.HomeLayoutAlbumTrackBinding;
import com.gykj.zhumulangma.home.mvvm.ViewModelFactory;
import com.gykj.zhumulangma.home.mvvm.viewmodel.AlbumDetailViewModel;
import com.jakewharton.rxbinding3.view.RxView;
import com.library.flowlayout.FlowLayoutManager;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.enums.PopupPosition;
import com.lxj.xpopup.interfaces.SimpleCallback;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;
import com.ximalaya.ting.android.sdkdownloader.XmDownloadManager;
import com.ximalaya.ting.android.sdkdownloader.downloadutil.DownloadState;
import com.ximalaya.ting.android.sdkdownloader.downloadutil.IDoSomethingProgress;
import com.ximalaya.ting.android.sdkdownloader.downloadutil.IXmDownloadTrackCallBack;
import com.ximalaya.ting.android.sdkdownloader.exception.AddDownloadException;
import com.ximalaya.ting.android.sdkdownloader.task.Callback;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Author: Thomas.
 * <br/>Date: 2019/8/14 13:41
 * <br/>Email: 1071931588@qq.com
 * <br/>Description:???????????????
 */
@Route(path = Constants.Router.Home.F_ALBUM_DETAIL)
public class AlbumDetailActivity extends BaseRefreshActivity<HomeActivityAlbumDetailBinding,
        AlbumDetailViewModel, Track> implements BaseQuickAdapter.OnItemClickListener, 
        BaseQuickAdapter.OnItemChildClickListener, View.OnClickListener,
        TrackPagerPopup.onPopupDismissingListener {

    @Autowired(name = KeyCode.Home.ALBUMID)
    public long mAlbumId;
    private Album mAlbum;
    private String mSort = "time_desc";
    private Track mLastPlay;
    private AlbumTrackAdapter mAlbumTrackAdapter;
    private AlbumTagAdapter mAlbumTagAdapter;
    private TrackPagerPopup mPagerPopup;
    private XmPlayerManager mPlayerManager = XmPlayerManager.getInstance(this);

    private HomeLayoutAlbumDetailBinding mDetailBind;
    private HomeLayoutAlbumTrackBinding mTrackBind;

    public AlbumDetailActivity() {
    }

    @Override
    public void initView() {
        super.initView();
        String[] tabs = {"??????", "??????"};

        mTrackBind = DataBindingUtil.inflate(getLayoutInflater(), R.layout.home_layout_album_track, null, false);
        mDetailBind = DataBindingUtil.inflate(getLayoutInflater(), R.layout.home_layout_album_detail, null, false);


        mDetailBind.rvTag.setLayoutManager(new FlowLayoutManager());
        mDetailBind.rvTag.setHasFixedSize(true);
        mAlbumTagAdapter = new AlbumTagAdapter(R.layout.common_item_tag);
        mAlbumTagAdapter.bindToRecyclerView(mDetailBind.rvTag);


        mBinding.viewpager.setAdapter(new TPagerAdapter(mDetailBind.getRoot(),mTrackBind.getRoot()));
        final CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new TabNavigatorAdapter(Arrays.asList(tabs), mBinding.viewpager, 125));
        mBinding.magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mBinding.magicIndicator, mBinding.viewpager);
        RecyclerView recyclerView = (RecyclerView)mBinding.viewpager.getChildAt(0);
        recyclerView.scrollToPosition(1);

        mAlbumTrackAdapter = new AlbumTrackAdapter(R.layout.home_item_album_track);
        mTrackBind.includeList.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mTrackBind.includeList.recyclerview.setHasFixedSize(true);
        mAlbumTrackAdapter.bindToRecyclerView(mTrackBind.includeList.recyclerview);
        mPagerPopup = new TrackPagerPopup(this, this);
        mPagerPopup.setDismissingListener(this);
    }

    @Override
    public void initListener() {
        super.initListener();
        XmDownloadManager.getInstance().addDownloadStatueListener(mDownloadStatueListener);
        mPlayerManager.addPlayerStatusListener(playerStatusListener);
        mAlbumTrackAdapter.setOnItemClickListener(this);
        mAlbumTrackAdapter.setOnItemChildClickListener(this);
        mDetailBind.includeAnnouncer.clAnnouncer.setOnClickListener(this);
        mTrackBind.llSelect.setOnClickListener(this);
        mTrackBind.llDownload.setOnClickListener(this);
        RxView.clicks(mTrackBind.llSort)
                .doOnSubscribe(this)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(unit -> {
                    if (mPagerPopup.isShow()) {
                        return;
                    }
                    mSort = "time_desc".equals(mSort) ? "time_asc" : "time_desc";
                    mViewModel.getTrackList(mSort);
                });
        RxView.clicks(mBinding.llPlay)
                .throttleFirst(1, TimeUnit.SECONDS)
                .doOnSubscribe(this)
                .subscribe(unit -> {
                    mBinding.tvPlay.setText("????????????");
                    if (mLastPlay != null) {
                        int index = mAlbumTrackAdapter.getData().indexOf(mLastPlay);
                        if (index != -1) {
                            mLastPlay = mAlbumTrackAdapter.getItem(index);
                            mBinding.gpLastplay.setVisibility(View.VISIBLE);
                            mBinding.tvLastplay.setText(getString(R.string.lastplay, mLastPlay.getTrackTitle()));
                            XmPlayerManager.getInstance(this).playList(mViewModel.getCommonTrackList(),
                                    index);
                            RouteHelper.navigateTo(Constants.Router.Home.F_PLAY_TRACK);
                        } else {
                            mViewModel.getPlayTrackList();
                        }
                    } else {
                        mLastPlay = mAlbumTrackAdapter.getItem(0);
                        mBinding.gpLastplay.setVisibility(View.VISIBLE);
                        mBinding.tvLastplay.setText(getString(R.string.lastplay, mLastPlay.getTrackTitle()));
                        XmPlayerManager.getInstance(this).playList(mViewModel.getCommonTrackList(), 0);
                        RouteHelper.navigateTo(Constants.Router.Home.F_PLAY_TRACK);
                    }
                });


        RxView.clicks(mBinding.llSubscribe)
                .doOnSubscribe(this)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(unit -> mViewModel.subscribe(mAlbum));
        RxView.clicks(mBinding.llUnsubscribe)
                .doOnSubscribe(this)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(unit -> mViewModel.unsubscribe(mAlbum));
    }


    @NonNull
    @Override
    protected WrapRefresh onBindWrapRefresh() {
        return new WrapRefresh(mTrackBind.includeList.refreshLayout, mAlbumTrackAdapter);
    }

    @Override
    public void initData() {
        mViewModel.setArguments(mAlbumId,mSort);
        mViewModel.init();
    }

    @Override
    public void initViewObservable() {
        mViewModel.getSubscribeEvent().observe(this, aBoolean -> {
            mBinding.llSubscribe.setVisibility(aBoolean ? View.GONE : View.VISIBLE);
            mBinding.llUnsubscribe.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
        });

        mViewModel.getAlbumEvent().observe(this, album -> {
            mAlbum = album;
            Glide.with(this).load(mAlbum.getCoverUrlMiddle()).into(mBinding.ivCover);
            mBinding.tvAlbum.setText(mAlbum.getAlbumTitle());
            mBinding.tvAuthor.setText((String.format(getResources().getString(R.string.zhubo),
                    mAlbum.getAnnouncer().getNickname())));
            mBinding.tvPlaycount.setText(String.format(getResources().getString(R.string.ci)
                    , ZhumulangmaUtil.toWanYi(mAlbum.getPlayCount())));
            mBinding.tvTrackcount.setText(String.format(getResources().getString(R.string.gong_ji),
                    mAlbum.getIncludeTrackCount()));
            mBinding.tvSbcount.setText(String.format(getResources().getString(R.string.sb)
                    , ZhumulangmaUtil.toWanYi(mAlbum.getSubscribeCount())));

            Glide.with(this).load(album.getAnnouncer().getAvatarUrl()).into(mDetailBind.includeAnnouncer.ivAnnouncerCover);
            mDetailBind.includeAnnouncer.tvAnnouncerName.setText(album.getAnnouncer().getNickname());
            mDetailBind.tvIntro.setText(album.getAlbumIntro());
            if (!TextUtils.isEmpty(album.getAlbumTags())) {
                mAlbumTagAdapter.addData(Arrays.asList(album.getAlbumTags().split(",")));
            }

            setPager(album.getIncludeTrackCount());

        });
        mViewModel.getInitTracksEvent().observe(this, trackList -> mAlbumTrackAdapter.setNewData(trackList.getTracks()));

        mViewModel.getPlayTracksEvent().observe(this, tracks -> {
            setPager(tracks.getTotalCount());
            mAlbumTrackAdapter.setNewData(tracks.getTracks());
            XmPlayerManager.getInstance(this).playList(mViewModel.getCommonTrackList(),
                    mAlbumTrackAdapter.getData().indexOf(mLastPlay));
            RouteHelper.navigateTo(Constants.Router.Home.F_PLAY_TRACK);
        });

        mViewModel.getTracksSortEvent().observe(this, tracks -> {
            if (CollectionUtils.isEmpty(tracks.getTracks())) {
                showEmptyView();
            } else {
                setPager(tracks.getTotalCount());
                mAlbumTrackAdapter.setNewData(tracks.getTracks());
            }
        });
        mViewModel.getLastplayEvent().observe(this, track -> {
            if (null != track) {
                mBinding.tvPlay.setText("????????????");
                mLastPlay = track;
                mBinding.gpLastplay.setVisibility(View.VISIBLE);
                mBinding.tvLastplay.setText(getString(R.string.lastplay, track.getTrackTitle()));
            }
        });
        mViewModel.getAnnouncerEvent().observe(this, announcer -> {
            mDetailBind.includeAnnouncer.tvVip.setVisibility(announcer.isVerified() ? View.VISIBLE : View.GONE);
            String vsignature = announcer.getVsignature();
            if (TextUtils.isEmpty(vsignature)) {
                mDetailBind.includeAnnouncer.tvVsignature.setVisibility(View.GONE);
            } else {
                mDetailBind.includeAnnouncer.tvVsignature.setVisibility(View.VISIBLE);
                mDetailBind.includeAnnouncer.tvVsignature.setText(vsignature);
            }
            mDetailBind.includeAnnouncer.tvFollowingCount.setText(getString(R.string.following_count,
                    ZhumulangmaUtil.toWanYi(announcer.getFollowerCount())));
        });
    }

    @Override
    protected void onRefreshSucc(List<Track> list) {
        mAlbumTrackAdapter.addData(0, list);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter == mAlbumTrackAdapter) {
            mPlayerManager.playList(mViewModel.getCommonTrackList(), position);
            mLastPlay = mAlbumTrackAdapter.getItem(position);
            mBinding.gpLastplay.setVisibility(View.VISIBLE);
            mBinding.tvLastplay.setText(getString(R.string.lastplay, mAlbumTrackAdapter.getItem(position).getTrackTitle()));
            RouteHelper.navigateTo(Constants.Router.Home.F_PLAY_TRACK);
        } else {
            mPagerPopup.dismissWith(() -> mViewModel.getTrackList(position + 1));
        }
    }

    @Override
    public void onItemChildClick(final BaseQuickAdapter adapter, View view, final int position) {
        int id = view.getId();
        if (R.id.iv_download == id) {
            XmDownloadManager.getInstance().downloadSingleTrack(
                    mAlbumTrackAdapter.getItem(position).getDataId(), new IDoSomethingProgress<AddDownloadException>() {
                        @Override
                        public void begin() {
                        }

                        @Override
                        public void success() {
                        }

                        @Override
                        public void fail(AddDownloadException e) {
                            if (e.getCode() == AddDownloadException.CODE_NULL) {
                                ToastUtil.showToast("???????????????null");
                            } else if (e.getCode() == AddDownloadException.CODE_MAX_OVER) {
                                ToastUtil.showToast("?????????????????????????????????");
                            } else if (e.getCode() == AddDownloadException.CODE_NOT_FIND_TRACK) {
                                ToastUtil.showToast("???????????????????????????");
                            } else if (e.getCode() == AddDownloadException.CODE_MAX_DOWNLOADING_COUNT) {
                                ToastUtil.showToast("???????????????????????????????????????500");
                            } else if (e.getCode() == AddDownloadException.CODE_DISK_OVER) {
                                ToastUtil.showToast("????????????");
                            } else if (e.getCode() == AddDownloadException.CODE_MAX_SPACE_OVER) {
                                ToastUtil.showToast("?????????????????????????????????????????????");
                            } else if (e.getCode() == AddDownloadException.CODE_NO_PAY_SOUND) {
                                ToastUtil.showToast("???????????????????????????????????????");
                            }
                        }
                    });
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (R.id.ll_select == id) {
            switchPager();
        } else if (id == R.id.ll_download) {
            RouteHelper.navigateTo(mRouter.build(Constants.Router.Listen.F_BATCH_DOWNLOAD)
                    .withLong(KeyCode.Home.ALBUMID, mAlbumId));
        } else if (id == R.id.cl_announcer) {
            RouteHelper.navigateTo(mRouter.build(Constants.Router.Home.F_ANNOUNCER_DETAIL)
                    .withLong(KeyCode.Home.ANNOUNCER_ID, mAlbum.getAnnouncer().getAnnouncerId())
                    .withString(KeyCode.Home.ANNOUNCER_NAME, mAlbum.getAnnouncer().getNickname()));
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mAlbumTrackAdapter.notifyDataSetChanged();
    }

    @Override
    public int onBindLayout() {
        return R.layout.home_activity_album_detail;
    }

    @Override
    public String[] onBindBarTitleText() {
        return new String[]{"????????????"};
    }

    @Override
    public Integer[] onBindBarRightIcon() {
        return new Integer[]{R.drawable.ic_common_more, R.drawable.ic_common_share};
    }


    @Override
    public ViewModelProvider.Factory onBindViewModelFactory() {
        return ViewModelFactory.getInstance(getApplication());
    }

    @Override
    public void onRight2Click(View v) {
        super.onRight2Click(v);
        share(null);
    }

    @Override
    public com.kingja.loadsir.callback.Callback getInitStatus() {
        return new DetailSkeleton();
    }

    /**
     * ????????????
     *
     * @param totalcount
     */
    private void setPager(long totalcount) {
        int pagesize = 20;

        mTrackBind.tvPagecount.setText(getString(R.string.pagecount, totalcount));
        List<String> list = new ArrayList<>();
        if (mSort.equals("time_desc")) {
            for (int i = 0; i < totalcount / pagesize; i++) {
                list.add(totalcount - (i * pagesize) + "~" + (totalcount - ((i + 1) * pagesize) + 1));
            }
            if (totalcount % pagesize != 0) {
                list.add(totalcount - totalcount / pagesize * pagesize + "~1");
            }
        } else {
            for (int i = 0; i < totalcount / pagesize; i++) {
                list.add((i * pagesize + 1) + "~" + ((i + 1) * pagesize));
            }
            if (totalcount % pagesize != 0) {
                list.add((totalcount / pagesize * pagesize + 1) + "~" + totalcount);
            }

        }
        mPagerPopup.getPagerAdapter().setNewData(list);
    }

    /**
     * ??????????????????
     *
     * @param track
     */
    private void updateDownloadStatus(Track track) {
        List<Track> tracks = mAlbumTrackAdapter.getData();
        int index = mAlbumTrackAdapter.getData().indexOf(track);

        if (index != -1) {
            DownloadState downloadStatus = XmDownloadManager.getInstance()
                    .getSingleTrackDownloadStatus(tracks.get(index).getDataId());
            View ivDownload = mAlbumTrackAdapter.getViewByPosition(index, R.id.iv_download);
            View progress = mAlbumTrackAdapter.getViewByPosition(index, R.id.progressBar);
            View ivDownloadSucc = mAlbumTrackAdapter.getViewByPosition(index, R.id.iv_downloadsucc);
            if (ivDownload == null || progress == null || ivDownloadSucc == null) {
                mAlbumTrackAdapter.notifyItemChanged(index);
                return;
            }
            switch (downloadStatus) {
                case FINISHED:
                    ivDownloadSucc.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.GONE);
                    ivDownload.setVisibility(View.GONE);
                    break;
                case STARTED:
                case WAITING:
                    ivDownloadSucc.setVisibility(View.GONE);
                    progress.setVisibility(View.VISIBLE);
                    ivDownload.setVisibility(View.GONE);
                    break;
                case STOPPED:
                case NOADD:
                case ERROR:
                    ivDownloadSucc.setVisibility(View.GONE);
                    progress.setVisibility(View.GONE);
                    ivDownload.setVisibility(View.VISIBLE);
                    break;
            }
        }

    }

    /**
     * ??????????????????
     */
    private void updatePlayStatus() {
        if (mPlayerManager.getCurrSound().getKind() != PlayableModel.KIND_TRACK) {
            return;
        }
        Track track = mPlayerManager.getCurrSoundIgnoreKind(true);
        if (null == track) {
            return;
        }
        List<Track> tracks = mAlbumTrackAdapter.getData();

        if (mAlbumId == track.getAlbum().getAlbumId()) {
            mLastPlay = track;
            mBinding.gpLastplay.setVisibility(View.VISIBLE);
            mBinding.tvLastplay.setText(getString(R.string.lastplay, mLastPlay.getTrackTitle()));
        }
        for (int i = 0; i < tracks.size(); i++) {
            PlayingIconView lavPlaying = (PlayingIconView) mAlbumTrackAdapter
                    .getViewByPosition(i, R.id.lav_playing);

            if (null != lavPlaying) {
                if (tracks.get(i).getDataId() == track.getDataId()) {
                    lavPlaying.setVisibility(View.VISIBLE);
                    if (XmPlayerManager.getInstance(this).isPlaying()) {
                        lavPlaying.playAnimation();
                    } else {
                        lavPlaying.pauseAnimation();
                    }
                } else {
                    lavPlaying.cancelAnimation();
                    lavPlaying.setVisibility(View.GONE);
                }
            } else {
                mAlbumTrackAdapter.notifyItemChanged(i);
            }
        }
    }

    /**
     * ??????????????????
     */
    private void updatePlayStatus(int currPos, int duration) {
        Track track = mPlayerManager.getCurrSoundIgnoreKind(true);
        if (null == track) {
            return;
        }
        int index = mAlbumTrackAdapter.getData().indexOf(track);
        if (index != -1) {
            TextView tvHasplay = (TextView) mAlbumTrackAdapter.getViewByPosition(index, R.id.tv_hasplay);
            mAlbumTrackAdapter.getItem(index).setSource(100 * currPos / duration);
            if (null != tvHasplay && mAlbumTrackAdapter.getItem(index).getDataId() == track.getDataId()) {
                tvHasplay.setText(getString(R.string.hasplay, mAlbumTrackAdapter.getItem(index).getSource()));
            } else {
                mAlbumTrackAdapter.notifyItemChanged(index);
            }
        }
    }

    /**
     * ??????????????????????????????
     */
    private void switchPager() {

        if (mPagerPopup.isShow()) {
            mPagerPopup.dismiss();
        } else {
            mTrackBind.ivSelectPage.animate().rotation(-90).setDuration(200);
            new XPopup.Builder(this).atView(mTrackBind.clActionbar).setPopupCallback(new SimpleCallback() {
                @Override
                public void onCreated(BasePopupView popupView) {
                    super.onCreated(popupView);
                    mPagerPopup.getRvPager().setOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                            changePageStatus();
                        }
                    });
                }

                @Override
                public void beforeShow(BasePopupView popupView) {
                    super.beforeShow(popupView);
                    changePageStatus();
                }
            }).popupPosition(PopupPosition.Bottom)
                    .asCustom(mPagerPopup).show();
        }
    }

    /**
     * ????????????????????????
     */
    private void changePageStatus() {
        for (int i = 0; i < mPagerPopup.getPagerAdapter().getData().size(); i++) {
            TextView viewByPosition = (TextView) mPagerPopup.getPagerAdapter().getViewByPosition(i, R.id.tv_page);
            if (viewByPosition != null) {
                if (mViewModel.getUpTrackPage() <= i && i <= mViewModel.getCurTrackPage() - 2) {
                    viewByPosition.setBackgroundResource(R.drawable.shap_common_pager_selected);
                    viewByPosition.setTextColor(Color.WHITE);
                } else {
                    viewByPosition.setBackgroundResource(R.drawable.shap_common_pager_defualt);
                    viewByPosition.setTextColor(getResources().getColor(R.color.textColorPrimary));
                }
            } else {
                mPagerPopup.getPagerAdapter().notifyItemChanged(i);
            }
        }
    }

    /**
     * ??????????????????
     */
    private IXmDownloadTrackCallBack mDownloadStatueListener = new IXmDownloadTrackCallBack() {

        @Override
        public void onWaiting(Track track) {
            updateDownloadStatus(track);
        }

        @Override
        public void onStarted(Track track) {
            updateDownloadStatus(track);
        }

        @Override
        public void onSuccess(Track track) {
            updateDownloadStatus(track);
        }

        @Override
        public void onError(Track track, Throwable throwable) {
            updateDownloadStatus(track);
            throwable.printStackTrace();
        }

        @Override
        public void onCancelled(Track track, Callback.CancelledException e) {
            updateDownloadStatus(track);
        }

        @Override
        public void onProgress(Track track, long l, long l1) {

        }

        @Override
        public void onRemoved() {
        }
    };
    private IXmPlayerStatusListener playerStatusListener = new IXmPlayerStatusListener() {

        @Override
        public void onPlayStart() {
            updatePlayStatus();
        }

        @Override
        public void onPlayPause() {
            updatePlayStatus();
        }

        @Override
        public void onPlayStop() {
            updatePlayStatus();
        }

        @Override
        public void onSoundPlayComplete() {
            updatePlayStatus();
        }

        @Override
        public void onSoundPrepared() {
            updatePlayStatus();
        }

        @Override
        public void onSoundSwitch(PlayableModel playableModel, PlayableModel playableModel1) {
            updatePlayStatus();

        }

        @Override
        public void onBufferingStart() {

        }

        @Override
        public void onBufferingStop() {

        }

        @Override
        public void onBufferProgress(int i) {

        }

        @Override
        public void onPlayProgress(int i, int i1) {
            updatePlayStatus(i, i1);
        }

        @Override
        public boolean onError(XmPlayerException e) {
            return false;
        }

    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAlbumTrackAdapter != null) {
            mAlbumTrackAdapter.setOnItemClickListener(null);
            mAlbumTrackAdapter.setOnItemChildClickListener(null);
        }
        XmDownloadManager.getInstance().removeDownloadStatueListener(mDownloadStatueListener);
        mPlayerManager.removePlayerStatusListener(playerStatusListener);
    }

    @Override
    public void onDismissing() {
        mTrackBind.ivSelectPage.animate().rotation(90).setDuration(200);
    }
}
