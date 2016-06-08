package com.leondroid.fluber.data.gallery;

import com.leondroid.fluber.data.api.model.FlickerResponse;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func0;

public class GalleryCacheDataSource implements GalleryDataSource {
    public static final String TAG = GalleryCacheDataSource.class.getSimpleName();

    private Map<String, Map<Integer, FlickerResponse>> searchResultMap = new HashMap<>();

    @Override
    public Observable<FlickerResponse> load(final String searchTerm, final int page) {
        return Observable.create(new Observable.OnSubscribe<FlickerResponse>() {
            @Override
            public void call(Subscriber<? super FlickerResponse> subscriber) {
                FlickerResponse response = loadResponse(searchTerm, page);

                if (response != null) {
                    subscriber.onNext(response);
                }

                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<FlickerResponse> save(final String searchTerm, final FlickerResponse response) {
        return Observable.defer(new Func0<Observable<FlickerResponse>>() {
            @Override
            public Observable<FlickerResponse> call() {
                return Observable.just(saveResponse(searchTerm, response));
            }
        });
    }

    private FlickerResponse loadResponse(String searchTerm, int page) {
        if (searchResultMap.containsKey(searchTerm)) {
            if (searchResultMap.get(searchTerm).containsKey(page)) {
                return searchResultMap.get(searchTerm).get(page);
            }
        }

        return null;
    }

    private FlickerResponse saveResponse(String searchTerm, FlickerResponse flickerResponse) {
        if (searchResultMap.containsKey(searchTerm)) {
            searchResultMap.get(searchTerm).put(flickerResponse.getPhotos().getPage(), flickerResponse);
        } else {
            Map<Integer, FlickerResponse> map = new HashMap<>();
            map.put(flickerResponse.getPhotos().getPage(), flickerResponse);
            searchResultMap.put(searchTerm, map);
        }

        return flickerResponse;
    }
}
