package com.project4;

import java.util.ArrayList;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.Config.MyFontEnum;
import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;
import com.project4.FilterPlayGround.FilterPlayGround;
import com.project4.datasource.Day;
import com.project4.datasource.Filter;
import com.project4.datasource.Tweet;
import com.project4.datasource.User;
import com.project4.dayview.DayView;
import com.project4.map.Map;

public class Application extends VizPanel implements TouchEnabled, EventSubscriber {

  private Map map;
  private BlackBox blackBox1;
  private BlackBox blackBox2;
  private Scroller scroller;
  private DayView dayView;
  private FilterPlayGround playGround;

  public Application(float x0, float y0, float width, float height) {
    super(x0, y0, width, height);
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    return propagateTouch(x, y, down, touchType);
  }

  @Override
  public void setup() {
    textFont(MyFontEnum.OPENSANS_REGULAR);
    setupMap();
    setupBlackBoxes();
    setupScroller();
    setupDayView();
    setupFilterPlayGround();
    m.notificationCenter.registerToEvent(EventName.BUTTON_TOUCHED, this);
    if (c.initializeVisualization) initializeVisualization();
  }

  private void setupFilterPlayGround() {
    playGround = new FilterPlayGround(scroller.getX1(), 0, getWidth() - 929, getHeight(), this);
    playGround.setup();
    addTouchSubscriber(playGround);
  }

  private void setupDayView() {
    dayView = new DayView(0, map.getY1(), map.getWidth(), getHeight() - map.getHeight(), this);
    dayView.setup();
    addTouchSubscriber(dayView);
  }

  private void setupScroller() {
    scroller = new Scroller(map.getX1(), 0, 300, getHeight(), this);
    scroller.setup();
    addTouchSubscriber(scroller);
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
    // m.dataSourceSQL.doStuff();
    ArrayList<Tweet> tweets = m.dataSourceSQL.getTweets("match(text) against('truck')");
    m.notificationCenter.notifyEvent(EventName.DATA_UPDATED, tweets);

    ArrayList<Filter> filters = new ArrayList<Filter>();
    filters.add(new Filter(0, MyColorEnum.RED, "match(text) against('truck')"));
    filters.add(new Filter(1, MyColorEnum.LIGHT_GREEN, "match(text) against('sick')"));
    ArrayList<Day> days = m.dataSourceSQL.getDays(filters);
    m.notificationCenter.notifyEvent(EventName.DAYS_UPDATED, days);

    ArrayList<Tweet> scrollingTweets = new ArrayList<Tweet>();
    for (int i = 0; i < 50; i++) {
      scrollingTweets.add(tweets.get(i));
    }
    m.notificationCenter.notifyEvent(EventName.SCROLLING_TWEETS_UPDATED, scrollingTweets);

    ArrayList<User> users = m.dataSourceSQL.getUsers("id < 1000", 15);
    m.notificationCenter.notifyEvent(EventName.USERS_UPDATED, users);
  }

  @Override
  public boolean draw() {
    pushStyle();
    map.draw();
    blackBox1.draw();
    blackBox2.draw();
    scroller.draw();
    dayView.draw();
    playGround.draw();
    popStyle();
    return false;
  }

  @Override
  public void eventReceived(EventName eventName, Object data) {}

}
