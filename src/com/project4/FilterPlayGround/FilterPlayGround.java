package com.project4.FilterPlayGround;

import static com.project4.FilterPlayGround.BoxConnectorType.INGOING;
import static com.project4.FilterPlayGround.BoxConnectorType.OUTGOING;

import java.util.ArrayList;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;

public final class FilterPlayGround extends VizPanel implements TouchEnabled, EventSubscriber {

  private static final int TERMINAL_COUNT_LIMIT = 3;
  public static float BOX_HEIGHT = 30;
  public static float FILTER_BOX_WIDTH = 80;
  public static float TERMINAL_BOX_WIDTH = 30;
  public static MyColorEnum LINES_COLOR = MyColorEnum.DARK_WHITE;

  private ArrayList<AbstractFilterBox> boxes = new ArrayList<AbstractFilterBox>();

  private MyColorEnum[] colors = {MyColorEnum.DARKERER_ORANGE, MyColorEnum.LIGHT_GREEN};
  private int terminalCount = 0;
  OptionButtons buttons = new OptionButtons(0, getHeight() / 2, this);

  public FilterPlayGround(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  @Override
  public void setup() {
    m.notificationCenter.registerToEvent(EventName.BUTTON_TOUCHED, this);
    buttons.setup();
    addTouchSubscriber(buttons);
  }

  public void addTerminalBox() {
    if (terminalCount >= TERMINAL_COUNT_LIMIT) return;
    terminalCount++;
    TerminalFilterBox tfb =
        new TerminalFilterBox(getRandomX(), getRandomY(), TERMINAL_BOX_WIDTH, BOX_HEIGHT,
            colors[terminalCount % colors.length], this);
    tfb.setup();
    boxes.add(tfb);
    addTouchSubscriber(tfb);
  }

  public void addFilterBox() {
    FilterBox fb = new FilterBox(getRandomX(), getRandomY(), FILTER_BOX_WIDTH, BOX_HEIGHT, this);
    fb.setup();
    boxes.add(fb);
    addTouchSubscriber(fb);
  }

  @Override
  public boolean draw() {
    pushStyle();
    fill(MyColorEnum.DARK_BLUE);
    rect(0, 0, getWidth(), getHeight());
    drawBoxes();
    buttons.draw();
    popStyle();
    return false;
  }

  private void drawBoxes() {
    pushStyle();
    stroke(LINES_COLOR);
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
  }

  private float getRandomX() {
    return (float) (Math.random() * getWidth());
  }

  private float getRandomY() {
    return (float) (Math.random() * getHeight());
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    if (down) {
      for (AbstractFilterBox afb : boxes) {
        handleConnectorTouch(x, y, afb, OUTGOING);
        if (!afb.isTerminal()) {
          handleConnectorTouch(x, y, afb, INGOING);
        }
      }
    }
    propagateTouch(x, y, down, touchType);
    return false;
  }

  private AbstractFilterBox currentBox = null;
  private AbstractBoxConnector currentConnector = null;

  private void handleConnectorTouch(float x, float y, AbstractFilterBox afb,
      BoxConnectorType matchAgainst) {
    AbstractBoxConnector connector =
        matchAgainst == INGOING ? afb.getOutputConnector() : afb.getInputConnector();
    if (!connector.containsPoint(x, y)) return;
    if (currentBox != null && currentConnector.getType() == matchAgainst) {
      if (matchAgainst == INGOING) addConection(afb, currentBox);
      if (matchAgainst == OUTGOING) addConection(currentBox, afb);
      connector.deactivate();
      currentConnector.deactivate();
      currentConnector = null;
      currentBox = null;
    } else {
      currentBox = afb;
      currentConnector = connector;
      connector.toggleActive();
    }
  }

  private void addConection(AbstractFilterBox from, AbstractFilterBox to) {
    System.out.println("connecting..." + from.isTerminal() + to.isTerminal());
    if (!from.isTerminal()) from.addOutgoingConnection(to);
  }

  @Override
  public void eventReceived(EventName eventName, Object data) {
    if (eventName.equals(EventName.BUTTON_TOUCHED)) {
      if (data.toString().equals("Add FilterButton")) addFilterBox();
      if (data.toString().equals("Add OutputButton")) addTerminalBox();
    }

  }
}
