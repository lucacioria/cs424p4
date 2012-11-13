package com.project4;

import java.util.ArrayList;
import java.util.TreeMap;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.Model;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;
import com.project4.datasource.Filter;
import com.project4.datasource.Tweet;

public class EventManager extends VizPanel implements EventSubscriber {

  private int[] minMax = {(int) Model.MIN_TIME, (int) Model.MAX_TIME};
  private ArrayList<Filter> filters;

  public EventManager(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void eventReceived(EventName eventName, Object data) {
    if (eventName == EventName.TIME_SLIDER_UPDATED) {
      minMax = (int[]) data;
      log("from: " + minMax[0]+ " to: " + minMax[1]);
      updateData();
    }
    if (eventName == EventName.FILTERS_UPDATED) {
      filters = (ArrayList<Filter>) data;
      updateData();
    }
  }

  private void updateData() {
    TreeMap<Filter, ArrayList<Tweet>> tweets = m.dataSourceSQL.getTweets(filters, minMax);
    m.notificationCenter.notifyEvent(EventName.DATA_UPDATED, tweets);
  }

  public void initInterface() {
    // TODO this should trigger filters.. not fake one..
    filters = new ArrayList<Filter>();
    filters.add(new Filter(0, MyColorEnum.RED, "match(text) against('truck')"));
    filters.add(new Filter(1, MyColorEnum.LIGHT_GREEN, "match(text) against('sick')"));
  }

  @Override
  public void setup() {
    m.notificationCenter.registerToEvent(EventName.TIME_SLIDER_UPDATED, this);
    m.notificationCenter.registerToEvent(EventName.FILTERS_UPDATED, this);
  }

  @Override
  public boolean draw() {
    return false;
  }

}
