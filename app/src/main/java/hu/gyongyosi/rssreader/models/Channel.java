package hu.gyongyosi.rssreader.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by gyongyosit on 2017.04.02..
 */

public class Channel extends RealmObject {

    @PrimaryKey
    private String url;
    private String title;
    private String description;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
