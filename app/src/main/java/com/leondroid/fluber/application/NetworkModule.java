package com.leondroid.fluber.application;

import com.leondroid.fluber.data.api.BackendConfig;
import com.leondroid.fluber.data.api.FlickerService;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

public class NetworkModule {

    private final Retrofit retrofit;
    private FlickerService service;

    public NetworkModule() {
        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        retrofit = new Retrofit.Builder()
                .baseUrl(BackendConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(rxAdapter)
                .build();
    }

    public FlickerService provideFlickerSearchApi() {
        if (service == null) {
            service = retrofit.create(FlickerService.class);
        }

        return service;
    }
}
