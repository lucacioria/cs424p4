package com.example.app;

import java.util.ArrayList;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizList;
import com.anotherbrick.inthewall.VizList.SelectionMode;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;

class SelectorPanel extends VizPanel implements TouchEnabled, EventSubscriber {

  public SelectorPanel(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  private VizList list;
  public String panelName;
  private SelectorPanelData dataObj;

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    return propagateTouch(x, y, down, touchType);
  }

  @Override
  public void setup() {
    m.notificationCenter.registerToEvent(EventName.SELECTOR_PANEL_CLOSE, this);
    m.notificationCenter.registerToEvent(EventName.SELECTOR_PANEL_OPEN, this);
  }

  @SuppressWarnings("unchecked")
  private void setupVizList(SelectorPanelData data) {
    list = new VizList(0, 0, getWidth(), getHeight(), this);
    list.setup(MyColorEnum.LIGHT_GRAY, MyColorEnum.MEDIUM_GRAY, (int) (getHeight() / 20),
        data.values, false, SelectionMode.SINGLE);
    ArrayList<Object> attributeValues = (ArrayList<Object>) (ArrayList<? extends Object>) m.selectorPanelsState
        .get(data.name);
    if ((attributeValues != null && attributeValues.size() > 0)) {
      list.setSelected(new ArrayList<Object>(attributeValues));
    } else {
      // set default
      if (data.defaultValue != null && data.defaultValue.length() > 0) {
        list.selectElementByString(data.defaultValue);
      }
    }
    addTouchSubscriber(list);
  }

  @Override
  public boolean draw() {
    if (!isVisible()) return false;
    pushStyle();
    background(MyColorEnum.DARK_GRAY);
    list.draw();
    popStyle();
    return false;
  }

  @Override
  public void eventReceived(EventName eventName, Object data) {
    switch (eventName) {
    case SELECTOR_PANEL_OPEN:
      if (((SelectorPanelData) data).panelName.equals(panelName)) {
        dataObj = ((SelectorPanelData) data);
        setupVizList((SelectorPanelData) data);
        setVisible(true);
      }
      break;
    case SELECTOR_PANEL_CLOSE:
      if (((SelectorPanelData) data).panelName.equals(panelName)) {
        if (isVisible()) {
          setVisible(false);
          removeTouchSubscriber(list);
          updateFilter();
        }
      }
      break;
    default:
      break;
    }
  }

  private void updateFilter() {
    ArrayList<? extends Object> selected = list.getSelected();
    m.selectorPanelsState.put(dataObj.name, selected);
  }

}