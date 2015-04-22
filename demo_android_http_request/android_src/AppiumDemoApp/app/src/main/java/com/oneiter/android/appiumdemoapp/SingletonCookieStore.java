package com.oneiter.android.appiumdemoapp;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tima on 4/22/15.
 */
public class SingletonCookieStore {

    private static SingletonCookieStore mSingletonCookieStore;
    private BasicCookieStore mCookieStore;

    private SingletonCookieStore() {

    }

    public static SingletonCookieStore getInstance() {
        if (null == mSingletonCookieStore) {
            mSingletonCookieStore = new SingletonCookieStore();
        }
        return mSingletonCookieStore;
    }

    public String getByName(String key) {

        if (null == mCookieStore) {
            return null;
        }

        for(Cookie cookie : mCookieStore.getCookies()) {
            if (cookie.getName().equals(key)) {
               return cookie.getValue();
            }
        }

        return null;
    }

    public String getFormatCookieByKey(String[] keys) {
        if (null == mCookieStore) {
            return null;
        }

        StringBuffer sb = new StringBuffer();

        for(String key : keys) {
            for(Cookie cookie : mCookieStore.getCookies()) {
                if (key.equals(cookie.getName())) {
                    sb.append(cookie.getName()+"="+cookie.getValue()+";");
                }
            }
        }

        return sb.toString();
    }

    public void storeCookieStore(BasicCookieStore respCookieStore) {
        mCookieStore = respCookieStore;
    }

    public void clearCookie() {
        mCookieStore = null;
    }
}
