package com.leondroid.fluber.presentation.search;

import com.leondroid.fluber.data.search.SearchHistoryRepository;
import com.leondroid.fluber.presentation.presenter.Presenter;
import com.leondroid.fluber.rx.SimpleObserver;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class SearchPresenter implements Presenter<SearchView> {
    private SearchView searchView;

    SearchHistoryRepository searchHistoryRepository;

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public SearchPresenter(SearchHistoryRepository searchHistoryRepository) {
        this.searchHistoryRepository = searchHistoryRepository;
    }

    @Override
    public void onAttachView(final SearchView searchView) {
        this.searchView = searchView;

        compositeSubscription.add(searchHistoryRepository
                .loadHistory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver<List<String>>() {
                    public void onNext(List<String> searchHistory) {
                        SearchPresenter.this.searchView.onSearchHistoryLoaded(searchHistory);
                    }
                }));
    }

    @Override
    public void onDetachView() {
        compositeSubscription.unsubscribe();
        searchHistoryRepository.saveList();
    }

    public void saveSearchTerm(String searchTerm) {
        searchHistoryRepository.addSearchTerm(searchTerm);
    }

    public void clearHistory() {
        searchHistoryRepository.clearHistory();
    }
}
