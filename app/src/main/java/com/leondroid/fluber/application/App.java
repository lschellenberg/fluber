package com.leondroid.fluber.application;

import android.app.Application;
import android.content.Context;

import com.leondroid.fluber.data.gallery.GalleryCacheDataSource;
import com.leondroid.fluber.data.gallery.GalleryCloudDataSource;
import com.leondroid.fluber.data.gallery.GalleryRepository;
import com.leondroid.fluber.data.search.SearchHistoryRepository;
import com.leondroid.fluber.presentation.gallery.SearchTermResultViewModel;

public class App extends Application {
    private static final String TAG = App.class.getSimpleName();
    private static final String PREF_KEY = "com.leondroid.fluber";

    private static App instance;
    private NetworkModule networkModule;
    private SearchController searchController;
    private SearchTermResultViewModel searchTermViewModel;
    private SearchHistoryRepository searchHistoryRepository;
    private GalleryRepository galleryRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        networkModule = new NetworkModule();
        searchController = new SearchController();
        searchHistoryRepository = new SearchHistoryRepository(getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE));
        galleryRepository = new GalleryRepository(new GalleryCloudDataSource(networkModule), new GalleryCacheDataSource());
        searchTermViewModel = new SearchTermResultViewModel(galleryRepository);
    }

    public static SearchHistoryRepository getSearchHistoryRepository() {
        return instance.searchHistoryRepository;
    }

    public static SearchController getSearchController() {
        return instance.searchController;
    }

    public static SearchTermResultViewModel getSearchTermViewModel() {
        return instance.searchTermViewModel;
    }

}
