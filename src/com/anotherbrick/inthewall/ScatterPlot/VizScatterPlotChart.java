package com.anotherbrick.inthewall.ScatterPlot;

import processing.core.PApplet;
import processing.core.PVector;

import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizPanel;
import com.anotherbrick.inthewall.Config.MyColorEnum;

public class VizScatterPlotChart extends VizPanel implements TouchEnabled {

  public MyColorEnum backgroundColor;
  public MyColorEnum pointColor;
  public ScatterPlotData data;
  public float pointRadius;

  public VizScatterPlotChart(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    return propagateTouch(x, y, down, touchType);
  }

  @Override
  public void setup() {
  }

  @Override
  public boolean draw() {
    background(backgroundColor);
    drawPoints();
    drawXAxis();
    return false;
  }

  public void drawXAxis() {
    pushStyle();
    textAlign(PApplet.CENTER, PApplet.TOP);
    textSize(8);
    fill(MyColorEnum.WHITE);
    float max = data.getXMax();
    float min = data.getXMin();
    int numberOfTicks = 10;
    float tickValue = (max - min) / numberOfTicks;
    for (int i = 0; i < numberOfTicks; i++) {
      float x = pointRadius + PApplet.map(i, 0, numberOfTicks - 1, 0, getWidth() - 2 * pointRadius);
      String numberLabel = (int) (tickValue * i) + "";
      text(numberLabel, x, getHeight() + 5);
    }
    popStyle();
  }

  private void drawPoints() {
    if (data == null) return;
    pushStyle();
    fill(pointColor, 150f);
    noStroke();
    for (PVector point : data.getPoints()) {
      float x = PApplet.map(point.x, data.getXMin(), data.getXMax(), 0 + pointRadius, getWidth()
          - pointRadius);
      float y = PApplet.map(point.y, data.getYMin(), data.getYMax(), getHeight() - pointRadius,
          0 + pointRadius);
      ellipse(x, y, pointRadius * 2, pointRadius * 2);
    }
    popStyle();
  }
}
