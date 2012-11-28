package com.project4.map;

import java.util.Stack;

import processing.core.PImage;
import processing.core.PVector;

import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;
import com.project4.datasource.Tweet;

public class Map extends VizPanel implements TouchEnabled, EventSubscriber {

  static final double MAX_LAT = 42.30146f;
  static final double MIN_LAT = 42.16198f;
  static final double MIN_LON = -93.56731f;
  static final double MAX_LON = -93.19066f;

  private double maxLat;
  private double minLat;
  private double maxLon;
  private double minLon;

  private PImage mapImage;
  private Scatter scatter;
  private HeatMap heatMap;
  private TagCloud tagCloud;
  private Stack<Double> zoomStackLon = new Stack<Double>();
  private Stack<Double> zoomStackLat = new Stack<Double>();
  private LinkMap linkMap;
  private ControlPanel controlPanel;

  private boolean filter1Visible = false;
  private boolean filter2Visible = false;
  private boolean filter3Visible = false;

  public Map(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  public boolean isAnyFilterSelected() {
    return (filter1Visible || filter2Visible || filter3Visible);
  }

  public boolean isFilterVisible(int filterNumber) {
    if (filterNumber == 1) return filter1Visible;
    if (filterNumber == 2) return filter2Visible;
    if (filterNumber == 3) return filter3Visible;
    return false;
  }

  @Override
  public void eventReceived(EventName eventName, Object data) {
    if (eventName == EventName.BUTTON_TOUCHED) {
      manageButtons(data.toString());
      manageFilterButtons(data.toString());
    }
    if (eventName == EventName.BUTTON_TOUCHED && data.toString().equals("scatterButton")) {
      scatter.toggleVisible();
      if (scatter.isVisible()) {
        m.notificationCenter.notifyEvent(EventName.BUTTON_SELECTED, "scatterButton");
        heatMap.setVisible(false);
        m.notificationCenter.notifyEvent(EventName.BUTTON_DESELECTED, "heatmapButton");
      } else {
        m.notificationCenter.notifyEvent(EventName.BUTTON_DESELECTED, "scatterButton");
      }
    }
    if (eventName == EventName.BUTTON_TOUCHED && data.toString().equals("heatmapButton")) {
      heatMap.toggleVisible();
      if (heatMap.isVisible()) {
        m.notificationCenter.notifyEvent(EventName.BUTTON_SELECTED, "heatmapButton");
        scatter.setVisible(false);
        m.notificationCenter.notifyEvent(EventName.BUTTON_DESELECTED, "scatterButton");
      } else {
        m.notificationCenter.notifyEvent(EventName.BUTTON_DESELECTED, "heatmapButton");
      }
    }
    if (eventName == EventName.BUTTON_TOUCHED && data.toString().equals("wordcloudButton")) {
      tagCloud.toggleVisible();
      if (tagCloud.isVisible()) {
        m.notificationCenter.notifyEvent(EventName.BUTTON_SELECTED, "heatmapButton");
        scatter.setVisible(false);
        m.notificationCenter.notifyEvent(EventName.BUTTON_DESELECTED, "scatterButton");
      } else {
        m.notificationCenter.notifyEvent(EventName.BUTTON_DESELECTED, "wordcloudButton");
      }
    }
  }

  private void manageButtons(String buttonName) {
    if (buttonName.equals("zoomInButton")) {
      zoomIn();
      m.notificationCenter.notifyEvent(EventName.MAP_ZOOMED_OR_PANNED);
    } else if (buttonName.equals("zoomOutButton")) {
      zoomOut();
      m.notificationCenter.notifyEvent(EventName.MAP_ZOOMED_OR_PANNED);
    } else if (buttonName.equals("panUpButton")) {
      panUp();
      m.notificationCenter.notifyEvent(EventName.MAP_ZOOMED_OR_PANNED);
    } else if (buttonName.equals("panDownButton")) {
      panDown();
      m.notificationCenter.notifyEvent(EventName.MAP_ZOOMED_OR_PANNED);
    } else if (buttonName.equals("panRightButton")) {
      panRight();
      m.notificationCenter.notifyEvent(EventName.MAP_ZOOMED_OR_PANNED);
    } else if (buttonName.equals("panLeftButton")) {
      panLeft();
      m.notificationCenter.notifyEvent(EventName.MAP_ZOOMED_OR_PANNED);
    }
  }

  private void manageFilterButtons(String buttonName) {
    if (buttonName.equals("filter1Button")) {
      filter1Visible = !filter1Visible;
      if (filter1Visible) {
        filter2Visible = filter3Visible = false;
        m.notificationCenter.notifyEvent(EventName.BUTTON_SELECTED, "filter1Button");
        m.notificationCenter.notifyEvent(EventName.BUTTON_DESELECTED, "filter2Button");
        m.notificationCenter.notifyEvent(EventName.BUTTON_DESELECTED, "filter3Button");
      } else {
        m.notificationCenter.notifyEvent(EventName.BUTTON_DESELECTED, "filter1Button");
      }
    }
    if (buttonName.equals("filter2Button")) {
      filter2Visible = !filter2Visible;
      if (filter2Visible) {
        filter1Visible = filter3Visible = false;
        m.notificationCenter.notifyEvent(EventName.BUTTON_SELECTED, "filter2Button");
        m.notificationCenter.notifyEvent(EventName.BUTTON_DESELECTED, "filter1Button");
        m.notificationCenter.notifyEvent(EventName.BUTTON_DESELECTED, "filter3Button");
      } else {
        m.notificationCenter.notifyEvent(EventName.BUTTON_DESELECTED, "filter2Button");
      }
    }
    if (buttonName.equals("filter3Button")) {
      filter3Visible = !filter3Visible;
      if (filter3Visible) {
        filter1Visible = filter2Visible = false;
        m.notificationCenter.notifyEvent(EventName.BUTTON_SELECTED, "filter3Button");
        m.notificationCenter.notifyEvent(EventName.BUTTON_DESELECTED, "filter1Button");
        m.notificationCenter.notifyEvent(EventName.BUTTON_DESELECTED, "filter2Button");
      } else {
        m.notificationCenter.notifyEvent(EventName.BUTTON_DESELECTED, "filter3Button");
      }
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
    setupControlPanel();
    setupScatter();
    setupHeatMap();
    setupLinkMap();
    setupTagCloud();
    m.notificationCenter.registerToEvent(EventName.BUTTON_TOUCHED, this);
  }

  private void setupTagCloud() {
    tagCloud = new TagCloud(0, 0, getWidth(), getHeight(), this);
    tagCloud.setup();
    addTouchSubscriber(tagCloud);
  }

  private void setupControlPanel() {
    controlPanel =
        new ControlPanel(getWidth() - ControlPanel.width, getHeight() - ControlPanel.height, this);
    controlPanel.setup();
    addTouchSubscriber(controlPanel);
  }

  private void setupScatter() {
    scatter = new Scatter(0, 0, getWidth(), getHeight(), this);
    scatter.setup();
    addTouchSubscriber(scatter);
  }

  private void setupHeatMap() {
    heatMap = new HeatMap(0, 0, getWidth(), getHeight(), this);
    heatMap.setup();
    addTouchSubscriber(heatMap);
  }

  private void setupLinkMap() {
    linkMap = new LinkMap(0, 0, getWidth(), getHeight(), this);
    linkMap.setup();
    addTouchSubscriber(linkMap);
  }

  @Override
  public boolean draw() {
    drawImage();
    scatter.draw();
    heatMap.draw();
    linkMap.draw();
    tagCloud.draw();
    controlPanel.draw();
    return false;
  }

  private void drawImage() {
    double zoom = (MAX_LON - MIN_LON) / (maxLon - minLon);
    double width = zoom * getWidth();
    double height = zoom * getHeight();
    double offsetLon = ((minLon - MIN_LON) / (MAX_LON - MIN_LON)) * width;
    double offsetLat = ((minLat - MIN_LAT) / (MAX_LAT - MIN_LAT)) * height;
    image(mapImage, (float) -offsetLon, (float) -offsetLat, (float) width, (float) height);
  }

  private void zoomIn() {
    double lon = (maxLon - minLon) / 7;
    double lat = (maxLat - minLat) / 7;
    zoomStackLon.add(lon);
    zoomStackLat.add(lat);
    minLon += lon;
    maxLon -= lon;
    minLat += lat;
    maxLat -= lat;
  }

  private double getPanOffset() {
    return (maxLat - minLat) / 10;
  }

  private void panUp() {
    double offset = Math.min(minLat - MIN_LAT, getPanOffset());
    minLat -= offset;
    maxLat -= offset;
  }

  private void panDown() {
    double offset = Math.min(MAX_LAT - maxLat, getPanOffset());
    minLat += offset;
    maxLat += offset;
  }

  private void panLeft() {
    double offset = Math.min(Math.abs(MIN_LON - minLon), getPanOffset());
    minLon -= offset;
    maxLon -= offset;
  }

  private void panRight() {
    double offset = Math.min(Math.abs(maxLon - MAX_LON), getPanOffset());
    minLon += offset;
    maxLon += offset;
  }

  private void zoomOut() {
    if (zoomStackLat.size() == 0) {
      minLon = MIN_LON;
      minLat = MIN_LAT;
      maxLon = MAX_LON;
      maxLat = MAX_LAT;
      return;
    }
    double lat = zoomStackLat.pop();
    double lon = zoomStackLon.pop();
    minLon -= lon;
    maxLon += lon;
    minLat -= lat;
    maxLat += lat;
    repositionMap();
  }

  private void repositionMap() {
    // fix errors
    if (minLon < MIN_LON) {
      double val = MIN_LON - minLon;
      minLon += val;
      maxLon += val;
    } else if (maxLon > MAX_LON) {
      double val = MAX_LON - maxLon;
      minLon -= val;
      maxLon -= val;
    }
    if (minLat < MIN_LAT) {
      double val = MIN_LAT - minLat;
      minLat += val;
      maxLat += val;
    } else if (maxLat > MAX_LAT) {
      double val = maxLat - MAX_LAT;
      minLat -= val;
      maxLat -= val;
    }
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

  public boolean isVisible(Tweet t) {
    return t.getLat() > minLat && t.getLat() < maxLat && t.getLon() > minLon && t.getLon() < maxLat;
  }

  public PVector getPositionByLatLon(double lat, double lon) {
    float x = map((float) lon, (float) getMinLon(), (float) getMaxLon(), 0, getWidth());
    float y = map((float) lat, (float) getMinLat(), (float) getMaxLat(), 0, getHeight());
    return new PVector(x, y);
  }


}
