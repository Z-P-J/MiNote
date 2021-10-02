package com.zpj.minote.ui;

import android.os.Bundle;

import com.zpj.fragmentation.SupportActivity;
import com.zpj.minote.R;
import com.zpj.minote.ui.fragment.MainFragment;
import com.zpj.utils.StatusBarUtils;

public class MainActivity extends SupportActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.transparentStatusBar(this);
        setContentView(R.layout.activity_main);
        MainFragment mainFragment = findFragment(MainFragment.class);
        if (mainFragment == null) {
            mainFragment = new MainFragment();
            loadRootFragment(R.id.fl_container, mainFragment);
        }
    }
}