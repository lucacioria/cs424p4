package com.project4;

import java.util.ArrayList;

import processing.core.PApplet;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.datasource.Tweet;
import com.anotherbrick.inthewall.Helper;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizPanel;

public class ScrollerTweet extends VizPanel implements TouchEnabled {

  private Scroller scroller;

  public ScrollerTweet(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
    this.scroller = (Scroller) parent;
  }

  public Tweet tweet;
  private boolean selected;

  @Override
  public void setup() {

  }

  @Override
  public boolean draw() {
    pushStyle();
    if (selected) background(MyColorEnum.RED);
    fill(MyColorEnum.LIGHT_GRAY);
    textSize(12);
    ArrayList<String> lines =
        Helper.wordWrap(tweet.getText(), (int) (getWidth() - 20), (PApplet) m.p);
    textAlign(PApplet.LEFT, PApplet.TOP);
    int lineHeight = 15;
    for (int i = 0; i < Math.min(lines.size(), 4); i++) {
      text(lines.get(i), 10, 10 + i * lineHeight);
    }
    popStyle();
    return false;
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    if (!down) {
      if (selected) {
        m.notificationCenter.notifyEvent(EventName.TWEET_DESELECTED);
        selected = false;
      } else {
        if (scroller.selectedTweet == null) {
          m.notificationCenter.notifyEvent(EventName.TWEET_SELECTED, tweet);
          selected = true;
        }
      }
      return true;
    }
    return false;
  }
}
