package com.codepath.apps.TwitterClient;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;


public class TwitterAPI extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "oUWL0t1RsbKYqET155cnhBzyq";       // Change this
    public static final String REST_CONSUMER_SECRET = "mx75QaOyDETDQRk7dmER3yIEOKwMD27uqLWsTrPDkwRf5z7eqh"; // Change this
    public static final String REST_CALLBACK_URL = "oauth://TwitterClient"; // Change this (here and in manifest)

    public TwitterAPI(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    public void fetch_user_profile(AsyncHttpResponseHandler handler) {

        String url = getApiUrl("/account/verify_credentials.json");
        client.get(url, null, handler);
    }

    public void fetch_user_tweets(AsyncHttpResponseHandler handler) {

        String url = getApiUrl("statuses/home_timeline.json?count=10");
        client.get(url, null, handler);
    }

    public void update_user_bio(String text, AsyncHttpResponseHandler handler) {

        String url = getApiUrl("account/update_profile.json?description=" + text);
        client.post(url, null, handler);
    }

    public void post_tweet(String text,  AsyncHttpResponseHandler handler) {

        String url = getApiUrl("statuses/update.json?status=" + text);
        client.post(url, null, handler);
    }
}
