package com.project4;

import java.util.ArrayList;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;
import com.project4.datasource.Tweet;

public class Scroller extends VizPanel implements TouchEnabled, EventSubscriber {

  public Scroller(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  private ArrayList<Tweet> tweets;
  private int startMillis;
  private ArrayList<ScrollerTweet> scrollerTweets = new ArrayList<ScrollerTweet>();
  private int lastMillis;
  private float scrollHeight;
  private boolean scrolling;
  private float scrolledAmount;
  public Tweet selectedTweet;
  private boolean paused;

  @SuppressWarnings("unchecked")
  @Override
  public void eventReceived(EventName eventName, Object data) {
    if (eventName == EventName.SCROLLING_TWEETS_UPDATED) {
      tweets = (ArrayList<Tweet>) data;
      startScrolling();
    }
    // else if (eventName == EventName.TWEET_SELECTED) {
    // selectedTweet = (Tweet) data;
    // pauseScrolling();
    //
    // } else if (eventName == EventName.TWEET_DESELECTED) {
    // selectedTweet = null;
    // resumeScrolling();
    // }
  }

  private void pauseScrolling() {
    paused = true;
  }

  private void resumeScrolling() {
    paused = false;
  }

  private void startScrolling() {
    startMillis = millis();
    lastMillis = millis();
    createScrollerTweets();
  }

  private void createScrollerTweets() {
    scrollerTweets.clear();
    scrollHeight = 80f;
    for (int i = 0; i < tweets.size(); i++) {
      ScrollerTweet scrollerTweet =
          new ScrollerTweet(0, i * scrollHeight, getWidth(), scrollHeight, this);
      scrollerTweet.tweet = tweets.get(i);
      scrollerTweets.add(scrollerTweet);
      addTouchSubscriber(scrollerTweet);
    }
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    // return propagateTouch(x, y, down, touchType);
    return true;
  }

  @Override
  public void setup() {
    m.notificationCenter.registerToEvent(EventName.SCROLLING_TWEETS_UPDATED, this);
    m.notificationCenter.registerToEvent(EventName.TWEET_SELECTED, this);
    m.notificationCenter.registerToEvent(EventName.TWEET_DESELECTED, this);
  }

  @Override
  public boolean draw() {
    background(MyColorEnum.DARK_GRAY);
    advanceScrolling();
    for (ScrollerTweet t : scrollerTweets) {
      t.draw();
    }
    return false;
  }

  private void advanceScrolling() {
    if (scrolling) scroll();
    float timePassed = (millis() - lastMillis);
    if (timePassed < 3500 || paused) return;
    lastMillis = millis();
    scrolling = true;
  }

  private void scroll() {
    if (scrolledAmount >= scrollHeight) {
      scrolledAmount = 0;
      scrolling = false;
      return;
    }
    float scrollPixels = 4 / (m.p.frameRate / 30);
    float scrollingBy =
        Math.min(1 + scrollPixels * (scrollHeight - scrolledAmount) / scrollHeight, scrollHeight
            - scrolledAmount);
    for (int i = 0; i < scrollerTweets.size(); i++) {
      scrollerTweets.get(i).move(0, -scrollingBy);
    }
    scrolledAmount += scrollingBy;
  }
}
