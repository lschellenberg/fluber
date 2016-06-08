package com.leondroid.fluber.presentation.gallery;

import com.leondroid.fluber.presentation.presenter.PresenterView;


public interface GalleryView extends PresenterView {
    void showLoadingSpinner(boolean show);

    void onNoItems();

    void onItemsLoaded();

    void onMoreItemsLoaded(int page);
}
