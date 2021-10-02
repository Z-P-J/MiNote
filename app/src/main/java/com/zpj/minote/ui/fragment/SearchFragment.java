package com.zpj.minote.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.zpj.fragmentation.dialog.animator.DialogAnimator;
import com.zpj.fragmentation.dialog.impl.FullScreenDialogFragment;
import com.zpj.minote.R;
import com.zpj.minote.api.HttpApi;
import com.zpj.minote.model.NoteItem;
import com.zpj.minote.ui.widget.SearchBar;
import com.zpj.minote.utils.KeywordUtil;
import com.zpj.recyclerview.EasyRecyclerView;
import com.zpj.utils.PrefsHelper;
import com.zpj.widget.toolbar.ZSearchBar;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SearchFragment extends FullScreenDialogFragment {

    private final List<NoteItem> list = new ArrayList<>();

    private EasyRecyclerView<NoteItem> mRecyclerView;
    private String mKeyword;

    @Override
    protected int getImplLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    protected DialogAnimator onCreateDialogAnimator(ViewGroup contentView) {
        return null;
    }

    @Override
    public long getShowAnimDuration() {
        return 0;
    }

    @Override
    public long getDismissAnimDuration() {
        return 0;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        String cookie = PrefsHelper.with().getString("cookie");
        Log.d("MainActivity", "cookie=" + cookie);

        SearchBar searchBar = findViewById(R.id.search_bar);
        searchBar.setOnSearchListener(new ZSearchBar.OnSearchListener() {
            @Override
            public void onSearch(String keyword) {
                search(keyword);
            }
        });

        findViewById(R.id.btn_back).setOnClickListener(v -> dismiss());

        mRecyclerView = new EasyRecyclerView<>(findViewById(R.id.recycler_view));
        mRecyclerView.getRecyclerView().setAlpha(0f);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        mRecyclerView.setData(list)
                .setItemRes(R.layout.item_note)
                .setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL))
                .onBindViewHolder((holder, list, position, payloads) -> {
                    NoteItem note = list.get(position);
                    holder.setText(R.id.tv_content, KeywordUtil.hightlightKeyword(Color.RED, note.getSnippet(), mKeyword));
                    holder.setText(R.id.tv_info, format.format(new Date(note.getModifyDate())));
                })
                .onItemClick((holder, view1, data) -> {
                    start(NoteDetailFragment.newInstance(data.getId()));
                })
                .build();
        mRecyclerView.showEmpty();
        searchBar.post(new Runnable() {
            @Override
            public void run() {
                showSoftInput(searchBar.getEditor());
            }
        });

        mRecyclerView.getRecyclerView()
                .animate()
                .alpha(1f)
                .setDuration(360)
                .start();
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        hideSoftInput();
    }

    private void search(String keyword) {
        mKeyword = keyword;
        list.clear();
        mRecyclerView.showLoading();
        HttpApi.getSearchResults(PrefsHelper.with().getString("cookie"), keyword)
                .onSuccess(obj -> {
                    Log.d("MainActivity", "getSearchResults obj=" + obj);
                    if (obj.has("data")) {
                        JSONArray entries = obj.getJSONObject("data").getJSONArray("searchEntries");
                        Log.d("MainActivity", "getSearchResults entries=" + entries);

                        for (int i = 0; i < entries.length(); i++) {
                            list.add(NoteItem.from(entries.getJSONObject(i).getJSONObject("note")));
                        }
                    }
                    if (list.isEmpty()) {
                        mRecyclerView.showEmpty();
                    } else {
                        mRecyclerView.showContent();
                    }
                })
                .subscribe();
    }

}
