package com.project4.FilterPlayGround;

import java.util.Iterator;

public class FilterBuilder {
  public static String getFilterString(AbstractFilterBox afb) {
    Iterator<AbstractFilterBox> i = afb.getIngoingConnections().iterator();
    String ret = "";
    if (i.hasNext()) {
      AbstractFilterBox next = i.next();
      ret += getFilterString(next);
      ret += i.next().getFilter() + " ";
    }
    return ret;
  }
}
