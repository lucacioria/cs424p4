package com.project4.FilterPlayGround;

import com.anotherbrick.inthewall.VizPanel;
import com.anotherbrick.inthewall.Config.MyColorEnum;

public class TerminalFilterBox extends AbstractFilterBox {

  private static final long serialVersionUID = -780292097125399883L;


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
  public void setup() {
  }

  @Override
  public String getFilter() {
    return "";
  }

}
