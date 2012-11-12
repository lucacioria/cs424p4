package com.project4.map;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;
import com.project4.datasource.Tweet;

public class Scatter extends VizPanel implements TouchEnabled, EventSubscriber {

  private ArrayList<Tweet> tweets = new ArrayList<Tweet>();
  private Map map;

  public Scatter(float x0, float y0, float width, float height, Map parent) {
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
    m.notificationCenter.registerToEvent(EventName.BUTTON_TOUCHED, this);
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
    PVector position = map.getPositionByLatLon(t.getLat(), t.getLon());
    ellipse(position.x, position.y, radius, radius);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void eventReceived(EventName eventName, Object data) {
    if (eventName == EventName.DATA_UPDATED) {
      this.tweets = (ArrayList<Tweet>) data;
    }
    if (eventName == EventName.BUTTON_TOUCHED && data.toString().equals("scatterButton")) {
      toggleVisible();
    }
  }



}
