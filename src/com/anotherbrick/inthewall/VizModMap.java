package com.anotherbrick.inthewall;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.datasource.DSCrash;
import com.modestmaps.InteractiveMap;
import com.modestmaps.core.Point2f;
import com.modestmaps.geo.Location;
import com.modestmaps.providers.Microsoft;

public class VizModMap extends VizPanel implements TouchEnabled, EventSubscriber {
  private InteractiveMap map;
  private PVector mapOffset;
  private PVector mapSize;
  private PVector firstTouch = new PVector(0, 0);
  private ArrayList<DSCrash> accidents = new ArrayList<DSCrash>();
  private boolean mapTouched;
  long lastTouchTime;
  private VizMapLegend legend;
  private int numberOfClusters = 10;
  private ArrayList<Cluster> clusters = new ArrayList<Cluster>();
  private int currentProvider = 1;
  private String colorFilter = "alcohol_involved";
  private int clusterLevel;

  public VizModMap(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
    this.parent = parent;
  }

  @Override
  public void setup() {

    m.notificationCenter.registerToEvent(EventName.CRASHES_UPDATED, this);
    m.notificationCenter.registerToEvent(EventName.BUTTON_TOUCHED, this);
    m.notificationCenter.registerToEvent(EventName.CHANGED_MAP_COLOR_ATTRIBUTE, this);

    mapOffset = new PVector(0, 0);
    mapSize = new PVector(getWidthZoom(), getHeightZoom());

    map = new InteractiveMap(m.p, new Microsoft.RoadProvider(), getX0AbsoluteZoom(),
        getY0AbsoluteZoom(), mapSize.x, mapSize.y);
    centerAndZoomOnState(17);
    setupLegend();

    clusterLevel = (int) getWidth() / numberOfClusters;

    updateCorners();
    updateClusters();
  }

  private void setupLegend() {
    legend = new VizMapLegend(0, 0, getWidth(), getHeight() * 0.2f, this);
    legend.setColorFilter(colorFilter);
    legend.setup();
  }

  private void centerAndZoomOnState(int stateCode) {
    float[] array = focusOnState(stateCode);
    int zoom = (int) array[2];
    if (c.multiply == 2) {
      zoom += 1;
    } else if (c.multiply == 6) {
      zoom += 2;
    }
    map.setCenterZoom(new Location(array[0], array[1]), zoom);
  }

  @Override
  public boolean draw() {
    manageDrag();
    pushStyle();
    background(MyColorEnum.BLACK);
    map.draw();
    // drawClusterGrid();
    drawClusters();
    drawAccidents(accidents);
    setupLegend();
    legend.draw();

    popStyle();

    return false;
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    if (down) {

      manageAccidentPopup(x, y);

      firstTouch = new PVector(x, y);
      mapTouched = true;
      // if double touch, then zoom
      if ((System.currentTimeMillis() - lastTouchTime) < 400) {

        Point2f center = new Point2f(x * c.multiply, y * c.multiply);
        map.setCenterZoom(map.pointLocation(center), map.getZoom() + 1);
        // map.setCenter(map.pointLocation(center));
        lastTouchTime = 0;
        updateCorners();
        updateClusters();

      }
      lastTouchTime = System.currentTimeMillis();
      updateCorners();
      updateClusters();
      return true;
    } else {
      mapTouched = false;
      updateCorners();
      updateClusters();
      return false;
    }
  }

  public void drawAccidents(ArrayList<DSCrash> accidents) {
    DSCrash crashForPopup = null;
    for (DSCrash crash : accidents) {
      if (crash.isVisibleOnMap()) {
        Location location = new Location(crash.latitude, crash.longitude);
        Point2f p = map.locationPoint(location);
        p.x /= c.multiply;
        p.y /= c.multiply;
        fill(colorBy(colorFilter, crash), 150f);
        strokeWeight(0.3f);
        stroke(MyColorEnum.BLACK);
        if (crash.getCluster() != null && crash.getCluster().getCount() <= 1) {
          ellipse(p.x - getX0(), p.y - getY0(), 10, 10);

          if (crash.selected) {
            crashForPopup = crash;
          }
        }
      }
    }
    if (crashForPopup != null) {
      Location location = new Location(crashForPopup.latitude, crashForPopup.longitude);
      Point2f p = map.locationPoint(location);
      p.x /= c.multiply;
      p.y /= c.multiply;
      drawPopUp(crashForPopup, p.x - getX0(), p.y - getY0());
    }
  }

