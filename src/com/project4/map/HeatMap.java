package com.project4.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeMap;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;
import com.project4.datasource.Filter;
import com.project4.datasource.Tweet;

public class HeatMap extends VizPanel implements TouchEnabled, EventSubscriber {

  private TreeMap<Filter, ArrayList<Tweet>> tweets = new TreeMap<Filter, ArrayList<Tweet>>();
  private Map map;
  private int[][] grid;
  private int gridSizeX, gridSizeY;
  private float gridW, gridH;
  private int maxCount;

  public HeatMap(float x0, float y0, float width, float height, Map parent) {
    super(x0, y0, width, height, parent);
    this.map = parent;
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    return false;
  }

  @Override
  public void setup() {
    m.notificationCenter.registerToEvent(EventName.DATA_UPDATED, this);
    m.notificationCenter.registerToEvent(EventName.MAP_ZOOMED_OR_PANNED, this);
    gridSetup();
    setVisible(false);
  }

  private void gridSetup() {
    gridSizeY = 400;
    gridSizeX = 800;
    gridW = getWidth() / gridSizeX;
    gridH = getHeight() / gridSizeY;
    grid = new int[gridSizeY][gridSizeX];
  }

  @Override
  public boolean draw() {
    if (!isVisible()) return false;
    pushStyle();
    noStroke();
    drawGrid();
    popStyle();
    return false;
  }

  private void drawGrid() {
    float alpha = 255;
    for (int j = 0; j < grid.length; j++) {
      for (int i = 0; i < grid[j].length; i++) {
        if (grid[j][i] > maxCount / 1.5)
          fill(MyColorEnum.HEAT_MAP_4, alpha*0.9f);
        else if (grid[j][i] > maxCount / 2)
          fill(MyColorEnum.HEAT_MAP_3, alpha*0.8f);
        else if (grid[j][i] > maxCount / 5)
          fill(MyColorEnum.HEAT_MAP_2, alpha*0.7f);
        else if (grid[j][i] > maxCount / 10)
          fill(MyColorEnum.HEAT_MAP_1, alpha*0.6f);
        else
          noFill();
        rect(i * gridW, j * gridH, gridW, gridH);
      }
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public void eventReceived(EventName eventName, Object data) {
    if (eventName == EventName.DATA_UPDATED) {
      this.tweets = (TreeMap<Filter, ArrayList<Tweet>>) data;
      gridUpdate();
    }
    if (eventName == EventName.MAP_ZOOMED_OR_PANNED) {
      gridUpdate();
    }
  }

  private void gridUpdate() {
    gridInit();
    int x, y;
    Iterator<Filter> i = tweets.keySet().iterator();
    while (i.hasNext()) {
      Filter key = i.next();
      for (Tweet t : tweets.get(key)) {
        if (map.isVisible(t)) {
          x =
              (int) ((t.getLon() - map.getMinLon()) / (map.getMaxLon() - map.getMinLon()) * gridSizeX);
          y =
              (int) ((t.getLat() - map.getMinLat()) / (map.getMaxLat() - map.getMinLat()) * gridSizeY);
          increaseValue(x, y);
        }
      }
    }
    updateMax();
  }

  private void updateMax() {
    maxCount = Integer.MIN_VALUE;
    for (int j = 0; j < gridSizeY; j++) {
      for (int i = 0; i < gridSizeX; i++) {
        if (grid[j][i] > maxCount) maxCount = grid[j][i];
      }
    }
  }

  private void increaseValue(int x, int y) {
    int square = 41;
    int incr;
    int finX, finY;
    for (int j = 0; j < square; j++) {
      for (int i = 0; i < square; i++) {
        finX = (int) (x - Math.floor(square / 2) + i);
        finY = (int) (y - Math.floor(square / 2) + j);
        if (finX > 0 && finX < gridSizeX && finY > 0 && finY < gridSizeY) {
          //incr = Math.max(Math.abs(x - finX), Math.abs(y - finY));
          incr = (int) Math.floor(Math.sqrt((Math.pow((x - finX), 2.0) + (int) (Math.pow((y - finY), 2.0)))));
          grid[finY][finX] += (Math.floor(square / 2) - incr + 1);
        }
      }
    }
    @SuppressWarnings("unused")
    float a = 2;
  }


  private void gridInit() {
    for (int[] row : grid)
      Arrays.fill(row, 0);
  }



}
