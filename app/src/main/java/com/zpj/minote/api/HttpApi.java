package com.zpj.minote.api;

import android.net.Uri;
import android.util.Log;

import com.zpj.http.ZHttp;
import com.zpj.http.core.HttpObserver;
import com.zpj.http.parser.html.TokenQueue;
import com.zpj.utils.PrefsHelper;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class HttpApi {

    public static HttpObserver<JSONObject> getNoteList(String cookie) {
        return ZHttp.get("https://i.mi.com/note/full/page/?ts=" + System.currentTimeMillis())
                .cookieJar(null)
                .cookie(cookie)
                .toJsonObject();
    }

    public static HttpObserver<JSONObject> getNoteDetail(String id) {
        return ZHttp.get("https://i.mi.com/note/note/" + id + "/?ts=" + System.currentTimeMillis())
                .cookieJar(null)
                .cookie(PrefsHelper.with().getString("cookie"))
                .toJsonObject();
    }

    public static HttpObserver<JSONObject> getSearchResults(String cookie, String keyword) {
        TokenQueue cd = new TokenQueue(cookie);
        cd.chompTo("serviceToken=");
        String serviceToken = cd.consumeTo(";").trim();
        Log.d("MainActivity", "serviceToken=" + serviceToken);

        Log.d("MainActivity", "keyword=" + keyword);
        return ZHttp.post("https://i.mi.com/note/search")
                .data("queryString", keyword)
                .data("beginTag", "0")
                .data("limit", "30")
                .data("serviceToken", serviceToken)
                .cookieJar(null)
                .cookie(cookie)
                .toJsonObject();
    }

}
