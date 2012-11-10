package com.anotherbrick.inthewall;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.datasource.DSFilter;

public class VizRow extends VizPanel {

  private ArrayList<PVector> values;
  public boolean selected = false;
  public MyColorEnum backgroundColorSelected, backgroundColor, strokeColor;
  public String name;
  public int cropAtNChars = -1;

  public VizRow(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  public void setValues(ArrayList<PVector> values) {
    this.values = values;
  }

  public ArrayList<PVector> getValues() {
    return this.values;
  }

  public void addValue(PVector value) {
    values.add(value);
  }

  public void removeValue(PVector value) {
    if (values.contains(value)) {
      values.remove(value);
    }
  }

  public boolean draw() {
    pushStyle();
    noStroke();
    textSize(12);
    fill(MyColorEnum.WHITE);
    textAlign(PApplet.LEFT, PApplet.CENTER);
    // SCHIFEZZA PER PROGETTO 3 ATTENZIONE!!
    String nomePulito = DSFilter.getClearLabel(name);
    text(
        cropAtNChars != -1 ? Helper.limitStringLength(nomePulito, cropAtNChars, true) : nomePulito,
        10, getHeight() / 2);
    popStyle();
    return false;
  }

  public void drawBackground() {
    pushStyle();
    if (selected) {
      background(backgroundColorSelected);
    } else {
      background(backgroundColor);
    }
    if (strokeColor != null) {
      stroke(strokeColor);
    } else {
      noStroke();
    }
    popStyle();
  }

  public String getName() {
    return name;
  }

  @Override
  public void setup() {
    // TODO Auto-generated method stub

  }
}
