package com.example.karan.wallpaper;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;

import twitter4j.HttpClient;
import twitter4j.HttpResponse;
import twitter4j.MediaEntity;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import com.twitter.sdk.android.core.*;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.*;
import io.fabric.sdk.android.Fabric;
import twitter4j.TwitterFactory;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class MainActivity extends AppCompatActivity {

    private static final String TWITTER_CONSUMER_KEY = "nfwunbymv27sogLQHkjAbx5k3";
    private static final String TWITTER_CONSUMER_SECRET = "jlw3XMNRqcprxYFxaZW4bKaE8blmelPFBPY2TFuImvDRGCaciC";
    ImageView txt;
    final private  Configuration twitConf = new ConfigurationBuilder()
            .setOAuthConsumerKey(TWITTER_CONSUMER_KEY)
            .setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET)
            .setOAuthAccessToken("2928799379-ELDcLocHYVj9NwqK5VpG0Yr2QrX4vZK9A0mt3Zn")
            .setOAuthAccessTokenSecret("rwO6FQPL42YGeLgVg1NOZSlCFHs0SO07qv4eNLOX8o9XP")
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        txt = (ImageView) findViewById(R.id.imageView);

        getImage();
        Intent mServiceIntent = new Intent();
        mServiceIntent = new Intent(MainActivity.this, SimpleIntentService.class);
        MainActivity.this.startService(mServiceIntent);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImage();
            }
        });
    }

    @Override
    protected void onPause(){
super.onPause();
}

    public void getImage() {
        int k = 0;
        final Twitter twitter = new TwitterFactory(twitConf).getInstance();
        final Paging paging = new Paging(1, 5);
        try {
            List<Status> status = twitter.getUserTimeline("StationCDRKelly", paging);
            for (Status status1 : status) {

                MediaEntity[] media = status1.getMediaEntities();
                for (MediaEntity m : media) {
                    if (k < 1) {
                        System.out.println(m.getMediaURL());
                        Picasso.with(MainActivity.this).load(m.getMediaURLHttps()).into(txt);
                        txt.setScaleType(ImageView.ScaleType.FIT_XY);
                    }
                    k++;
                }
            }
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setImage() {
        try {
            int k = 0;
            final  Twitter twitter = new TwitterFactory(twitConf).getInstance();
            final  Paging paging = new Paging(1, 5);

            List<Status> status = twitter.getUserTimeline("StationCDRKelly", paging);
            for (Status status1 : status) {
                MediaEntity[] media = status1.getMediaEntities();
                for (MediaEntity m : media) {
                    if (k < 1) {
                        WallpaperManager myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                        URL file = new URL(m.getMediaURLHttps());

                        DisplayMetrics metrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(metrics);
                        int height = metrics.heightPixels;
                        int width = metrics.widthPixels;
                        //BitmapFactory.Options options = new BitmapFactory.Options();
                        // options.inScaled = true;
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;

                        Bitmap bitmap = BitmapFactory.decodeStream(file.openConnection().getInputStream());
                        Bitmap resize = Bitmap.createScaledBitmap(bitmap, width,height, true);
                   //     myWallpaperManager.suggestDesiredDimensions(width, height);
                        myWallpaperManager.setBitmap(resize);
                        Toast.makeText(MainActivity.this, "Updated Wallpaper",Toast.LENGTH_SHORT).show();
                    }
                    k++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

