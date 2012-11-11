package com.anotherbrick.inthewall.FilterPlayGround;

import com.anotherbrick.inthewall.VizPanel;

public class TerminalFilterBox extends AbstractFilterBox {

    private static final long serialVersionUID = -780292097125399883L;

    public TerminalFilterBox(float x0, float y0, float width, float height,
	    VizPanel parent) {
	super(x0, y0, width, height, parent);
    }

    @Override
    public boolean isTerminal() {
	return true;
    }

    @Override
    public void setup() {
	// TODO Auto-generated method stub

    }

}
