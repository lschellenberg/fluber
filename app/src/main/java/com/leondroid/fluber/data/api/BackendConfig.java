package com.leondroid.fluber.data.api;

public class BackendConfig {
    public static final String BASE_URL = "https://api.flickr.com/services/rest/";
    // Format of the url is http://farm{farm}.static.flickr.com/{server}/{id}_{secret}.jpg
    public static final String IMAGE_URL = "http://farm%s.static.flickr.com/%s/%s_%s.jpg";
}
