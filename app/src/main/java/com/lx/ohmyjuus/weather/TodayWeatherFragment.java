package com.lx.ohmyjuus.weather;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.lx.ohmyjuus.weather.Common.Common;
import com.lx.ohmyjuus.weather.Model.WeatherResult;
import com.lx.ohmyjuus.weather.Retrofit.IOpenWeatherMap;
import com.lx.ohmyjuus.weather.Retrofit.RetrofitClient;
import com.lx.ohmyjuus.R;
import com.squareup.picasso.Picasso;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;



/**
 * A simple {@link Fragment} subclass.
 */
public class TodayWeatherFragment extends Fragment {

    ImageView img_weather;
    TextView txt_city_name, txt_humidity, txt_sunrise, txt_sunset, txt_pressure,
            txt_temperature, txt_description, txt_date_time, txt_wind, txt_geo_coord;
    LinearLayout weather_panel;
    ProgressBar loading;

    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mService;



    static TodayWeatherFragment instance;

    public static TodayWeatherFragment getInstance(){
        if (instance ==null)
            instance=new TodayWeatherFragment();
        return instance;
    }

    public TodayWeatherFragment() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getWeatherInformation();
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_today_weather, container, false);

        img_weather = (ImageView)itemView.findViewById(R.id.img_weather);
        txt_humidity= (TextView)itemView.findViewById(R.id.txt_humidity);
        txt_sunrise= (TextView)itemView.findViewById(R.id.txt_sunrise);
        txt_sunset= (TextView)itemView.findViewById(R.id.txt_sunset);
        txt_pressure= (TextView)itemView.findViewById(R.id.txt_pressure);
        txt_temperature= (TextView)itemView.findViewById(R.id.txt_temperature);
        txt_description= (TextView)itemView.findViewById(R.id.txt_description);
        txt_date_time= (TextView)itemView.findViewById(R.id.txt_date_time);
        txt_wind= (TextView)itemView.findViewById(R.id.txt_wind);
        txt_geo_coord= (TextView)itemView.findViewById(R.id.txt_geo_coord);
        txt_city_name= (TextView)itemView.findViewById(R.id.txt_city_name);

        weather_panel = (LinearLayout)itemView.findViewById(R.id.weather_panel);
        loading = (ProgressBar)itemView.findViewById(R.id.loading);



        return itemView;
    }

    private void getWeatherInformation() {
        compositeDisposable.add(mService.getWeatherByLatLng(String.valueOf(Common.current_location.getLatitude()),
                String.valueOf(Common.current_location.getLongitude()),
                Common.APP_ID,
                "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherResult>() {
                    @Override
                    public void accept(WeatherResult weatherResult) throws Exception {

                        //Load image
                        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/wn/").append(weatherResult.getWeather().get(0).getIcon())
                                .append(".png").toString()).into(img_weather);

                        //Load Info
                        txt_city_name.setText(weatherResult.getName() + "   " + weatherResult.getSys().getCountry());
                        txt_description.setText(new StringBuilder("Current weather in ")
                                .append(weatherResult.getName()).toString());
                        txt_temperature.setText(new StringBuilder(
                                String.valueOf(weatherResult.getMain().getTemp())).append(" °C").toString());
                        txt_date_time.setText(Common.convertUnixToDate(weatherResult.getDt()));
                        txt_pressure.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getPressure())).append(" hpa").toString());
                        txt_humidity.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getHumidity())).append("%").toString());
                        txt_sunrise.setText(Common.convertUnixToHour(weatherResult.getSys().getSunrise()));
                        txt_sunset.setText(Common.convertUnixToHour(weatherResult.getSys().getSunset()));
                        txt_geo_coord.setText(new StringBuilder(weatherResult.getCoord().toString()).toString());
                        txt_wind.setText(new StringBuilder(String.valueOf(weatherResult.getWind().getSpeed())).append(" Kph - ").append(weatherResult.getWind().getWindDirection()));

                        //Display panel
                        weather_panel.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);


                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getActivity() , ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        );
    }

    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}