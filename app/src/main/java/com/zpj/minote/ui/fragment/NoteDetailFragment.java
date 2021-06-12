package com.zpj.minote.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zpj.fragmentation.BaseFragment;
import com.zpj.fragmentation.BaseSwipeBackFragment;
import com.zpj.minote.R;
import com.zpj.minote.api.HttpApi;
import com.zpj.statemanager.StateManager;

public class NoteDetailFragment extends BaseSwipeBackFragment {

    private static final String KEY_ID = "key_id";

    private EditText mTvContent;

    private StateManager mStateManager;

    public static NoteDetailFragment newInstance(String id) {

        Bundle args = new Bundle();
        args.putString(KEY_ID, id);
        NoteDetailFragment fragment = new NoteDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_note_detail;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        if (getArguments() == null) {
            return;
        }
        String id = getArguments().getString(KEY_ID, "");
        if (TextUtils.isEmpty(id)) {
            return;
        }
        mTvContent = findViewById(R.id.tv_content);
        mStateManager = StateManager.with(mTvContent)
                .onRetry(manager -> getNoteDetail(id))
                .showLoading();
        getNoteDetail(id);
    }

    @Override
    public void onDestroyView() {
        hideSoftInput();
        super.onDestroyView();
    }

    private void getNoteDetail(String id) {
        HttpApi.getNoteDetail(id)
                .onSuccess(data -> {
                    Log.d("NoteDetailFragment", "getNoteDetail data=" + data);

                    mTvContent.setText(data.getJSONObject("data").getJSONObject("entry").getString("content"));
                    mStateManager.showContent();
                })
                .onError(throwable -> mStateManager.showError(throwable.getMessage()))
                .subscribe();
    }

}
