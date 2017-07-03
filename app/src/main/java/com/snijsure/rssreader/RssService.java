package com.snijsure.rssreader;
/**
 * Created by subodhnijsure on 5/4/16.
 */


import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.os.StrictMode;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.snijsure.rssreader.model.RSSDatabaseHandler;
import com.snijsure.rssreader.model.RssFeed;
import com.snijsure.rssreader.model.RssFeedItem;
import com.snijsure.rssreader.model.WebSite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.select.Elements;

import de.greenrobot.event.EventBus;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class RssService extends Service {

    private EventBus eventBus = EventBus.getDefault();
    public static final String URL = "url";
    private final static String TAG = "RssService";
    RSSDatabaseHandler rssDb;
    List<WebSite> siteList;
    Intent mIntent;

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        Log.d(TAG, "Service started");
        mIntent = intent;
        rssDb = new RSSDatabaseHandler(
                getApplicationContext());
        siteList = rssDb.getAllSites();
        ArrayList<String> urls = mIntent.getStringArrayListExtra(URL);
        updateRssItems(urls, 0);

        return Service.START_STICKY;
    }

    void updateRssItems(final ArrayList<String> urls, final int i) {

        if(urls != null) {
            if (urls.size() != 0) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

                SimpleXmlConverterFactory conv = SimpleXmlConverterFactory.createNonStrict();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://habrahabr.ru/")
                        .client(client)
                        .addConverterFactory(conv)
                        .build();
                RssRetrofitAdapter retrofitService = retrofit.create(RssRetrofitAdapter.class);

                final String urlsLink = urls.get(i);
                final String url = getRSSLinkFromURL(urls.get(i));
                Call<RssFeed> call = retrofitService.profilePicture(url);
                call.enqueue(new Callback<RssFeed>() {
                    @Override
                    public void onResponse(Call<RssFeed> call, Response<RssFeed> response) {

                        RssFeed feed = response.body();
                        List<RssFeedItem> mItems = feed.getChannel().getItemList();


                        if (mItems != null) {
                            List<RssFeedItem> cachedList = new ArrayList<>(mItems);
                            Log.d(TAG, "Initialized  cached list");


                            Gson gson = new Gson();
                            String inputString = gson.toJson(cachedList);
                            /*WebSite site = new WebSite(feed.getChannel().getTitle(), feed.getChannel().getLink(), url,
                                    feed.getChannel().getDescription(), inputString);*/
                            WebSite site = new WebSite(feed.getChannel().getTitle(), urlsLink, url,
                                    feed.getChannel().getDescription(), inputString);
                            rssDb.addSite(site);

                            int k = i + 1;

                            if (k < urls.size()) {
                                updateRssItems(urls, k);
                            } else {
                                sendCachedList();
                            }
                        }
                    }


                    @Override
                    public void onFailure(Call<RssFeed> call, Throwable t) {
                        Log.d(TAG, "OnFailure Error is " + t);
                    }
                });
            }
        }
        }

    void sendCachedList( ) {
            Log.d(TAG, "Sending cachedList");

            eventBus.post(true);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent)
    {
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        mIntent = intent;
        Log.d(TAG, "Bound to service");

        return null;
    }


    @Override
    public void onDestroy() {
        Log.d(TAG, "Service is destroyed");
    }


    public String getRSSLinkFromURL(String url) {
        // RSS url
        String rss_url = null;

        try {

            org.jsoup.nodes.Document doc = Jsoup.connect(url).ignoreContentType(true).get();

           /* org.jsoup.select.Elements links = doc
                    .select("link[type=application/rss+xml]");*/
            Elements links = doc.select("head > link" +
                    "[rel=alternate]" +
                    "[type~=(application/(rss|(x(\\.|\\-))?atom|rdf)\\+|text/)xml]" +
                    "[href~=.+]");
            Log.d("No of RSS links found", " " + links.size());


            if (links.size() > 0) {
                //rss_url = links.get(0).attr("href").toString();
                rss_url = links.get(0).attr("abs:href").toString();
            } else {

                org.jsoup.select.Elements links1 = doc
                        .select("link[type=application/atom+xml]");
                if (links1.size() > 0) {
                    rss_url = links1.get(0).attr("abs:href").toString();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return rss_url;
    }

}
