package com.example.app;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizButton;
import com.anotherbrick.inthewall.VizPanel;

public class EventButtons extends VizPanel implements TouchEnabled {

  private VizButton selectEvent;
  public static float width = 50;
  public static float height = 20;

  public EventButtons(float x0, float y0, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  @Override
  public void setup() {
    setupZoomButtons();
  }

  public void setupZoomButtons() {
    selectEvent = new VizButton(0, 0, 50, 20, this);
    selectEvent.name = "selectEventButton";
    selectEvent.text = "events";
    selectEvent.setStyle(MyColorEnum.LIGHT_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY, 255f,
        255f, 10);
    selectEvent.setStylePressed(MyColorEnum.MEDIUM_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY,
        255f, 10);
    addTouchSubscriber(selectEvent);
  }

  @Override
  public boolean draw() {
    selectEvent.draw();
    return false;
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    return propagateTouch(x, y, down, touchType);
  }

}
