package com.zpj.minote.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.just.agentweb.AbsAgentWebSettings;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.AgentWebConfig;
import com.just.agentweb.IAgentWebSettings;
import com.zpj.minote.R;
import com.zpj.utils.PrefsHelper;
import com.zpj.utils.StatusBarUtils;

public class LoginActivity extends AppCompatActivity {

    private static final String UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.182 Safari/537.36 Edg/88.0.705.81";

    //    private WebView webView;
    private AgentWeb mAgentWeb;
    private boolean isLoad = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.transparentStatusBar(this);
        setContentView(R.layout.activity_login);
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        try {
            int color = Color.parseColor(PrefsHelper.with().getString("currentColor", "#2196f3"));
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
            StatusBarUtils.setStatusBarColor(LoginActivity.this.getWindow(), color, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (PrefsHelper.with().getInt("add", 0) == 1) {

            PrefsHelper.with().putInt("add", 0);
        }
//        CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(this);
//        CookieManager cookieManager = CookieManager.getInstance();
//        cookieManager.removeAllCookie();
        initView();
//        new Thread(() -> PoJieUtil.checkShoujileyuan(getApplicationContext())).start();

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        LinearLayout content = findViewById(R.id.content);
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(content, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .setAgentWebWebSettings(new CustomSettings())
                .setWebViewClient(new WebViewClient() {
//                    @Override
//                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                        if (!isLoad) {
//                            view.loadUrl(url);
//                        }
//                        return true;
//                    }


                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        Log.d("LoginActivity", "shouldOverrideUrlLoading url=" + url);
                        return super.shouldOverrideUrlLoading(view, url);
                    }

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                    }

                    private boolean hasChecked = true;

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        String cookie = AgentWebConfig.getCookiesByUrl(url);
                        Log.d("LoginActivity", "hasChecked=" + hasChecked);
                        Log.d("LoginActivity", "url=" + url + " cookie=" + cookie);
                        if (url.startsWith("https://i.mi.com/#/")) {
                            hasChecked = false;
                            view.loadUrl("https://i.mi.com/note/h5");
                        } else if (url.startsWith("https://account.xiaomi.com")) {
                            hasChecked = true;
                        } else if (url.startsWith("https://i.mi.com/note/h5") && hasChecked) {
                            isLoad = false;
                            PrefsHelper.with().putString("cookie", cookie);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                })
                .createAgentWeb()
                .ready()
                .go(TextUtils.isEmpty(AgentWebConfig.getCookiesByUrl("https://i.mi.com")) ? "https://i.mi.com/" : "https://i.mi.com/note/h5#/");
    }

    private class CustomSettings extends AbsAgentWebSettings {

        @Override
        protected void bindAgentWebSupport(AgentWeb agentWeb) {

        }

        @Override
        public IAgentWebSettings toSetting(WebView webView) {
            super.toSetting(webView);
            getWebSettings().setBlockNetworkImage(false);//是否阻塞加载网络图片  协议http or https
            getWebSettings().setAllowFileAccess(false); //允许加载本地文件html  file协议, 这可能会造成不安全 , 建议重写关闭
            getWebSettings().setNeedInitialFocus(true);
            getWebSettings().setGeolocationEnabled(false);
            getWebSettings().setUserAgentString(UA);
            getWebSettings().setDomStorageEnabled(true);
            getWebSettings().setUseWideViewPort(true);
            getWebSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            getWebSettings().setLoadWithOverviewMode(true);
            getWebSettings().setSupportZoom(true);
            getWebSettings().setBuiltInZoomControls(true);
            return this;
        }
    }


    @Override
    protected void onDestroy() {
        mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    private void showLoading(String content) {

    }

    private void closeLoading() {

    }

}
