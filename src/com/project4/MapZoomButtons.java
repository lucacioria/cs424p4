package com.project4;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizButton;
import com.anotherbrick.inthewall.VizPanel;

public class MapZoomButtons extends VizPanel implements TouchEnabled {

  private VizButton zoomInButton;
  private VizButton zoomOutButton;

  public MapZoomButtons(float x0, float y0, VizPanel parent) {
    super(x0, y0, 20, 40, parent);
  }

  @Override
  public void setup() {
    setupZoomButtons();
  }

  public void setupZoomButtons() {
    zoomInButton = new VizButton(0, 0, 20, 20, this);
    zoomInButton.name = "zoomInButton";
    zoomInButton.text = "+";
    zoomInButton.setStyle(MyColorEnum.LIGHT_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY, 155f,
        155f, 10);
    zoomInButton.setStylePressed(MyColorEnum.MEDIUM_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY,
        155f, 10);
    addTouchSubscriber(zoomInButton);

    zoomOutButton = new VizButton(0, 20, 20, 20, this);
    zoomOutButton.name = "zoomOutButton";
    zoomOutButton.text = "-";
    zoomOutButton.setStyle(MyColorEnum.LIGHT_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY, 155f,
        155f, 10);
    zoomOutButton.setStylePressed(MyColorEnum.MEDIUM_GRAY, MyColorEnum.WHITE,
        MyColorEnum.DARK_GRAY, 155f, 10);
    addTouchSubscriber(zoomOutButton);
  }

  @Override
  public boolean draw() {
    zoomInButton.draw();
    zoomOutButton.draw();
    return false;
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    return propagateTouch(x, y, down, touchType);
  }

}
