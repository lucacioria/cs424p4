package com.anotherbrick.inthewall.ScatterPlot;

import java.util.ArrayList;

import processing.core.PVector;

public class ScatterPlotData {
  public String title;
  public String xLabel;
  public String yLabel;

  private ArrayList<PVector> points;

  private float xMin = Float.MAX_VALUE;
  private float xMax = Float.MIN_VALUE;
  private float yMin = Float.MAX_VALUE;
  private float yMax = Float.MIN_VALUE;

  private boolean boundsAreUpToDate = false;

  public void setPoints(ArrayList<PVector> points) {
    this.points = points;
    boundsAreUpToDate = false;
  }

  public ArrayList<PVector> getPoints() {
    return points;
  }

  public float getXMin() {
    if (!boundsAreUpToDate) computeBounds();
    return xMin;
  }

  public float getXMax() {
    if (!boundsAreUpToDate) computeBounds();
    return xMax;
  }

  public float getYMin() {
    if (!boundsAreUpToDate) computeBounds();
    return yMin;
  }

  public float getYMax() {
    if (!boundsAreUpToDate) computeBounds();
    return yMax;
  }

  private void computeBounds() {
    for (PVector point : points) {
      if (point.x > xMax) xMax = point.x;
      if (point.y > yMax) yMax = point.y;
      if (point.x < xMin) xMin = point.x;
      if (point.y < yMin) yMin = point.y;
    }
    boundsAreUpToDate = true;
  }

}