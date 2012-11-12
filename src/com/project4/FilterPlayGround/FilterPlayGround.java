package com.project4.FilterPlayGround;

import java.util.ArrayList;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizPanel;

public final class FilterPlayGround extends VizPanel implements TouchEnabled {

  public float BOX_HEIGHT = 80;
  public float BOX_WIDTH = 30;

  ArrayList<FilterLine> lines = new ArrayList<FilterLine>();

  public FilterPlayGround(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  @Override
  public void setup() {
    addFilterLine();
    addFilterBox();
  }

  public void addTerminalBox() {
    FilterLine line = lines.get(lines.size() - 1);
    TerminalFilterBox tfb =
        new TerminalFilterBox(getRandomX(), getRandomY(), BOX_WIDTH, BOX_HEIGHT, line);
    line.addBox(tfb);
    addTouchSubscriber(tfb);
  }

  public void addFilterBox() {
    FilterLine line = lines.get(lines.size() - 1);
    FilterBox fb = new FilterBox(getRandomX(), getRandomY(), BOX_WIDTH, BOX_HEIGHT, line);
    line.addBox(fb);
  }

  @Override
  public boolean draw() {
    pushStyle();
    fill(MyColorEnum.DARK_BLUE);
    rect(0, 0, getWidth(), getHeight());
    for (FilterLine fl : lines)
      fl.draw();
    popStyle();
    return false;
  }

  public void addFilterLine() {
    FilterLine fl = new FilterLine(0, 0, getWidth(), getHeight(), this, MyColorEnum.DARK_BROWN);
    lines.add(fl);
    addTouchSubscriber(fl);
  }

  private float getRandomX() {
    return (float) (Math.random() * getWidth());
  }

  private float getRandomY() {
    return (float) (Math.random() * getHeight());
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    propagateTouch(x, y, down, touchType);
    return false;
  }
}
