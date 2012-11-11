package com.project4;

import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;

public class Application extends VizPanel implements TouchEnabled, EventSubscriber {

  private Map map;
  private BlackBox blackBox1;
  private BlackBox blackBox2;

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
    setupBlackBoxes();
    m.notificationCenter.registerToEvent(EventName.BUTTON_TOUCHED, this);
    if (c.initializeVisualization) initializeVisualization();
  }

  private void setupBlackBoxes() {
    blackBox1 = new BlackBox(map.getX1(), 0, getWidth(), getHeight(), this);
    blackBox2 = new BlackBox(0, map.getY1(), map.getWidth(), 200, this);
  }

  private void setupMap() {
    map = new Map(0, 0, 629, 320, this);
    map.setup();
    addTouchSubscriber(map);
  }

  private void initializeVisualization() {
    //ArrayList<Tweet> tweets = m.dataSourceSQL.getTweets("match(text) against('truck')");
    //m.notificationCenter.notifyEvent(EventName.DATA_UPDATED, tweets);
  }

  @Override
  public boolean draw() {
    pushStyle();
    map.draw();
    blackBox1.draw();
    blackBox2.draw();
    popStyle();
    return false;
  }

  @Override
  public void eventReceived(EventName eventName, Object data) {}

}
