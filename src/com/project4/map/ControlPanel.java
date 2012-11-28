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
  private FilterButtons filterButtons;

  @Override
  public void setup() {
    setupZoomButtons();
    setupPanButtons();
    setupLayerButtons();
    setupFilterButtons();
    m.notificationCenter.registerToEvent(EventName.BUTTON_TOUCHED, this);
    m.notificationCenter.registerToEvent(EventName.TOGGLE_CONTROL_PANEL, this);
  }

  @Override
  public boolean draw() {
    zoomButtons.draw();
    panButtons.draw();
    layerButtons.draw();
    filterButtons.draw();
    return false;
  }

  private void setupZoomButtons() {
    zoomButtons =
        new ZoomButtons(getWidth() - 70, getHeight() - ZoomButtons.height - padding, this);
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
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    return propagateTouch(x, y, down, touchType);
  }

}
