package com.project4.dayview;

import java.util.Arrays;

import processing.core.PApplet;
import processing.core.PVector;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizButton;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;

public class TimeSlider extends VizPanel implements TouchEnabled, EventSubscriber {

  private VizButton left, right, center;
  private boolean leftMoving, rightMoving, centerMoving;
  private PVector touchPoint;
  private float leftX, rightX;
  private float minCenterWidth = 10;
  private float maxValue = 1305935940;
  private float minValue = 1304121600;

  public TimeSlider(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  @Override
  public void setup() {
    setupButtons();
    m.notificationCenter.registerToEvent(EventName.BUTTON_PRESSED, this);
    m.notificationCenter.registerToEvent(EventName.BUTTON_TOUCHED, this);
  }

  private void setupButtons() {
    left = new VizButton(0, 0, 20, 20, this);
    left.name = "left";
    left.eventOnPressed = true;
    addTouchSubscriber(left);
    center = new VizButton(20, 0, 60, 20, this);
    center.name = "center";
    center.eventOnPressed = true;
    center.setStyle(MyColorEnum.BLACK, MyColorEnum.BLACK, MyColorEnum.BLACK, 50f, 0, 0);
    addTouchSubscriber(center);
    right = new VizButton(80, 0, 20, 20, this);
    right.name = "right";
    right.eventOnPressed = true;
    addTouchSubscriber(right);
  }

  @Override
  public boolean draw() {
    pushStyle();
    background(MyColorEnum.WHITE, 50f);
    updatePosition();
    left.draw();
    center.draw();
    right.draw();
    popStyle();
    return false;
  }

  private void updatePosition() {
    float offset;
    if (centerMoving) {
      offset =
          PApplet.constrain((m.touchX - touchPoint.x), -leftX,
              getWidth() - rightX - right.getWidth());
      left.modifyPosition(leftX + offset, 0);
      right.modifyPosition(rightX + offset, 0);
      center.modifyPosition(leftX + left.getWidth() + offset, 0);
    } else if (leftMoving) {
      offset =
          PApplet.constrain((m.touchX - touchPoint.x), -leftX, -leftX + rightX - right.getWidth()
              - minCenterWidth);
      left.modifyPosition(leftX + offset, 0);
      center.modifyPositionAndSize(leftX + left.getWidth() + offset, 0,
          -left.getX1() + right.getX0(), center.getHeight());
    } else if (rightMoving) {
      offset =
          PApplet.constrain((m.touchX - touchPoint.x), -rightX + leftX + left.getWidth()
              + minCenterWidth, -rightX + getWidth() - right.getWidth());
      right.modifyPosition(rightX + offset, 0);
      center.modifyPositionAndSize(center.getX0(), 0, -left.getX1() + right.getX0(),
          center.getHeight());
    }

  }

  @Override
  public void eventReceived(EventName eventName, Object data) {
    if (eventName == EventName.BUTTON_PRESSED) {
      if (data.toString().equals("left")) {
        leftMoving = true;
        left.setModal(true);
      }
      if (data.toString().equals("right")) {
        rightMoving = true;
        right.setModal(true);
      }
      if (data.toString().equals("center")) {
        centerMoving = true;
        center.setModal(true);
      }
      touchPoint = new PVector(m.touchX, m.touchY);
      saveLeftAndRightX();
    } else if (eventName == EventName.BUTTON_TOUCHED) {
      if (Arrays.asList(new String[] {"left", "center", "right"}).contains(data.toString())) {
        leftMoving = rightMoving = centerMoving = false;
        center.setModal(false);
        right.setModal(false);
        left.setModal(false);
        raiseMovedEvent();
      }
    }
  }

  private void saveLeftAndRightX() {
    leftX = left.getX0();
    rightX = right.getX0();
  }

  private void raiseMovedEvent() {
    int[] minMax = new int[2];
    minMax[0] = (int) map(left.getX0(), 0, getWidth(), minValue, maxValue);
    minMax[1] = (int) map(right.getX1(), 0, getWidth(), minValue, maxValue);
    m.notificationCenter.notifyEvent(EventName.TIME_SLIDER_UPDATED, minMax);
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    return propagateTouch(x, y, down, touchType);
  }

}
