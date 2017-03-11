package com.codepath.apps.TwitterClient;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by kia on 3/8/17.
 */

public class UserFeed extends AppCompatActivity implements View.OnClickListener, ConnectionCallbacks, OnConnectionFailedListener {

    TextView profileDetails, bio, followers, following, lat_long;
    EditText tweet_filed;
    ImageView profieImage;
    ListView List;
    Button edit_Bio,tweet, refresh, logout;

    Uri imageUri;

    FetchClient fetch;
    PutClient put;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private LocationListener mLocationListener;

    public static Boolean location_denied ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);

        profileDetails = (TextView) findViewById(R.id.Details);
        lat_long = (TextView) findViewById(R.id.lat_long);
        bio = (TextView) findViewById(R.id.Bio);
        followers = (TextView) findViewById(R.id.Followers);
        following = (TextView) findViewById(R.id.Following);
        tweet_filed = (EditText) findViewById(R.id.tweetField);
        profieImage = (ImageView) findViewById(R.id.profileImage);
        edit_Bio = (Button) findViewById(R.id.Edit);
        tweet = (Button) findViewById(R.id.Tweet);
        refresh = (Button) findViewById(R.id.refresh);
        logout = (Button) findViewById(R.id.logout);

        fetch = new FetchClient();
        put = new PutClient();

        edit_Bio.setOnClickListener(this);
        tweet.setOnClickListener(this);
        refresh.setOnClickListener(this);
        logout.setOnClickListener(this);
        lat_long.setOnClickListener(this);

        tweet.setBackgroundResource(R.drawable.tweet);
        logout.setBackgroundResource(R.drawable.logout);
        refresh.setBackgroundResource(R.drawable.refresh);

        tweet_filed.setInputType(InputType.TYPE_NULL);

        fetch.fetchUserInfo(profileDetails, bio, followers, following, profieImage, getBaseContext());
        fetchTweets();

        requestOnStart();
    }

    public void showDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter New Bio Info :)");
        
        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String bio_text = input.getText().toString();
                String padded = bio_text.replaceAll(" ", "%20");
                put.updateBio(padded);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.Edit: {

                showDialog();
                break;
            }

            case R.id.Tweet: {

                if (tweet_filed.getText().toString().isEmpty()) {

                    errorDialog("Invalid Input", "Text Cant Be Empty");
                    break;
                }

                String inputed_tweet = tweet_filed.getText().toString();
                String tweet_padded  = inputed_tweet.replaceAll(" ", "%20");
                put.makeTweet(tweet_padded);
                tweet_filed.setText("");
                break;
            }

            case R.id.refresh: {

                refreshFetch();
                break;
            }

            case R.id.logout: {

                RestApplication.getRestClient().clearAccessToken();
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
                break;
            }

            case R.id.lat_long: {

                requestLocationPermission();
                break;
            }
        }
    }

    private void refreshFetch() {

        fetch.fetchUserInfo(profileDetails, bio, followers, following, profieImage, getBaseContext());
        fetchTweets();
    }

    public void fetchTweets() {

        RestApplication.getRestClient().fetch_user_tweets(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                ArrayList<TweetModel> tweetsArray = TweetModel.fromJson(response);

                List = (ListView) findViewById(R.id.tweets_list);
                Adapter adapter = new Adapter(getBaseContext(), tweetsArray);
                List.setAdapter(adapter);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Log.d("FAILED", errorResponse.toString());
            }
        });
    }

    private void requestOnStart() {

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {

                    String formatted_lat_long = "<b>" + "Latitude: " + "</b>" + String.valueOf(mLastLocation.getLatitude())  + "<b>" + " Longtitude: "  + "</b>" + String.valueOf(mLastLocation.getLongitude());

                    lat_long.setText(Html.fromHtml(formatted_lat_long));

                } else {
                    Log.d("ERROR", "NO LOCATION AVAILABLE");
                }
            }
        };
    }

    private void requestLocationPermission() {

        if (ContextCompat.checkSelfPermission(UserFeed.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(UserFeed.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(UserFeed.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(UserFeed.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Log.d("PERMISSIOM", "GRANTED");
                location_denied = false;
                updateLocation();

            } else {

                Log.d("PERMISSIOM", "DENIED");

                String formatted_denie = "<b> Permission Was Denied (Click Me For Request)</b>";

                lat_long.setText(Html.fromHtml(formatted_denie));

            }
            return;
        }
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {

        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }

        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) {

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }

        updateLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

        Log.d("ERROR", "Connection Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.d("ERROR", connectionResult.getErrorMessage());
    }

    public void updateLocation() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {

            location_denied = false;

            String formatted_lat_long = "<b>" + "Latitude: " + "</b>" + String.valueOf(mLastLocation.getLatitude())  + "<b>" + " Longtitude: "  + "</b>" + String.valueOf(mLastLocation.getLongitude());

            lat_long.setText(Html.fromHtml(formatted_lat_long));

        } else {

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, mLocationListener);
        }
    }

    public void errorDialog(String title, String Message) {

        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(UserFeed.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(Message);
        alertDialog.setButton(android.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.show();
    }
}
