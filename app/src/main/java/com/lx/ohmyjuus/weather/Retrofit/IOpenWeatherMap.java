package com.lx.ohmyjuus.weather.Retrofit;



import com.lx.ohmyjuus.weather.Model.ForecastResult;
import com.lx.ohmyjuus.weather.Model.WeatherResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWeatherMap {
    @GET("weather")
    Observable<WeatherResult> getWeatherByLatLng(@Query("lat") String lat,
                                                 @Query("lon") String lng,
                                                 @Query("appid") String appid,
                                                 @Query("units") String unit);

    @GET("forecast")
    Observable<ForecastResult> getForecastByLatLng(@Query("lat") String lat,
                                                   @Query("lon") String lng,
                                                   @Query("appid") String appid,
                                                   @Query("units") String unit);

}


