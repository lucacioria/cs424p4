package com.anotherbrick.inthewall.FilterPlayGround;

import com.anotherbrick.inthewall.VizPanel;

public class FilterBox extends AbstractFilterBox {

    private static final long serialVersionUID = 4567678179498517824L;

    public FilterBox(float x0, float y0, float width, float height,
	    VizPanel parent) {
	super(x0, y0, width, height, parent);

	outputConnector = new BoxConnectorOutgoing(getWidth(), getHeight() / 2,
		CONNECTOR_SIZE, CONNECTOR_SIZE, this);
    }

    @Override
    public boolean isTerminal() {
	return false;
    }

    @Override
    public void setup() {
	// TODO Auto-generated method stub

    }

}
