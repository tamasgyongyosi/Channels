package hu.gyongyosi.rssreader.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by gyongyosit on 2017.04.02..
 */

@Root(name="rss", strict=false)
public class Feed {

    @Element(name="title")
    @Path("channel")
    private String channelTitle;

    @Element(name="description", required = false)
    @Path("channel")
    private String description;

    @ElementList(name="item", inline=true)
    @Path("channel")
    private List<Article> articleList;

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
