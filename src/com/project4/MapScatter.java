package com.project4;

import java.util.ArrayList;

import processing.core.PApplet;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;

public class MapScatter extends VizPanel implements TouchEnabled, EventSubscriber {

  private ArrayList<Tweet> tweets;
  private Map map;

  public MapScatter(float x0, float y0, float width, float height, Map parent) {
    super(x0, y0, width, height, parent);
    this.map = parent;
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    return false;
  }

  @Override
  public void setup() {
    m.notificationCenter.registerToEvent(EventName.DATA_UPDATED, this);
  }

  @Override
  public boolean draw() {
    if (!isVisible()) return false;
    pushStyle();
    for (Tweet t : tweets) {
      drawTweet(t);
    }
    popStyle();
    return false;
  }

  private void drawTweet(Tweet t) {
    pushStyle();
    float radius = 10;
    float x = PApplet.map((float) t.getLon(), map.getMinLon(), map.getMaxLon(), 0, getWidth());
    float y = PApplet.map((float) t.getLat(), map.getMinLat(), map.getMaxLat(), 0, getHeight());
    fill(MyColorEnum.LIGHT_ORANGE);
    noStroke();
    ellipse(x, y, radius, radius);
    popStyle();
  }

  @Override
  public void eventReceived(EventName eventName, Object data) {
    if (eventName == EventName.DATA_UPDATED) {
      this.tweets = (ArrayList<Tweet>) data;
    }
  }



}
