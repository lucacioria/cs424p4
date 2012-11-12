package com.project4.dayview;

import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;
import com.anotherbrick.inthewall.BarChart.VizBarChart;

public class BarChart extends VizPanel implements TouchEnabled, EventSubscriber {

  private VizBarChart barChart;

  public BarChart(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  @Override
  public void setup() {
    setupBarChart();
  }

  private void setupBarChart() {
    barChart = new VizBarChart(0, 0, getWidth(), getHeight(), this);
  }

  @Override
  public boolean draw() {
    barChart.draw();
    return false;
  }

  @Override
  public void eventReceived(EventName eventName, Object data) {
  }
  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    return propagateTouch(x, y, down, touchType);
  }

}
