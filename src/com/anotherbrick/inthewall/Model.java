package com.anotherbrick.inthewall;

import java.util.ArrayList;
import java.util.HashMap;

import com.anotherbrick.inthewall.BarChart.BarData;
import com.anotherbrick.inthewall.datasource.DSCrash;
import com.anotherbrick.inthewall.datasource.DSFilter;
import com.anotherbrick.inthewall.datasource.DataSourceSQL;
import com.modestmaps.geo.Location;

public class Model {

  static Model instance;
  public Main p;
  public VizNotificationCenter notificationCenter;
  public float touchX;
  public float touchY;
  public float touchXZoom;
  public float touchYZoom;
  public TouchEnabled currentModalVizPanel;
  public DataSourceSQL dataSourceSQL;

  // global application variables
  public Location upperLeftLocation;
  public Location lowerRightLocation;
  public DSFilter currentFilter = new DSFilter();
  public ArrayList<DSCrash> crashes = null;
  public int currentStateCode;
  public String currentGroupField1;
  public String currentGroupField2;
  public ArrayList<BarData> crashesCountForBarchart1;
  public ArrayList<BarData> crashesCountForBarchart2;
  public HashMap<String, ArrayList<? extends Object>> selectorPanelsState = new HashMap<String, ArrayList<? extends Object>>();
  public boolean population = false;

  public static void setup(Main p, DataSourceSQL dataSourceSQL,
      VizNotificationCenter notificationCenter) {
    Model.instance = new Model(p, dataSourceSQL, notificationCenter);
  }

  private Model(Main p, DataSourceSQL dataSourceSQL, VizNotificationCenter notificationCenter) {
    this.p = p;
    this.dataSourceSQL = dataSourceSQL;
    this.notificationCenter = notificationCenter;
    ArrayList<String> list = new ArrayList<String>();
    list.add("weather");
    selectorPanelsState.put("selectorBarChart1XAxis", list);
    list = new ArrayList<String>();
    list.add("day_of_week");
    selectorPanelsState.put("selectorBarChart2XAxis", list);
    list = new ArrayList<String>();
    list.add("Illinois");
    selectorPanelsState.put("selectorBarChart1State", list);
    list = new ArrayList<String>();
    list.add("Alaska");
    selectorPanelsState.put("selectorBarChart2State", list);
    list = new ArrayList<String>();
    list.add("alcohol_involved");
    selectorPanelsState.put("mapSelector", list);
    list = new ArrayList<String>();
    list.add("age");
    selectorPanelsState.put("scatterSelectorY", list);
    list = new ArrayList<String>();
    list.add("travel_speed");
    selectorPanelsState.put("scatterSelectorX", list);
    list = new ArrayList<String>();
    list.add("2001 - Illinois and alcohol");
    selectorPanelsState.put("eventSelector", list);
  }

  public static Model getInstance() {
    return Model.instance;
  }

  public void loadFiles() {
    // TODO Auto-generated method stub

  }

}
