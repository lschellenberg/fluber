package com.leondroid.fluber.data.search;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.functions.Func1;

public class SearchHistoryRepository {
    private static final String SHARED_PREFS_KEY_SEARCH_HISTORY = "SHARED_PREFS_KEY_SEARCH_HISTORY";
    private Set<String> historyCache;
    private SharedPreferences sharedPreferences;

    public SearchHistoryRepository(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        historyCache = new HashSet<>(sharedPreferences.getStringSet(SHARED_PREFS_KEY_SEARCH_HISTORY, new HashSet<String>()));
    }

    public Observable<List<String>> loadHistory() {
        return Observable.just(historyCache).map(new Func1<Set<String>, List<String>>() {
            @Override
            public List<String> call(Set<String> cache) {
                return new ArrayList<>(cache);
            }
        });
    }

    public void addSearchTerm(String searchTerm) {
        historyCache.add(searchTerm);
    }

    public void saveList() {
        sharedPreferences.edit().putStringSet(SHARED_PREFS_KEY_SEARCH_HISTORY, historyCache).apply();
    }

    public void clearHistory() {
        historyCache.clear();
    }
}
