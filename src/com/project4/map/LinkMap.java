package com.project4.map;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import processing.core.PVector;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;
import com.project4.datasource.Filter;
import com.project4.datasource.Tweet;
import com.project4.datasource.User;

public class LinkMap extends VizPanel implements TouchEnabled, EventSubscriber {

  private TreeMap<Filter, ArrayList<User>> users = new TreeMap<Filter, ArrayList<User>>();
  private Map map;
  private TreeMap<Filter, ArrayList<ArrayList<PVector>>> lines;

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
    m.notificationCenter.registerToEvent(EventName.BUTTON_TOUCHED, this);
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
    if (lines == null) return;
    Iterator<Filter> i = lines.keySet().iterator();
    while (i.hasNext()) {
      Filter key = i.next();
      stroke(key.getColor());
      for (ArrayList<PVector> line : lines.get(key)) {
      for (int j = 1; j < line.size(); j++) {
        line(line.get(j - 1).x, line.get(j - 1).y, line.get(j).x, line.get(j).y);
      }
    }
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public void eventReceived(EventName eventName, Object data) {
    if (eventName == EventName.USERS_UPDATED) {
      this.users = (TreeMap<Filter, ArrayList<User>>) data;
      udpateLinks();
    }
    if (eventName == EventName.MAP_ZOOMED_OR_PANNED) {
      udpateLinks();
    }
    if (eventName == EventName.BUTTON_TOUCHED && data.toString().equals("linesButton")) {
      toggleVisible();
    }
  }

  private void udpateLinks() {
    lines = new TreeMap<Filter, ArrayList<ArrayList<PVector>>>();
    Iterator<Filter> i = users.keySet().iterator();
    while (i.hasNext()) {
      ArrayList<ArrayList<PVector>> _lines = new ArrayList<ArrayList<PVector>>();
      Filter key = i.next();
      for (User u : users.get(key)) {
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
        _lines.add(line);
      }
      lines.put(key, _lines);
    }
  }

}
