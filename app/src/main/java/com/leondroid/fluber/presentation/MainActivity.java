package com.leondroid.fluber.presentation;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.leondroid.fluber.KeyboardUtils;
import com.leondroid.fluber.R;
import com.leondroid.fluber.application.App;
import com.leondroid.fluber.application.SearchController;
import com.leondroid.fluber.presentation.gallery.GalleryFragment;
import com.leondroid.fluber.presentation.search.SearchHistoryFragment;

public class MainActivity extends AppCompatActivity implements SearchController.Callback, TextView.OnEditorActionListener {
    public static final String TAG = MainActivity.class.getSimpleName();
    private static final String SAVED_STATE_SEARCH_OPENED = "SAVED_STATE_SEARCH_OPENED";
    private static final String SAVED_STATE_SEARCH_TEXT = "SAVED_STATE_SEARCH_TEXT";
    private static final String SAVED_STATE_SEARCH_TITLE = "SAVED_STATE_TITLE";
    private static final String SEARCH_TERM_TITLE_FORMAT = "\"%s\"";

    private SearchController searchController;

    private MenuItem menuItemSearch;
    private boolean isSearchOpened = false;
    private EditText editTextSearch;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchController = App.getSearchController();
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT);

        getSupportActionBar().setCustomView(LayoutInflater.from(this).inflate(R.layout.search_bar, null), params);
        editTextSearch = (EditText) getSupportActionBar().getCustomView().findViewById(R.id.edtSearch);
        editTextSearch.setOnEditorActionListener(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container__main, GalleryFragment.newInstance(), GalleryFragment.TAG)
                    .commit();
            setTitle(getResources().getString(R.string.app_name));
        } else {
            isSearchOpened = savedInstanceState.getBoolean(SAVED_STATE_SEARCH_OPENED);
            String savedSearchDraft = savedInstanceState.getString(SAVED_STATE_SEARCH_TEXT);
            editTextSearch.setText(savedSearchDraft);
            editTextSearch.setSelection(savedSearchDraft.length());
            setTitle(savedInstanceState.getString(SAVED_STATE_SEARCH_TITLE));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        searchController.add(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_STATE_SEARCH_OPENED, isSearchOpened);
        outState.putString(SAVED_STATE_SEARCH_TEXT, editTextSearch != null ? editTextSearch.getText().toString() : "");
        outState.putString(SAVED_STATE_SEARCH_TITLE, title);
    }

    @Override
    protected void onStop() {
        searchController.remove(this);
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (isSearchOpened) {
            activateSearchBar(false);
            return;
        }

        super.onBackPressed();
    }

    @Override
    public void onSearchTermSelected(String searchTerm) {
        setTitle(String.format(SEARCH_TERM_TITLE_FORMAT, searchTerm));
        activateSearchBar(false);
        closeSearchFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menuItemSearch = menu.findItem(R.id.action_search);
        activateSearchBar(isSearchOpened);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                if (isSearchOpened) {
                    activateSearchBar(false);
                    closeSearchFragment();
                } else {
                    activateSearchBar(true);
                    openSearchFragment();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void activateSearchBar(boolean active) {
        isSearchOpened = active;
        getSupportActionBar().setDisplayShowCustomEnabled(active);
        getSupportActionBar().setDisplayShowTitleEnabled(!active);
        if (active) {
            KeyboardUtils.openAndRequestFocus(this, editTextSearch);
            menuItemSearch.setIcon(getResources().getDrawable(android.R.drawable.ic_menu_close_clear_cancel));
        } else {
            KeyboardUtils.close(this, editTextSearch);
            menuItemSearch.setIcon(getResources().getDrawable(android.R.drawable.ic_menu_search));
        }
    }

    private void openSearchFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.container__main, SearchHistoryFragment.newInstance(), SearchHistoryFragment.TAG)
                .addToBackStack(SearchHistoryFragment.TAG)
                .commit();
    }

    private void closeSearchFragment() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String text = v.getText().toString().trim();
            if(!TextUtils.isEmpty(text.trim())) {
                searchController.onSearchTermSelected(text);
                editTextSearch.setText("");
                activateSearchBar(false);
                closeSearchFragment();
                return true;
            }
        }
        return false;
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        this.title = title.toString();
    }
}
