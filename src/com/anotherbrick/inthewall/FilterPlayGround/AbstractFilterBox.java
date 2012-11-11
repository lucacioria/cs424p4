package com.anotherbrick.inthewall.FilterPlayGround;

import java.io.Serializable;
import java.util.ArrayList;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizPanel;

public abstract class AbstractFilterBox extends VizPanel implements
	Serializable, TouchEnabled {

    private static final long serialVersionUID = 5807010684381393247L;
    private AbstractBoxConnector inputConnector;
    private AbstractBoxConnector outputConnector;

    public AbstractBoxConnector getInputConnector() {
	return inputConnector;
    }

    public AbstractBoxConnector getOutputConnector() {
	return outputConnector;
    }

    public float CONNECTOR_SIZE = 30;
    public MyColorEnum BOX_COLOR = MyColorEnum.DARK_WHITE;

    public AbstractFilterBox(float x0, float y0, float width, float height,
	    VizPanel parent) {
	super(x0, y0, width, height, parent);
	float connectorX0 = CONNECTOR_SIZE / 2;
	float connectorY0 = (getHeight() - CONNECTOR_SIZE) / 2;
	inputConnector = new BoxConnectorIngoing(connectorX0, connectorY0,
		CONNECTOR_SIZE, CONNECTOR_SIZE, this);
    }

    private ArrayList<AbstractFilterBox> outgoingConnections = new ArrayList<AbstractFilterBox>();

    public void addOutgoingConnection(AbstractFilterBox afb) {
	outgoingConnections.add(afb);
    }

    public void removeOutgoingConnection(AbstractFilterBox afb) {
	outgoingConnections.remove(afb);
    }

    public boolean draw() {
	pushStyle();
	fill(BOX_COLOR);
	rect(getX0(), getY0(), getWidth(), getHeight());
	inputConnector.draw();
	if (!isTerminal()) {
	    outputConnector.draw();
	}
	popStyle();
	return false;
    }

    public ArrayList<AbstractFilterBox> getOutgoingConnections() {
	return outgoingConnections;
    }

    public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
	return false;
    }

    public abstract boolean isTerminal();

}
