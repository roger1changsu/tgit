package com.oneiter.android.appiumdemoapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class HttpClientWithCookieActivity extends ActionBarActivity {

    private final static int SHOW_TEXT_RESP = 0;
    private final static int SHOW_IMG = 1;
    private final static String COOKIE_KEY_JSESSIONID = "jsessionid";
    private final static String SHARED_PREFERENCE_KEY_URL = "req_url";
    private final static String DEFAULT_URL = "http://172.17.11.234:3000/api/";
    private final static String DEFAULT_IMG_URL = "https://www.baidu.com/img/bdlogo.png";

    private EditText mETShowResp;
    private EditText mETUrl;
    private Button mBtnGetResp;
    private Button mBtnSaveURL;
    private Button mBtnPostRes;
    private Button mBtnGetImg;
    private ImageView mIvShowDownloadImg;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case SHOW_TEXT_RESP:
                    mETShowResp.setText(msg.obj.toString());
                    break;
                case SHOW_IMG:
                    // TODO -
                    mIvShowDownloadImg.setImageBitmap((Bitmap) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_client_with_cookie);

        initView();
        initEvent();
    }

    private void initView() {
        mBtnGetResp = (Button) findViewById(R.id.btn_get_http_resp_with_cookie);
        mBtnSaveURL = (Button) findViewById(R.id.btn_save_url);
        mBtnPostRes = (Button) findViewById(R.id.btn_post_get_http_resp_with_cookie);
        mBtnGetImg = (Button) findViewById(R.id.btn_get_image);

        mETShowResp = (EditText) findViewById(R.id.et_show_resp);
        mETUrl = (EditText) findViewById(R.id.et_url);

        mIvShowDownloadImg = (ImageView) findViewById(R.id.iv_show_download_img);

        mETUrl.setText(DEFAULT_URL);
    }

    private void initEvent() {

        mBtnGetImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] reqCookie = {COOKIE_KEY_JSESSIONID};
                String url = DEFAULT_IMG_URL;

                new CookieHttpClientGetThread(
                        HttpClientWithCookieActivity.this,
                        url,
                        SharedPreferencesCookieStore.getInstance().getFormatedCookies(HttpClientWithCookieActivity.this, reqCookie),
                        new CookieHttpClientRequestCallbackHandler() {
                            @Override
                            public void onError() {

                            }

                            @Override
                            public void onSuccess(HttpEntity httpRespEntity, SharedPreferencesCookieStore sharedPreferencesCookieStore) {

                                try {
                                    InputStream is = httpRespEntity.getContent();
                                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                                    Message message = new Message();
                                    message.what = SHOW_IMG;
                                    message.obj = bitmap;
                                    mHandler.sendMessage(message);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                ).start();

            }
        });

        mBtnSaveURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = HttpClientWithCookieActivity.this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(SHARED_PREFERENCE_KEY_URL, String.valueOf(mETUrl.getText()));
                editor.commit();
            }
        });

        mBtnGetResp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] reqCookie = {COOKIE_KEY_JSESSIONID};

                // Get URL from Shared Preference or Get URL from URL EditTextView
                SharedPreferences sharedPreferences = HttpClientWithCookieActivity.this.getPreferences(Context.MODE_PRIVATE);
                String url = sharedPreferences.getString(SHARED_PREFERENCE_KEY_URL, String.valueOf(mETUrl.getText()));

                new CookieHttpClientGetThread(
                        HttpClientWithCookieActivity.this,
                        url,
                        SharedPreferencesCookieStore.getInstance().getFormatedCookies(HttpClientWithCookieActivity.this, reqCookie),
                        new CookieHttpClientRequestCallbackHandler(){

                            @Override
                            public void onError() {

                            }

                            @Override
                            public void onSuccess(HttpEntity httpRespEntity, SharedPreferencesCookieStore sharedPreferencesCookieStore) {
                                try {
                                    JSONObject respJsonObject = new JSONObject(EntityUtils.toString(httpRespEntity));
                                    Message message = new Message();
                                    message.what = SHOW_TEXT_RESP;
                                    if (sharedPreferencesCookieStore.getStringCookie(HttpClientWithCookieActivity.this, COOKIE_KEY_JSESSIONID) != null) {
                                        message.obj = "Get Request Body:\n" + respJsonObject.toString() + "\n\nCookie:\n"+sharedPreferencesCookieStore.getStringCookie(HttpClientWithCookieActivity.this, COOKIE_KEY_JSESSIONID);
                                    } else {
                                        message.obj = "Get Request Body:\n" + respJsonObject.toString();
                                    }
                                    mHandler.sendMessage(message);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                ).start();
            }
        });

        mBtnPostRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] reqCookie = {COOKIE_KEY_JSESSIONID};

                // Get URL from Shared Preference or Get URL from URL EditTextView
                SharedPreferences sharedPreferences = HttpClientWithCookieActivity.this.getPreferences(Context.MODE_PRIVATE);
                String url = sharedPreferences.getString(SHARED_PREFERENCE_KEY_URL, String.valueOf(mETUrl.getText()));

                List<NameValuePair> lstParams = new ArrayList<NameValuePair>();

                new CookieHttpClientPostThread(
                        HttpClientWithCookieActivity.this,
                        url,
                        SharedPreferencesCookieStore.getInstance().getFormatedCookies(HttpClientWithCookieActivity.this, reqCookie),
                        lstParams,
                        new CookieHttpClientRequestCallbackHandler(){

                            @Override
                            public void onError() {

                            }

                            @Override
                            public void onSuccess(HttpEntity httpRespEntity, SharedPreferencesCookieStore sharedPreferencesCookieStore) {
                                JSONObject respJsonObject = null;
                                try {
                                    respJsonObject = new JSONObject(EntityUtils.toString(httpRespEntity));
                                    Message message = new Message();
                                    message.what = SHOW_TEXT_RESP;
                                    if (sharedPreferencesCookieStore.getStringCookie(HttpClientWithCookieActivity.this, COOKIE_KEY_JSESSIONID) != null) {
                                        message.obj = "Post Request Body:\n" + respJsonObject.toString() + "\n\nCookie:\n"+sharedPreferencesCookieStore.getStringCookie(HttpClientWithCookieActivity.this, COOKIE_KEY_JSESSIONID);
                                    } else {
                                        message.obj = "Post Request Body:\n" + respJsonObject.toString();
                                    }
                                    mHandler.sendMessage(message);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                }).start();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_http_client_with_cookie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    private class GetRespRunnable implements Runnable {

        @Override
        public void run() {
            DefaultHttpClient httpClient = new DefaultHttpClient();

            HttpGet httpGet = new HttpGet(String.valueOf(mETUrl.getText()));

            httpGet.setHeader("Cookie", "jsessionid=jsessionid%2cfrom%2crequest;");

            try {
                HttpResponse httpResp = httpClient.execute(httpGet);

                HttpEntity httpEntity = httpResp.getEntity();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpEntity.getContent(), "UTF-8"), 8*1024);

                StringBuffer sb = new StringBuffer();
                JSONObject respJsonObject = null;

                if (httpEntity != null) {
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }
                    respJsonObject = new JSONObject(sb.toString());
                }

                String showStr = "";
                showStr += respJsonObject.toString();

                BasicCookieStore respCookieStore = (BasicCookieStore) httpClient.getCookieStore();

                for(Cookie respCookie : respCookieStore.getCookies()) {
                    if (respCookie.getName().equals("jsessionid")) {
                        showStr += "\n Cookie - jsessionid: "+respCookie.getValue();
                    }
                }

                Message message = new Message();
                message.what = SHOW_TEXT_RESP;
                message.obj = showStr;
                mHandler.sendMessage(message);

            } catch (IOException e) {
                e.printStackTrace();
                Message message = new Message();
                message.what = SHOW_TEXT_RESP;
                message.obj = e.getMessage();
                mHandler.sendMessage(message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    */

}
