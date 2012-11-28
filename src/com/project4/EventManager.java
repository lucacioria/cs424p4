package com.project4;

import java.util.ArrayList;
import java.util.TreeMap;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.Model;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;
import com.project4.datasource.Day;
import com.project4.datasource.Filter;
import com.project4.datasource.Tweet;
import com.project4.datasource.User;

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
      log("from: " + minMax[0] + " to: " + minMax[1]);
      updateTweets();
      if (m.userLinesVisible) {
        updateUsers();
      }
    }
    if (eventName == EventName.FILTERS_UPDATED) {
      filters = (ArrayList<Filter>) data;
      updateData();
    }
  }

  private void updateData() {
    updateTweets();
    udpateDays();
    updateUsers();
  }

  private void udpateDays() {
    ArrayList<Day> days = m.dataSourceSQL.getDays(filters);
    m.notificationCenter.notifyEvent(EventName.DAYS_UPDATED, days);
  }

  private void updateUsers() {
    TreeMap<Filter, ArrayList<User>> users = m.dataSourceSQL.getUsers(filters, minMax, 2);
    m.notificationCenter.notifyEvent(EventName.USERS_UPDATED, users);
  }

  private void updateTweets() {
    TreeMap<Filter, ArrayList<Tweet>> tweets = m.dataSourceSQL.getTweets(filters, minMax);
    m.notificationCenter.notifyEvent(EventName.DATA_UPDATED, tweets);
  }

  public void initInterface() {
    // TODO this should trigger filters.. not fake one..
    filters = new ArrayList<Filter>();
    filters.add(new Filter(1, MyColorEnum.FILTER_1, "match(text) against('truck')"));
    filters.add(new Filter(2, MyColorEnum.FILTER_2, "match(text) against('sick')"));
    filters.add(new Filter(3, MyColorEnum.FILTER_3, "match(text) against('ball')"));
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
