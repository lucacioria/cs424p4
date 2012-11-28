package com.anotherbrick.inthewall;

import com.project4.datasource.DataSourceSQL;

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
  public boolean userLinesVisible = false;

  public static final float MAX_TIME = 1305935940;
  public static final float MIN_TIME = 1304121600;


  public static void setup(Main p, DataSourceSQL dataSourceSQL,
      VizNotificationCenter notificationCenter) {
    Model.instance = new Model(p, dataSourceSQL, notificationCenter);
  }

  private Model(Main p, DataSourceSQL dataSourceSQL, VizNotificationCenter notificationCenter) {
    this.p = p;
    this.dataSourceSQL = dataSourceSQL;
    this.notificationCenter = notificationCenter;
  }

  public static Model getInstance() {
    return Model.instance;
  }

  public void loadFiles() {}

}
