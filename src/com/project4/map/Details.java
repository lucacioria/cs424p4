package com.project4.map;

import java.util.ArrayList;

import processing.core.PApplet;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.Helper;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;
import com.project4.datasource.Tweet;

public class Details extends VizPanel implements TouchEnabled, EventSubscriber {

  private Tweet tweet;
  public static final float height = 80;
  public static final float width = 220;


  public Details(float x0, float y0, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  @Override
  public void setup() {
    setVisible(false);
    m.notificationCenter.registerToEvent(EventName.TWEET_SELECTED, this);
    m.notificationCenter.registerToEvent(EventName.TWEET_DESELECTED, this);
  }

  @Override
  public boolean draw() {
    if (!isVisible()) return false;
    pushStyle();
    drawBackground();
    drawHeader();
    drawText();
    popStyle();
    return false;
  }

  private void drawText() {
    pushStyle();
    fill(MyColorEnum.BLACK);
    textSize(9);
    ArrayList<String> lines =
        Helper.wordWrap(tweet.getText().replaceAll("\n", " "), (int) (getWidth() - 20), (PApplet) m.p);
    textAlign(PApplet.LEFT, PApplet.TOP);
    int lineHeight = 15;
    for (int i = 0; i < Math.min(lines.size(), 4); i++) {
      text(lines.get(i), 10, 20 + i * lineHeight);
    }
    popStyle();
  }

  private void drawHeader() {
    pushStyle();
    textSize(9);
    fill(MyColorEnum.DARK_GRAY);
    text(tweet.getDateFormatted(), 10, 10);
    popStyle();
  }

  private void drawBackground() {
    pushStyle();
    stroke(MyColorEnum.BLACK);
    fill(MyColorEnum.WHITE);
    rect(0, 0, getWidth(), getHeight(), 10, 10, 10, 10);
    popStyle();
  }

  @Override
  public void eventReceived(EventName eventName, Object data) {
    if (eventName == EventName.TWEET_SELECTED) {
      tweet = (Tweet) data;
      modifyPositionWithAbsoluteValue(m.touchX + 20, Helper.costrain(m.touchY + 20, 0, 220));
      setVisible(true);
    } else if (eventName == EventName.TWEET_DESELECTED) {
      setVisible(false);
    }
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    return propagateTouch(x, y, down, touchType);
  }

}
