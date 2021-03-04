package com.zpj.minote;

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
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.just.agentweb.AbsAgentWebSettings;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.AgentWebConfig;
import com.just.agentweb.IAgentWebSettings;
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = new MenuInflater(LoginActivity.this);
//        //MenuInflater inflater = getMenuInflater();//另一种方法
//        inflater.inflate(R.menu.login_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.change_web) {
//            if (item.getTitle().equals("切换电脑版网页")) {
//                item.setTitle("切换手机版网页");
////                webView.getSettings().setUserAgentString(UAHelper.getRandomPCUA());
////                webView.loadUrl("https://passport.baidu.com/v2/?login&u=https%3A%2F%2Fpan.baidu.com%2F");
//                mAgentWeb.getAgentWebSettings().getWebSettings().setUserAgentString(UAHelper.getRandomPCUA());
//                mAgentWeb.getWebCreator().getWebView().loadUrl("https://passport.baidu.com/v2/?login&u=https%3A%2F%2Fpan.baidu.com%2F");
//            } else if (item.getTitle().equals("切换手机版网页")) {
//                item.setTitle("切换电脑版网页");
//                mAgentWeb.getAgentWebSettings().getWebSettings().setUserAgentString(UAHelper.getRandomMobileUA());
//                mAgentWeb.getWebCreator().getWebView().loadUrl("https://wappass.baidu.com/passport?login&authsite=1&tpl=netdisk&display=mobile&u=https://pan.baidu.com/");
//            }
//        }
//        return super.onOptionsItemSelected(item);
//    }

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
//                        if (url.startsWith("https://pan.baidu.com/wap/home")) {
//                            view.getSettings().setUserAgentString(UAHelper.getSystemUA());
//                            showLoading("加载中，请稍后...");
//                            isLoad = true;
//                        } else if (url.startsWith("https://wappass.baidu.com/passport?login")
//                                || url.startsWith("https://passport.baidu.com/v2/?login")) {
//                            showLoading("加载中，请稍后...");
//                        } else if (url.startsWith("https://pan.baidu.com/")) {
//                            view.getSettings().setUserAgentString(UAHelper.getPCBaiduNetdiskUA());
//                            showLoading("加载中，请稍后...");
//                        }
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
