package hu.gyongyosi.rssreader.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by gyongyosit on 2017.04.02..
 */

@Root(name = "item", strict = false)
public class Article {

    @Element(name = "title")
    private String title;

    @Element(name = "link")
    private String link;

    @Element(name = "description", required = false)
    //@Path("item")
    private String articleDescription;

    @Element(name = "pubDate")
    private String pubDate;

    @Element(name = "enclosure", required = false)
    private Enclosure enclosure;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getArticleDescription() {
        return articleDescription;
    }

    public void setArticleDescription(String articleDescription) {
        this.articleDescription = articleDescription;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public Enclosure getEnclosure() {
        return enclosure;
    }

    public void setEnclosure(Enclosure enclosure) {
        this.enclosure = enclosure;
    }
}
