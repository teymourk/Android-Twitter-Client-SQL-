package com.codepath.apps.TwitterClient;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kia on 3/8/17.
 */

public class UserModel {

    private Integer _id;
    private Integer _followers_count;
    private Integer _following_count;
    private String  _image_url;
    private String _screen_name;
    private String _name;
    private String _description;

    public Integer getUserId() {
        return _id;
    }

    public Integer getFollowers() {
        return _followers_count;
    }

    public Integer getFollowing() {
        return _following_count;
    }

    public String get_image_url() {
        return _image_url;
    }

    public String getScreenName() {
        return _screen_name;
    }

    public String getName() {
        return _name;
    }

    public String getDescription() {
        return _description;
    }

    public UserModel(JSONObject jsonData) {

        if (jsonData != null) {

            try {

                _screen_name = jsonData.getString("screen_name");
                _name = jsonData.getString("name");
                _image_url = jsonData.getString("profile_image_url");
                _description = jsonData.getString("description");
                _followers_count = jsonData.getInt("followers_count");
                _following_count = jsonData.getInt("friends_count");
                _id = jsonData.getInt("id");

            } catch (JSONException e) {

                e.printStackTrace();
            }
        }
    }
}
