package com.lx.ohmyjuus.weather;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lx.ohmyjuus.weather.Adapter.ForecastAdapter;
import com.lx.ohmyjuus.weather.Common.Common;
import com.lx.ohmyjuus.weather.Model.ForecastResult;
import com.lx.ohmyjuus.weather.Retrofit.IOpenWeatherMap;
import com.lx.ohmyjuus.weather.Retrofit.RetrofitClient;
import com.lx.ohmyjuus.R;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastFragment extends Fragment {

    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mService;

    TextView txt_city_name,txt_geo_coord;
    RecyclerView recycler_forecast;


    static ForecastFragment instance;

    public static ForecastFragment getInstance() {
        if (instance == null)
            instance = new ForecastFragment();

        return instance;
    }

    public ForecastFragment() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);
    }


//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View itemView = inflater.inflate(R.layout.fragment_forecast, container, false);
//
////        txt_city_name = (TextView)itemView.findViewById(R.id.txt_city_name);
//        txt_geo_coord = (TextView)itemView.findViewById(R.id.geo_coord);
//
//        recycler_forecast = (RecyclerView)itemView.findViewById(R.id.recycler_forecast);
//        recycler_forecast.setHasFixedSize(true);
//        recycler_forecast.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
//
//        getForecastInformation();
//
//
//        return itemView;
//    }

    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    //Ctrl + O
    private void getForecastInformation() {
        compositeDisposable.add(mService.getForecastByLatLng(
                String.valueOf(Common.current_location.getLatitude()),
                String.valueOf(Common.current_location.getLongitude()),
                Common.APP_ID,
                "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ForecastResult>() {
                    @Override
                    public void accept(ForecastResult forecastResult) throws Exception {
                        displayForecast(forecastResult);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("ERROR",""+throwable.getMessage());
                    }
                })
        );
    }

    private void displayForecast(ForecastResult forecastResult) {
        txt_city_name.setText(new StringBuilder(forecastResult.city.name));
        txt_geo_coord.setText(new StringBuilder(forecastResult.city.coord.toString()));

        ForecastAdapter adapter = new ForecastAdapter(getContext(),forecastResult);
        recycler_forecast.setAdapter(adapter);


    }
}
