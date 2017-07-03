package com.snijsure.rssreader;

import com.snijsure.rssreader.model.RssFeed;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

interface RssRetrofitAdapter {

   @GET
   Call<RssFeed> profilePicture(@Url String url);
}
