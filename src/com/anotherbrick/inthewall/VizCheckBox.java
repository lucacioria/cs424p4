package com.anotherbrick.inthewall;

import processing.core.PApplet;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;

public class VizCheckBox extends VizPanel implements TouchEnabled {

  private boolean disabled = false;
  private boolean selected = false;
  public String name;

  public VizCheckBox(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  @Override
  public boolean draw() {
    pushStyle();
    drawBackground();
    if (selected) drawTick();
    popStyle();
    return false;
  }

  private void drawTick() {
    pushStyle();
    textAlign(PApplet.CENTER, PApplet.CENTER);
    fill(MyColorEnum.BLACK);
    textSize(10);
    text("X", getWidth() / 2, getHeight() / 2);
    popStyle();
  }

  private void drawBackground() {
    strokeWeight(1);
    if (!disabled) {
      fill(MyColorEnum.WHITE);
    } else {
      fill(MyColorEnum.LIGHT_GRAY);
    }
    stroke(MyColorEnum.BLACK);
    rect(0, 0, getWidth(), getHeight());
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    if (!down) {
      if (!disabled) {
        toggleSelected();
      }
    }
    return true;
  }

  private void toggleSelected() {
    selected = !selected;
    sendEvent();
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  private void sendEvent() {
    m.notificationCenter.notifyEvent(EventName.CHECKBOX_CHANGED, name, selected);
  }

  @Override
  public void setup() {

  }


}
