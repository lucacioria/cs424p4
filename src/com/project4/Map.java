package com.project4;

import processing.core.PImage;

import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;

public class Map extends VizPanel implements TouchEnabled, EventSubscriber {

  static final double MAX_LAT = 42.30146f;
  static final double MIN_LAT = 42.16198f;
  static final double MAX_LON = 93.56731f;
  static final double MIN_LON = 93.19066f;

  private double maxLat;
  private double minLat;
  private double maxLon;
  private double minLon;

  private PImage mapImage;
  private MapScatter mapScatter;
  private MapZoomButtons mapZoomButtons;

  public Map(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  @Override
  public void eventReceived(EventName eventName, Object data) {
    if(eventName==EventName.BUTTON_TOUCHED){
      manageButtons(data.toString());
    }
  }
  
  private void manageButtons(String buttonName){
    if(buttonName.equals("zoomInButton")){
      zoomIn();
    }
    if(buttonName.equals("zoomOutButton")){
      zoomOut();
    }
  }

 

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    return propagateTouch(x, y, down, touchType);
  }

  @Override
  public void setup() {
    maxLat = MAX_LAT;
    minLat = MIN_LAT;
    maxLon = MAX_LON;
    minLon = MIN_LON;
    mapImage = c.getImage("map", "png");
    setupMapZoomButtons();
    setupMapScatter();
    m.notificationCenter.registerToEvent(EventName.BUTTON_TOUCHED, this);
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
    double zoom = (MAX_LON - MIN_LON) / (maxLon - minLon);
    double width = zoom * getWidth();
    double height = zoom * getHeight();
    double offsetLon = ((minLon - MIN_LON) / (MAX_LON - MIN_LON) ) * width;
    double offsetLat = ((minLat - MIN_LAT) / (MAX_LAT - MIN_LAT) ) * height;
    image(mapImage,(float) -offsetLon,(float) -offsetLat,(float) width,(float) height);
  }
 
  private void zoomIn() {
    minLon += (maxLon - minLon)/3;
    maxLon -= (maxLon - minLon)/3;
    minLat += (maxLat - minLat)/3;
    maxLat -= (maxLat - minLat)/3;
  }
  private void zoomOut() {
    minLon -= (maxLon - minLon);
    maxLon += (maxLon - minLon);
    minLat -= (maxLat - minLat);
    maxLat += (maxLat - minLat);
  }

  public double getMaxLat() {
    return maxLat;
  }

  public double getMinLat() {
    return minLat;
  }

  public double getMaxLon() {
    return maxLon;
  }

  public double getMinLon() {
    return minLon;
  }


}
