package com.project4;

import java.util.ArrayList;

import processing.core.PApplet;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.datasource.Tweet;
import com.anotherbrick.inthewall.Helper;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizPanel;

public class ScrollerTweet extends VizPanel implements TouchEnabled {

  public ScrollerTweet(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }
  
  public Tweet tweet;

  @Override
  public void setup() {
    
  }

  @Override
  public boolean draw() {
    pushStyle();
    fill(MyColorEnum.LIGHT_GRAY);
    textSize(12);
    ArrayList<String> lines = Helper.wordWrap(tweet.getText(), (int) (getWidth() - 20), (PApplet) m.p);
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
    return false;
  }

}
