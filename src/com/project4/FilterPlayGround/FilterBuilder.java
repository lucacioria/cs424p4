package com.project4.FilterPlayGround;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.TreeMap;
import java.util.concurrent.LinkedBlockingQueue;

public class FilterBuilder {

  private HashMap<AbstractFilterBox, Boolean> markeds = new HashMap<AbstractFilterBox, Boolean>();
  HashMap<AbstractFilterBox, Integer> levels = new HashMap<AbstractFilterBox, Integer>();
  TreeMap<Integer, ArrayList<AbstractFilterBox>> list =
      new TreeMap<Integer, ArrayList<AbstractFilterBox>>();

  public String getFilterString(AbstractFilterBox afb) {
    String ret = "";
    Queue<AbstractFilterBox> q = new LinkedBlockingQueue<AbstractFilterBox>();
    q.add(afb);
    mark(afb);
    levels.put(afb, 0);
    while (!q.isEmpty()) {
      AbstractFilterBox t = q.remove();
      for (AbstractFilterBox e : t.getIngoingConnections()) {
        if (!isMarked(e)) {
          levels.put(e, levels.get(t) + 1);
          mark(e);
          q.add(e);
        }
      }
    }
    for (AbstractFilterBox a : levels.keySet()) {
      if (a.isTerminal()) continue;
      if (!list.containsKey(levels.get(a)))
        list.put(levels.get(a), new ArrayList<AbstractFilterBox>());
      list.get(levels.get(a)).add(a);
    }
    for (Integer i : list.descendingKeySet()) {
      ret += " (";
      for (AbstractFilterBox af : list.get(i)) {
        ret += af.getFilter() + " OR ";
      }
      ret += " FALSE ) AND ";
    }
    ret += "TRUE ";
    return ret;

  }

  private void mark(AbstractFilterBox afb) {
    markeds.put(afb, true);
  }

  private boolean isMarked(AbstractFilterBox afb) {
    return (markeds.get(afb) != null && markeds.get(afb));
  }
}
