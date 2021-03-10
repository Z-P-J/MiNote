package com.zpj.minote.api;

import com.zpj.http.ZHttp;
import com.zpj.http.core.HttpObserver;

import org.json.JSONObject;

public class HttpApi {

    public static HttpObserver<JSONObject> getNodeList(String cookie) {
        return ZHttp.get("https://i.mi.com/note/full/page/?ts=" + System.currentTimeMillis())
                .cookieJar(null)
                .cookie(cookie)
                .toJsonObject();
    }

}
