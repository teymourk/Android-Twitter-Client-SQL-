package com.codepath.apps.TwitterClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

/**
 * Created by kia on 3/9/17.
 */

public class Adapter extends ArrayAdapter<TweetModel> {

    public Adapter(Context context, List<TweetModel> tweets) {
        super(context, 0, tweets);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_layout, null);
        }

        TweetModel tweet = getItem(position);

        TextView tweet_text = (TextView) view.findViewById(R.id.tweet_text);
        TextView screen_name = (TextView) view.findViewById(R.id.user_name);
        final ImageView profileImage = (ImageView) view.findViewById(R.id.user_profile);

        String screen_name_formatted = "<b>" + "<font color='black'>" + tweet.getUser().getName() + "</font>" + "\t@" + tweet.getUser().getScreenName();

        tweet_text.setText(tweet.getBody());
        screen_name.setText(Html.fromHtml(screen_name_formatted));

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration
                .Builder(getContext()).build();
        ImageLoader.getInstance().init(configuration);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.loadImage(tweet.getUser().get_image_url(), new SimpleImageLoadingListener(){

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);

                profileImage.setImageBitmap(loadedImage);
            }

        });

        return view;
    }
}
