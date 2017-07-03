package com.snijsure.rssreader.model;
/**
 * Created by subodhnijsure on 5/4/16.
 */

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import java.io.Serializable;
import java.util.List;
@Root(strict = false)
public class RssChannel implements Serializable {
    @ElementList(name = "item", required = true, inline = true)
    private List<RssFeedItem> itemList;

    @Element(name = "title", required = true )
    private String title;

    /*@Element(name = "link", required = true )*/
    @Path("link")
    @Text(required=false)
    private String link;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Element(name = "description", required = false )
    private String description;

    public List<RssFeedItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<RssFeedItem> itemList) {
        this.itemList = itemList;
    }

    public RssChannel(List<RssFeedItem> mFeedItems) {
        this.itemList = mFeedItems;
    }

    public RssChannel() {
    }

    public String getLink() {

        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
