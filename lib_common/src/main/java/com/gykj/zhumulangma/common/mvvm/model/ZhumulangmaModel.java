package com.gykj.zhumulangma.common.mvvm.model;

import android.app.Application;

import androidx.annotation.Nullable;

import com.gykj.zhumulangma.common.extra.RxField;
import com.gykj.zhumulangma.common.net.RxAdapter;
import com.gykj.zhumulangma.common.net.dto.BannerDTO;
import com.gykj.zhumulangma.common.net.dto.ColumnDTO;
import com.gykj.zhumulangma.common.net.dto.ColumnDetailDTO;
import com.gykj.zhumulangma.common.net.dto.ColumnInfoDTO;
import com.gykj.zhumulangma.common.net.exception.CustException;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.AlbumList;
import com.ximalaya.ting.android.opensdk.model.album.AnnouncerListByIds;
import com.ximalaya.ting.android.opensdk.model.album.BatchAlbumList;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;
import com.ximalaya.ting.android.opensdk.model.album.RelativeAlbums;
import com.ximalaya.ting.android.opensdk.model.album.SearchAlbumList;
import com.ximalaya.ting.android.opensdk.model.announcer.AnnouncerCategoryList;
import com.ximalaya.ting.android.opensdk.model.announcer.AnnouncerList;
import com.ximalaya.ting.android.opensdk.model.column.ColumnList;
import com.ximalaya.ting.android.opensdk.model.download.RecommendDownload;
import com.ximalaya.ting.android.opensdk.model.live.program.ProgramList;
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio;
import com.ximalaya.ting.android.opensdk.model.live.radio.RadioList;
import com.ximalaya.ting.android.opensdk.model.live.radio.RadioListByCategory;
import com.ximalaya.ting.android.opensdk.model.live.radio.RadioListById;
import com.ximalaya.ting.android.opensdk.model.live.schedule.Schedule;
import com.ximalaya.ting.android.opensdk.model.track.AnnouncerTrackList;
import com.ximalaya.ting.android.opensdk.model.track.LastPlayTrackList;
import com.ximalaya.ting.android.opensdk.model.track.SearchTrackList;
import com.ximalaya.ting.android.opensdk.model.track.SearchTrackListV2;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;
import com.ximalaya.ting.android.opensdk.model.user.XmBaseUserInfo;
import com.ximalaya.ting.android.opensdk.model.word.HotWordList;
import com.ximalaya.ting.android.opensdk.model.word.SuggestWords;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

import static com.gykj.zhumulangma.common.util.ZhumulangmaUtil.commonParams;

/**
 * Author: Thomas.
 * <br/>Date: 2019/7/31 17:27
 * <br/>Email: 1071931588@qq.com
 * <br/>Description:??????????????????Model
 */
public class ZhumulangmaModel extends BaseModel {

    public static final String OPERATION_CATEGORY_ID = "operation_category_id";
    public static final String COLUMN_CATEGORY_CROSSTALK = "1000537";
    public static final String COLUMN_CATEGORY_NOVEL= "1000538";
    public static final String COLUMN_CATEGORY_CHILD = "1000535";
    public static final String CONFIG_BANNER_ID = "35533";
    public static final int NOVEL_NAVIGATION_CATEGORY= 3;
    public static final int HOT_NAVIGATION_CATEGORY= 1;
    public static final int CHILD_NAVIGATION_CATEGORY= 6;
    public static final int CROSSTALK_NAVIGATION_CATEGORY= 12;
    public static final String IS_PAID = "is_paid";
    public static final String COLUMN_TITLE_SEPARATOR = "/";
    public static final String COLUMN_PAGE_SIZE = "3";
    public static final int COLUMN_TITLE_INDEX = 0;
    public static final int COLUMN_SIZE_INDEX = 1;
    public static final int COLUMN_ALBUM_TYPE_INDEX = 2;
    public static final String SCOPE = "scope";
    public static final String IDS = "ids";

    public ZhumulangmaModel(Application application) {
        super(application);
    }

