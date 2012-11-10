package com.example.app;

import java.util.ArrayList;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizList;
import com.anotherbrick.inthewall.VizList.SelectionMode;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;

class FilterList extends VizPanel implements TouchEnabled, EventSubscriber {

  public FilterList(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  private VizList list;
  public String name;

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    return propagateTouch(x, y, down, touchType);
  }

  @Override
  public void setup() {
    m.notificationCenter.registerToEvent(EventName.FILTER_LIST_CLOSE, this);
    m.notificationCenter.registerToEvent(EventName.FILTER_LIST_OPEN, this);
  }

  @SuppressWarnings("unchecked")
  private void setupVizList(FilterRow row) {
    list = new VizList(0, 0, getWidth(), getHeight(), this);
    list.setup(MyColorEnum.LIGHT_GRAY, MyColorEnum.MEDIUM_GRAY, 10, row.values, false,
        row.selectMultiple ? SelectionMode.MULTIPLE : SelectionMode.SINGLE);
    ArrayList<Object> attributeValues = (ArrayList<Object>) (ArrayList<? extends Object>) m.currentFilter
        .getAttributeValues(row.name);
    if (attributeValues.size() > 0 || row.defaultValue == null) {
      list.setSelected(new ArrayList<Object>(attributeValues));
    } else {
      // set default
      if (row.defaultValue != null && row.defaultValue.length() > 0) {
        list.selectElementByString(row.defaultValue);
      }
    }
    name = row.name;
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
    case FILTER_LIST_OPEN:
      setupVizList((FilterRow) data);
      setVisible(true);
      break;
    case FILTER_LIST_CLOSE:
      if (isVisible()) {
        setVisible(false);
        removeTouchSubscriber(list);
        updateFilter();
      }
      break;
    default:
      break;
    }
  }

  private void updateFilter() {
    ArrayList<? extends Object> selected = list.getSelected();
    m.currentFilter.setAttributeWithList(name, selected);
  }
}