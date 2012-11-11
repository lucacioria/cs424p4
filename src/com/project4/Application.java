package com.project4;

import java.util.ArrayList;

import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;

public class Application extends VizPanel implements TouchEnabled, EventSubscriber {

  private Map map;

  public Application(float x0, float y0, float width, float height) {
    super(x0, y0, width, height);
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    return propagateTouch(x, y, down, touchType);
  }

  @Override
  public void setup() {
    setupMap();
    m.notificationCenter.registerToEvent(EventName.BUTTON_TOUCHED, this);
    if (c.initializeVisualization) initializeVisualization();
  }

  private void setupMap() {
    map = new Map(0, 0, 629, 320, this);
    map.setup();
    addTouchSubscriber(map);
  }

  private void initializeVisualization() {
    ArrayList<Tweet> tweets = new ArrayList<Tweet>();
    tweets.add(new Tweet(42.22717, 93.33772, 1305725160, 1, 3));
    m.notificationCenter.notifyEvent(EventName.DATA_UPDATED, tweets);
  }

  @Override
  public boolean draw() {
    pushStyle();
    map.draw();
    popStyle();
    return false;
  }

  @Override
  public void eventReceived(EventName eventName, Object data) {}

}
