package com.example.app;

import java.util.ArrayList;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.datasource.DSFilter;
import com.anotherbrick.inthewall.VizPanel;

public class EventPanel extends VizPanel implements EventSubscriber {

  public EventPanel(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  private int currentEvent = 1;

  @Override
  public void setup() {
    m.notificationCenter.registerToEvent(EventName.EVENT_CHANGED, this);
  }

  @Override
  public boolean draw() {
    pushStyle();
    background(MyColorEnum.DARK_GRAY);
    fill(MyColorEnum.WHITE);
    switch (currentEvent) {
    case 1:
      textSize(15);
      text("2001 - Illinois and alcohol", 10, 20);
      textSize(10);
      text("Illinois State increases the the penalties for\nalcohol involvement in the DUL laws",
          10, 70);
      break;
    case 2:
      textSize(15);
      text("2003 - Michigan changes speed", 10, 20);
      textSize(10);
      text("Michigan State lowers the speed limit", 10, 70);
      break;
    case 3:
      textSize(15);
      text("2006 - Texas changes speed", 10, 20);
      textSize(10);
      text("Texas had the highest posted speed limit\nwhich is 80 mph in the US", 10, 70);
      break;
    case 4:
      textSize(15);
      text("2009 - Illinois trucks speed", 10, 20);
      textSize(10);
      text("Illinois increased the truck speed limit to 65 mph", 10, 70);
      break;
    case 5:
      textSize(15);
      text("2010 - Wisconsin and alcohol", 10, 20);
      textSize(10);
      text("Wisconsin State increases the the penalties\nfor alcohol related offenses", 10, 70);
      break;
    }
    popStyle();
    return false;
  }

  @Override
  public void eventReceived(EventName eventName, Object data) {
    if (eventName == EventName.EVENT_CHANGED) {
      if (data.toString().equals("2001 - Illinois and alcohol")) {
        DSFilter filter = new DSFilter();
        ArrayList<String> state = new ArrayList<String>();
        state.add("Illinois");
        filter.setAttributeWithList("_state", state);
        ArrayList<String> year = new ArrayList<String>();
        year.add("01_2001");
        filter.setAttributeWithList("_year", year);
        m.currentFilter = filter;
        ArrayList<String> list = new ArrayList<String>();
        list.add("alcohol_involved");
        m.selectorPanelsState.put("selectorBarChart1XAxis", list);
        m.notificationCenter.notifyEvent(EventName.BUTTON_TOUCHED, "submitFilterBox");
        m.notificationCenter.notifyEvent(EventName.BUTTON_TOUCHED, "barchartSelectXButton1");
        m.notificationCenter.notifyEvent(EventName.BUTTON_TOUCHED, "barchartSelectXButton1");
        m.notificationCenter.notifyEvent(EventName.BUTTON_TOUCHED, "barchartSelectXButton2");
        m.notificationCenter.notifyEvent(EventName.BUTTON_TOUCHED, "barchartSelectXButton2");
        currentEvent = 1;
      } else if (data.toString().equals("2003 - Michigan changes speed")) {
        DSFilter filter = new DSFilter();
        ArrayList<String> state = new ArrayList<String>();
        state.add("Michigan");
        filter.setAttributeWithList("_state", state);
        m.currentFilter = filter;
        ArrayList<String> list = new ArrayList<String>();
        list.add("_year");
        m.selectorPanelsState.put("selectorBarChart1XAxis", list);
        m.notificationCenter.notifyEvent(EventName.BUTTON_TOUCHED, "submitFilterBox");
        m.notificationCenter.notifyEvent(EventName.BUTTON_TOUCHED, "barchartSelectXButton1");
        m.notificationCenter.notifyEvent(EventName.BUTTON_TOUCHED, "barchartSelectXButton1");
        m.notificationCenter.notifyEvent(EventName.BUTTON_TOUCHED, "barchartSelectXButton2");
        m.notificationCenter.notifyEvent(EventName.BUTTON_TOUCHED, "barchartSelectXButton2");
        currentEvent = 2;
      } else if (data.toString().equals("2006 - Texas changes speed")) {
        DSFilter filter = new DSFilter();
        ArrayList<String> state = new ArrayList<String>();
        state.add("Texas");
        filter.setAttributeWithList("_state", state);
        ArrayList<String> year = new ArrayList<String>();
        year.add("06_2006");
        filter.setAttributeWithList("_year", year);
        m.currentFilter = filter;
        ArrayList<String> list = new ArrayList<String>();
        list.add("travel_speed_range");
        m.selectorPanelsState.put("selectorBarChart1XAxis", list);
        m.notificationCenter.notifyEvent(EventName.BUTTON_TOUCHED, "submitFilterBox");
        m.notificationCenter.notifyEvent(EventName.BUTTON_TOUCHED, "barchartSelectXButton1");
        m.notificationCenter.notifyEvent(EventName.BUTTON_TOUCHED, "barchartSelectXButton1");
        m.notificationCenter.notifyEvent(EventName.BUTTON_TOUCHED, "barchartSelectXButton2");
        m.notificationCenter.notifyEvent(EventName.BUTTON_TOUCHED, "barchartSelectXButton2");
        currentEvent = 3;
      } else if (data.toString().equals("2009 - Illinois trucks speed")) {
        DSFilter filter = new DSFilter();
        ArrayList<String> state = new ArrayList<String>();
        state.add("Illinois");
        filter.setAttributeWithList("_state", state);
        ArrayList<String> year = new ArrayList<String>();
        year.add("01_2001");
        filter.setAttributeWithList("_year", year);
        m.currentFilter = filter;
        ArrayList<String> list = new ArrayList<String>();
        list.add("alcohol_involved");
        m.selectorPanelsState.put("selectorBarChart1XAxis", list);
        m.notificationCenter.notifyEvent(EventName.BUTTON_TOUCHED, "submitFilterBox");
        m.notificationCenter.notifyEvent(EventName.BUTTON_TOUCHED, "barchartSelectXButton1");
        m.notificationCenter.notifyEvent(EventName.BUTTON_TOUCHED, "barchartSelectXButton1");
        m.notificationCenter.notifyEvent(EventName.BUTTON_TOUCHED, "barchartSelectXButton2");
        m.notificationCenter.notifyEvent(EventName.BUTTON_TOUCHED, "barchartSelectXButton2");
        currentEvent = 4;
      } else if (data.toString().equals("2010 - Wisconsin and alcohol")) {
        DSFilter filter = new DSFilter();
        ArrayList<String> state = new ArrayList<String>();
        state.add("Wisconsin");
        filter.setAttributeWithList("_state", state);
        ArrayList<String> year = new ArrayList<String>();
        year.add("10_2010");
        filter.setAttributeWithList("_year", year);
        m.currentFilter = filter;
        ArrayList<String> list = new ArrayList<String>();
        list.add("alcohol_involved");
        m.selectorPanelsState.put("selectorBarChart1XAxis", list);
        m.notificationCenter.notifyEvent(EventName.BUTTON_TOUCHED, "submitFilterBox");
        m.notificationCenter.notifyEvent(EventName.BUTTON_TOUCHED, "barchartSelectXButton1");
        m.notificationCenter.notifyEvent(EventName.BUTTON_TOUCHED, "barchartSelectXButton1");
        m.notificationCenter.notifyEvent(EventName.BUTTON_TOUCHED, "barchartSelectXButton2");
        m.notificationCenter.notifyEvent(EventName.BUTTON_TOUCHED, "barchartSelectXButton2");
        currentEvent = 5;
      }
    }

  }
}
