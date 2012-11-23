package com.project4.FilterPlayGround;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.VizPanel;
import com.project4.FilterPlayGround.serializables.AbstractSerializableBox;
import com.project4.FilterPlayGround.serializables.SerializableTerminalBox;

public class TerminalFilterBox extends AbstractFilterBox {


  public TerminalFilterBox(float x0, float y0, float width, float height, MyColorEnum color,
      VizPanel parent) {
    super(x0, y0, width, height, parent);
    this.COLOR = color;
  }


  @Override
  public boolean isTerminal() {
    return true;
  }

  @Override
  public void setup() {}

  @Override
  public String getFilter() {
    return "";
  }

  @Override
  public AbstractSerializableBox serialize() {
    return new SerializableTerminalBox(getX0(), getY0(), getWidth(), getHeight());
  }
}
