package com.project4.map;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizButton;
import com.anotherbrick.inthewall.VizPanel;

public class PanButtons extends VizPanel implements TouchEnabled {

  private VizButton panUpButton;
  private VizButton panDownButton;
  private VizButton panLeftButton;
  private VizButton panRightButton;

  public static final float height = 60;
  public static final float width = 60;
  
  public PanButtons(float x0, float y0, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  @Override
  public void setup() {
    setupPanButtons();
  }

  public void setupPanButtons() {
    panUpButton = new VizButton(20, 0, 20, 20, this);
    panUpButton.name = "panUpButton";
    panUpButton.text = "\u2191";
    panUpButton.repeatRate = 250;
    panUpButton.setStyle(MyColorEnum.LIGHT_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY, 155f,
        155f, 12);
    panUpButton.setStylePressed(MyColorEnum.MEDIUM_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY,
        155f, 12);
    addTouchSubscriber(panUpButton);
    addChild(panUpButton);

    panDownButton = new VizButton(20, 40, 20, 20, this);
    panDownButton.name = "panDownButton";
    panDownButton.text = "\u2193";
    panDownButton.repeatRate = 250;
    panDownButton.setStyle(MyColorEnum.LIGHT_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY, 155f,
      155f, 12);
    panDownButton.setStylePressed(MyColorEnum.MEDIUM_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY,
      155f, 12);
    addTouchSubscriber(panDownButton);
    addChild(panDownButton);

    panRightButton = new VizButton(40, 20, 20, 20, this);
    panRightButton.name = "panRightButton";
    panRightButton.text = "\u2192";
    panRightButton.repeatRate = 250;
    panRightButton.setStyle(MyColorEnum.LIGHT_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY, 155f,
      155f, 12);
    panRightButton.setStylePressed(MyColorEnum.MEDIUM_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY,
      155f, 12);
    addTouchSubscriber(panRightButton);
    addChild(panRightButton);
    
    panLeftButton = new VizButton(0, 20, 20, 20, this);
    panLeftButton.name = "panLeftButton";
    panLeftButton.text = "\u2190";
    panLeftButton.repeatRate = 250;
    panLeftButton.setStyle(MyColorEnum.LIGHT_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY, 155f,
      155f, 12);
    panLeftButton.setStylePressed(MyColorEnum.MEDIUM_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY,
      155f, 12);
    addTouchSubscriber(panLeftButton);
    addChild(panLeftButton);
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
