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
  private ArrayList<AbstractFilterBox> terminalBoxes = new ArrayList<AbstractFilterBox>();

  private MyColorEnum[] colors = {MyColorEnum.DARKERER_ORANGE, MyColorEnum.LIGHT_GREEN};
  private int terminalCount = 0;
  OptionButtons buttons = new OptionButtons(0, getHeight() / 2, this);

  Keyboard keyboard = new Keyboard(100, getHeight() - 90, 250, 90, this);

  public FilterPlayGround(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  @Override
  public void setup() {
    m.notificationCenter.registerToEvent(EventName.BUTTON_TOUCHED, this);
    buttons.setup();
    addTouchSubscriber(buttons);
    keyboard.setup();
    addTouchSubscriber(keyboard);
  }


  public AbstractFilterBox newTerminalBox() {
    if (terminalCount >= TERMINAL_COUNT_LIMIT) return null;
    terminalCount++;
    TerminalFilterBox tfb =
        new TerminalFilterBox(0, 0, TERMINAL_BOX_WIDTH, BOX_HEIGHT, colors[terminalCount
            % colors.length], this);
    tfb.setup();
    return tfb;
  }

  public void addBox(AbstractFilterBox box, float x, float y) {
    box.modifyPositionWithAbsoluteValue(x, y);
    if (box.isTerminal())
      terminalBoxes.add(box);
    else
      boxes.add(box);
    addTouchSubscriber(box);
  }

  public AbstractFilterBox newFilterBox() {
    FilterBox fb = new FilterBox(0, 0, FILTER_BOX_WIDTH, BOX_HEIGHT, this);
    fb.setup();
    return fb;

  }

  @Override
  public boolean draw() {
    pushStyle();
    fill(MyColorEnum.DARK_BLUE);
    rect(0, 0, getWidth(), getHeight());
    drawBoxes(boxes);
    drawBoxes(terminalBoxes);
    buttons.draw();
    keyboard.draw();
    popStyle();
    return false;
  }

  private void drawBoxes(ArrayList<AbstractFilterBox> boxes) {
    pushStyle();
    stroke(LINES_COLOR);
    strokeWeight(2);
    for (AbstractFilterBox abc : boxes) {
      abc.draw();
      AbstractFilterBox prev = abc;
      for (AbstractFilterBox next : abc.getIngoingConnections()) {
        float sourceX = prev.getInputConnector().getX0() + prev.getX0();
        float sourceY = prev.getInputConnector().getY0() + prev.getY0();
        float destX = next.getOutputConnector().getX0() + next.getX0();
        float destY = next.getOutputConnector().getY0() + next.getY0();
        line(sourceX, sourceY, destX, destY);
        prev = next;
      }
    }
    popStyle();
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    if (down) {

      for (AbstractFilterBox afb : boxes) {
        handleConnectorTouch(x, y, afb, OUTGOING);
        handleConnectorTouch(x, y, afb, INGOING);
      }
      for (AbstractFilterBox afb : terminalBoxes) {
        handleConnectorTouch(x, y, afb, OUTGOING);
      }

      if (boxToBeDropped != null) {
        addBox(boxToBeDropped, x, y);
        boxToBeDropped = null;
      }
    }
    propagateTouch(x, y, down, touchType);
    return false;
  }

  private AbstractFilterBox currentBox = null;
  private AbstractBoxConnector currentConnector = null;

  private void handleConnectorTouch(float x, float y, AbstractFilterBox afb,
      BoxConnectorType matchAgainst) {
    if (afb.equals(currentBox)) return;
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
      connector.activate();
    }
  }

  private void addConection(AbstractFilterBox from, AbstractFilterBox to) {
    if (!from.isTerminal()) to.addIngoingConnection(from);
  }

  AbstractFilterBox boxToBeDropped = null;

  @Override
  public void eventReceived(EventName eventName, Object data) {
    if (eventName.equals(EventName.BUTTON_TOUCHED)) {
      if (data.toString().equals("Add FilterButton")) boxToBeDropped = newFilterBox();
      if (data.toString().equals("Add OutputButton")) boxToBeDropped = newTerminalBox();
    }

  }
}
