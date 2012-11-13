package com.project4.FilterPlayGround;

import java.io.Serializable;
import java.util.ArrayList;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizPanel;

public abstract class AbstractFilterBox extends VizPanel implements Serializable, TouchEnabled {

  private static final long serialVersionUID = 5807010684381393247L;
  protected BoxConnectorIngoing inputConnector;
  protected BoxConnectorOutgoing outputConnector;

  protected MyColorEnum COLOR = MyColorEnum.RED;

  public AbstractBoxConnector getInputConnector() {
    return inputConnector;
  }

  public AbstractBoxConnector getOutputConnector() {
    return outputConnector;
  }

  public float CONNECTOR_SIZE = 20;
  public MyColorEnum BOX_COLOR = MyColorEnum.RED;

  public AbstractFilterBox(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
    inputConnector =
        new BoxConnectorIngoing(0, getHeight() / 2, CONNECTOR_SIZE, CONNECTOR_SIZE, this);
  }

  private ArrayList<AbstractFilterBox> outgoingConnections = new ArrayList<AbstractFilterBox>();

  public void addOutgoingConnection(AbstractFilterBox afb) {
    outgoingConnections.add(afb);
  }

  public void removeOutgoingConnection(AbstractFilterBox afb) {
    outgoingConnections.remove(afb);
  }

  float spanX;
  float spanY;

  public boolean draw() {
    pushStyle();
    background(COLOR);
    inputConnector.draw();
    if (!isTerminal()) {
      outputConnector.draw();
    }
    popStyle();
    if (dragging) {
      modifyPositionWithAbsoluteValue(m.touchX - spanX, m.touchY - spanY);
      inputConnector.modifyPosition(0, getHeight() / 2);
      if (!isTerminal()) outputConnector.modifyPosition(getWidth(), getHeight() / 2);
    }
    return false;
  }

  public ArrayList<AbstractFilterBox> getOutgoingConnections() {
    return outgoingConnections;
  }

  private boolean dragging = false;

  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {

    if (down) {
      dragging = true;
      setModal(true);
      spanX = m.touchX - getX0Absolute();
      spanY = m.touchY - getY0Absolute();
    } else {
      dragging = false;
      setModal(false);
    }
    return false;
  }

  public abstract boolean isTerminal();

}
