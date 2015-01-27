package me.suan.example.ui.mvc.Model;


import me.suan.common.ui.mvc.Model.BaseModel;

import org.simpleframework.xml.Element;

/**
 * Created by suanmiao on 14-12-3.
 * Object to hold all the data in a article
 */
@Element(name = "item")
public class ArticleModel extends BaseModel{

    @Element(name = "title")
    public String title;

    @Element(name = "link")
    public String link;

    @Element(name = "author")
    public String author;

    @Element(name = "description")
    public String description;

    @Element(name = "pubDate")
    public String pubDate;

    @Element(name = "guid", required = false)
    public String guid;

    @Element(name = "category", required = false)
    public String category;

    @Override
    public int getViewType() {
        return 0;
    }
}
