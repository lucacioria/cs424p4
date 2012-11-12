package com.project4.map;

import java.util.ArrayList;
import java.util.Arrays;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.Helper;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;
import com.project4.datasource.Tweet;

public class HeatMap extends VizPanel implements TouchEnabled, EventSubscriber {

  private ArrayList<Tweet> tweets = new ArrayList<Tweet>();
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
    m.notificationCenter.registerToEvent(EventName.BUTTON_TOUCHED, this);
    gridSetup();
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
    float alpha = 150;
    for (int j = 0; j < grid.length; j++) {
      for (int i = 0; i < grid[j].length; i++) {
        if (grid[j][i] > maxCount / 2)
          fill(MyColorEnum.DARK_BLUE, alpha);
        else if (grid[j][i] > maxCount / 10)
          fill(MyColorEnum.RED, alpha);
        else if (grid[j][i] > maxCount / 20)
          fill(MyColorEnum.LIGHT_ORANGE, alpha);
        else if (grid[j][i] > maxCount / 30)
          fill(MyColorEnum.YELLOW, alpha);
        else
          noFill();
        rect(i * gridW, j * gridH, gridW, gridH);
      }
    }
  }

  private void drawGrid2() {
    float alpha = 150;
    for (int j = 0; j < grid.length; j++) {
      for (int i = 0; i < grid[j].length; i++) {
        if (grid[j][i] < 5) continue;
        fill(m.p.lerpColor(Helper.hex2Rgb("#ff0000", m.p), Helper.hex2Rgb("#00ff00", m.p),
            map(grid[j][i], 0, maxCount, 0, 1)));
        rect(i * gridW, j * gridH, gridW, gridH);
      }
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public void eventReceived(EventName eventName, Object data) {
    if (eventName == EventName.DATA_UPDATED) {
      this.tweets = (ArrayList<Tweet>) data;
      gridUpdate();
    }
    if (eventName == EventName.MAP_ZOOMED_OR_PANNED) {
      gridUpdate();
    }
    if (eventName == EventName.BUTTON_TOUCHED && data.toString().equals("heatmapButton")) {
      toggleVisible();
    }
  }

  private void gridUpdate() {
    gridInit();
    int x, y;
    for (Tweet t : tweets) {
      if (map.isVisible(t)) {
        x =
            (int) ((t.getLon() - map.getMinLon()) / (map.getMaxLon() - map.getMinLon()) * gridSizeX);
        y =
            (int) ((t.getLat() - map.getMinLat()) / (map.getMaxLat() - map.getMinLat()) * gridSizeY);
        increaseValue(x, y);
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
    int square = 31;
    int incr;
    int finX, finY;
    for (int j = 0; j < square; j++) {
      for (int i = 0; i < square; i++) {
        finX = (int) (x - Math.floor(square / 2) + i);
        finY = (int) (y - Math.floor(square / 2) + j);
        if (finX > 0 && finX < gridSizeX && finY > 0 && finY < gridSizeY) {
          incr = Math.max(Math.abs(x - finX), Math.abs(y - finY));
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
