package com.project4.datasource;

import com.anotherbrick.inthewall.Config.MyColorEnum;

public class Filter implements Comparable<Filter> {

  private int number;
  private MyColorEnum color;
  private String where;

  public Filter(int number, MyColorEnum color, String where) {
    super();
    this.number = number;
    this.color = color;
    this.where = where;
  }

  public String getWhere() {
    return where;
  }
  
  public int getNumber() {
    return number;
  }

  public MyColorEnum getColor() {
    return color;
  }

  @Override
  public int compareTo(Filter o) {
    return new Integer(number).compareTo(o.number);
  }

}
