package com.anotherbrick.inthewall.datasource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;

import com.anotherbrick.inthewall.Config;
import com.anotherbrick.inthewall.Helper;
import com.anotherbrick.inthewall.BarChart.BarData;

public class DSFilter {
  public Float latitudeMin;
  public Float latitudeMax;
  public Float longitudeMin;
  public Float longitudeMax;
  private HashMap<String, ArrayList<String>> listAttributes = new HashMap<String, ArrayList<String>>();
  private static HashMap<String, HashMap<String, ArrayList<Integer>>> mappings = new HashMap<String, HashMap<String, ArrayList<Integer>>>();

  public DSFilter() {
    for (HashMap<String, String> h : DSFilter.getFilterNames()) {
      listAttributes.put(h.get("name"), new ArrayList<String>());
    }
  }

  public String getWhereClause(boolean includeState) {
    ArrayList<String> a = new ArrayList<String>();
    if (latitudeMax != null) a.add("latitude < " + latitudeMax);
    if (latitudeMin != null) a.add("latitude > " + latitudeMin);
    if (longitudeMax != null) a.add("longitude < " + longitudeMax);
    if (longitudeMin != null) a.add("longitude > " + longitudeMin);
    Iterator<String> it = listAttributes.keySet().iterator();
    while (it.hasNext()) {
      String name = it.next();
      if (includeState == false && name.equals("_state")) continue;
      ArrayList<String> values = listAttributes.get(name);
      if (values.size() > 0) {
        String codes = getCodesByName(name, values);
        a.add(name + " IN (" + codes + ")");
      }
    }
    return join(a, " AND ");
  }

  public static String getCodesByName(String name, ArrayList<String> values) {
    HashMap<String, ArrayList<Integer>> mappings = getMapping(name);
    ArrayList<String> out = new ArrayList<String>();
    for (String value : values) {
      ArrayList<Integer> codes = mappings.get(value);
      out.add(join(codes, ", "));
    }
    return join(out, ", ");
  }

  public void setAttributeWithList(String name, ArrayList<? extends Object> list) {
    ArrayList<String> attributeValues = listAttributes.get(name);
    attributeValues.clear();
    for (Object o : list) {
      attributeValues.add(o.toString());
    }
  }

  public ArrayList<String> getAttributeValues(String name) {
    return listAttributes.get(name);
  }

  public static String join(ArrayList<? extends Object> a, String separator) {
    String out = "";
    if (a.size() == 0) {
      return "";
    }
    for (Object s : a) {
      out += s.toString() + separator;
    }
    return out.substring(0, out.length() - separator.length());
  }

  public static ArrayList<String> getValues(String name) {
    ArrayList<String> o = new ArrayList<String>();
    try {
      JSONObject j = getJsonFile(name);
      Iterator<String> i = j.keys();
      while (i.hasNext()) {
        String key = i.next();
        o.add(key);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    Collections.sort(o);
    return o;
  }

  public static ArrayList<HashMap<String, String>> getFilterNames() {
    ArrayList<HashMap<String, String>> filterNames = new ArrayList<HashMap<String, String>>();
    try {
      JSONObject j = getJsonFile("_filters");
      JSONArray lines = j.getJSONArray("lines");
      for (int i = 0; i < lines.length(); i++) {
        JSONObject line = lines.getJSONObject(i);
        HashMap<String, String> val = new HashMap<String, String>();
        val.put("name", line.getString("name"));
        val.put("label", line.getString("label"));
        val.put("select", line.getString("select"));
        val.put("default", line.getString("default"));
        filterNames.add(val);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return filterNames;
  }

  public static String getLabelByName(String name) {
    if (name != null && name.equals("travel_speed_range")) return "travel speed range";
    for (HashMap<String, String> h : getFilterNames()) {
      if (h.get("name").equals(name)) return h.get("label");
    }
    return null;
  }

  public static String getSelectClause() {
    String o = "";
    JSONObject j = getJsonFile("select.json");

    return o;
  }

  private static HashMap<String, ArrayList<Integer>> getMapping(String name) {
    // cache
    if (mappings.containsKey(name)) return mappings.get(name);
    // retrieve
    HashMap<String, ArrayList<Integer>> o = new HashMap<String, ArrayList<Integer>>();
    try {
      JSONObject j = getJsonFile(name);
      Iterator<String> i = j.keys();
      while (i.hasNext()) {
        String key = i.next();
        ArrayList<Integer> list = new ArrayList<Integer>();
        JSONArray ja = j.getJSONArray(key);
        for (int k = 0; k < ja.length(); k++) {
          list.add(ja.getInt(k));
        }
        o.put(key, list);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    mappings.put(name, o);
    return o;
  }

  public static String getValueByCode(String name, int code) {
    HashMap<String, ArrayList<Integer>> mapping = getMapping(name);
    Iterator<String> it = mapping.keySet().iterator();
    while (it.hasNext()) {
      String key = it.next();
      for (int _code : mapping.get(key)) {
        if (_code == code) return key;
      }
    }
    return null;
  }

  private static JSONObject getJsonFile(String name) {
    JSONObject obj = null;
    try {
      String configString = getFileContents("mappings/" + name + ".json");
      obj = new JSONObject(configString);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return obj;
  }

  private static String getFileContents(String filename) {
    try {
      return Helper.readFile(Config.getInstance().path + filename);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static int getIntValue(String name, int value) {
    if (value < 0) return -1;
    if (name.equals("number_of_fatalities")) {
      if (value == 99) return -1;
    }
    if (name.equals("travel_speed")) {
      if (value > 200 || value < 0) return -1;
    }
    if (name.equals("age")) {
      if (value > 97 || value < 0) return -1;
    }
    return value;
  }

  public static String getClearLabel(String label) {
    return label.replaceAll("^[0-9]+\\_", "");
  }

  public static ArrayList<BarData> adaptCountByToLabels(ArrayList<BarData> crashesCountForBarchart,
      String groupField) {
    ArrayList<BarData> o = new ArrayList<BarData>();
    HashMap<String, Integer> temp = new HashMap<String, Integer>();
    for (BarData barData : crashesCountForBarchart) {
      int code = Integer.parseInt(barData.label);
      String label = getValueByCode(groupField, code);
      if (temp.containsKey(label) == false) {
        temp.put(label, 0);
      }
      Integer val = temp.get(label);
      temp.put(label, val + (int) barData.value);
    }
    ArrayList<String> possibleValues = getValues(groupField);
    for (String possibleValue : possibleValues) {
      if (temp.containsKey(possibleValue)) {
        BarData barData = new BarData();
        barData.label = possibleValue;
        barData.value = temp.get(possibleValue);
        o.add(barData);
      }
    }
    return o;
  }
}
