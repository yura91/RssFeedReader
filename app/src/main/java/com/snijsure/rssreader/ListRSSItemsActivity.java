package com.snijsure.rssreader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.snijsure.rssreader.model.RSSDatabaseHandler;
import com.snijsure.rssreader.model.RssFeedItem;
import com.snijsure.rssreader.model.WebSite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListRSSItemsActivity extends Activity {

    private ProgressDialog pDialog;
    private RssItemAdapter mAdapter;
    private RecyclerView recyclerView;
    ImageButton btnAddSite;
    private ArrayList<RssFeedItem> finalOutputString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss_item_list);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view1);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent in = new Intent(getApplicationContext(), DisPlayWebPageActivity.class);
                in.putExtra("page_url", finalOutputString.get(position).getLink());
                startActivity(in);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        btnAddSite = (ImageButton) findViewById(R.id.btnAddSite);
        // hiding plus button
        btnAddSite.setVisibility(View.GONE);

        Intent i = getIntent();

        Integer site_id = Integer.parseInt(i.getStringExtra("id"));

        RSSDatabaseHandler rssDB = new RSSDatabaseHandler(getApplicationContext());


        WebSite site = rssDB.getSite(site_id);
        //String rss_link = site.getRSSLink();
        String itemList = site.getItemList();
        //page_url = site.getLink();
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<RssFeedItem>>() {
        }.getType();

        finalOutputString = gson.fromJson(itemList, type);
        mAdapter = new RssItemAdapter(finalOutputString);
        recyclerView.setAdapter(mAdapter);
    }

}
