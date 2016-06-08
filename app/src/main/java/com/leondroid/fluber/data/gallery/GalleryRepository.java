package com.leondroid.fluber.data.gallery;

import com.leondroid.fluber.data.api.model.FlickerResponse;

import rx.Observable;
import rx.functions.Func1;

public class GalleryRepository {
    private GalleryDataSource galleryCache;
    private GalleryDataSource galleryCloud;

    public GalleryRepository(GalleryCloudDataSource galleryCloud, GalleryCacheDataSource galleryCache) {
        this.galleryCloud = galleryCloud;
        this.galleryCache = galleryCache;
    }

    public Observable<FlickerResponse> load(final String searchTerm, int page) {
        return galleryCache
                .load(searchTerm, page)
                .switchIfEmpty(galleryCloud
                        .load(searchTerm, page)
                        .flatMap(new Func1<FlickerResponse, Observable<FlickerResponse>>() {
                            @Override
                            public Observable<FlickerResponse> call(FlickerResponse flickerResponse) {
                                return galleryCache.save(searchTerm, flickerResponse);
                            }
                        }));
    }


}
