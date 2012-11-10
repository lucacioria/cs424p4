package com.example.app;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizButton;
import com.anotherbrick.inthewall.VizPanel;

public class BarChartButtons extends VizPanel implements TouchEnabled {

  public int barChartNumber = 1;
  private VizButton selectXButton;
  private VizButton selectStateButton;
  private static float height = 20;
  static float width = 100;
  private float buttonWidth = 50;

  public BarChartButtons(float x0, float y0, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  @Override
  public void setup() {
    setupZoomButtons();
  }

  public void setupZoomButtons() {
    selectXButton = new VizButton(0, 0, buttonWidth, 20, this);
    selectXButton.name = "barchartSelectXButton" + barChartNumber;
    selectXButton.text = "X Axis";
    selectXButton.setStyle(MyColorEnum.LIGHT_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY, 255f,
        255f, 10);
    selectXButton.setStylePressed(MyColorEnum.MEDIUM_GRAY, MyColorEnum.WHITE,
        MyColorEnum.DARK_GRAY, 255f, 10);
    addTouchSubscriber(selectXButton);
    if (barChartNumber == 2) {
      selectStateButton = new VizButton(50, 0, buttonWidth, 20, this);
      selectStateButton.name = "barchartSelectStateButton" + barChartNumber;
      selectStateButton.text = "State";
      selectStateButton.setStyle(MyColorEnum.LIGHT_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY,
          255f, 255f, 10);
      selectStateButton.setStylePressed(MyColorEnum.MEDIUM_GRAY, MyColorEnum.WHITE,
          MyColorEnum.DARK_GRAY, 255f, 10);
      addTouchSubscriber(selectStateButton);
    }
  }

  @Override
  public boolean draw() {
    selectXButton.draw();
    if (barChartNumber == 2) {
      selectStateButton.draw();
    }
    return false;
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    return propagateTouch(x, y, down, touchType);
  }

}
