package com.project4.map;

import java.util.ArrayList;

import processing.core.PVector;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;
import com.anotherbrick.inthewall.datasource.Tweet;
import com.anotherbrick.inthewall.datasource.User;

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
    m.notificationCenter.registerToEvent(EventName.USERS_UPDATED, this);
    m.notificationCenter.registerToEvent(EventName.MAP_ZOOMED_OR_PANNED, this);
  }

  @Override
  public boolean draw() {
    if (!isVisible()) return false;
    pushStyle();
    stroke(MyColorEnum.RED, 200f);
    drawLines();
    popStyle();
    return false;
  }

  private void drawLines() {
    for (ArrayList<PVector> line : lines) {
      for (int i = 1; i < line.size(); i++) {
        line(line.get(i - 1).x, line.get(i - 1).y, line.get(i).x, line.get(i).y);
      }
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public void eventReceived(EventName eventName, Object data) {
    if (eventName == EventName.USERS_UPDATED) {
      this.users = (ArrayList<User>) data;
      udpateLinks();
    }
    if (eventName == EventName.MAP_ZOOMED_OR_PANNED) {
      udpateLinks();
    }
  }

  private void udpateLinks() {
    lines = new ArrayList<ArrayList<PVector>>();
    for (User u : users) {
      ArrayList<PVector> line = new ArrayList<PVector>();
      float avgDist = 0;
      Tweet lastTweet = null;
      for (Tweet t : u.getTweets()) {
        PVector pos = map.getPositionByLatLon(t.getLat(), t.getLon());
        line.add(new PVector(pos.x, pos.y));
        if (lastTweet != null) {
          avgDist +=
              dist((float) t.getLat(), (float) t.getLon(), (float) lastTweet.getLat(),
                  (float) lastTweet.getLon());
        }
        lastTweet = t;
      }
      if (u.getTweets().size() > 1) {
        avgDist /= (u.getTweets().size() - 1);
        if (avgDist < 0.13 || avgDist > 0.17) continue; 
      }
      lines.add(line);
    }
  }

}