  private void drawPopUp(DSCrash accident, float ics, float ips) {
    pushStyle();
    fill(MyColorEnum.BLACK, 150f);
    rect(ics, ips, 100, 54);
    fill(255);
    textSize(6);
    // text("latitude: " + accident.latitude, ics + 5, ips + 7);
    // text("longitude: "+ accident.longitude, ics + 5, ips + 7+7);
    text("Date: " + accident.month.substring(2) + " " + accident._year, ics + 5, ips + 7);
    text("Day of week: " + accident.day_of_week.substring(2), ics + 5, ips + 7 + 7 * 1);
    text("Average age: " + accident.age, ics + 5, ips + 7 + 7 * 2);
    text("Number of fatalities: " + accident.number_of_fatalities, ics + 5, ips + 7 + 7 * 3);
    text("Roadway surface: " + accident.roadway_surface_condition, ics + 5, ips + 7 + 7 * 4);
    text("Genders involved: " + accident.sex, ics + 5, ips + 7 + 7 * 5);
    text("Vehicle type: " + accident.vehicle_configuration, ics + 5, ips + 7 + 7 * 6);

    popStyle();
  }

  public void manageAccidentPopup(float x, float y) {
    for (DSCrash crash : accidents) {
      if (crash.isVisibleOnMap()) {
        if (dist(x, y, crash.xOnMap, crash.yOnMap) < crash.dimension
            && crash.cluster.getCount() == 1) {
          crash.selected = !crash.selected;
          break;
        }
      }
    }
  }

  public void manageDrag() {
    if (mapTouched) {
      if (m.touchX != firstTouch.x && m.touchY != firstTouch.y && m.touchX != 0 && m.touchY != 0) {
        map.tx += (m.touchX - firstTouch.x) * c.multiply / map.sc;
        map.ty += (m.touchY - firstTouch.y) * c.multiply / map.sc;
        firstTouch = new PVector(m.touchX, m.touchY);
        updateCorners();
        updateClusters();
      }
    }
  }

  public void setProvider(int i) {
    if (i == 1) {
      map.setMapProvider(new Microsoft.RoadProvider());
    } else if (i == 2) {
      map.setMapProvider(new Microsoft.AerialProvider());
    } else if (i == 3) {
      map.setMapProvider(new Microsoft.HybridProvider());
    }
  }

