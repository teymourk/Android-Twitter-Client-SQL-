package com.codepath.apps.TwitterClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TweetModel  {

    private UserModel user;

    private String _text;

    public UserModel getUser() {
        return user;
    }

    public String getBody() {
        return _text;
    }

    public static TweetModel fromJson(JSONObject jsonObject) {

        TweetModel tweet = new TweetModel();

        try {

            tweet._text = jsonObject.getString("text");

            UserModel users = new UserModel(jsonObject.getJSONObject("user"));
            tweet.user = users;

        } catch (JSONException e) {

            e.printStackTrace();
        }

        return tweet;
    }

    public static ArrayList<TweetModel> fromJson(JSONArray jsonArray) {
        ArrayList<TweetModel> tweets = new ArrayList<TweetModel>(jsonArray.length());

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;

            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            TweetModel tweet = TweetModel.fromJson(tweetJson);

            if (tweet != null) {
                tweets.add(tweet);
            }
        }

        return tweets;
    }
}