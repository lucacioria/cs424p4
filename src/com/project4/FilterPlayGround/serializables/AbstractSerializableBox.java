package com.project4.FilterPlayGround.serializables;

import java.io.Serializable;

public class AbstractSerializableBox implements Serializable {

  private static final long serialVersionUID = -8324267215145828733L;

  public AbstractSerializableBox() {};

  private float x0, y0, width, height;

  public AbstractSerializableBox(float x0, float y0, float width, float height) {
    super();
    this.x0 = x0;
    this.y0 = y0;
    this.width = width;
    this.height = height;
  }

  public float getX0() {
    return x0;
  }

  public void setX0(float x0) {
    this.x0 = x0;
  }

  public float getY0() {
    return y0;
  }

  public void setY0(float y0) {
    this.y0 = y0;
  }

  public float getWidth() {
    return width;
  }

  public void setWidth(float width) {
    this.width = width;
  }

  public float getHeight() {
    return height;
  }

  public void setHeight(float height) {
    this.height = height;
  }

}
