package com.project4.dayview;

import java.util.ArrayList;

import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.BarChart.BarData;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;
import com.project4.datasource.Day;

public class DayView extends VizPanel implements TouchEnabled, EventSubscriber {

  private BarChart barChart;

  public DayView(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void eventReceived(EventName eventName, Object data) {
    if (eventName == EventName.DAYS_UPDATED) {
      ArrayList<Day> days = (ArrayList<Day>) data;
      ArrayList<BarData> barChartData = new ArrayList<BarData>();
      for (Day d : days) {
        BarData b = new BarData();
        b.label = d.toString();
        b.values = d.getSortedCounts();
        barChartData.add(b);
      }
      barChart.setData(days.get(0).getFilters(), barChartData);
    }
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    return propagateTouch(x, y, down, touchType);
  }

  @Override
  public void setup() {
    setupBarChart();
    m.notificationCenter.registerToEvent(EventName.DAYS_UPDATED, this);
  }

  private void setupBarChart() {
    barChart = new BarChart(0, 0, getWidth(), getHeight(), this);
    barChart.setup();
    addTouchSubscriber(barChart);
  }

  @Override
  public boolean draw() {
    barChart.draw();
    return false;
  }

}
