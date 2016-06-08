package com.leondroid.fluber.presentation.gallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.leondroid.fluber.R;
import com.leondroid.fluber.application.App;
import com.leondroid.fluber.application.SearchController;
import com.leondroid.fluber.data.api.model.Photo;

import java.util.List;

public class GalleryFragment extends Fragment implements SearchController.Callback, GalleryView {
    public static final String TAG = GalleryFragment.class.getSimpleName();
    private static final int NUMBER_OF_COLUMNS_IN_GRID = 3;

    public static GalleryFragment newInstance() {
        return new GalleryFragment();
    }

    private SearchController searchController;
    private GalleryAdapter galleryAdapter;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView textViewMessage;

    private SearchTermResultViewModel searchTermResultViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);

        searchController = App.getSearchController();
        searchTermResultViewModel = App.getSearchTermViewModel();
        galleryAdapter = new GalleryAdapter(searchTermResultViewModel);

        textViewMessage = (TextView) rootView.findViewById(R.id.tv__gallery_message);
        progressBar = (ProgressBar) rootView.findViewById(R.id.pb__gallery);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv__photos);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(container.getContext(), NUMBER_OF_COLUMNS_IN_GRID));
        recyclerView.setAdapter(galleryAdapter);

        showLoadingSpinner(false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        searchTermResultViewModel.attachView(this);
        searchController.addAndNotify(this);
    }

    @Override
    public void onStop() {
        searchController.remove(this);
        searchTermResultViewModel.detachView();
        super.onStop();
    }

    @Override
    public void showLoadingSpinner(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onNoItems() {
        textViewMessage.animate().alpha(1).withStartAction(new Runnable() {
            @Override
            public void run() {
                textViewMessage.setText(R.string.gallery__not_items_found);
            }
        });
    }

    @Override
    public void onItemsLoaded() {
        recyclerView.scrollToPosition(0);
    }

    @Override
    public void onMoreItemsLoaded(int page) {
        Toast.makeText(getActivity(), "More items loaded - Page: " + page, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSearchTermSelected(String searchTerm) {
        textViewMessage.animate().alpha(0).withEndAction(new Runnable() {
            @Override
            public void run() {
                textViewMessage.setVisibility(View.GONE);
            }
        });

        searchTermResultViewModel.searchFor(searchTerm);
    }
}
