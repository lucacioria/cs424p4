package com.anotherbrick.inthewall.FilterPlayGround;

import java.util.ArrayList;

import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizPanel;
import static com.anotherbrick.inthewall.FilterPlayGround.BoxConnectorType.*;

public class FilterLine extends VizPanel implements TouchEnabled {
    public FilterLine(float x0, float y0, float width, float height,
	    VizPanel parent) {
	super(x0, y0, width, height, parent);
    }

    private ArrayList<AbstractFilterBox> boxes = new ArrayList<AbstractFilterBox>();

    public void addBox(AbstractFilterBox afb) {
	boxes.add(afb);
    }

    public void removeBox(AbstractFilterBox afb) {
	boxes.remove(afb);
    }

    private void addConection(AbstractFilterBox from, AbstractFilterBox to) {
	if (!from.isTerminal())
	    from.addOutgoingConnection(to);
    }

    private AbstractFilterBox currentBox = null;
    private AbstractBoxConnector currentConnector = null;

    @Override
    public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
	if (down) {
	    for (AbstractFilterBox afb : boxes) {
		BoxConnectorIngoing bci = (BoxConnectorIngoing) afb
			.getInputConnector();
		if (bci.containsPoint(x, y)) {
		    if (currentBox != null
			    && currentConnector.getType() == OUTGOING) {
			addConection(currentBox, afb);
			currentConnector.toggleActive();
			bci.toggleActive();
		    } else {
			currentBox = afb;
			currentConnector = bci;
			currentConnector.toggleActive();
		    }
		}
		BoxConnectorOutgoing bco = (BoxConnectorOutgoing) afb
			.getInputConnector();
		if (bco.containsPoint(x, y)) {
		    if (currentBox != null
			    && currentConnector.getType() == INGOING) {
			addConection(afb, currentBox);
			currentConnector.toggleActive();
			bci.toggleActive();
		    } else {
			currentBox = afb;
			currentConnector = bco;
			currentConnector.toggleActive();
		    }
		}
	    }
	}
	propagateTouch(x, y, down, touchType);
	return false;
    }

    @Override
    public void setup() {

    }

    @Override
    public boolean draw() {
	for (AbstractFilterBox abc : boxes) {
	    abc.draw();
	    AbstractFilterBox prev = null;
	    for (AbstractFilterBox next : abc.getOutgoingConnections()) {
		if (prev != null)
		    line(prev.getOutputConnector().getX0(), prev
			    .getOutputConnector().getY0(), next
			    .getInputConnector().getX0(), next
			    .getInputConnector().getY0());
	    }
	}

	return false;
    }
}
