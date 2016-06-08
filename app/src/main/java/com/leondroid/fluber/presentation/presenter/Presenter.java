package com.leondroid.fluber.presentation.presenter;

public interface Presenter<P extends PresenterView> {
    void onAttachView(P view);

    void onDetachView();
}
