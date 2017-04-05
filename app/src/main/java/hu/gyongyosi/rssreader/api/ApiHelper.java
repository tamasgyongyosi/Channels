package hu.gyongyosi.rssreader.api;

import hu.gyongyosi.rssreader.models.Feed;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by gyongyosit on 2017.04.02..
 */

public class ApiHelper {

    public Call<Feed> getRssFeed(final String normalizedUrl, Callback<Feed> callback){
        Call<Feed> call = null;
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(normalizedUrl)
                    .client(new OkHttpClient())
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .build();

            RssApi rssApi = retrofit.create(RssApi.class);
            call = rssApi.getFeeds("");
            call.enqueue(callback);
        } catch (Exception e){
            callback.onFailure(call, e);
        }
        return call;
    }
}
