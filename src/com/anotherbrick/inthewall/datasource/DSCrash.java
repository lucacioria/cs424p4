package com.anotherbrick.inthewall.datasource;

import com.anotherbrick.inthewall.Cluster;

public class DSCrash {

  // mandatory attributes
  public int _state;
  public int _case;
  public int _year;
  public String month;
  public int hour;
  public String hour_range;
  public int minutes;
  public String day_of_week;
  public String weather;
  public String alcohol_involved;
  public int[] vehicle_configuration;
  public Float latitude;
  public Float longitude;

  public boolean visibleOnMap = true;
  public Cluster cluster;

  // not mandatory attributes
  public String drug_involved;
  public String roadway_surface_condition;
  public int number_of_fatalities;
  public int alcohol_test_result;
  public int travel_speed;
  public String travel_speed_range;
  public int age;
  public String age_range;
  public String sex;

  public Float dimension = 15f;
  public boolean selected = false;
  public float xOnMap;
  public float yOnMap;

  public DSCrash() {
  }

  @Override
  public String toString() {
    return _case + "";
  }

  public Cluster getCluster() {
    return cluster;
  }

  public void setCluster(Cluster cluster) {
    this.cluster = cluster;
  }

  public boolean isVisibleOnMap() {
    return visibleOnMap;
  }

  public void setVisibleOnMap(boolean visibleOnMap) {
    this.visibleOnMap = visibleOnMap;
  }

}
