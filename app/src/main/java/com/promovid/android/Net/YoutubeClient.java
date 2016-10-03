package com.promovid.android.Net;

import com.promovid.android.Entities.YoutubeChannelDetailsEntity;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by bvs on 3/10/16.
 */
public interface YoutubeClient {

    @GET("videos")
    Observable<YoutubeChannelDetailsEntity> getYoutubeChannelDetails(@Query("key") String key,
                                                                     @Query("part") String part,
                                                                     @Query("id") String id);
}
