package com.project4.datasource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;


public class Day {

  public enum WeatherEnum {
    CLOUDY, CLEAR, SHOWERS, RAIN;
    
    public static WeatherEnum fromString(String text) {
      if (text != null) {
        for (WeatherEnum b : WeatherEnum.values()) {
          if (text.equalsIgnoreCase(b.toString())) {
            return b;
          }
        }
      }
      return null;
    }
  }

  private WeatherEnum weather;
  private int windSpeed;
  // angle instead of NNW etc..
  private int windDirection;
  private TreeMap<Filter, Integer> counts;
  private int day;

  public WeatherEnum getWeather() {
    return weather;
  }

  public void setWeather(WeatherEnum weather) {
    this.weather = weather;
  }

  public void setWindSpeed(int windSpeed) {
    this.windSpeed = windSpeed;
  }

  public void setWindDirection(int windDirection) {
    this.windDirection = windDirection;
  }

  public int getWindSpeed() {
    return windSpeed;
  }

  public int getWindDirection() {
    return windDirection;
  }

  public TreeMap<Filter, Integer> getCounts() {
    return counts;
  }

  public Day(int day) {
    super();
    this.day = day;
  }
  
  public void setCount(Filter filter, Integer count) {
    if (counts == null) {
      counts = new TreeMap<Filter, Integer>();
    }
    counts.put(filter, count);
  }
  
  public String toString() {
    if (day==0) return "30 Apr";
    return day + " May";
  }

  public String toStringForBarchart() {
    if (day==0) return "30\nApr";
    if (day==1) return "1\nMay";
    return day + "";
  }

  public int getDay() {
    return day;
  }

  public float[] getSortedCounts() {
    TreeMap<Filter, Integer> c = getCounts();
    Iterator<Filter> i = c.keySet().iterator();
    float[] out = new float[c.size()];
    int j = 0;
    while (i.hasNext()) {
      out[j++] = c.get(i.next()).floatValue();
    }
    return out;
  }

  public ArrayList<Filter> getFilters() {
    ArrayList<Filter> out = new ArrayList<Filter>();
    Iterator<Filter> i = counts.keySet().iterator();
    while (i.hasNext()) {
      out.add(i.next());
    }
    return out;
  }

}
