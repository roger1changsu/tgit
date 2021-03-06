package com.oneiter.android.appiumdemoapp;

import android.content.Context;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.CookieStore;
import java.util.List;

/**
 * Created by tima on 4/22/15.
 */
public class CookieHttpClientPostThread extends Thread {
    private Context mContext;
    private String mUrl;
    private CookieHttpClientRequestCallbackHandler mHandler;
    private String mCookie;
    private List<NameValuePair> mReqParams;

    public CookieHttpClientPostThread(Context context, String url, String cookieInReq, List<NameValuePair> reqParams, CookieHttpClientRequestCallbackHandler handler) {
        mContext = context;
        mUrl = url;
        mCookie = cookieInReq;
        mReqParams = reqParams;
        mHandler = handler;
    }

    @Override
    public void run() {
        DefaultHttpClient httpClient = new DefaultHttpClient();

        HttpPost httpReq = new HttpPost(mUrl);

        httpReq.setHeader("Cookie", mCookie);


        try {
            HttpEntity httpEntity = new UrlEncodedFormEntity(mReqParams, "UTF-8");

            httpReq.setEntity(httpEntity);

            HttpResponse httpResp = httpClient.execute(httpReq);

            if (httpResp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity httpRespEntity = httpResp.getEntity();
//                JSONObject respJsonObject = new JSONObject(EntityUtils.toString(httpRespEntity));

//                BasicCookieStore respCookieStore = (BasicCookieStore) httpClient.getCookieStore();
                BasicCookieStore respCookieStore = (BasicCookieStore) httpClient.getCookieStore();

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
