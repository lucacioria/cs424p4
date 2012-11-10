package com.anotherbrick.inthewall.ScatterPlot;

import processing.core.PApplet;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizPanel;

public class VizScatterPlot extends VizPanel implements TouchEnabled {

  private ScatterPlotData data;
  public MyColorEnum backgroundColor = MyColorEnum.DARK_GRAY;
  public MyColorEnum chartBackgroundColor = MyColorEnum.MEDIUM_GRAY;
  public MyColorEnum pointColor = MyColorEnum.LIGHT_ORANGE;
  public MyColorEnum textColor = MyColorEnum.WHITE;
  public float pointRadius = -1;
  private VizScatterPlotChart chart;

  public VizScatterPlot(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    return propagateTouch(x, y, down, touchType);
  }

  public void setData(ScatterPlotData data) {
    this.data = data;
    setupChart();
  }

  @Override
  public boolean draw() {
    pushStyle();
    background(MyColorEnum.LIGHT_GREEN);
    drawTitle();
    drawYAxis();
    if (chart.data != null) {
      chart.draw();
    }
    popStyle();
    return false;
  }

  private void drawYAxis() {
    pushStyle();
    textAlign(PApplet.RIGHT, PApplet.CENTER);
    textSize(8);
    fill(MyColorEnum.WHITE);
    float max = data.getYMax();
    float min = data.getYMin();
    int numberOfTicks = 10;
    float tickValue = (max - min) / numberOfTicks;
    for (int i = 0; i < numberOfTicks; i++) {
      float y = chart.getY0() + chart.getHeight() - pointRadius
          - PApplet.map(i, 0, numberOfTicks - 1, 0, chart.getHeight() - 2 * pointRadius);
      String numberLabel = (int) (tickValue * i) + "";
      text(numberLabel, chart.getX0() - 5, y);
    }
    popStyle();
  }

  private void drawTitle() {
    if (data.title == null) return;
    pushStyle();
    textSize(15);
    fill(MyColorEnum.WHITE);
    textAlign(PApplet.CENTER, PApplet.TOP);
    text(data.title, chart.getX0() + chart.getWidth() / 2, 10);
    popStyle();
  }

  @Override
  public void setup() {
    if (pointRadius == -1) pointRadius = getWidth() / 20f;
    setupChart();
  }

  private void setupChart() {
    chart = new VizScatterPlotChart(50, 50, getWidth() - 70, getHeight() - 70, this);
    chart.backgroundColor = chartBackgroundColor;
    chart.pointColor = pointColor;
    chart.pointRadius = pointRadius;
    chart.data = data;
    chart.setup();
    addTouchSubscriber(chart);
  }

}
