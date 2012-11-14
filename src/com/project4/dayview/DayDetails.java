package com.project4.dayview;

import java.util.ArrayList;
import java.util.HashMap;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;

import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;
import com.project4.datasource.Day;
import com.project4.datasource.Day.WeatherEnum;

public class DayDetails extends VizPanel implements TouchEnabled, EventSubscriber {

  public final static float width = 100;
  public final static float height = 100;
  private int dayNumber;
  public float startX;
  public float endX;
  private Day day;
  private ArrayList<Day> days;
  private HashMap<String, PImage> weatherShapes;

  public DayDetails(float x0, float y0, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  @Override
  public void setup() {
    m.notificationCenter.registerToEvent(EventName.DAY_DESELECTED, this);
    m.notificationCenter.registerToEvent(EventName.DAY_SELECTED, this);
    m.notificationCenter.registerToEvent(EventName.DAYS_UPDATED, this);
    weatherShapes = new HashMap<String, PImage>();
    for (String name : new String[] {"clear", "cloudy", "rain", "showers"}) {
      weatherShapes.put(name, c.getImage("weather/" + name, "png"));
    }
    setVisible(false);
  }

  @Override
  public boolean draw() {
    if (!isVisible()) return false;
    drawBackground();
    drawWeather();
    text(day.getDay() + " - " + day.getWindSpeed(), getWidth() / 2, getHeight() / 2);
    return false;
  }

  private void drawWeather() {
    image(weatherShapes.get(day.getWeather().toString().toLowerCase()), 10, 10);
  }

  private void drawBackground() {
    noStroke();
    fill(MyColorEnum.LIGHT_BLUE, 200f);
    beginShape();
    vertex(getWidth() / 2 - 10, getHeight() - 20);
    vertex(getWidth() / 2 + 10, getHeight() - 20);
    vertex(getWidth() / 2, getHeight());
    endShape(PApplet.CLOSE);
    rect(0, 0, getWidth(), getHeight() - 20, 10, 10, 10, 10);
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