  public float[] focusOnState(int i) {

    if (i == 1) {
      return new float[] { 33.027088f, -86.616211f, 6 };
    } else if (i == 2) {
      return new float[] { 64.54844f, -139.945312f, 3 };
    } else if (i == 4) {
      return new float[] { 34.361576f, -111.665039f, 6 };
    } else if (i == 5) {
      return new float[] { 34.849875f, -92.197266f, 6 };
    } else if (i == 6) {
      return new float[] { 37.834557f, -120.014648f, 5 };
    } else if (i == 8) {
      return new float[] { 38.925229f, -105.724805f, 6 };
    } else if (i == 9) {
      return new float[] { 41.590797f, -72.597656f, 8 };
    } else if (i == 10) {
      return new float[] { 39.095963f, -75.454102f, 8 };
    } else if (i == 11) {
      return new float[] { 38.895112f, -77.036366f, 6 };
    } else if (i == 12) {
      return new float[] { 28.116767f, -83.518555f, 6 };
    } else if (i == 13) {
      return new float[] { 32.953368f, -83.320312f, 6 };
    } else if (i == 15) {
      return new float[] { 20.704739f, -157.137451f, 6 };
    } else if (i == 16) {
      return new float[] { 45.614037f, -115.290527f, 5 };
    } else if (i == 17) {
      return new float[] { 39.833125f, -89.398528f, 6 };
    } else if (i == 18) {
      return new float[] { 40.069665f, -86.033936f, 6 };
    } else if (i == 19) {
      return new float[] { 42.098222f, -93.405762f, 6 };
    } else if (i == 20) {
      return new float[] { 38.634036f, -98.239746f, 6 };
    } else if (i == 21) {
      return new float[] { 37.544577f, -84.737549f, 6 };
    } else if (i == 22) {
      return new float[] { 31.344254f, -92.307129f, 6 };
    } else if (i == 23) {
      return new float[] { 45.483244f, -69.071045f, 6 };
    } else if (i == 24) {
      return new float[] { 38.967951f, -76.508789f, 6 };
    } else if (i == 25) {
      return new float[] { 42.366662f, -72.004395f, 7 };
    } else if (i == 26) {
      return new float[] { 43.786958f, -84.550781f, 6 };
    } else if (i == 27) {
      return new float[] { 46.407564f, -94.262695f, 6 };
    } else if (i == 28) {
      return new float[] { 32.657876f, -89.472656f, 6 };
    } else if (i == 29) {
      return new float[] { 38.565348f, -92.504883f, 6 };
    } else if (i == 30) {
      return new float[] { 47.010226f, -109.775391f, 5 };
    } else if (i == 31) {
      return new float[] { 41.492121f, -99.558105f, 6 };
    } else if (i == 32) {
      return new float[] { 38.80547f, -116.71875f, 5 };
    } else if (i == 33) {
      return new float[] { 43.866218f, -71.520996f, 7 };
    } else if (i == 34) {
      return new float[] { 40.33817f, -74.498291f, 7 };
    } else if (i == 35) {
      return new float[] { 34.51994f, -105.87009f, 6 };
    } else if (i == 36) {
      return new float[] { 42.90816f, -75.981445f, 6 };
    } else if (i == 37) {
      return new float[] { 35.496456f, -78.09082f, 6 };
    } else if (i == 38) {
      return new float[] { 47.551493f, -101.002012f, 6 };
    } else if (i == 39) {
      return new float[] { 40.417287f, -82.907123f, 6 };
    } else if (i == 40) {
      return new float[] { 35.407752f, -98.492877f, 6 };
    } else if (i == 41) {
      return new float[] { 43.804133f, -120.554201f, 6 };
    } else if (i == 42) {
      return new float[] { 41.203322f, -77.194525f, 6 };
    } else if (i == 44) {
      return new float[] { 41.580095f, -71.477429f, 8 };
    } else if (i == 45) {
      return new float[] { 33.836081f, -81.163724f, 6 };
    } else if (i == 46) {
      return new float[] { 44.569515f, -100.501813f, 6 };
    } else if (i == 47) {
      return new float[] { 35.517491f, -86.580447f, 6 };
    } else if (i == 48) {
      return new float[] { 31.968599f, -99.901813f, 5 };
    } else if (i == 49) {
      return new float[] { 39.32098f, -111.693731f, 6 };
    } else if (i == 50) {
      return new float[] { 43.958803f, -72.577841f, 7 };
    } else if (i == 51) {
      return new float[] { 37.431573f, -80.156894f, 6 };
    } else if (i == 53) {
      return new float[] { 47.751074f, -120.740139f, 6 };
    } else if (i == 54) {
      return new float[] { 39.697626f, -80.454903f, 6 };
    } else if (i == 55) {
      return new float[] { 44.78444f, -89.787868f, 6 };
    } else if (i == 56) {
      return new float[] { 43.075968f, -107.690284f, 6 };
    } else
      return new float[] { 0, 0, 0 };

  }

  public ArrayList<DSCrash> getAccidents() {
    return accidents;
  }

  public void setAccidents(ArrayList<DSCrash> accidents) {
    this.accidents = accidents;
  }

  public void updateCorners() {
    m.upperLeftLocation = map.pointLocation(getX0AbsoluteZoom(), getY0AbsoluteZoom());
    m.lowerRightLocation = map.pointLocation(getX0AbsoluteZoom() + getWidthZoom(),
        getY0AbsoluteZoom() + getHeightZoom());
  }

