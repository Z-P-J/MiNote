package com.zpj.minote.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zpj.fragmentation.SimpleFragment;
import com.zpj.fragmentation.dialog.IDialog;
import com.zpj.fragmentation.dialog.enums.DialogPosition;
import com.zpj.minote.R;
import com.zpj.minote.api.HttpApi;
import com.zpj.minote.model.NoteItem;
import com.zpj.minote.ui.LoginActivity;
import com.zpj.minote.ui.widget.HighlightTextView;
import com.zpj.minote.ui.widget.SearchBar;
import com.zpj.minote.ui.widget.SolidArrowView;
import com.zpj.recyclerview.EasyRecyclerView;
import com.zpj.utils.PrefsHelper;
import com.zpj.utils.ScreenUtils;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainFragment extends SimpleFragment {

    private static final String TAG = "MainFragment";

    private final List<NoteItem> list = new ArrayList<>();

    private AppBarLayout mAppBarLayout;
    private FrameLayout mContainer;
    private FrameLayout mSearchContainer;

    private boolean isSearch = false;
    private boolean mExpand = true;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        String cookie = PrefsHelper.with().getString("cookie");
        Log.d("MainActivity", "cookie=" + cookie);

        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinator);
        mAppBarLayout = findViewById(R.id.app_bar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        mContainer = findViewById(R.id.fl_container);
        mSearchContainer = findViewById(R.id.fl_searcher);
//        EditText tvSearch = findViewById(R.id.tv_search);
        SearchBar searchBar = findViewById(R.id.search_bar);

        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvInfo = findViewById(R.id.tv_info);
        View headerView = findViewById(R.id.header);
        ViewGroup.LayoutParams lp = headerView.getLayoutParams();
        int dp56 = ScreenUtils.dp2pxInt(56);
        SolidArrowView arrowView = findViewById(R.id.arrow_view);
        LinearLayout llTitle = findViewById(R.id.ll_title);
        llTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrowView.switchState();
                new CategoryFragment()
                        .setDialogPosition(DialogPosition.Bottom)
                        .setOnDismissListener(new IDialog.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                arrowView.switchState();
                            }
                        })
                        .show(tvInfo);
            }
        });
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftInput();
                mExpand = true;
                mAppBarLayout.setExpanded(true, true);
            }
        });
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                int delta = Math.abs(i);
                Log.d(TAG, "delta=" + delta + " appbarHeight=" + appBarLayout.getHeight() + " dp56=" + dp56);

                int h = appBarLayout.getHeight() - delta - dp56;

                if (h >= dp56) {
                    float p = (float) (h - dp56) / (appBarLayout.getHeight() - 2 * dp56);
                    Log.d(TAG, "p=" + p);
                    tvTitle.setTextSize(18 + p * 10);
                    tvInfo.setAlpha(p);
                    arrowView.setSize(ScreenUtils.dp2pxInt(12 + p * 4));
                } else {
                    tvTitle.setTextSize(18);
                    tvInfo.setAlpha(0);
                    arrowView.setSize(ScreenUtils.dp2pxInt(12));
                    float p = (float) h / dp56;
                    headerView.setAlpha(p);
                }
                lp.height = h;
                headerView.setLayoutParams(lp);


                if (isSearch) {
                    if (delta <= appBarLayout.getTotalScrollRange()) {
                        float percent = (float) delta / appBarLayout.getTotalScrollRange();
                        Log.d(TAG, "percent=" + percent);
//                        mSearchContainer.setBackgroundColor(ColorUtils.alphaColor(Color.BLACK, percent * 0.4f));
                        btnBack.setVisibility(View.VISIBLE);
                        btnBack.setAlpha(percent);
//                        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) tvSearch.getLayoutParams();
//                        mlp.leftMargin = ScreenUtils.dp2pxInt(16 + percent * 40);
//                        tvSearch.setLayoutParams(mlp);
                        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) searchBar.getLayoutParams();
                        mlp.leftMargin = ScreenUtils.dp2pxInt(16 + percent * 40);
                        searchBar.setLayoutParams(mlp);


                        if (mExpand && percent == 0) {
                            mSearchContainer.setOnClickListener(null);
                            mSearchContainer.setClickable(false);
                            isSearch = false;
                            int height = ScreenUtils.dp2pxInt(100);
                            ViewGroup.LayoutParams params = toolbar.getLayoutParams();
                            params.height = height;
                            toolbar.setLayoutParams(params);
                            params = mContainer.getLayoutParams();
                            params.height = coordinatorLayout.getHeight() - height;
                            mContainer.setLayoutParams(params);
                            btnBack.setVisibility(View.GONE);

                            searchBar.setText(null);
                            searchBar.setEditable(false);

                        } else if (!mExpand && percent == 1f) {
                            mSearchContainer.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    hideSoftInput();
                                    mExpand = true;
                                    mAppBarLayout.setExpanded(true, true);
                                }
                            });
                            Log.d(TAG, "isEditable=" + searchBar.isEditable());
