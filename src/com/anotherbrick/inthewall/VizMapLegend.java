package com.anotherbrick.inthewall;

import java.util.ArrayList;

import processing.core.PConstants;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.datasource.DSFilter;

public class VizMapLegend extends VizPanel implements TouchEnabled {
  private String colorFilter = "";
  private ArrayList<MyColorEnum> legendColors;
  private ArrayList<String> labels;

  public VizMapLegend(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  public void setup() {

    legendColors = new ArrayList<MyColorEnum>();
    labels = new ArrayList<String>();
    legendByFilter();

  }

  public boolean draw() {
    pushStyle();
    fill(MyColorEnum.BLACK);
    rect(0, 0, getWidth(), getHeight());
    textSize(12);
    fill(MyColorEnum.WHITE);
    text("Color coding by: " + (DSFilter.getLabelByName(colorFilter)).toUpperCase(), 5, 15);
    int legendColumn = 1;

    // for(float i=getHeight()/3+5;i<getHeight();i=i+getHeight()/3){
    for (int i = 1; i <= legendColors.size(); i++) {

      fill(legendColors.get(i - 1));
      stroke(MyColorEnum.WHITE);
      strokeWeight(0.5f);
      rectMode(PConstants.CENTER);
      rect(70 + 95 * (legendColumn - 1), (i + 1) % 2 * getHeight() / 3 + 30, 10, 10);
      fill(MyColorEnum.WHITE);
      textAlign(PConstants.LEFT, PConstants.CENTER);
      textSize(10);
      text(labels.get(i - 1), 80 + 95 * (legendColumn - 1), (i + 1) % 2 * getHeight() / 3 + 30);
      if (i % 2 == 0) {
        legendColumn++;
      }
    }

    popStyle();
    return false;

  }

  public String getColorFilter() {
    return colorFilter;
  }

  public void setColorFilter(String colorFilter) {
    this.colorFilter = colorFilter;
  }

  public void legendByFilter() {
    if (colorFilter.equals("weather")) {
      legendColors.clear();
      labels.clear();

      labels.add("Sunny");
      legendColors.add(MyColorEnum.YELLOW);

      labels.add("Cloudy");
      legendColors.add(MyColorEnum.LIGHT_BLUE);

      labels.add("Rainy / Hail");
      legendColors.add(MyColorEnum.DARK_BLUE);

      labels.add("Snow");
      legendColors.add(MyColorEnum.WHITE);

      labels.add("Foggy / Windy");
      legendColors.add(MyColorEnum.LIGHT_GRAY);

      labels.add("Unknown");
      legendColors.add(MyColorEnum.BLACK);
    }

    else if (colorFilter.equals("alcohol_involved") || colorFilter.equals("drug_involved")) {
      legendColors.clear();
      labels.clear();

      labels.add("no");
      legendColors.add(MyColorEnum.RED);

      labels.add("yes");
      legendColors.add(MyColorEnum.LIGHT_GREEN);

      labels.add("unknown");
      legendColors.add(MyColorEnum.BLACK);
    }

    else if (colorFilter.equals("number_of_fatalities")) {
      legendColors.clear();
      labels.clear();

      labels.add("1");
      legendColors.add(MyColorEnum.YELLOW);

      labels.add("2");
      legendColors.add(MyColorEnum.LIGHT_ORANGE);

      labels.add("3+");
      legendColors.add(MyColorEnum.RED);

      labels.add("unknown");
      legendColors.add(MyColorEnum.BLACK);
    }

  }

  public ArrayList<MyColorEnum> getLegendColors() {
    return legendColors;
  }

  public void setLegendColors(ArrayList<MyColorEnum> legendColors) {
    this.legendColors = legendColors;
  }

  public ArrayList<String> getLabels() {
    return labels;
  }

  public void setLabels(ArrayList<String> labels) {
    this.labels = labels;
  }

  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    return false;
  }
}
