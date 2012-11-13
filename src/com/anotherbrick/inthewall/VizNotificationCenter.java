package com.anotherbrick.inthewall;

import java.util.ArrayList;
import java.util.HashMap;

public class VizNotificationCenter {

  private static VizNotificationCenter instance = new VizNotificationCenter();

  public enum EventName {
    SCROLLING_TWEETS_UPDATED, BUTTON_TOUCHED, DATA_UPDATED, MAP_ZOOMED_OR_PANNED, USERS_UPDATED, TWEET_SELECTED, TWEET_DESELECTED, DAYS_UPDATED, BUTTON_PRESSED, TIME_SLIDER_UPDATED, FILTERS_UPDATED
  }

  private HashMap<EventName, ArrayList<EventSubscriber>> subscribers;

  public static VizNotificationCenter getInstance() {
    return instance;
  }

  private VizNotificationCenter() {
    subscribers = new HashMap<VizNotificationCenter.EventName, ArrayList<EventSubscriber>>();
    for (EventName eventName : EventName.values()) {
      subscribers.put(eventName, new ArrayList<EventSubscriber>());
    }
  }

  public void registerToEvent(EventName eventName, EventSubscriber eventSubscriber) {
    if (!subscribers.get(eventName).contains(eventSubscriber)) {
      subscribers.get(eventName).add(eventSubscriber);
    }
  }

  public void notifyEvent(EventName eventName) {
    notifyEvent(eventName, null);
  }

  public void notifyEvent(EventName eventName, Object data) {
    System.out.println("=== EVENT: " + eventName.toString() + "("
        + (data == null ? "" : Helper.limitStringLength(data.toString(), 50, true)) + ")");
    if (subscribers.get(eventName) == null) return;
    for (EventSubscriber es : subscribers.get(eventName)) {
      System.out.println("    --> " + es.getClass().getSimpleName().toString());
      es.eventReceived(eventName, data);
    }
  }
}
