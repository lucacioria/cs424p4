package com.example.app;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizButton;
import com.anotherbrick.inthewall.VizPanel;

public class MapButtons2 extends VizPanel implements TouchEnabled {

  private VizButton selectColorByButton;
  private VizButton changeProviderButton;
  public static float width = 100;
  public static float height = 20;

  public MapButtons2(float x0, float y0, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  @Override
  public void setup() {
    setupZoomButtons();
  }

  public void setupZoomButtons() {
    selectColorByButton = new VizButton(0, 0, 50, 20, this);
    selectColorByButton.name = "selectColorByButton";
    selectColorByButton.text = "color by";
    selectColorByButton.setStyle(MyColorEnum.LIGHT_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY,
        255f, 255f, 10);
    selectColorByButton.setStylePressed(MyColorEnum.MEDIUM_GRAY, MyColorEnum.WHITE,
        MyColorEnum.DARK_GRAY, 255f, 10);
    addTouchSubscriber(selectColorByButton);

    changeProviderButton = new VizButton(50, 0, 50, 20, this);
    changeProviderButton.name = "changeProviderButton";
    changeProviderButton.text = "Style";
    changeProviderButton.setStyle(MyColorEnum.LIGHT_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY,
        255f, 255f, 10);
    changeProviderButton.setStylePressed(MyColorEnum.MEDIUM_GRAY, MyColorEnum.WHITE,
        MyColorEnum.DARK_GRAY, 255f, 10);
    addTouchSubscriber(changeProviderButton);
  }

  @Override
  public boolean draw() {
    selectColorByButton.draw();
    changeProviderButton.draw();
    return false;
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    return propagateTouch(x, y, down, touchType);
  }

}
