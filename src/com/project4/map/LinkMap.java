package com.project4.map;

import java.util.ArrayList;
import java.util.Arrays;

import processing.core.PVector;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.datasource.Tweet;
import com.anotherbrick.inthewall.datasource.User;
import com.anotherbrick.inthewall.VizPanel;

public class LinkMap extends VizPanel implements TouchEnabled, EventSubscriber {

  private ArrayList<User> users = new ArrayList<User>();
  private Map map;
  private ArrayList<ArrayList<PVector>> lines;

  public LinkMap(float x0, float y0, float width, float height, Map parent) {
    super(x0, y0, width, height, parent);
    this.map = parent;
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    return propagateTouch(x, y, down, touchType);
  }

  @Override
  public void setup() {
    m.notificationCenter.registerToEvent(EventName.DATA_UPDATED, this);
    m.notificationCenter.registerToEvent(EventName.MAP_ZOOMED_OR_PANNED, this);
  }

  @Override
  public boolean draw() {
    if (!isVisible()) return false;
    pushStyle();
    popStyle();
    return false;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void eventReceived(EventName eventName, Object data) {
    if (eventName == EventName.DATA_UPDATED) {
      this.users = (ArrayList<User>) data;
      udpateLinks();
    }
    if (eventName == EventName.MAP_ZOOMED_OR_PANNED) {
      udpateLinks();
    }
  }

  private void udpateLinks() {
    lines = new ArrayList<ArrayList<PVector>>();
    for (User u:users) {
      for (Tweet t: u.getTweets()) {
        
      }
    }
    
  }

}