  public void drawClusterGrid() {

    int clusterLevel = (int) getWidth() / numberOfClusters;
    for (int i = 0; i < getWidth(); i++) {
      if (i % clusterLevel == 0) {
        fill(MyColorEnum.RED);
        line(0, i, getWidth(), i);
      }
    }
    for (int j = 0; j < getWidth(); j++) {
      if (j % clusterLevel == 0) {
        fill(MyColorEnum.RED);
        line(+j, 0, j, getHeight());

      }
    }
  }

  public void updateClusters() {
    clusters.clear();
    int clusterLevel = (int) getWidth() / numberOfClusters;
    updateVisibilityOfCrashes();
    for (int i = 0; i < numberOfClusters; i++) {
      for (int j = 0; j < numberOfClusters; j++) {
        Cluster cluster = new Cluster(new Point2f(clusterLevel / 2 + clusterLevel * i, clusterLevel
            / 2 + clusterLevel * j));
        clusters.add(cluster);
        updateClusterCount(i, j, cluster);
      }
    }

  }

  private void updateVisibilityOfCrashes() {
    for (DSCrash crash : accidents) {
      if (crash.longitude > m.upperLeftLocation.lon && crash.longitude < m.lowerRightLocation.lon
          && crash.latitude < m.upperLeftLocation.lat && crash.latitude > m.lowerRightLocation.lat) {
        crash.setVisibleOnMap(true);
        crash.xOnMap = map.locationPoint(new Location(crash.latitude, crash.longitude)).x
            / c.multiply;
        crash.yOnMap = map.locationPoint(new Location(crash.latitude, crash.longitude)).y
            / c.multiply;
      } else {
        crash.setVisibleOnMap(false);
      }
    }

  }

  public void updateClusterCount(int i, int j, Cluster cluster) {
    int count = 0;
    String checker = "";
    int maximum = legend.getLabels().size();
    // attenzione al 3
    cluster.counters.clear();
    for (int w = 0; w < maximum; w++) {
      cluster.counters.add(0f);
    }
    cluster.percentages.clear();
    for (int w = 0; w < maximum; w++) {
      cluster.percentages.add(0f);
    }
    for (DSCrash crash : accidents) {
      if (crash.isVisibleOnMap()) {
        if (crash.xOnMap > getX0() + clusterLevel * (i)
            && crash.xOnMap < getX0() + clusterLevel * (i + 1)
            && crash.yOnMap > getY0() + clusterLevel * (j)
            && crash.yOnMap < getY0() + clusterLevel * (j + 1)) {
          count++;
          // inizio porcate
          if (colorFilter.equals("drug_involved")) {
            checker = crash.drug_involved;
          } else if (colorFilter.equals("alcohol_involved")) {
            checker = crash.alcohol_involved;
          } else if (colorFilter.equals("weather")) {
            if (crash.weather.equals("1_sunny"))
              checker = "Sunny";
            else if (crash.weather.equals("2_rainy") || crash.weather.equals("3_hail"))
              checker = "Rainy / Hail";
            else if (crash.weather.equals("4_snow"))
              checker = "Snow";
            else if (crash.weather.equals("5_fog") || crash.weather.equals("6_windy"))
              checker = "Foggy / Windy";
            else if (crash.weather.equals("7_cloudy"))
              checker = "Cloudy";
            else if (crash.weather.equals("8_unknown")) checker = "Unknown";
          } else if (colorFilter.equals("number_of_fatalities")) {
            if (crash.number_of_fatalities == 1)
              checker = "1";
            else if (crash.number_of_fatalities == 2)
              checker = "2";
            else if (crash.number_of_fatalities >= 2) checker = "3+";
          }
          boolean found = false;
          for (int q = 0; q < legend.getLabels().size() && found == false; q++) {

            String currentLabel = legend.getLabels().get(q);
            if (checker.equals(currentLabel)) {
              cluster.counters.set(q, cluster.counters.get(q) + 1);
              found = true;
            }
          }

          // fine porcate
          cluster.setCount(count);
          crash.setCluster(cluster);
        }
      }
    }
    cluster.countToPerc();
  }

