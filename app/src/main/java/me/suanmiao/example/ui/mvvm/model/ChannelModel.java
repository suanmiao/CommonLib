package me.suanmiao.example.ui.mvvm.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by suanmiao on 15/1/19.
 */
@Root(strict = false)
public class ChannelModel {

  @Path("channel")
  @Element(name = "title")
  public String title;

  @Path("channel")
  @Element(name = "link")
  public String link;

  @Path("channel")
  @Element(name = "description", required = false)
  public String description;

  @Path("channel")
  @Element(name = "pubDate")
  public String pubDate;

  @Path("channel")
  @Element(name = "lastBuildDate")
  public String lastBuildDate;

  @Path("channel")
  @Element(name = "docs",required = false)
  public String docs;

  @Path("channel")
  @Element(name = "image",required = false)
  public String image;

  @Path("channel")
  @ElementList(entry = "item", inline = true)
  public List<ArticleModel> itemList;
}
