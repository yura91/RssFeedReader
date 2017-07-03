package com.snijsure.rssreader;
/**
 * Created by subodhnijsure on 5/4/16.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.snijsure.rssreader.model.RSSDatabaseHandler;
import com.snijsure.rssreader.model.RssFeedItem;
import com.snijsure.rssreader.model.WebSite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {
    RecyclerView rss_feed_list;
    private ArrayList<HashMap<String, String>> rssFeedList;
    RssFeedsAdapter adapter;
    private ImageButton btnAddSite;
    private EventBus eventBus = EventBus.getDefault();
    private String[] sqliteIds;
    private String TAG_ID = "id";
    private String TAG_TITLE = "title";
    private String TAG_LINK = "link";
    private RSSDatabaseHandler rssDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rssFeedList = new ArrayList<>();
        adapter = new RssFeedsAdapter(this);
        btnAddSite = (ImageButton) findViewById(R.id.btnAddSite);
        rssDb = new RSSDatabaseHandler(
                getApplicationContext());
        btnAddSite.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddNewSiteActivity.class);

                startActivityForResult(i, 100);
            }
        });

        rss_feed_list = (RecyclerView) findViewById(R.id.recycler_view);
        rss_feed_list.setHasFixedSize(true);
        rss_feed_list.addItemDecoration(new MarginDecoration(this));
        LinearLayoutManager layoutMgr = new LinearLayoutManager(this);
        rss_feed_list.setLayoutManager(layoutMgr);
        rss_feed_list.setAdapter(adapter);
        rss_feed_list.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), rss_feed_list, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String sqlite_id = ((TextView) view.findViewById(R.id.sqlite_id)).getText().toString();
                Intent in = new Intent(getApplicationContext(), ListRSSItemsActivity.class);
                in.putExtra(TAG_ID, sqlite_id);
                startActivity(in);
            }

            @Override
            public void onLongClick(final View view, final int position) {

                String tittle = ((TextView) view.findViewById(R.id.title)).getText().toString();

                final CharSequence[] items = {"Delete"};

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Delete" + " " + tittle);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        String sqlite_id = ((TextView) view.findViewById(R.id.sqlite_id)).getText().toString();
                        rssFeedList.remove(position);
                        WebSite site = rssDb.getSite(Integer.parseInt(sqlite_id));
                        rssDb.deleteSite(site);
                        adapter.update(rssFeedList);
                    }
                });
                builder.show();
            }
        }));


        rssFeedList.clear();
        adapter.update(rssFeedList);

        ArrayList<String> urls = new ArrayList<>();
        List<WebSite> siteList = rssDb.getAllSites();

        if (isOnline() && siteList.size() == 0) {

            urls.add("https://habrahabr.ru/posts/top/");
            urls.add("https://habrahabr.ru/posts/top/weekly/");
            urls.add("https://habrahabr.ru/posts/top/monthly/");
            urls.add("https://habrahabr.ru/posts/top/alltime/");

            Intent intent = new Intent(getApplicationContext(), RssService.class);
            intent.putExtra(RssService.URL, urls);
            getApplicationContext().startService(intent);
        } else {

            if (isOnline()) {

                for (int i = 0; i < siteList.size(); i++) {
                    urls.add(siteList.get(i).getLink());
                }

                Intent intent = new Intent(getApplicationContext(), RssService.class);
                intent.putExtra(RssService.URL, urls);
                getApplicationContext().startService(intent);

            } else {
                sqliteIds = new String[siteList.size()];
                for (int i = 0; i < siteList.size(); i++) {

                    WebSite s = siteList.get(i);


                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put(TAG_ID, s.getId().toString());
                    map.put(TAG_TITLE, s.getTitle());
                    map.put(TAG_LINK, s.getLink());

                    rssFeedList.add(map);

                    sqliteIds[i] = s.getId().toString();
                }

                adapter.update(rssFeedList);
                stopService(new Intent(this, RssService.class));
            }
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        eventBus.register(this);
        /*eventBus.register(this);
        rssFeedList.clear();
        adapter.update(rssFeedList);
        RSSDatabaseHandler rssDb = new RSSDatabaseHandler(
                getApplicationContext());

        ArrayList<String> urls = new ArrayList<>();
        List<WebSite> siteList = rssDb.getAllSites();

        if (isOnline() && siteList.size() == 0) {

            urls.add("https://habrahabr.ru/posts/top/");
            urls.add("https://habrahabr.ru/posts/top/weekly/");
            urls.add("https://habrahabr.ru/posts/top/monthly/");
            urls.add("https://habrahabr.ru/posts/top/alltime/");

            Intent intent = new Intent(getApplicationContext(), RssService.class);
            intent.putExtra(RssService.URL, urls);
            getApplicationContext().startService(intent);
        } else {

            if (isOnline()) {

                for (int i = 0; i < siteList.size(); i++) {
                    urls.add(siteList.get(i).getLink());
                }

                Intent intent = new Intent(getApplicationContext(), RssService.class);
                intent.putExtra(RssService.URL, urls);
                getApplicationContext().startService(intent);
            } else {
                sqliteIds = new String[siteList.size()];
                for (int i = 0; i < siteList.size(); i++) {

                    WebSite s = siteList.get(i);


                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put(TAG_ID, s.getId().toString());
                    map.put(TAG_TITLE, s.getTitle());
                    map.put(TAG_LINK, s.getLink());

                    rssFeedList.add(map);

                    sqliteIds[i] = s.getId().toString();
                }

                adapter.update(rssFeedList);
                stopService(new Intent(this, RssService.class));
            }
        }*/

    }

    @Override
    protected void onStop() {
        super.onStop();
        eventBus.unregister(this);
    }

    public class MarginDecoration extends RecyclerView.ItemDecoration {
        private int margin;

        public MarginDecoration(Context context) {
            margin = context.getResources().getDimensionPixelSize(R.dimen.item_margin);
        }

        @Override
        public void getItemOffsets(
                Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.set(margin, margin, margin, margin);
        }
    }

    public void onEvent(Boolean bool) {

        if (bool) {
            rssFeedList.clear();
            RSSDatabaseHandler rssDb = new RSSDatabaseHandler(
                    getApplicationContext());

            List<WebSite> siteList = rssDb.getAllSites();
            sqliteIds = new String[siteList.size()];
            for (int i = 0; i < siteList.size(); i++) {

                WebSite s = siteList.get(i);


                HashMap<String, String> map = new HashMap<String, String>();

                map.put(TAG_ID, s.getId().toString());
                map.put(TAG_TITLE, s.getTitle());
                map.put(TAG_LINK, s.getLink());

                rssFeedList.add(map);

                sqliteIds[i] = s.getId().toString();
            }

            adapter.update(rssFeedList);
            stopService(new Intent(this, RssService.class));


        }
    }

    public boolean isOnline() {
        String cs = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager cm = (ConnectivityManager)
                this.getSystemService(cs);
        if (cm.getActiveNetworkInfo() == null) {
            return false;
        }
        return cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }


}
