package com.project4;

import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizButton;
import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;
import com.project4.FilterPlayGround.FilterPlayGround;
import com.project4.dayview.DayView;
import com.project4.map.Map;

public class Application extends VizPanel implements TouchEnabled, EventSubscriber {

  public VizButton standardButton;
  private Map map;
  private BlackBox blackBox1;
  private BlackBox blackBox2;
  private Scroller scroller;
  private DayView dayView;
  private FilterPlayGround playGround;
  private EventManager eventManager;

  public Application(float x0, float y0, float width, float height) {
    super(x0, y0, width, height);
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    return propagateTouch(x, y, down, touchType);
  }

  @Override
  public void setup() {
    setButtonStyles();
//    textFont(MyFontEnum.OPENSANS_REGULAR);
    setupMap();
    setupBlackBoxes();
    setupScroller();
    setupDayView();
    setupFilterPlayGround();
    m.notificationCenter.registerToEvent(EventName.BUTTON_TOUCHED, this);
    m.notificationCenter.registerToEvent(EventName.TIME_SLIDER_UPDATED, this);
    setupEventManager();
    if (c.initializeVisualization) initializeVisualization();
  }

  private void setButtonStyles() {
    standardButton = new VizButton(0, 0, 0, 0, this);
    standardButton.setStyle(MyColorEnum.LIGHT_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY, 155f,
        155f, 10);
    standardButton.setStylePressed(MyColorEnum.MEDIUM_GRAY, MyColorEnum.WHITE,
        MyColorEnum.DARK_GRAY, 155f, 10);
    standardButton.setStyleSelected(MyColorEnum.DARK_GRAY, MyColorEnum.WHITE,
      MyColorEnum.WHITE, 155f, 155f, 10);
    standardButton.setStyleDisabled(MyColorEnum.MEDIUM_GRAY, MyColorEnum.LIGHT_GRAY,
      MyColorEnum.DARK_GRAY, 55f, 55f, 10);

  }
  
  private void setupEventManager() {
    eventManager = new EventManager(0, 0, 0, 0, this);
    eventManager.setup();
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
    map = new Map(0, 0, 600, 305, this);
    map.setup();
    addTouchSubscriber(map);
  }

  private void initializeVisualization() {
    eventManager.initInterface();
    // m.dataSourceSQL.doStuff();

    // ArrayList<Tweet> scrollingTweets = new ArrayList<Tweet>();
    // for (int i = 0; i < 50; i++) {
    // scrollingTweets.add(tweets.get(i));
    // }
    // m.notificationCenter.notifyEvent(EventName.SCROLLING_TWEETS_UPDATED, scrollingTweets);

    // ArrayList<User> users = m.dataSourceSQL.getUsers("id < 1000", 15);
    // m.notificationCenter.notifyEvent(EventName.USERS_UPDATED, users);
    //
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
