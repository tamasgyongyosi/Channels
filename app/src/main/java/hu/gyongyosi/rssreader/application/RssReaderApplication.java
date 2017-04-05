package hu.gyongyosi.rssreader.application;

import android.app.Application;

import hu.gyongyosi.rssreader.models.Channel;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by gyongyosit on 2017.04.02..
 */

public class RssReaderApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .initialData(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Channel channelHvg = realm.createObject(Channel.class, "http://hvg.hu/rss/");
                        channelHvg.setTitle("hvg.hu RSS");
                        channelHvg.setDescription("hvg.hu RSS");
                        Channel channelNasa = realm.createObject(Channel.class, "https://www.nasa.gov/rss/dyn/breaking_news.rss/");
                        channelNasa.setTitle("NASA Breaking News");
                        channelNasa.setDescription("A RSS news feed containing the latest NASA news articles and press releases.");
                    }})
                .build();
        Realm.setDefaultConfiguration(realmConfig);
    }

}
