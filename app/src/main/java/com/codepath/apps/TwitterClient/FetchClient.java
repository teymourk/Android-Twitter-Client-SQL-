package com.codepath.apps.TwitterClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by kia on 3/8/17.
 */

public class FetchClient extends AppCompatActivity {

    public void fetchUserInfo(final TextView profileDetails, final TextView bio, final TextView following, final TextView followers, final ImageView image, final Context context) {

        RestApplication.getRestClient().fetch_user_profile(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                UserModel userInfo = new UserModel(response);

                String screen_name_formatted = "<b>" + "<font color='black'>" + userInfo.getName() + "</font>" + "<b>" + "<br>" + "@" + userInfo.getScreenName();
                String following_formetted = "<b>" + "<font color='black'>" + Integer.toString(userInfo.getFollowers()) + "</font>" + "</b>" + " Followers";
                String followers_formatted = "<b>" + "<font color='black'>" + Integer.toString(userInfo.getFollowing()) + "</font>" + "</b>" + " Following ";

                profileDetails.setText(Html.fromHtml(screen_name_formatted));
                bio.setText(userInfo.getDescription());
                following.setText(Html.fromHtml(following_formetted));
                followers.setText(Html.fromHtml(followers_formatted));

                ImageLoaderConfiguration configuration = new ImageLoaderConfiguration
                        .Builder(context).build();
                ImageLoader.getInstance().init(configuration);
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.loadImage(userInfo.get_image_url(), new SimpleImageLoadingListener(){

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);

                        image.setImageBitmap(loadedImage);
                    }
                });
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }
        });
    }

//    public void fetchTweets() {
//
//        RestApplication.getRestClient().fetch_user_tweets(new JsonHttpResponseHandler() {
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                super.onSuccess(statusCode, headers, response);
//
//                ArrayList<TweetModel> tweetsArray = null;
//
//                for (int i = 0; i < response.length(); i++) {
//
//                    try {
//
//                        JSONObject tweetJson = response.getJSONObject(i);
//
//                        TweetModel tweetInfo = new TweetModel(tweetJson);
//
//                        tweetsArray = new ArrayList<TweetModel>();
//                        tweetsArray.add(tweetInfo);
//
//                    } catch (JSONException e) {
//
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
//                super.onFailure(statusCode, headers, throwable, errorResponse);
//            }
//        });
//    }
}
