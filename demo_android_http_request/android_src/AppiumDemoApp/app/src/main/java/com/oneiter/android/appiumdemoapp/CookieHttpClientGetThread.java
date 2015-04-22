package com.oneiter.android.appiumdemoapp;

import android.content.Context;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.CookieStore;

/**
 * Created by tima on 4/22/15.
 */
public class CookieHttpClientGetThread extends Thread {
    private Context mContext;
    private String mUrl;
    private CookieHttpClientRequestCallbackHandler mHandler;
    private String mCookie;

    public CookieHttpClientGetThread(Context context, String url, String cookieInReq, CookieHttpClientRequestCallbackHandler handler) {
        mContext = context;
        mUrl = url;
        mCookie = cookieInReq;
        mHandler = handler;
    }

    @Override
    public void run() {
        DefaultHttpClient httpClient = new DefaultHttpClient();

        HttpGet httpGet = new HttpGet(mUrl);

        httpGet.setHeader("Cookie", mCookie);

        try {
            HttpResponse httpResp = httpClient.execute(httpGet);

            if (httpResp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity httpRespEntity = httpResp.getEntity();

//                JSONObject respJsonObject = new JSONObject(EntityUtils.toString(httpRespEntity));
//
//                BasicCookieStore respCookieStore = (BasicCookieStore) httpClient.getCookieStore();
                BasicCookieStore respCookieStore = (BasicCookieStore) httpClient.getCookieStore();
//
//                SingletonCookieStore singletonCookieStore = SingletonCookieStore.getInstance();
//                singletonCookieStore.storeCookieStore(respCookieStore);

                SharedPreferencesCookieStore sharedPreferencesCookieStore = SharedPreferencesCookieStore.getInstance();
                sharedPreferencesCookieStore.setCookieStore(mContext, respCookieStore);

//                mHandler.onSuccess(respJsonObject, singletonCookieStore);
//                mHandler.onSuccess(httpRespEntity, singletonCookieStore);
                mHandler.onSuccess(httpRespEntity, sharedPreferencesCookieStore);
            } else {
                // TODO -
                mHandler.onError();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
//        catch (JSONException e) {
//            e.printStackTrace();
//        }
    }
}
