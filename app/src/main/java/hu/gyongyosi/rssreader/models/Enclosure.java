package hu.gyongyosi.rssreader.models;

/**
 * Created by gyongyosit on 2017.04.03..
 */

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "enclosure", strict = false)
public class Enclosure {

    @Attribute(name = "url")
    private String url;

    @Attribute(name = "type", required = false)
    private String type;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
