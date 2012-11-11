package com.project4;

import processing.core.PImage;

import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;

public class Map extends VizPanel implements TouchEnabled, EventSubscriber {

  static final float MAX_LAT = 42.30146f;
  static final float MIN_LAT = 42.16198f;
  static final float MAX_LON = 93.56731f;
  static final float MIN_LON = 93.19066f;

  private float maxLat;
  private float minLat;
  private float maxLon;
  private float minLon;

  private PImage mapImage;
  private MapScatter mapScatter;
  private MapZoomButtons mapZoomButtons;

  public Map(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  @Override
  public void eventReceived(EventName eventName, Object data) {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    return propagateTouch(x, y, down, touchType);
  }

  @Override
  public void setup() {
    maxLat = 42.26f;
    minLat = MIN_LAT;
    maxLon = 93.40f;
    minLon = MIN_LON;
    mapImage = c.getImage("map", "png");
    setupMapZoomButtons();
    setupMapScatter();
  }

  private void setupMapZoomButtons() {
    mapZoomButtons = new MapZoomButtons(getWidth() - 40, getHeight() - 60, this);
    addTouchSubscriber(mapZoomButtons);
    mapZoomButtons.setup();
  }

  private void setupMapScatter() {
    mapScatter = new MapScatter(0, 0, getWidth(), getHeight(), this);
    mapScatter.setup();
    addTouchSubscriber(mapScatter);
  }

  @Override
  public boolean draw() {
    drawImage();
    mapScatter.draw();
    mapZoomButtons.draw();
    return false;
  }

  private void drawImage() {
    float zoom = (MAX_LON - MIN_LON) / (maxLon - minLon);
    float width = zoom * getWidth();
    float height = zoom * getHeight();
    float offsetLon = ((minLon - MIN_LON) / (MAX_LON - MIN_LON) ) * width;
    float offsetLat = ((minLat - MIN_LAT) / (MAX_LAT - MIN_LAT) ) * height;
    image(mapImage, -offsetLon, -offsetLat, width, height);
  }
  
  private void zoomIn() {
    
  }

  public float getMaxLat() {
    return maxLat;
  }

  public float getMinLat() {
    return minLat;
  }

  public float getMaxLon() {
    return maxLon;
  }

  public float getMinLon() {
    return minLon;
  }


}
