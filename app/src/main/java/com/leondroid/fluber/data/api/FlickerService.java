package com.leondroid.fluber.data.api;

import com.leondroid.fluber.data.api.model.FlickerResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface FlickerService {

    @GET("?method=flickr.photos.search&api_key=3e7cc266ae2b0e0d78e279ce8e361736&format=json&nojsoncallback=1")
    Observable<FlickerResponse> list(@Query("api_key") String apiKey, @Query("text") String searchTerm, @Query("page") int page);
}
