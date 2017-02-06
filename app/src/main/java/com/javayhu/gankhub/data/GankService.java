package com.javayhu.gankhub.data;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Gank service
 * <p>
 * http://gank.io/api
 * <p>
 * Created by javayhu on 1/21/17.
 */
public interface GankService {

    //http://gank.io/api/day/2015/08/06
    @GET("day/{year}/{month}/{day}")
    Observable<GankDayData> getSomeDayData(@Path("year") int year, @Path("month") int month, @Path("day") int day);

}
