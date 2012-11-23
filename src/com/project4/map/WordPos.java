package com.project4.map;

import processing.core.PVector;

public class WordPos implements Comparable<WordPos> {
  private int count;
  private PVector meanPosition;
  public String word;

  public WordPos(PVector position, String word) {
    count = 1;
    this.word = word;
    meanPosition = position;
  }

  public void addLocation(PVector position) {
    meanPosition.x = (meanPosition.x * count + position.x) / (count + 1);
    meanPosition.y = (meanPosition.y * count + position.y) / (count + 1);
    count++;
  }

  @Override
  public String toString() {
    return ""+count;
  }

  public int getCount() {
    return count;
  }

  public PVector getPosition() {
    return meanPosition;
  }

  @Override
  public int compareTo(WordPos arg0) {
    return ((Integer) count).compareTo(arg0.count);
  }
}
