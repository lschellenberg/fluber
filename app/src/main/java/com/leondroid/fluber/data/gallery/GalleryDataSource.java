package com.leondroid.fluber.data.gallery;


import com.leondroid.fluber.data.api.model.FlickerResponse;

import rx.Observable;

public interface GalleryDataSource {
    Observable<FlickerResponse> load(String searchTerm, int page);

    Observable<FlickerResponse> save(String searchTerm, FlickerResponse response);
}