  public void drawClusters() {
    pushStyle();
    int clusterLevel = (int) getWidth() / numberOfClusters;
    for (Cluster cluster : clusters) {
      if (cluster.getCount() > 1) {
        float dimension = PApplet.map(cluster.getCount(), 0, 50, 20, clusterLevel);
        if (dimension > clusterLevel * 0.9) {
          dimension = clusterLevel * 0.9f;
        }
        drawPieChart(cluster.getPercentages(), legend.getLegendColors(), dimension,
            cluster.getCenter().x, cluster.getCenter().y);
        if (colorFilter.equals("weather")) {
          fill(MyColorEnum.BLACK);
        } else {
          fill(MyColorEnum.WHITE);
        }
        textSize(10);
        textAlign(PConstants.CENTER, PConstants.CENTER);
        text(cluster.getCount() + "", cluster.getCenter().x, cluster.getCenter().y);
      }
    }
    popStyle();
  }

  public MyColorEnum colorBy(String filter, DSCrash crash) {
    if (filter.equals("alcohol_involved")) {
      if (crash.alcohol_involved.equals("no")) {
        return legend.getLegendColors().get(0);
      } else if (crash.alcohol_involved.equals("yes")) {
        return legend.getLegendColors().get(1);
      }
    } else if (filter.equals("drug_involved")) {
      if (crash.drug_involved.equals("no")) {
        return legend.getLegendColors().get(0);
      } else if (crash.drug_involved.equals("yes")) {
        return legend.getLegendColors().get(1);
      }
    } else if (filter.equals("weather")) {
      if (crash.weather.equals("sunny")) {
        return legend.getLegendColors().get(0);
      } else if (crash.weather.equals("cloudy")) {
        return legend.getLegendColors().get(1);
      } else if (crash.weather.equals("rainy") || crash.weather.equals("hail")) {
        return legend.getLegendColors().get(2);
      } else if (crash.weather.equals("snow")) {
        return legend.getLegendColors().get(3);
      } else if (crash.weather.equals("fog") || crash.weather.equals("windy")) {
        return legend.getLegendColors().get(4);
      }
    } else if (filter.equals("number_of_fatalities")) {

      if (crash.number_of_fatalities == 1) {
        return legend.getLegendColors().get(0);
      } else if (crash.number_of_fatalities == 2) {
        return legend.getLegendColors().get(1);
      } else if (crash.number_of_fatalities >= 3) {
        return legend.getLegendColors().get(2);
      }

    }
    return MyColorEnum.BLACK;
  }

  @Override
  public void eventReceived(EventName eventName, Object data) {
    if (eventName == EventName.CRASHES_UPDATED) {
      setAccidents(m.crashes);
      centerAndZoomOnState(m.currentStateCode);
      updateCorners();
      updateClusters();
    }
    if (eventName == EventName.BUTTON_TOUCHED) {
      if (data.toString().equals("zoomInButton")) {
        map.setZoom(map.getZoom() + 1);
        updateCorners();
        updateClusters();
      } else if (data.toString().equals("zoomOutButton")) {
        map.setZoom(map.getZoom() - 1);
        updateCorners();
        updateClusters();
      } else if (data.toString().equals("changeProviderButton")) {
        if (currentProvider == 3) {
          currentProvider = 1;
        } else {
          currentProvider++;
        }
        setProvider(currentProvider);
      }
    }
    if (eventName == EventName.CHANGED_MAP_COLOR_ATTRIBUTE) {
      this.colorFilter = data.toString();
      setupLegend();
      updateClusters();
    }

  }

  public void drawPieChart(ArrayList<Float> percentages, ArrayList<MyColorEnum> colors,
      float diameterr, float x, float y) {
    pushStyle();
    strokeWeight(0.3f);
    stroke(MyColorEnum.BLACK);
    ArrayList<Float> angles = new ArrayList<Float>();
    float centerX = x;
    float centerY = y;
    float lastAngle = 0;

    for (int i = 0; i < percentages.size(); i++) {
      angles.add(percentages.get(i) * 360);
    }
    float diameter = diameterr;

    for (int i = 0; i < angles.size(); i++) {
      fill(colors.get(i), 150f);
      arc(centerX, centerY, diameter, diameter, lastAngle, lastAngle + radians(angles.get(i)));
      lastAngle += radians(angles.get(i));
    }
    popStyle();
  }

}
