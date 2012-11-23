package com.project4.map;

import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;

public class Details extends VizPanel implements TouchEnabled, EventSubscriber{

  public Details(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  @Override
  public void setup() {
    m.notificationCenter.registerToEvent(EventName.TWEET_SELECTED, this);
    m.notificationCenter.registerToEvent(EventName.TWEET_DESELECTED, this);
  }

  @Override
  public boolean draw() {
    return false;
  }

  @Override
  public void eventReceived(EventName eventName, Object data) {
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    return propagateTouch(x, y, down, touchType);
  }

}
