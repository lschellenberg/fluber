package com.leondroid.fluber.presentation.gallery;

import android.databinding.ObservableArrayList;

import com.leondroid.fluber.data.api.model.FlickerResponse;
import com.leondroid.fluber.data.api.model.Photo;
import com.leondroid.fluber.data.gallery.GalleryRepository;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class SearchTermResultViewModel extends ObservableArrayList<Photo> implements Observer<FlickerResponse> {

    private final GalleryRepository galleryRepository;
    private GalleryView galleryView;
    private int page;
    private String searchTerm;

    private CompositeSubscription compositeSubscription;
    private int pendingLoad;

    public void attachView(GalleryView galleryView){
        this.galleryView = galleryView;
        compositeSubscription = new CompositeSubscription();
    }

    public void detachView() {
        this.galleryView = null;
        compositeSubscription.unsubscribe();
    }

    public SearchTermResultViewModel(GalleryRepository galleryRepository) {
        this.galleryRepository = galleryRepository;
    }

    public void searchFor(String searchTerm) {
        if(this.searchTerm != null && this.searchTerm.equals(searchTerm)) {
            return;
        }

        clear();

        this.searchTerm = searchTerm;
        page = 1;
        pendingLoad = -1;

        compositeSubscription.add(galleryRepository
                .load(searchTerm, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        galleryView.showLoadingSpinner(false);
                        galleryView.onItemsLoaded();
                    }
                })
                .subscribe(this));

        galleryView.showLoadingSpinner(true);
    }

    public void loadMore() {
        if(pendingLoad == page) {
            return;
        }

        pendingLoad = page;
        compositeSubscription.add(galleryRepository
                .load(searchTerm, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<FlickerResponse>() {
                    @Override
                    public void call(FlickerResponse flickerResponse) {
                        galleryView.onMoreItemsLoaded(page);
                    }
                })
                .subscribe(this));
    }

    @Override
    public void onCompleted() {
        galleryView.showLoadingSpinner(false);
    }

    @Override
    public void onError(Throwable e) {
        galleryView.showLoadingSpinner(false);
        galleryView.onNoItems();
    }

    @Override
    public void onNext(FlickerResponse flickerResponse) {
        page = flickerResponse.getPhotos().getPage() + 1;
        addAll(flickerResponse.getPhotos().getPhoto());
    }
}
