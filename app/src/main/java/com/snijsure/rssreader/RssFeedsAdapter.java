package com.snijsure.rssreader;

import android.content.Context;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RssFeedsAdapter extends RecyclerView.Adapter<RssFeedsAdapter.MyViewHolder> {

    private String TAG_ID = "id";
    private String TAG_TITLE = "title";
    private String TAG_LINK = "link";
    private Context ctx;
    private List<HashMap<String, String>> rssFeedsList = new ArrayList<HashMap<String, String>>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView sqlite_id, title, link;

        public MyViewHolder(View view) {
            super(view);
            sqlite_id = (TextView) view.findViewById(R.id.sqlite_id);
            title = (TextView) view.findViewById(R.id.title);
            link = (TextView) view.findViewById(R.id.link);
        }

        void bindData(HashMap<String, String> rssItem) {
            sqlite_id.setText(rssItem.get(TAG_ID));
            title.setText(rssItem.get(TAG_TITLE));
            link.setText(rssItem.get(TAG_LINK));
        }
    }

    public RssFeedsAdapter(Context context) {

        this.ctx = context;

    }

    public void update(List<HashMap<String, String>> items) {
        this.rssFeedsList.clear();
        this.rssFeedsList.addAll(items);
        notifyDataSetChanged();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.site_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HashMap<String, String> rssItem = rssFeedsList.get(position);
        if (rssItem != null) {
            holder.bindData(rssItem);
        }
    }

    @Override
    public int getItemCount() {
        return rssFeedsList.size();
    }
}
