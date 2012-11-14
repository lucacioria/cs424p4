package com.project4.dayview;

import java.util.ArrayList;

import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;
import com.project4.datasource.Day;

public class DayDetails extends VizPanel implements TouchEnabled, EventSubscriber {

  public final static float width = 100;
  public final static float height = 100;
  private int dayNumber;
  public float startX;
  public float endX;
  private Day day;
  private ArrayList<Day> days;

  public DayDetails(float x0, float y0, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  @Override
  public void setup() {
    m.notificationCenter.registerToEvent(EventName.DAY_DESELECTED, this);
    m.notificationCenter.registerToEvent(EventName.DAY_SELECTED, this);
    m.notificationCenter.registerToEvent(EventName.DAYS_UPDATED, this);
    setVisible(false);
  }

  @Override
  public boolean draw() {
    if (!isVisible()) return false;
    background(MyColorEnum.RED);
    text(day.getDay() + " - " + day.getWindSpeed(), getWidth() / 2, getHeight() / 2);
    return false;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void eventReceived(EventName eventName, Object data) {
    if (eventName == EventName.DAY_SELECTED) {
      dayNumber = (Integer) data;
      modifyPosition(map(dayNumber, 0, 20, startX, endX), getY0());
      day = days.get(dayNumber);
      this.setVisible(true);
    }
    if (eventName == EventName.DAY_DESELECTED) {
      this.setVisible(false);
    }
    if (eventName == EventName.DAYS_UPDATED) {
      days = (ArrayList<Day>) data;
    }
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    return propagateTouch(x, y, down, touchType);
  }

}
