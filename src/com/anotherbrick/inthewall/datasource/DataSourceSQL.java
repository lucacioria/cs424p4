package com.anotherbrick.inthewall.datasource;

import java.util.ArrayList;

import com.anotherbrick.inthewall.Model;
import com.anotherbrick.inthewall.BarChart.BarData;

import processing.core.PApplet;
import de.bezier.data.sql.MySQL;

public class DataSourceSQL {

  private MySQL sql;

  public DataSourceSQL(PApplet context) {
    String user = "organic";
    String pass = "sharpcheddar";
    String database = "crash";
    String host = "inacarcrash.cnrtm99w3c2x.us-east-1.rds.amazonaws.com";
    sql = new MySQL(context, host, database, user, pass);
    try {
      Class.forName("com.mysql.jdbc.Driver").newInstance();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void getCrashes(DSFilter f) {
    ArrayList<DSCrash> crashes = new ArrayList<DSCrash>();
    String query;
    if (sql.connect()) {
      query = "SELECT _year,_state,_case,number_of_vehicles,number_of_persons,weather,city,county,day,"
          + "hour,month,crash_related_factor_1,crash_related_factor_2,crash_related_factor_3,time,"
          + "day_of_week,first_harmful_event,holiday_related,latitude,longitude,light_condition,"
          + "number_of_fatalities,number_of_travel_lanes,roadway_function_class,roadway_surface_condition,"
          + "roadway_surface_type,route_signing,speed_limit,trafficway_flow,age, age_range, air_bag_availability,"
          + "alcohol_test_result,injury_severity,person_related_factors_1,person_related_factors_2,"
          + "person_related_factors_3,person_type,alcohol_involved,drug_involved,"
          + "race,seating_position,sex,body_type,most_harmful_event,number_of_occupants,"
          + "travel_speed,vehicle_configuration,vehicle_related_factors_1,vehicle_related_factors_2,"
          + "driver_related_factors_1,driver_related_factors_2,driver_related_factors_3,driver_related_factors_4 "
          + " FROM krashes3 WHERE " + f.getWhereClause(true);
      sql.query(query);
      createArrayFromQuery(crashes);
      Model.getInstance().crashes = crashes;
      Model.getInstance().currentStateCode = crashes.get(0)._state;
    }
  }

  public void getCrashesCountBy(DSFilter f, String groupField, String state, int barChartNumber) {
    ArrayList<BarData> crashesCountForBarchart = new ArrayList<BarData>();
    String query;
    if (sql.connect()) {
      ArrayList<String> states = new ArrayList<String>();
      states.add(barChartNumber == 1 ? DSFilter.getValueByCode("_state",
          Model.getInstance().currentStateCode) : state);
      String where = f.getWhereClause(false);
      query = "SELECT " + groupField + " as label, count(*) as count" + " FROM krashes3 WHERE "
          + where + (where.length() > 0 ? " AND " : " ") + "_state IN ("
          + DSFilter.getCodesByName("_state", states) + ") GROUP BY " + groupField;
      System.out.println(query);
      sql.query(query);
      while (sql.next()) {
        BarData barData = new BarData();
        barData.value = sql.getInt("count");
        barData.label = sql.getString("label");
        crashesCountForBarchart.add(barData);
      }
      if (barChartNumber == 1) {
        Model.getInstance().crashesCountForBarchart1 = DSFilter.adaptCountByToLabels(
            crashesCountForBarchart, groupField);
        Model.getInstance().currentGroupField1 = groupField;
      } else {
        Model.getInstance().crashesCountForBarchart2 = DSFilter.adaptCountByToLabels(
            crashesCountForBarchart, groupField);
        Model.getInstance().currentGroupField2 = groupField;
      }
    }
  }

  public int getStatePopulation(int currentStateCode) {
    String query;
    if (sql.connect()) {
      query = "SELECT population FROM states WHERE id = " + currentStateCode;
      System.out.println(query);
      sql.query(query);
      while (sql.next()) {
        return sql.getInt("population");
      }
    }
    return -1;
  }

  public int getStatePopulation(String currentStateCode) {
    String query;
    if (sql.connect()) {
      query = "SELECT population FROM states WHERE name like '" + currentStateCode + "'";
      System.out.println(query);
      sql.query(query);
      while (sql.next()) {
        return sql.getInt("population");
      }
    }
    return -1;
  }

  private void createArrayFromQuery(ArrayList<DSCrash> array) {
    while (sql.next()) {
      DSCrash event = new DSCrash();
      event._year = sql.getInt("_year");
      event._state = sql.getInt("_state");
      event._case = sql.getInt("_case");
      event.latitude = sql.getFloat("latitude");
      event.longitude = sql.getFloat("longitude");
      // value
      event.weather = DSFilter.getValueByCode("weather", sql.getInt("weather"));
      event.alcohol_involved = DSFilter.getValueByCode("alcohol_involved",
          sql.getInt("alcohol_involved"));
      event.drug_involved = DSFilter.getValueByCode("drug_involved", sql.getInt("drug_involved"));
      event.day_of_week = DSFilter.getValueByCode("day_of_week", sql.getInt("day_of_week"));
      event.month = DSFilter.getValueByCode("month", sql.getInt("month"));
      event.age_range = DSFilter.getValueByCode("age_range", sql.getInt("age_range"));
      event.sex = DSFilter.getValueByCode("sex", ((int) sql.getFloat("sex")));
      // numeric
      event.number_of_fatalities = DSFilter.getIntValue("number_of_fatalities",
          sql.getInt("number_of_fatalities"));
      event.age = DSFilter.getIntValue("age", sql.getInt("age"));
      event.travel_speed = DSFilter.getIntValue("travel_speed", sql.getInt("travel_speed"));
      //
      array.add(event);
    }
  }
}
