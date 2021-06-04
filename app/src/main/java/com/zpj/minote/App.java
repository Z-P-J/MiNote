package com.zpj.minote;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.zpj.http.ZHttp;
import com.zpj.http.core.CookieJar;
import com.zpj.http.core.DefaultCookieJar;
import com.zpj.http.parser.html.TokenQueue;
import com.zpj.utils.PrefsHelper;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ZHttp.config()
                .cookieJar(new AppCookieJar())
                .init();
    }

    public static class AppCookieJar implements CookieJar {

        private static final String TAG = "AppCookieJar";

        private final Map<String, Map<String, String>> cookiesMap = new HashMap<>();

        @Nullable
        @Override
        public Map<String, String> loadCookies(@NonNull URL url) {
            synchronized (cookiesMap) {
                String host = url.getHost();
                Log.d(TAG, "loadCookies host=" + host + " url=" + url.toString());
                Map<String, String> cookieMap = cookiesMap.get(host);
                Log.d(TAG, "loadCookies cookieMap=" + cookieMap);
                if (cookieMap != null) {
                    for (Map.Entry<String, String> entry : cookieMap.entrySet()) {
                        Log.d(TAG, "loadCookies key=" + entry.getKey());
                        Log.d(TAG, "loadCookies value=" + entry.getValue());
                    }
                } else {
                    cookieMap = new HashMap<>();
                    cookiesMap.put(host, cookieMap);
                    String cookie = PrefsHelper.with().getString("cookie");
                    TokenQueue cd = new TokenQueue(cookie);
                    while (!cd.isEmpty()) {
                        String cookieName = cd.chompTo("=").trim();
                        String cookieVal = cd.consumeTo(";").trim();
                        cd.chompTo(";");
                        cookieMap.put(cookieName, cookieVal);
                    }
                }

                return cookieMap;
            }
        }

        @Override
        public void saveCookies(@NonNull URL url, @NonNull Map<String, String> cookieMap) {
            synchronized (cookiesMap) {
                String host = url.getHost();
                Log.d(TAG, "saveCookies host=" + host + " url=" + url.toString());
                for (Map.Entry<String, String> entry : cookieMap.entrySet()) {
                    Log.d(TAG, "saveCookies key111=" + entry.getKey());
                    Log.d(TAG, "saveCookies value111=" + entry.getValue());
                }
                Map<String, String> cookies = cookiesMap.get(host);
                Log.d(TAG, "loadCookies cookieMap=" + cookieMap + " cookies=" + cookies);
                if (cookies == null) {
                    cookiesMap.put(host, cookieMap);
                } else if (cookies != cookieMap) {
                    for (Map.Entry<String, String> entry : cookieMap.entrySet()) {
                        cookies.put(entry.getKey(), entry.getValue());
                        Log.d(TAG, "saveCookies key222=" + entry.getKey());
                        Log.d(TAG, "saveCookies value222=" + entry.getValue());
                    }
                }
            }
        }
    }

}
