package com.project4.map;

import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;

public class ControlPanel extends VizPanel implements TouchEnabled, EventSubscriber {

  public ControlPanel(float x0, float y0, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  private ZoomButtons zoomButtons;
  private PanButtons panButtons;
  private LayerButtons layerButtons;

  public static final float width = 80f;
  public static final float height = 210f;
  private float padding = 10;
  private boolean hidden = false;
  private boolean animating = false;
  private int startTime;
  private float originalY0;
  private float animationDuration = 5000;
  private FilterButtons filterButtons;

  @Override
  public void setup() {
    originalY0 = getY0();
    setupZoomButtons();
    setupPanButtons();
    setupLayerButtons();
    setupFilterButtons();
    m.notificationCenter.registerToEvent(EventName.BUTTON_TOUCHED, this);
    m.notificationCenter.registerToEvent(EventName.TOGGLE_CONTROL_PANEL, this);
  }

  @Override
  public boolean draw() {
    updatePosition();
    zoomButtons.draw();
    panButtons.draw();
    layerButtons.draw();
    filterButtons.draw();
    return false;
  }

  private void updatePosition() {
    if (animating) {
      if (hidden) { // showing animation

      } else { // hiding animation
        move(0, 500 / m.p.frameRate);
        log("new position: " + getY0());
        if (getY0() >= originalY0 + getHeight() - 30) {
          log("finished transition!");
          animating = false;
          hidden = true;
        }
      }
    }
  }

  private void setupZoomButtons() {
    zoomButtons =
        new ZoomButtons(getWidth() - 60, getHeight() - ZoomButtons.height - padding, this);
    addTouchSubscriber(zoomButtons);
    addChild(zoomButtons);
    zoomButtons.setup();
  }

  private void setupPanButtons() {
    panButtons =
        new PanButtons(getWidth() - 80, zoomButtons.getY0() - PanButtons.height - padding, this);
    addTouchSubscriber(panButtons);
    addChild(panButtons);
    panButtons.setup();
  }

  private void setupLayerButtons() {
    layerButtons =
        new LayerButtons(getWidth() - 80, panButtons.getY0() - LayerButtons.height - padding, this);
    addTouchSubscriber(layerButtons);
    addChild(layerButtons);
    layerButtons.setup();
  }

  private void setupFilterButtons() {
    filterButtons =
        new FilterButtons(getWidth() - 80, layerButtons.getY0() - FilterButtons.height - padding, this);
    addTouchSubscriber(filterButtons);
    addChild(filterButtons);
    filterButtons.setup();
  }
  
  @Override
  public void eventReceived(EventName eventName, Object data) {
    if (eventName == EventName.TOGGLE_CONTROL_PANEL) {
      if (animating) return;
      if (hidden) {
        startShowAnimation();
      } else {
        startHideAnimation();
      }
    }
  }

  private void startHideAnimation() {
    log("start hiding animation");
    startTime = millis();
    animating = true;
  }

  private void startShowAnimation() {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    return propagateTouch(x, y, down, touchType);
  }

}
