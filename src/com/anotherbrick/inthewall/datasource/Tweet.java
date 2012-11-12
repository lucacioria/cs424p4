package com.anotherbrick.inthewall.datasource;

import com.anotherbrick.inthewall.Model;

public class Tweet {
  private double lat;
  private double lon;
  private String text;
  private int date;
  private int id;
  private int userId;

  public Tweet(double lat, double lon, int date, int id, int userId) {
    super();
    this.lat = lat;
    this.lon = lon;
    this.text = null;
    this.date = date;
    this.id = id;
    this.userId = userId;
  }

  public String getText() {
    if (text == null) {
        text = Model.getInstance().dataSourceSQL.getTweetText(id);
    }
    return text;
  }

  public double getLat() {
    return lat;
  }

  public double getLon() {
    return lon;
  }

  public int getDate() {
    return date;
  }

  public int getId() {
    return id;
  }

  public int getUserId() {
    return userId;
  }
}
