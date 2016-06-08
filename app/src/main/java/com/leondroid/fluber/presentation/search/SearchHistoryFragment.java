package com.leondroid.fluber.presentation.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leondroid.fluber.R;
import com.leondroid.fluber.application.App;
import com.leondroid.fluber.application.SearchController;

import java.util.List;

public class SearchHistoryFragment extends Fragment implements SearchView, SearchAdapter.Callback, SearchController.Callback, View.OnClickListener {
    public static final String TAG = SearchHistoryFragment.class.getSimpleName();

    private SearchPresenter searchPresenter;
    private SearchAdapter searchAdapter = new SearchAdapter();
    private SearchController searchController;
    private View viewClearHistory;

    public static SearchHistoryFragment newInstance() {
        return new SearchHistoryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchController = App.getSearchController();
        searchPresenter = new SearchPresenter(App.getSearchHistoryRepository());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_history, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rv__search);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        recyclerView.setAdapter(searchAdapter);

        viewClearHistory = rootView.findViewById(R.id.tv__search_history__clear);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        searchPresenter.onAttachView(this);
        searchController.add(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        searchAdapter.setCallback(this);
        viewClearHistory.setOnClickListener(this);
    }

    @Override
    public void onPause() {
        viewClearHistory.setOnClickListener(null);
        searchAdapter.setCallback(null);
        super.onPause();
    }

    @Override
    public void onStop() {
        searchController.remove(this);
        searchPresenter.onDetachView();
        super.onStop();
    }

    @Override
    public void onSearchHistoryLoaded(List<String> searchHistory) {
        searchAdapter.onSearchHistoryLoaded(searchHistory);
    }

    @Override
    public void onItemClicked(int pos, String searchTerm) {
        searchController.onSearchTermSelected(searchTerm);
    }

    @Override
    public void onSearchTermSelected(String searchTerm) {
        searchPresenter.saveSearchTerm(searchTerm);
    }

    @Override
    public void onClick(View v) {
        switch (viewClearHistory.getId()) {
            case R.id.tv__search_history__clear:
                searchPresenter.clearHistory();
                searchAdapter.removeAll();
                break;
        }
    }
}
