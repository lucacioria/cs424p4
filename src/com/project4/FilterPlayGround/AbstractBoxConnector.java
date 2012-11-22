package com.project4.FilterPlayGround;

import java.io.Serializable;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.VizPanel;

public abstract class AbstractBoxConnector extends VizPanel implements Serializable {

  private static final long serialVersionUID = 8197227398393603393L;
  private boolean active = false;
  public MyColorEnum ACTIVE_COLOR = MyColorEnum.RED;
  public MyColorEnum UNACTIVE_COLOR = MyColorEnum.DARK_GRAY;

  public AbstractBoxConnector(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  @Override
  public void setup() {

  }

  public void activate() {
    active = true;
  }

  public void deactivate() {
    active = false;
  }

  public void toggleActive() {
    active = !active;
  }

  public boolean isActive() {
    return active;
  }

  @Override
  public boolean draw() {
    pushStyle();
    fill(active ? ACTIVE_COLOR : UNACTIVE_COLOR);
    ellipse(0, 0, getWidth(), getHeight());
    popStyle();
    return false;
  }

  public abstract BoxConnectorType getType();

  @Override
  public boolean containsPoint(float x, float y) {
    return x > getX0Absolute() - getWidth() / 2 && x < getX0Absolute() + getWidth() / 2
        && y > getY0Absolute() - getHeight() / 2 && y < getY0Absolute() + getHeight() / 2;
  }
}
