package com.leondroid.fluber.presentation.search;

import com.leondroid.fluber.presentation.presenter.PresenterView;

import java.util.List;
import java.util.Set;

public interface SearchView extends PresenterView {
    void onSearchHistoryLoaded(List<String> searchHistory);
}
