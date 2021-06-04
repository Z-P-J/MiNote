package com.zpj.minote.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zpj.fragmentation.BaseFragment;
import com.zpj.minote.R;
import com.zpj.minote.api.HttpApi;
import com.zpj.minote.model.NoteItem;
import com.zpj.minote.ui.LoginActivity;
import com.zpj.recyclerview.EasyRecyclerView;
import com.zpj.utils.PrefsHelper;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainFragment extends BaseFragment {

    private final List<NoteItem> list = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        String cookie = PrefsHelper.with().getString("cookie");
        Log.d("MainActivity", "cookie=" + cookie);

        findViewById(R.id.tv_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(new SearchFragment());
            }
        });

        EasyRecyclerView<NoteItem> recyclerView = new EasyRecyclerView<>(findViewById(R.id.recycler_view));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        recyclerView.setData(list)
                .setItemRes(R.layout.item_note)
                .setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL))
                .onBindViewHolder((holder, list, position, payloads) -> {
                    NoteItem note = list.get(position);
                    holder.setText(R.id.tv_content, note.getSnippet());
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
                        recyclerView.showContent();
                    } else {
                        Toast.makeText(context, "登录已过期，请重新登录！", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(context, LoginActivity.class));
                        _mActivity.finish();
                    }

                })
                .subscribe();
    }
}
