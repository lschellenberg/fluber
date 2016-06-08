package com.leondroid.fluber.application;


import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchController {
    private List<Callback> callbacks = new ArrayList<>();

    private String lastSearchTerm;


    public void addAndNotify(Callback callback) {
        add(callback);
        if(!TextUtils.isEmpty(lastSearchTerm)) {
            callback.onSearchTermSelected(lastSearchTerm);
        }
    }

    public void add(Callback callback) {
        callbacks.add(callback);
    }

    public void remove(Callback callback) {
        callbacks.remove(callback);
    }

    public void onSearchTermSelected(String searchTerm) {
        lastSearchTerm = searchTerm;

        for (Callback callback : callbacks) {
            callback.onSearchTermSelected(searchTerm);
        }
    }

    public interface Callback {
        void onSearchTermSelected(String searchTerm);
    }
}
