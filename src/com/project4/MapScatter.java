package com.project4;

import java.util.ArrayList;

import processing.core.PApplet;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;

public class MapScatter extends VizPanel implements TouchEnabled, EventSubscriber {

  private ArrayList<Tweet> tweets = new ArrayList<Tweet>();
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
    fill(MyColorEnum.LIGHT_ORANGE, 100f);
    noStroke();
    for (Tweet t : tweets) {
      drawTweet(t);
    }
    popStyle();
    return false;
  }

  private void drawTweet(Tweet t) {
    float radius = 10;
    float x = PApplet.map((float) t.getLon(),(float) map.getMinLon(),(float) map.getMaxLon(), 0, getWidth());
    float y = PApplet.map((float) t.getLat(),(float) map.getMinLat(),(float) map.getMaxLat(), 0, getHeight());
    ellipse(x, y, radius, radius);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void eventReceived(EventName eventName, Object data) {
    if (eventName == EventName.DATA_UPDATED) {
      this.tweets = (ArrayList<Tweet>) data;
    }
  }



}
