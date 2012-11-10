package com.example.app;

import java.util.ArrayList;
import java.util.HashMap;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizButton;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;
import com.anotherbrick.inthewall.datasource.DSFilter;

public class FilterBox extends VizPanel implements TouchEnabled, EventSubscriber {

  private ArrayList<FilterRow> filterRows;
  private FilterList filterList;
  private VizButton submitButton;

  public FilterBox(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  @Override
  public void setup() {
    setupFilterRows();
    setupFilterList();
    m.notificationCenter.registerToEvent(EventName.BUTTON_TOUCHED, this);
    setupSubmitButton();

  }

  private void setupSubmitButton() {
    submitButton = new VizButton(10, getHeight() - 30, 50, 20, this);
    submitButton.name = "submitFilterBox";
    submitButton.text = "Submit";
    submitButton.setStyle(MyColorEnum.LIGHT_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY, 255f,
        255f, 10);
    submitButton.setStylePressed(MyColorEnum.MEDIUM_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY,
        255f, 10);
    addTouchSubscriber(submitButton);
  }

  private void setupFilterList() {
    filterList = new FilterList(200, 0, 200, getHeight(), this);
    filterList.setVisible(false);
    filterList.setup();
    addTouchSubscriber(filterList);
  }

  private void setupFilterRows() {
    filterRows = new ArrayList<FilterRow>();
    float rowHeight = 22;
    ArrayList<HashMap<String, String>> filterNames = DSFilter.getFilterNames();
    for (int i = 0; i < filterNames.size(); i++) {
      FilterRow row = new FilterRow(0, i * rowHeight, 200, rowHeight, this);
      row.label = filterNames.get(i).get("label");
      row.name = filterNames.get(i).get("name");
      row.defaultValue = filterNames.get(i).get("default");
      row.selectMultiple = filterNames.get(i).get("select").equals("multiple") ? true : false;
      row.setup();
      filterRows.add(row);
      addTouchSubscriber(row);
    }
  }

  @Override
  public boolean draw() {
    pushStyle();
    for (FilterRow row : filterRows) {
      row.draw();
    }
    filterList.draw();
    submitButton.draw();
    popStyle();
    return false;
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    return propagateTouch(x, y, down, touchType);
  }

  @Override
  public void eventReceived(EventName eventName, Object data) {
    if (eventName == EventName.BUTTON_TOUCHED) {
      // show list
      for (FilterRow row : filterRows) {
        if (data.toString().equals(row.name + "FilterRowButton")) {
          if (row.openListButton.isSelected()) {
            row.openListButton.setSelected(false);
            m.notificationCenter.notifyEvent(EventName.FILTER_LIST_CLOSE);
            deselectAllFilterRows();
          } else {
            m.notificationCenter.notifyEvent(EventName.FILTER_LIST_CLOSE);
            deselectAllFilterRows();
            row.openListButton.setSelected(true);
            m.notificationCenter.notifyEvent(EventName.FILTER_LIST_OPEN, row);
          }
        }
      }
      // submit
      if (data.toString().equals("submitFilterBox")) {
        m.notificationCenter.notifyEvent(EventName.FILTER_LIST_CLOSE);
        m.notificationCenter.notifyEvent(EventName.CURRENT_FILTER_UPDATED);
      }
    }
  }

  private void deselectAllFilterRows() {
    for (FilterRow row : filterRows) {
      row.openListButton.setSelected(false);
    }
  }
}
