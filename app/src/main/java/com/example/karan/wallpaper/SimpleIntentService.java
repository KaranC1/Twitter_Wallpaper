package com.example.karan.wallpaper;

import android.app.IntentService;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
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
    private static final String TWITTER_CONSUMER_KEY = "nfwunbymv27sogLQHkjAbx5k3";
    private static final String TWITTER_CONSUMER_SECRET = "jlw3XMNRqcprxYFxaZW4bKaE8blmelPFBPY2TFuImvDRGCaciC";

    final private  Configuration twitConf = new ConfigurationBuilder()
            .setOAuthConsumerKey(TWITTER_CONSUMER_KEY)
            .setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET)
            .setOAuthAccessToken("2928799379-ELDcLocHYVj9NwqK5VpG0Yr2QrX4vZK9A0mt3Zn")
            .setOAuthAccessTokenSecret("rwO6FQPL42YGeLgVg1NOZSlCFHs0SO07qv4eNLOX8o9XP")
            .build();
    public SimpleIntentService() {
        super("SimpleIntentService");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {

        t = new Timer();
        final String username = intent.getStringExtra("username");
        System.out.println("00000000000"+username);
        task = new TimerTask() {
            @Override
            public void run() {
                setImage(username);
            }
        };
        t.scheduleAtFixedRate(task, 3600000, 3600000); /*Update every 1 hour*/
    }

    public void setImage(String s) {
        try {
            int k = 0;
         Twitter twitter = new TwitterFactory(twitConf).getInstance();
            Paging paging = new Paging(1, 5);
            List<Status> status = twitter.getUserTimeline(s, paging);
            for (Status status1 : status) {
                MediaEntity[] media = status1.getMediaEntities();
                for (MediaEntity m : media) {
                    if (k < 5) {
                        URL file = new URL(m.getMediaURLHttps());
                        applyWallpaperfromFile(file);
                    }
                    k++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void applyWallpaperfromFile(URL file) throws IOException {
        Bitmap wallpaperImage = BitmapFactory.decodeStream(file.openConnection().getInputStream());
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);

        try {
            if((wallpaperManager.getDesiredMinimumWidth()>0)&&(wallpaperManager.getDesiredMinimumHeight()> 0)) {

                Bitmap blank = BitmapHelper.createNewBitmap(wallpaperManager.getDesiredMinimumWidth(), wallpaperManager.getDesiredMinimumHeight());
                Bitmap overlay = BitmapHelper.overlayIntoCentre(blank, wallpaperImage);
                wallpaperManager.setBitmap(overlay);
            }
            else
            {
                wallpaperManager.setBitmap(wallpaperImage);
            }
        } catch (IOException e) {
          
            e.printStackTrace();
        }


    }


}