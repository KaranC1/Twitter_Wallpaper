package com.example.karan.wallpaper;

import android.app.IntentService;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import twitter4j.MediaEntity;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by Karan on 11/13/15.
 */
public class SimpleIntentService extends IntentService {
    Timer t;
    TimerTask task;
    public SimpleIntentService() {
        super("SimpleIntentService");
    }



    @Override
    protected void onHandleIntent(Intent intent) {

        t= new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                setImage();
            }
        };
        t.scheduleAtFixedRate(task, 0, 100*30*60);
    }

    public void setImage() {
        final String TWITTER_CONSUMER_KEY = "nfwunbymv27sogLQHkjAbx5k3";
        final String TWITTER_CONSUMER_SECRET = "jlw3XMNRqcprxYFxaZW4bKaE8blmelPFBPY2TFuImvDRGCaciC";
        final  Configuration twitConf = new ConfigurationBuilder()
                .setOAuthConsumerKey(TWITTER_CONSUMER_KEY)
                .setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET)
                .setOAuthAccessToken("2928799379-ELDcLocHYVj9NwqK5VpG0Yr2QrX4vZK9A0mt3Zn")
                .setOAuthAccessTokenSecret("rwO6FQPL42YGeLgVg1NOZSlCFHs0SO07qv4eNLOX8o9XP")
                .build();
        try {
            int k = 0;
            final Twitter twitter = new TwitterFactory(twitConf).getInstance();
            final Paging paging = new Paging(1, 5);

            List<Status> status = twitter.getUserTimeline("StationCDRKelly", paging);
            for (Status status1 : status) {
                MediaEntity[] media = status1.getMediaEntities();
                for (MediaEntity m : media) {
                    if (k < 1) {
                        WallpaperManager myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                        URL file = new URL(m.getMediaURLHttps());
                        DisplayMetrics metrics = new DisplayMetrics();
                        WindowManager windowManager = (WindowManager)SimpleIntentService.this
                                .getSystemService(Context.WINDOW_SERVICE);
                        windowManager.getDefaultDisplay().getMetrics(metrics);
                        int width = metrics.widthPixels;
                        int height= metrics.heightPixels;

                        Bitmap bitmap = BitmapFactory.decodeStream(file.openConnection().getInputStream());
                        Bitmap resize = Bitmap.createScaledBitmap(bitmap,width, height,false);

                        myWallpaperManager.suggestDesiredDimensions(width, height);
                        myWallpaperManager.setBitmap(resize);
                     }
                    k++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}