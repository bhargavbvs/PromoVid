package com.promovid.android.Net;

import com.promovid.android.Entities.YoutubeIdsEntity;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by bvs on 3/10/16.
 */
public interface SnucklsClient {

    @GET("andriodTest.php")
    Observable<YoutubeIdsEntity> getYoutubeIds(@Query("key") String key);
}
