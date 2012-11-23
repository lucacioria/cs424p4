package com.project4.FilterPlayGround;

import com.anotherbrick.inthewall.VizPanel;

public class BoxConnectorIngoing extends AbstractBoxConnector {

  public BoxConnectorIngoing(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  @Override
  public BoxConnectorType getType() {
    return BoxConnectorType.INGOING;
  }

}
