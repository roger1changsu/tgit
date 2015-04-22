package com.oneiter.android.appiumdemoapp;

import org.apache.http.HttpEntity;
import org.json.JSONObject;

/**
 * Created by tima on 4/22/15.
 */
public interface CookieHttpClientRequestCallbackHandler {
    public void onError();
//    public void onSuccess(JSONObject respJsonObject, SingletonCookieStore singletonCookieStore);
    public void onSuccess(HttpEntity httpRespEntity, SingletonCookieStore singletonCookieStore);
}
