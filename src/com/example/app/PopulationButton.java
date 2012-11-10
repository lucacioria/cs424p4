package com.example.app;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizButton;
import com.anotherbrick.inthewall.VizPanel;

public class PopulationButton extends VizPanel implements TouchEnabled {

  private VizButton populationButton;
  public static float width = 70;
  public static float height = 20;

  public PopulationButton(float x0, float y0, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  @Override
  public void setup() {
    setupZoomButtons();
  }

  public void setupZoomButtons() {
    populationButton = new VizButton(0, 0, 70, 20, this);
    populationButton.name = "populationButton";
    populationButton.text = "Per Capita";
    populationButton.setStyle(MyColorEnum.LIGHT_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY,
        255f, 255f, 10);
    populationButton.setStylePressed(MyColorEnum.MEDIUM_GRAY, MyColorEnum.WHITE,
        MyColorEnum.DARK_GRAY, 255f, 10);
    addTouchSubscriber(populationButton);
  }

  @Override
  public boolean draw() {
    populationButton.draw();
    return false;
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    return propagateTouch(x, y, down, touchType);
  }

}
