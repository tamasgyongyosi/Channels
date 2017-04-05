package hu.gyongyosi.rssreader.realm;

import android.util.Log;

import hu.gyongyosi.rssreader.models.Channel;
import hu.gyongyosi.rssreader.models.Feed;
import io.realm.Realm;
import io.realm.Realm.Transaction;
import io.realm.Realm.Transaction.OnError;
import io.realm.Realm.Transaction.OnSuccess;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by gyongyosit on 2017.04.02..
 */

public class RealmHelper {
    private Realm realm;

    public RealmHelper(Realm realm){
        this.realm = realm;
    }

    public RealmResults<Channel> getAllChannel(String sort, Sort order){
        return realm.where(Channel.class).findAll().sort(sort, order);
    }

    public void addToRealm(final Feed feed, final String url, OnSuccess success, OnError error){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Channel channel = realm.createObject(Channel.class, url);
                channel.setDescription(feed.getDescription());
                channel.setTitle(feed.getChannelTitle());
            }
        }, success, error);
    }

    public void deleteFromRealm(final String url, OnSuccess success, OnError error){
        realm.executeTransactionAsync(new Transaction() {
            @Override
            public void execute(Realm realm) {
                Channel channel = realm.where(Channel.class).equalTo("url", url).findFirst();
                Log.i("Realm", "channel= " + channel.getUrl());
                channel.deleteFromRealm();
            }
        }, success, error);
    }
}
