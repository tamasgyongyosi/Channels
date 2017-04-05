package hu.gyongyosi.rssreader.api;

import hu.gyongyosi.rssreader.models.Feed;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by gyongyosit on 2017.04.02..
 */

public interface RssApi {
    @GET("{url}")
    Call<Feed> getFeeds(@Path("url") String url);
}
