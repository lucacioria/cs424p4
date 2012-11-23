package com.project4.map;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import processing.core.PVector;

import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;
import com.project4.datasource.Filter;
import com.project4.datasource.Tweet;

public class Scatter extends VizPanel implements TouchEnabled, EventSubscriber {

  private TreeMap<Filter, ArrayList<Tweet>> tweets = new TreeMap<Filter, ArrayList<Tweet>>();
  private Map map;
  private boolean touching;
  private Tweet selectedTweet;

  public Scatter(float x0, float y0, float width, float height, Map parent) {
    super(x0, y0, width, height, parent);
    this.map = parent;
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    if (down) {
      touching = true;
      setModal(true);
    } else {
      touching = false;
      setModal(false);
    }
    return true;
  }

  @Override
  public void setup() {
    m.notificationCenter.registerToEvent(EventName.DATA_UPDATED, this);
    setVisible(false);
  }

  @Override
  public boolean draw() {
    if (!isVisible()) return false;
    if (touching) {
      updateSelectedTweet();
    }
    pushStyle();
    noStroke();
    Iterator<Filter> i = tweets.keySet().iterator();
    while (i.hasNext()) {
      Filter key = i.next();
      fill(key.getColor(), 100f);
      for (Tweet t : tweets.get(key)) {
        drawTweet(t);
      }
    }
    popStyle();
    return false;
  }

  private void updateSelectedTweet() {
    for (Tweet t: tweets.firstEntry().getValue()) {
      PVector tweetPosition = map.getPositionByLatLon(t.getLat(), t.getLon());
      if (dist(m.touchX, m.touchY, tweetPosition.x, tweetPosition.y) < 5 &&
          t != selectedTweet) {
        m.notificationCenter.notifyEvent(EventName.TWEET_SELECTED, t);
        log("selected tweet: " + t.getText());
        selectedTweet = t;
        return;
      }
    }
    
  }

  private void drawTweet(Tweet t) {
    float radius = 10;
    PVector position = map.getPositionByLatLon(t.getLat(), t.getLon());
    ellipse(position.x, position.y, radius, radius);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void eventReceived(EventName eventName, Object data) {
    if (eventName == EventName.DATA_UPDATED) {
      this.tweets = (TreeMap<Filter, ArrayList<Tweet>>) data;
    }
  }



}
