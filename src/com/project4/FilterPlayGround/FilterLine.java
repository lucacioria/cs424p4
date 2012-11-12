package com.project4.FilterPlayGround;

import static com.project4.FilterPlayGround.BoxConnectorType.INGOING;
import static com.project4.FilterPlayGround.BoxConnectorType.OUTGOING;

import java.util.ArrayList;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizPanel;

public class FilterLine extends VizPanel implements TouchEnabled {
  private MyColorEnum color;

  public FilterLine(float x0, float y0, float width, float height, VizPanel parent,
      MyColorEnum color) {
    super(x0, y0, width, height, parent);
    this.color = color;

  }

  private ArrayList<AbstractFilterBox> boxes = new ArrayList<AbstractFilterBox>();

  public void addBox(AbstractFilterBox afb) {
    boxes.add(afb);
    addTouchSubscriber(afb);
  }

  public void removeBox(AbstractFilterBox afb) {
    boxes.remove(afb);
    removeTouchSubscriber(afb);
  }

  private void addConection(AbstractFilterBox from, AbstractFilterBox to) {
    System.out.println("connecting..." + from.isTerminal() + to.isTerminal());
    if (!from.isTerminal()) from.addOutgoingConnection(to);
  }

  private AbstractFilterBox currentBox = null;
  private AbstractBoxConnector currentConnector = null;

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    if (down) {
      for (AbstractFilterBox afb : boxes) {
        BoxConnectorIngoing bci = (BoxConnectorIngoing) afb.getInputConnector();
        if (bci.containsPoint(x, y) && currentBox != null && currentConnector.getType() == OUTGOING) {

          addConection(currentBox, afb);
          bci.deactivate();
          currentConnector.deactivate();
          currentConnector = null;
          currentBox = null;
        } else if (bci.containsPoint(x, y)) {
          currentBox = afb;
          currentConnector = bci;
          bci.toggleActive();
        }
        if (!afb.isTerminal()) {
          BoxConnectorOutgoing bco = (BoxConnectorOutgoing) afb.getOutputConnector();
          if (bco.containsPoint(x, y) && currentBox != null
              && currentConnector.getType() == INGOING) {
            addConection(afb, currentBox);
            bco.deactivate();
            currentConnector.deactivate();
            currentConnector = null;
            currentBox = null;

          } else if (bco.containsPoint(x, y)) {
            currentBox = afb;
            currentConnector = bco;
            bco.toggleActive();
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
    pushStyle();
    stroke(color);
    strokeWeight(2);
    for (AbstractFilterBox abc : boxes) {
      abc.draw();
      AbstractFilterBox prev = abc;
      for (AbstractFilterBox next : abc.getOutgoingConnections()) {
        float sourceX = prev.getOutputConnector().getX0() + prev.getX0();
        float sourceY = prev.getOutputConnector().getY0() + prev.getY0();
        float destX = next.getInputConnector().getX0() + next.getX0();
        float destY = next.getInputConnector().getY0() + next.getY0();
        line(sourceX, sourceY, destX, destY);
        prev = next;
      }
    }
    popStyle();
    return false;
  }
}
