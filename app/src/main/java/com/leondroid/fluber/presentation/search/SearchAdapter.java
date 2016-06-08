package com.leondroid.fluber.presentation.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leondroid.fluber.R;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<String> searchHistory;
    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void onSearchHistoryLoaded(List<String> searchHistory) {
        this.searchHistory = searchHistory;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final String searchTerm = searchHistory.get(position);
        holder.textView.setText(searchTerm);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callback != null) {
                    callback.onItemClicked(position, searchTerm);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchHistory == null ? 0 : searchHistory.size();
    }

    public void removeAll() {
        notifyItemRangeRemoved(0, getItemCount());
        searchHistory.clear();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv__item_search);
        }
    }

    public interface Callback {
        void onItemClicked(int pos, String searchTerm);
    }
}
