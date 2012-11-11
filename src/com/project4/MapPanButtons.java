package com.project4;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizButton;
import com.anotherbrick.inthewall.VizPanel;

public class MapPanButtons extends VizPanel implements TouchEnabled {

  private VizButton panUpButton;
  private VizButton panDownButton;
  private VizButton panLeftButton;
  private VizButton panRightButton;

  public MapPanButtons(float x0, float y0, VizPanel parent) {
    super(x0, y0, 60, 60, parent);
  }

  @Override
  public void setup() {
    setupZoomButtons();
  }

  public void setupZoomButtons() {
    panUpButton = new VizButton(20, 0, 20, 20, this);
    panUpButton.name = "panUpButton";
    panUpButton.text = "\u2191";
    panUpButton.setStyle(MyColorEnum.LIGHT_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY, 155f,
        155f, 12);
    panUpButton.setStylePressed(MyColorEnum.MEDIUM_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY,
        155f, 12);
    addTouchSubscriber(panUpButton);

    panDownButton = new VizButton(20, 40, 20, 20, this);
    panDownButton.name = "panDownButton";
    panDownButton.text = "\u2193";
    panDownButton.setStyle(MyColorEnum.LIGHT_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY, 155f,
      155f, 12);
    panDownButton.setStylePressed(MyColorEnum.MEDIUM_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY,
      155f, 12);
    addTouchSubscriber(panDownButton);

    panRightButton = new VizButton(40, 20, 20, 20, this);
    panRightButton.name = "panRightButton";
    panRightButton.text = "\u2192";
    panRightButton.setStyle(MyColorEnum.LIGHT_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY, 155f,
      155f, 12);
    panRightButton.setStylePressed(MyColorEnum.MEDIUM_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY,
      155f, 12);
    addTouchSubscriber(panRightButton);
    
    panLeftButton = new VizButton(0, 20, 20, 20, this);
    panLeftButton.name = "panLeftButton";
    panLeftButton.text = "\u2190";
    panLeftButton.setStyle(MyColorEnum.LIGHT_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY, 155f,
      155f, 12);
    panLeftButton.setStylePressed(MyColorEnum.MEDIUM_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY,
      155f, 12);
    addTouchSubscriber(panLeftButton);
  }

  @Override
  public boolean draw() {
    panDownButton.draw();
    panUpButton.draw();
    panLeftButton.draw();
    panRightButton.draw();
    return false;
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    return propagateTouch(x, y, down, touchType);
  }

}