//                            if (!searchBar.isEditable()) {
//                                searchBar.setEditable(true);
//                                Log.d(TAG, "isEditable=" + searchBar.isEditable());
//                                showSoftInput(searchBar.getEditor());
//                            }
                            if (getTopFragment() instanceof MainFragment) {
                                SearchFragment fragment = new SearchFragment();
                                fragment.setOnDismissListener(new IDialog.OnDismissListener() {
                                    @Override
                                    public void onDismiss() {
                                        hideSoftInput();
                                        mExpand = true;
                                        mAppBarLayout.setExpanded(true, true);
                                    }
                                });
                                fragment.show(context);
                            }
                        }
                    }
                }
            }
        });

        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSearch) {
                    return;
                }

                isSearch = true;
                int height = ScreenUtils.dp2pxInt(56);
                ViewGroup.LayoutParams params = toolbar.getLayoutParams();
                params.height = height;
                toolbar.setLayoutParams(params);
                params = mContainer.getLayoutParams();
                params.height = coordinatorLayout.getHeight() - height;
                mContainer.setLayoutParams(params);

                mExpand = false;
                mAppBarLayout.setExpanded(false, true);
            }
        });

        EasyRecyclerView<NoteItem> recyclerView = new EasyRecyclerView<>(findViewById(R.id.recycler_view));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setData(list)
                .setItemRes(R.layout.item_note)
                .setLayoutManager(layoutManager)
                .onBindViewHolder((holder, list, position, payloads) -> {
                    NoteItem note = list.get(position);
                    HighlightTextView highlightTextView = holder.getView(R.id.tv_text);
                    highlightTextView.setText(note.getSnippet());
//                    holder.setText(R.id.tv_content, note.getSnippet());
                    holder.setText(R.id.tv_info, format.format(new Date(note.getModifyDate())));
                })
                .onItemClick((holder, view1, data) -> {
                    start(NoteDetailFragment.newInstance(data.getId()));
                })
                .build();
        recyclerView.showLoading();


        HttpApi.getNoteList(cookie)
                .onSuccess(obj -> {
                    Log.d("MainActivity", "obj=" + obj);
                    if (obj.has("data")) {
                        JSONArray entries = obj.getJSONObject("data").getJSONArray("entries");
                        for (int i = 0; i < entries.length(); i++) {
                            list.add(NoteItem.from(entries.getJSONObject(i)));
                        }
                        tvInfo.setText(list.size() + "条便签");
                        recyclerView.showContent();
                    } else {
                        Toast.makeText(context, "登录已过期，请重新登录！", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(context, LoginActivity.class));
                        _mActivity.finish();
                    }

                })
                .subscribe();
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        darkStatusBar();
    }

    @Override
    public boolean onBackPressedSupport() {
        if (isSearch) {
            mExpand = true;
            mAppBarLayout.setExpanded(true, true);
            return true;
        }
        return super.onBackPressedSupport();
    }
}
