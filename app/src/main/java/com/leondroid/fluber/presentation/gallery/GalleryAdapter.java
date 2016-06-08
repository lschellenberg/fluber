package com.leondroid.fluber.presentation.gallery;

import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.leondroid.fluber.R;
import com.leondroid.fluber.data.api.BackendConfig;
import com.leondroid.fluber.data.api.model.Photo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private static final int TRESHOLD = 80;

    private SearchTermResultViewModel searchTermResultViewModel;

    public GalleryAdapter(SearchTermResultViewModel searchTermResultViewModel) {
        this.searchTermResultViewModel = searchTermResultViewModel;
        searchTermResultViewModel.addOnListChangedCallback(onListChangedCallback);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Photo photo = searchTermResultViewModel.get(position);
        String url = String.format(BackendConfig.IMAGE_URL, photo.getFarm(), photo.getServer(), photo.getId(), photo.getSecret());
        Picasso.with(holder.itemView.getContext()).setIndicatorsEnabled(true);
        Picasso.with(holder.itemView.getContext()).load(url).into(holder.imageView);

        if(getItemCount() - position < TRESHOLD) {
            searchTermResultViewModel.loadMore();
        }
    }

    @Override
    public int getItemCount() {
        return searchTermResultViewModel.size();
    }

    ObservableList.OnListChangedCallback<SearchTermResultViewModel> onListChangedCallback = new ObservableList.OnListChangedCallback<SearchTermResultViewModel>() {
        @Override
        public void onChanged(SearchTermResultViewModel searchTermResultViewModel) {
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(SearchTermResultViewModel searchTermResultViewModel,
                                       int positionStart, int itemCount) {
            notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeInserted(SearchTermResultViewModel searchTermResultViewModel,
                                        int positionStart, int itemCount) {
            notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(SearchTermResultViewModel searchTermResultViewModel, int i,
                                     int i1, int i2) {
        }

        @Override
        public void onItemRangeRemoved(SearchTermResultViewModel searchTermResultViewModel,
                                       int positionStart, int itemCount) {
            notifyItemRangeRemoved(positionStart, itemCount);
        }
    };

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv__photo);
        }
    }
}
