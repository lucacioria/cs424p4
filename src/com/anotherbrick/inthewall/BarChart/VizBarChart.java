package com.anotherbrick.inthewall.BarChart;

import java.util.ArrayList;

import processing.core.PApplet;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.Helper;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizPanel;

public class VizBarChart extends VizPanel implements TouchEnabled {

  public String title;
  private ArrayList<BarData> data;
  public MyColorEnum backgroundColor = MyColorEnum.DARK_GRAY;
  public MyColorEnum[] barColors = {MyColorEnum.LIGHT_BLUE, MyColorEnum.LIGHT_ORANGE, MyColorEnum.LIGHT_GREEN, MyColorEnum.LIGHT_GREEN, MyColorEnum.LIGHT_GREEN};
  public MyColorEnum textColor = MyColorEnum.WHITE;

  private float chartXLeft, chartXRight, chartYBottom, chartYTop, chartWidth, chartHeight;
  private ArrayList<VizBar> bars = new ArrayList<VizBar>();

  public VizBarChart(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    return propagateTouch(x, y, down, touchType);
  }

  @Override
  public boolean draw() {
    pushStyle();
    background(backgroundColor);
    drawBars();
    drawTitle();
    drawYAxis();
    popStyle();
    return false;
  }
  
  public void setData(ArrayList<BarData> data) {
    this.data = data;
    updateBars();
  }

  private void drawYAxis() {
    pushStyle();
    textAlign(PApplet.RIGHT, PApplet.CENTER);
    textSize(8);
    fill(MyColorEnum.WHITE);
    float max = getMaxValue();
    int numberOfTicks = 10;
    float tickValue = max / numberOfTicks;
    for (int i = 0; i < numberOfTicks; i++) {
      float y = chartYBottom - PApplet.map(i, 0, numberOfTicks - 1, 0, chartHeight);
      String numberLabel = "";
      if ((tickValue * i) > 10) {
        numberLabel = Helper.floatToString((tickValue * i), 0, true);
      } else if ((tickValue * i) > 1) {
        numberLabel = Helper.floatToString((tickValue * i), 1, true);
      } else {
        numberLabel = Helper.floatToString((tickValue * i), 3, true);
      }
      text(numberLabel, chartXLeft - 5, y);
    }
    popStyle();
  }

  private void drawTitle() {
    if (title == null) return;
    pushStyle();
    textSize(15);
    fill(MyColorEnum.WHITE);
    textAlign(PApplet.CENTER, PApplet.TOP);
    text(title, chartXLeft + chartWidth / 2, 10);
    popStyle();
  }

  private void drawBars() {
    for (VizBar bar : bars) {
      bar.draw();
    }
  }

  @Override
  public void setup() {
    setupChartLocation();
    updateBars();
  }

  private void setupChartLocation() {
    chartXLeft = 50;
    chartXRight = getWidth() - 20;
    chartYTop = 10;
    chartYBottom = getHeight() - 30;
    chartWidth = chartXRight - chartXLeft;
    chartHeight = chartYBottom - chartYTop;
  }

  private void updateBars() {
    bars.clear();
    if (data == null) return;
    int n = data.size();
    float barWidth = chartWidth / n;
    float maxValue = getMaxValue();
    for (int i = 0; i < n; i++) {
      BarData barData = data.get(i);
      float x0 = chartXLeft + barWidth * i;
      VizBar bar = new VizBar(x0, chartYTop, barWidth, chartHeight, this);
      bar.barData = barData;
      bar.barColors = barColors;
      bar.maxValue = maxValue;
      bar.setup();
      addTouchSubscriber(bar);
      bars.add(bar);
    }
  }

  private float getMaxValue() {
    float max = Float.MIN_VALUE;
    for (BarData barData : data) {
      for (int i = 0; i < barData.values.length; i++) {        
        if (barData.values[i] > max) {
          max = barData.values[i];
        }
      }
    }
    return max;
  }
}
