package com.leondroid.fluber.data.gallery;

import com.leondroid.fluber.application.NetworkModule;
import com.leondroid.fluber.data.api.model.FlickerResponse;

import rx.Observable;

public class GalleryCloudDataSource implements GalleryDataSource {

    private NetworkModule networkModule;

    public GalleryCloudDataSource(NetworkModule networkModule) {
        this.networkModule = networkModule;
    }

    @Override
    public Observable<FlickerResponse> load(String searchTerm, int page) {
        return networkModule.provideFlickerSearchApi().list(networkModule.flickrApiKey, searchTerm, page);
    }

    @Override
    public Observable<FlickerResponse> save(String searchTerm, FlickerResponse response) {
        return Observable.just(response);
    }
}
