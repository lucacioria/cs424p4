package com.anotherbrick.inthewall.BarChart;

import processing.core.PApplet;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizPanel;

public class VizBar extends VizPanel implements TouchEnabled {

  public VizBar(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  public BarData barData;
  public float maxValue;
  public MyColorEnum[] barColors;
  public MyColorEnum textColor = MyColorEnum.WHITE;

  private float paddingPercentage = 0.1f;

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    log("bar touched: " + barData.label);
    return true;
  }

  @Override
  public boolean draw() {
    pushStyle();
    // set styles
    noStroke();
    // rect
    int n = barData.values.length;
    float xLeft = getWidth() * paddingPercentage;
    float xRight = getWidth() - getWidth() * paddingPercentage;
    float yBottom = getHeight();
    for (int i = 0; i < n; i++) {
      fill(barColors[i]);
      float xLeftBar = xLeft + (xRight - xLeft) / n * i;
      float xRightBar = xLeftBar + (xRight - xLeft) / n;
      float yTop = yBottom - (barData.values[i] / maxValue) * getHeight();      
      rect(xLeftBar, yTop, xRightBar - xLeftBar, yBottom - yTop);
    }
    // label
    drawLabel();
    popStyle();
    return false;
  }

  private void drawLabel() {
    fill(textColor);
    float xLeft = getWidth() * paddingPercentage;
    float xRight = getWidth() - getWidth() * paddingPercentage;
    float yBottom = getHeight();
    textSize(8);
    textAlign(PApplet.CENTER, PApplet.TOP);
    text(barData.label, xLeft + (xRight - xLeft) / 2, yBottom + 3);
  }

  @Override
  public void setup() {
  }

}
