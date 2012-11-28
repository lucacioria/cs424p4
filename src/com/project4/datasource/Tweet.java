package com.project4.datasource;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TreeMap;

import com.anotherbrick.inthewall.Model;

public class Tweet {
  private double lat;
  private double lon;
  private String text;
  private int date;
  private int id;
  private int userId;
  public TreeMap<String, Double> tf;
  public String[] words;


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

  public void setText(String text) {
    this.text = text;
  }
  
  public void generateWords(){
    if(words==null){
      words = text.split(" ");
    } 
  }

  public String getDateFormatted() {
    Calendar c = Calendar.getInstance();
    c.setTimeInMillis((long) getDate() * 1000l);
    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");
    return sdf.format(c.getTime());
  }
}
