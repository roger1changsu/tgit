package com.oneiter.android.appiumdemoapp;

import android.content.Context;
import android.content.SharedPreferences;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.util.List;

/**
 * Created by tima on 4/22/15.
 */
public class SharedPreferencesCookieStore {

    public static SharedPreferencesCookieStore mSharedPreferencesCookieStore;

    private static final String SHARED_PREF_KEY_COOKIE="cookie";

    private SharedPreferencesCookieStore() {

    }

    public static SharedPreferencesCookieStore getInstance() {
        if (null == mSharedPreferencesCookieStore) {
            mSharedPreferencesCookieStore = new SharedPreferencesCookieStore();
        }
        return mSharedPreferencesCookieStore;
    }

    /**
     * 设置CookieStore
     * @param context
     * @param cookieStore
     */
    public void setCookieStore(Context context, BasicCookieStore cookieStore) {
//        List<HttpCookie> lstCookies = cookieStore.getCookies();
        List<Cookie> lstCookies = cookieStore.getCookies();
        for(Cookie cookie : lstCookies) {
            setStringCookie(context, cookie.getName(), cookie.getValue());
        }
    }

    /**
     * 获取cookie，
     * @param context
     * @param name
     * @return
     */
    public String getStringCookie(Context context, String name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_KEY_COOKIE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(name, "");
    }

    /**
     * 设置cookie
     * @param context
     * @param name
     * @param value
     */
    public void setStringCookie(Context context, String name, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_KEY_COOKIE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(name, value);
        editor.commit();
    }

    public String getFormatedCookies(Context context, String[] cookieKeys) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_KEY_COOKIE, Context.MODE_PRIVATE);

        StringBuffer sb = new StringBuffer();

        for (String key : cookieKeys) {

            sb.append(String.format("%s=%s;", key, sharedPreferences.getString(key, "")));

        }
        return sb.toString();
    }

//    public int getIntCookie(Context context, String name) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_KEY_COOKIE, Context.MODE_PRIVATE);
//        return sharedPreferences.getInt(name, -1);
//    }
//
//    public void setIntCookie(Context context, String name, int value) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_KEY_COOKIE, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt(name, value);
//        editor.commit();
//    }
}
