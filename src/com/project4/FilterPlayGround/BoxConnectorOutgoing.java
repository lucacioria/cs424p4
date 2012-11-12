package com.project4.FilterPlayGround;

import com.anotherbrick.inthewall.VizPanel;

public class BoxConnectorOutgoing extends AbstractBoxConnector {

    private static final long serialVersionUID = 1081549325518537741L;

    public BoxConnectorOutgoing(float x0, float y0, float width, float height,
	    VizPanel parent) {
	super(x0, y0, width, height, parent);
    }

    @Override
    public BoxConnectorType getType() {
	return BoxConnectorType.OUTGOING;
    }

}