    /**
     * ???????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<BannerDTO> getBanners(Map<String, String> specificParams) {
        return commonParams(specificParams).flatMap((Function<Map<String, String>,
                ObservableSource<BannerDTO>>) stringStringMap ->
                mNetManager.getHomeService().getBanners(stringStringMap))
                .compose(RxAdapter.schedulersTransformer())
                .compose(RxAdapter.exceptionTransformer());
    }
    /**
     * ???????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<BannerDTO> getBanners1(Map<String, String> specificParams) {
        return commonParams(specificParams).flatMap((Function<Map<String, String>,
                ObservableSource<BannerDTO>>) stringStringMap ->
                mNetManager.getHomeService().getBanners(stringStringMap))
                .compose(RxAdapter.exceptionTransformer());
    }
    /**
     * ?????????????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<ColumnDTO> getColumns1(Map<String, String> specificParams) {
        return commonParams(specificParams).flatMap((Function<Map<String, String>,
                ObservableSource<ColumnDTO>>) stringStringMap ->
                mNetManager.getHomeService().getColumns(stringStringMap))
                .compose(RxAdapter.exceptionTransformer());
    }
    /**
     * ?????????????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<ColumnDTO> getColumns(Map<String, String> specificParams) {
        return commonParams(specificParams).flatMap((Function<Map<String, String>,
                ObservableSource<ColumnDTO>>) stringStringMap ->
                mNetManager.getHomeService().getColumns(stringStringMap))
                .compose(RxAdapter.schedulersTransformer())
                .compose(RxAdapter.exceptionTransformer());
    }

    /**
     * ????????????????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<ColumnInfoDTO> getColumnInfo(Map<String, String> specificParams) {
        return commonParams(specificParams).flatMap((Function<Map<String, String>,
                ObservableSource<ColumnInfoDTO>>) stringStringMap ->
                mNetManager.getHomeService().getColumnInfo(stringStringMap))
                .compose(RxAdapter.schedulersTransformer())
                .compose(RxAdapter.exceptionTransformer());
    }

    /**
     * ????????????????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<ColumnDetailDTO<Album>> getBrowseAlbumColumn1(Map<String, String> specificParams) {
        return commonParams(specificParams).flatMap((Function<Map<String, String>,
                ObservableSource<ColumnDetailDTO<Album>>>) stringStringMap ->
                mNetManager.getHomeService().getBrowseAlbumColumn(stringStringMap))
                .compose(RxAdapter.exceptionTransformer());
    }
    /**
     * ????????????????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<ColumnDetailDTO<Album>> getBrowseAlbumColumn(Map<String, String> specificParams) {
        return commonParams(specificParams).flatMap((Function<Map<String, String>,
                ObservableSource<ColumnDetailDTO<Album>>>) stringStringMap ->
                mNetManager.getHomeService().getBrowseAlbumColumn(stringStringMap))
                .compose(RxAdapter.schedulersTransformer())
                .compose(RxAdapter.exceptionTransformer());
    }
    /**
     * ??????????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<List<Album>> getGuessLike(Map<String, String> specificParams) {
        return commonParams(specificParams).flatMap((Function<Map<String, String>,
                ObservableSource<List<Album>>>) stringStringMap ->
                mNetManager.getHomeService().getGuessLike(stringStringMap))
                .compose(RxAdapter.exceptionTransformer());
    }

    /**
     * ??????????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<GussLikeAlbumList> getGuessLikeAlbum(Map<String, String> specificParams) {
        return Observable.create((ObservableOnSubscribe<GussLikeAlbumList>) emitter ->
                CommonRequest.getGuessLikeAlbum(specificParams,
                        new IDataCallBack<GussLikeAlbumList>() {
                            @Override
                            public void onSuccess(@Nullable GussLikeAlbumList gussLikeAlbumList) {
                                emitter.onNext(gussLikeAlbumList);
                                emitter.onComplete();
                            }

                            @Override
                            public void onError(int i, String s) {
                                emitter.onError(new CustException(String.valueOf(i), s));
                            }
                        })).compose(RxAdapter.exceptionTransformer());
    }

    /**
     * ????????????????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<AlbumList> getAllPaidAlbums(Map<String, String> specificParams) {
        return Observable.create((ObservableOnSubscribe<AlbumList>) emitter ->
                CommonRequest.getAllPaidAlbums(specificParams,
                        new IDataCallBack<AlbumList>() {
                            @Override
                            public void onSuccess(@Nullable AlbumList albumList) {
                                emitter.onNext(albumList);
                                emitter.onComplete();
                            }

                            @Override
                            public void onError(int i, String s) {
                                emitter.onError(new CustException(String.valueOf(i), s));
                            }
                        })).compose(RxAdapter.exceptionTransformer());
    }

    /**
     * ??????????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<AlbumList> getAlbumList(Map<String, String> specificParams) {
        return Observable.create((ObservableOnSubscribe<AlbumList>) emitter ->
                CommonRequest.getAlbumList(specificParams,
                        new IDataCallBack<AlbumList>() {
                            @Override
                            public void onSuccess(@Nullable AlbumList albumList) {
                                emitter.onNext(albumList);
                                emitter.onComplete();
                            }

                            @Override
                            public void onError(int i, String s) {
                                emitter.onError(new CustException(String.valueOf(i), s));
                            }
                        })).compose(RxAdapter.exceptionTransformer());
    }

    /**
     * ????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<RadioList> getRadios(Map<String, String> specificParams) {
      /*  return Observable.create((ObservableOnSubscribe<RadioList>) emitter ->
                CommonRequest.getRadios(specificParams,
                        new IDataCallBack<RadioList>() {
                            @Override
                            public void onSuccess(@Nullable RadioList radioList) {
                                emitter.onNext(radioList);
                                emitter.onComplete();
                            }

                            @Override
                            public void onError(int i, String s) {
                                emitter.onError(new CustException(String.valueOf(i), s));
                            }
                        })).compose(RxAdapter.exceptionTransformer());*/
        return null;
    }

    /**
     * ????????????????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<ColumnList> getColumnList(Map<String, String> specificParams) {
        return Observable.create((ObservableOnSubscribe<ColumnList>) emitter ->
                CommonRequest.getColumnList(specificParams,
                        new IDataCallBack<ColumnList>() {
                            @Override
                            public void onSuccess(@Nullable ColumnList columnList) {
                                emitter.onNext(columnList);
                                emitter.onComplete();
                            }

                            @Override
                            public void onError(int i, String s) {
                                emitter.onError(new CustException(String.valueOf(i), s));
                            }
                        })).compose(RxAdapter.exceptionTransformer());
    }

    /**
     * ??????tag??????????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<AlbumList> getPaidAlbumByTag(Map<String, String> specificParams) {
        return Observable.create((ObservableOnSubscribe<AlbumList>) emitter ->
                CommonRequest.getPaidAlbumByTag(specificParams,
                        new IDataCallBack<AlbumList>() {
                            @Override
                            public void onSuccess(@Nullable AlbumList albumList) {
                                emitter.onNext(albumList);
                                emitter.onComplete();
                            }

                            @Override
                            public void onError(int i, String s) {
                                emitter.onError(new CustException(String.valueOf(i), s));
                            }
                        })).compose(RxAdapter.exceptionTransformer());
    }

    /**
     * ????????????????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<RadioList> getRadiosByCity(Map<String, String> specificParams) {
     /*   return Observable.create((ObservableOnSubscribe<RadioList>) emitter ->
                CommonRequest.getRadiosByCity(specificParams,
                        new IDataCallBack<RadioList>() {
                            @Override
                            public void onSuccess(@Nullable RadioList radioList) {
                                emitter.onNext(radioList);
                                emitter.onComplete();
                            }

                            @Override
                            public void onError(int i, String s) {
                                emitter.onError(new CustException(String.valueOf(i), s));
                            }
                        })).compose(RxAdapter.exceptionTransformer());*/

        return null;
    }

    /**
     * ??????????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<RadioList> getRankRadios(Map<String, String> specificParams) {
       /* return Observable.create((ObservableOnSubscribe<RadioList>) emitter ->
                CommonRequest.getRankRadios(specificParams,
                        new IDataCallBack<RadioList>() {
                            @Override
                            public void onSuccess(@Nullable RadioList radioList) {
                                emitter.onNext(radioList);
                                emitter.onComplete();
                            }

                            @Override
                            public void onError(int i, String s) {
                                emitter.onError(new CustException(String.valueOf(i), s));
                            }
                        })).compose(RxAdapter.exceptionTransformer());*/

        return null;
    }

    /**
     * ????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<HotWordList> getHotWords(Map<String, String> specificParams) {
        return Observable.create((ObservableOnSubscribe<HotWordList>) emitter ->
                CommonRequest.getHotWords(specificParams,
                        new IDataCallBack<HotWordList>() {
                            @Override
                            public void onSuccess(@Nullable HotWordList hotWordList) {
                                emitter.onNext(hotWordList);
                                emitter.onComplete();
                            }

                            @Override
                            public void onError(int i, String s) {
                                emitter.onError(new CustException(String.valueOf(i), s));
                            }
                        })).compose(RxAdapter.exceptionTransformer());
    }

    /**
     * ????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<SearchAlbumList> getSearchedAlbums(Map<String, String> specificParams) {
        return Observable.create((ObservableOnSubscribe<SearchAlbumList>) emitter ->
                CommonRequest.getSearchedAlbums(specificParams,
                        new IDataCallBack<SearchAlbumList>() {
                            @Override
                            public void onSuccess(@Nullable SearchAlbumList albumList) {
                                emitter.onNext(albumList);
                                emitter.onComplete();
                            }

                            @Override
                            public void onError(int i, String s) {
                                emitter.onError(new CustException(String.valueOf(i), s));
                            }
                        })).compose(RxAdapter.exceptionTransformer());
    }

    /**
     * ????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<SearchTrackList> getSearchedTracks(Map<String, String> specificParams) {
        return Observable.create((ObservableOnSubscribe<SearchTrackList>) emitter ->
                CommonRequest.getSearchedTracks(specificParams,
                        new IDataCallBack<SearchTrackList>() {
                            @Override
                            public void onSuccess(@Nullable SearchTrackList trackList) {
                                emitter.onNext(trackList);
                                emitter.onComplete();
                            }

                            @Override
                            public void onError(int i, String s) {
                                emitter.onError(new CustException(String.valueOf(i), s));
                            }
                        })).compose(RxAdapter.exceptionTransformer());
    }

    /**
     * ????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<RadioList> getSearchedRadios(Map<String, String> specificParams) {
       /* return Observable.create((ObservableOnSubscribe<RadioList>) emitter ->
                CommonRequest.getSearchedRadios(specificParams,
                        new IDataCallBack<RadioList>() {
                            @Override
                            public void onSuccess(@Nullable RadioList radioList) {
                                emitter.onNext(radioList);
                                emitter.onComplete();
                            }

                            @Override
                            public void onError(int i, String s) {
                                emitter.onError(new CustException(String.valueOf(i), s));
                            }
                        })).compose(RxAdapter.exceptionTransformer());*/

        return null;
    }

    /**
     * ????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<AnnouncerList> getSearchAnnouncers(Map<String, String> specificParams) {
        return Observable.create((ObservableOnSubscribe<AnnouncerList>) emitter ->
                CommonRequest.getSearchAnnouncers(specificParams,
                        new IDataCallBack<AnnouncerList>() {
                            @Override
                            public void onSuccess(@Nullable AnnouncerList announcerList) {
                                emitter.onNext(announcerList);
                                emitter.onComplete();
                            }

                            @Override
                            public void onError(int i, String s) {
                                emitter.onError(new CustException(String.valueOf(i), s));
                            }
                        })).compose(RxAdapter.exceptionTransformer());
    }

    /**
     * ????????????????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<BatchAlbumList> getBatch(Map<String, String> specificParams) {
        return Observable.create((ObservableOnSubscribe<BatchAlbumList>) emitter ->
                CommonRequest.getBatch(specificParams,
                        new IDataCallBack<BatchAlbumList>() {
                            @Override
                            public void onSuccess(@Nullable BatchAlbumList batchAlbumList) {
                                emitter.onNext(batchAlbumList);
                                emitter.onComplete();
                            }

                            @Override
                            public void onError(int i, String s) {
                                emitter.onError(new CustException(String.valueOf(i), s));
                            }
                        })).compose(RxAdapter.exceptionTransformer());
    }

    /**
     * ???????????????????????????ID??????????????????????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<TrackList> getTracks(Map<String, String> specificParams) {
        return Observable.create((ObservableOnSubscribe<TrackList>) emitter ->
                CommonRequest.getTracks(specificParams,
                        new IDataCallBack<TrackList>() {
                            @Override
                            public void onSuccess(@Nullable TrackList trackList) {
                                emitter.onNext(trackList);
                                emitter.onComplete();
                            }

                            @Override
                            public void onError(int i, String s) {
                                emitter.onError(new CustException(String.valueOf(i), s));
                            }
                        })).compose(RxAdapter.exceptionTransformer());
    }

    /**
     * ??????????????????????????????id??????????????????????????????????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<LastPlayTrackList> getLastPlayTracks(Map<String, String> specificParams) {
        return Observable.create((ObservableOnSubscribe<LastPlayTrackList>) emitter ->
                CommonRequest.getLastPlayTracks(specificParams,
                        new IDataCallBack<LastPlayTrackList>() {
                            @Override
                            public void onSuccess(@Nullable LastPlayTrackList trackList) {
                                emitter.onNext(trackList);
                                emitter.onComplete();
                            }

                            @Override
                            public void onError(int i, String s) {
                                emitter.onError(new CustException(String.valueOf(i), s));
                            }
                        })).compose(RxAdapter.exceptionTransformer());
    }

    /**
     * ????????????????????????????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<AnnouncerList> getAnnouncerList(Map<String, String> specificParams) {
        return Observable.create((ObservableOnSubscribe<AnnouncerList>) emitter ->
                CommonRequest.getAnnouncerList(specificParams,
                        new IDataCallBack<AnnouncerList>() {
                            @Override
                            public void onSuccess(@Nullable AnnouncerList announcerList) {
                                emitter.onNext(announcerList);
                                emitter.onComplete();
                            }

                            @Override
                            public void onError(int i, String s) {
                                emitter.onError(new CustException(String.valueOf(i), s));
                            }
                        })).compose(RxAdapter.exceptionTransformer());
    }

    /**
     * ???????????????????????????????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<RelativeAlbums> getRelativeAlbumsUseTrackId(Map<String, String> specificParams) {
        return Observable.create((ObservableOnSubscribe<RelativeAlbums>) emitter ->
                CommonRequest.getRelativeAlbumsUseTrackId(specificParams,
                        new IDataCallBack<RelativeAlbums>() {
                            @Override
                            public void onSuccess(@Nullable RelativeAlbums relativeAlbums) {
                                emitter.onNext(relativeAlbums);
                                emitter.onComplete();
                            }

                            @Override
                            public void onError(int i, String s) {
                                emitter.onError(new CustException(String.valueOf(i), s));
                            }
                        })).compose(RxAdapter.exceptionTransformer());
    }

    /**
     * ?????????????????????????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<RelativeAlbums> getRelativeAlbums(Map<String, String> specificParams) {
        return Observable.create((ObservableOnSubscribe<RelativeAlbums>) emitter ->
                CommonRequest.getRelativeAlbums(specificParams,
                        new IDataCallBack<RelativeAlbums>() {
                            @Override
                            public void onSuccess(@Nullable RelativeAlbums relativeAlbums) {
                                emitter.onNext(relativeAlbums);
                                emitter.onComplete();
                            }

                            @Override
                            public void onError(int i, String s) {
                                emitter.onError(new CustException(String.valueOf(i), s));
                            }
                        })).compose(RxAdapter.exceptionTransformer());
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<RecommendDownload> getRecommendDownloadList(Map<String, String> specificParams) {
        return Observable.create((ObservableOnSubscribe<RecommendDownload>) emitter ->
                CommonRequest.getRecommendDownloadList(specificParams,
                        new IDataCallBack<RecommendDownload>() {
                            @Override
                            public void onSuccess(@Nullable RecommendDownload recommendDownload) {
                                emitter.onNext(recommendDownload);
                                emitter.onComplete();
                            }

                            @Override
                            public void onError(int i, String s) {
                                emitter.onError(new CustException(String.valueOf(i), s));
                            }
                        })).compose(RxAdapter.exceptionTransformer());
    }

    /**
     * ????????????????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<ProgramList> getProgram(Map<String, String> specificParams) {
       /* return Observable.create((ObservableOnSubscribe<ProgramList>) emitter ->
                CommonRequest.getProgram(specificParams,
                        new IDataCallBack<ProgramList>() {
                            @Override
                            public void onSuccess(@Nullable ProgramList programList) {
                                emitter.onNext(programList);
                                emitter.onComplete();
                            }

                            @Override
                            public void onError(int i, String s) {
                                emitter.onError(new CustException(String.valueOf(i), s));
                            }
                        })).compose(RxAdapter.exceptionTransformer());*/

        return null;
    }

    /**
     * ????????????????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<RadioListById> getRadiosByIds(Map<String, String> specificParams) {
       /* return Observable.create((ObservableOnSubscribe<RadioListById>) emitter ->
                CommonRequest.getRadiosByIds(specificParams,
                        new IDataCallBack<RadioListById>() {
                            @Override
                            public void onSuccess(@Nullable RadioListById radioListById) {
                                emitter.onNext(radioListById);
                                emitter.onComplete();
                            }

                            @Override
                            public void onError(int i, String s) {
                                emitter.onError(new CustException(String.valueOf(i), s));
                            }
                        })).compose(RxAdapter.exceptionTransformer());*/

        return null;
    }

    /**
     * ??????????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<List<Schedule>> getSchedules(Map<String, String> specificParams) {
      /*  return Observable.create((ObservableOnSubscribe<List<Schedule>>) emitter -> CommonRequest.getSchedules(specificParams,
                new IDataCallBack<ScheduleList>() {
                    @Override
                    public void onSuccess(@Nullable ScheduleList scheduleList) {
                        if (CollectionUtils.isEmpty(scheduleList.getmScheduleList())) {
                            emitter.onError(new Exception("??????????????????"));
                        } else {
                            emitter.onNext(scheduleList.getmScheduleList());
                            emitter.onComplete();
                        }
                    }

                    @Override
                    public void onError(int i, String s) {
                        emitter.onError(new CustException(String.valueOf(i), s));
                    }
                })).compose(RxAdapter.exceptionTransformer());*/

        return null;
    }


    /**
     * ????????????????????????????????????????????????ID????????????????????????ID????????????????????????
     * ????????????ID??????????????????????????????????????????ID?????????????????????????????????????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<SearchTrackListV2> searchTrackV2(Map<String, String> specificParams) {
        return Observable.create((ObservableOnSubscribe<SearchTrackListV2>) emitter ->
                CommonRequest.searchTrackV2(specificParams,
                        new IDataCallBack<SearchTrackListV2>() {
                            @Override
                            public void onSuccess(@Nullable SearchTrackListV2 searchTrackListV2) {
                                emitter.onNext(searchTrackListV2);
                                emitter.onComplete();
                            }

                            @Override
                            public void onError(int i, String s) {
                                emitter.onError(new CustException(String.valueOf(i), s));
                            }
                        })).compose(RxAdapter.exceptionTransformer());
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<RadioListByCategory> getRadiosByCategory(Map<String, String> specificParams) {
       /* return Observable.create((ObservableOnSubscribe<RadioListByCategory>) emitter ->
                CommonRequest.getRadiosByCategory(specificParams,
                        new IDataCallBack<RadioListByCategory>() {
                            @Override
                            public void onSuccess(@Nullable RadioListByCategory radioListByCategory) {
                                emitter.onNext(radioListByCategory);
                                emitter.onComplete();
                            }

                            @Override
                            public void onError(int i, String s) {
                                emitter.onError(new CustException(String.valueOf(i), s));
                            }
                        })).compose(RxAdapter.exceptionTransformer());*/

        return null;
    }

    /**
     * ??????????????????ID????????????????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<AnnouncerListByIds> getAnnouncersBatch(Map<String, String> specificParams) {
        return Observable.create((ObservableOnSubscribe<AnnouncerListByIds>) emitter ->
                CommonRequest.getAnnouncersBatch(specificParams,
                        new IDataCallBack<AnnouncerListByIds>() {
                            @Override
                            public void onSuccess(@Nullable AnnouncerListByIds announcerListByIds) {
                                emitter.onNext(announcerListByIds);
                                emitter.onComplete();
                            }

                            @Override
                            public void onError(int i, String s) {
                                emitter.onError(new CustException(String.valueOf(i), s));
                            }
                        })).compose(RxAdapter.exceptionTransformer());
    }

    /**
     * ???????????????????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<AlbumList> getAlbumsByAnnouncer(Map<String, String> specificParams) {
        return Observable.create((ObservableOnSubscribe<AlbumList>) emitter ->
                CommonRequest.getAlbumsByAnnouncer(specificParams,
                        new IDataCallBack<AlbumList>() {
                            @Override
                            public void onSuccess(@Nullable AlbumList albumList) {
                                emitter.onNext(albumList);
                                emitter.onComplete();
                            }

                            @Override
                            public void onError(int i, String s) {
                                emitter.onError(new CustException(String.valueOf(i), s));
                            }
                        })).compose(RxAdapter.exceptionTransformer());
    }

    /**
     * ?????????????????????????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<AnnouncerTrackList> getTracksByAnnouncer(Map<String, String> specificParams) {
        return Observable.create((ObservableOnSubscribe<AnnouncerTrackList>) emitter ->
                CommonRequest.getTracksByAnnouncer(specificParams,
                        new IDataCallBack<AnnouncerTrackList>() {
                            @Override
                            public void onSuccess(@Nullable AnnouncerTrackList trackList) {
                                emitter.onNext(trackList);
                                emitter.onComplete();
                            }

                            @Override
                            public void onError(int i, String s) {
                                emitter.onError(new CustException(String.valueOf(i), s));
                            }
                        })).compose(RxAdapter.exceptionTransformer());
    }

    /**
     * ?????????????????????????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<AnnouncerCategoryList> getAnnouncerCategoryList(Map<String, String> specificParams) {
        return Observable.create((ObservableOnSubscribe<AnnouncerCategoryList>) emitter ->
                CommonRequest.getAnnouncerCategoryList(specificParams,
                        new IDataCallBack<AnnouncerCategoryList>() {
                            @Override
                            public void onSuccess(@Nullable AnnouncerCategoryList categoryList) {
                                emitter.onNext(categoryList);
                                emitter.onComplete();
                            }

                            @Override
                            public void onError(int i, String s) {
                                emitter.onError(new CustException(String.valueOf(i), s));
                            }
                        })).compose(RxAdapter.exceptionTransformer());
    }


    /**
     * ?????????????????????????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<SuggestWords> getSuggestWord(Map<String, String> specificParams) {
        return Observable.create((ObservableOnSubscribe<SuggestWords>) emitter ->
                CommonRequest.getSuggestWord(specificParams,
                        new IDataCallBack<SuggestWords>() {
                            @Override
                            public void onSuccess(@Nullable SuggestWords suggestWords) {
                                emitter.onNext(suggestWords);
                                emitter.onComplete();
                            }

                            @Override
                            public void onError(int i, String s) {
                                emitter.onError(new CustException(String.valueOf(i), s));
                            }
                        })).compose(RxAdapter.exceptionTransformer());
    }

    /**
     * ????????????ID????????????????????????
     *
     * @param specificParams
     * @return
     */
    public Observable<XmBaseUserInfo> getBaseUserInfo(Map<String, String> specificParams) {
        return Observable.create((ObservableOnSubscribe<XmBaseUserInfo>) emitter ->
                CommonRequest.getBaseUserInfo(specificParams,
                        new IDataCallBack<XmBaseUserInfo>() {
                            @Override
                            public void onSuccess(@Nullable XmBaseUserInfo xmBaseUserInfo) {
                                emitter.onNext(xmBaseUserInfo);
                                emitter.onComplete();
                            }

                            @Override
                            public void onError(int i, String s) {
                                emitter.onError(new CustException(String.valueOf(i), s));
                            }
                        })).compose(RxAdapter.exceptionTransformer());
    }

    public Observable<List<Schedule>> getSchedulesSource(String radioId) {
        RxField<Radio> radio = new RxField<>();

        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.RADIO_IDS, radioId);

        return getRadiosByIds(map).doOnNext(radioListById -> radio.set(radioListById.getRadios().get(0)))
                .flatMap((Function<RadioListById, ObservableSource<List<Schedule>>>) radioListById -> getSchedulesSource(radio.get()));
    }

    public Observable<List<Schedule>> getSchedulesSource(final Radio radio) {
      /*  List<Schedule> schedulesx = new ArrayList<>();
        Map<String, String> yestoday = new HashMap<>();
        yestoday.put("radio_id", radio.getDataId() + "");
        Calendar calendar0 = Calendar.getInstance();
        calendar0.add(Calendar.DAY_OF_MONTH, -1);
        yestoday.put("weekday", calendar0.get(Calendar.DAY_OF_WEEK) - 1 + "");

        Map<String, String> today = new HashMap<>();
        today.put("radio_id", radio.getDataId() + "");

        Map<String, String> tomorrow = new HashMap<>();
        tomorrow.put("radio_id", radio.getDataId() + "");
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DAY_OF_MONTH, 1);
        tomorrow.put("weekday", calendar0.get(Calendar.DAY_OF_WEEK) - 1 + "");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy:MM:dd");

        return getSchedules(yestoday).doOnNext(schedules -> {
            Iterator var7 = schedules.iterator();
            while (var7.hasNext()) {
                Schedule schedulex = (Schedule) var7.next();
                schedulex.setStartTime(simpleDateFormat.format(calendar0.getTime()) + ":" + schedulex.getStartTime());
                schedulex.setEndTime(simpleDateFormat.format(calendar0.getTime()) + ":" + schedulex.getEndTime());
            }
            schedulesx.addAll(schedules);
        }).flatMap((Function<List<Schedule>, ObservableSource<List<Schedule>>>) schedules ->
                getSchedules(today)).doOnNext(schedules -> {
            Iterator var7 = schedules.iterator();
            while (var7.hasNext()) {
                Schedule schedulex = (Schedule) var7.next();
                schedulex.setStartTime(simpleDateFormat.format(Calendar.getInstance().getTime()) + ":" + schedulex.getStartTime());
                schedulex.setEndTime(simpleDateFormat.format(Calendar.getInstance().getTime()) + ":" + schedulex.getEndTime());
            }
            schedulesx.addAll(schedules);
        }).flatMap((Function<List<Schedule>, ObservableSource<List<Schedule>>>) schedules ->
                getSchedules(tomorrow))
                .doOnNext(schedules -> {
                    Iterator var7 = schedules.iterator();
                    while (var7.hasNext()) {
                        Schedule schedulex = (Schedule) var7.next();
                        schedulex.setStartTime(simpleDateFormat.format(Calendar.getInstance().getTime()) + ":" + schedulex.getStartTime());
                        schedulex.setEndTime(simpleDateFormat.format(Calendar.getInstance().getTime()) + ":" + schedulex.getEndTime());
                    }
                    schedulesx.addAll(schedules);
                })
                .map(schedules -> {
                    RadioUtil.fillData(schedulesx, radio);
                    return schedulesx;
                });*/

        return null;
    }

}
