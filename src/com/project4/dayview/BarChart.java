package com.project4.dayview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizPanel;
import com.anotherbrick.inthewall.BarChart.BarData;
import com.anotherbrick.inthewall.BarChart.VizBarChart;
import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.project4.datasource.Filter;

public class BarChart extends VizPanel implements TouchEnabled {

  private VizBarChart barChart;

  public BarChart(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  @Override
  public void setup() {
    setupBarChart();
  }
  
  public void setData(ArrayList<Filter> f, ArrayList<BarData> data) {
    for (int i = 0; i < f.size(); i++) {
      barChart.barColors[i] = f.get(i).getColor();
    }
    barChart.setData(data);
  }

  private void setupBarChart() {
    barChart = new VizBarChart(0, 0, getWidth(), getHeight(), this);
    barChart.setup();
    addTouchSubscriber(barChart);
  }

  @Override
  public boolean draw() {
    barChart.draw();
    return false;
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    return propagateTouch(x, y, down, touchType);
  }

}
