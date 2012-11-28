package com.project4.FilterPlayGround;

import static com.project4.FilterPlayGround.BoxConnectorType.INGOING;
import static com.project4.FilterPlayGround.BoxConnectorType.OUTGOING;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;
import com.project4.FilterPlayGround.serializables.AbstractSerializableBox;
import com.project4.FilterPlayGround.serializables.SerializableFilterBox;
import com.project4.FilterPlayGround.serializables.SerializableTemporalBox;
import com.project4.FilterPlayGround.serializables.SerializableTerminalBox;
import com.sun.swing.internal.plaf.synth.resources.synth;

public final class FilterPlayGround extends VizPanel implements TouchEnabled, EventSubscriber {

  private static final int TERMINAL_COUNT_LIMIT = 3;
  public static float BOX_HEIGHT = 50;
  public static float FILTER_BOX_WIDTH = 100;
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
    DictionaryAccess.instanciate("./data/wordnet/dict");
  }

  @Override
  public void setup() {
    m.notificationCenter.registerToEvent(EventName.BUTTON_TOUCHED, this);
    buttons.setup();
    addTouchSubscriber(buttons);
    keyboard.setup();
    keyboard.setVisible(false);
    addTouchSubscriber(keyboard);
    loadLastConfiguration();
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

  private int lastId = 0;

  public void addBox(AbstractFilterBox box, float x, float y) {
    box.modifyPositionWithAbsoluteValue(x, y);
    if (box.isTerminal())
      terminalBoxes.add(box);
    else
      boxes.add(box);
    box.setFocus(true);
    box.setId(lastId);
    lastId++;
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
      for (AbstractFilterBox next : abc.getIngoingConnections()) {
        float sourceX = abc.getInputConnector().getX0() + abc.getX0();
        float sourceY = abc.getInputConnector().getY0() + abc.getY0();
        float destX = next.getOutputConnector().getX0() + next.getX0();
        float destY = next.getOutputConnector().getY0() + next.getY0();
        line(sourceX, sourceY, destX, destY);
      }
    }
    popStyle();
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    if (down && !keyboard.containsPoint(x, y)) {
      keyboard.setVisible(false);
      for (AbstractFilterBox afb : boxes) {
        handleConnectorTouch(x, y, afb, OUTGOING);
        handleConnectorTouch(x, y, afb, INGOING);
        handleFocus(x, y, afb);
      }
      for (AbstractFilterBox afb : terminalBoxes) {
        handleConnectorTouch(x, y, afb, OUTGOING);
        handleFocus(x, y, afb);
      }

      if (boxToBeDropped != null) {
        addBox(boxToBeDropped, x, y);
        boxToBeDropped = null;
      }
    }
    propagateTouch(x, y, down, touchType);
    return false;
  }

  private void handleFocus(float x, float y, AbstractFilterBox afb) {
    if (afb.containsPoint(x, y) && !afb.isTerminal()) {
      afb.setFocus(true);
      keyboard.setVisible(true);
    } else
      afb.setFocus(false);
  }

  private AbstractFilterBox currentBox = null;
  private AbstractBoxConnector currentConnector = null;

  private boolean handleConnectorTouch(float x, float y, AbstractFilterBox afb,
      BoxConnectorType matchAgainst) {
    if (afb.equals(currentBox)) return false;
    AbstractBoxConnector connector =
        matchAgainst == INGOING ? afb.getOutputConnector() : afb.getInputConnector();
    if (!connector.containsPoint(x, y)) return false;
    if (currentBox != null && currentConnector.getType() == matchAgainst) {
      System.out.println("connecting " + currentBox.getFilter() + "---" + afb.getFilter());
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
      System.out.println("activated " + afb.getFilter());
    }
    return true;
  }

  private void addConection(AbstractFilterBox from, AbstractFilterBox to) {
    if (!from.isTerminal()) to.addIngoingConnection(from);
  }

  public boolean storeCurrentConfiguration() {
    try {
      FileOutputStream fos = new FileOutputStream(new File("./data/filters/filter1.ser"));
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      ArrayList<AbstractSerializableBox> sBoxes = new ArrayList<AbstractSerializableBox>();
      ArrayList<AbstractSerializableBox> sTerminalBoxes = new ArrayList<AbstractSerializableBox>();
      HashMap<Integer, ArrayList<Integer>> connections = new HashMap<Integer, ArrayList<Integer>>();
      for (AbstractFilterBox afb : boxes) {
        connections.put(afb.getId(), new ArrayList<Integer>());
        sBoxes.add(afb.serialize());
        for (AbstractFilterBox a : afb.getIngoingConnections()) {
          connections.get(afb.getId()).add(a.getId());
        }
      }
      for (AbstractFilterBox afb : terminalBoxes) {
        connections.put(afb.getId(), new ArrayList<Integer>());
        sTerminalBoxes.add(afb.serialize());
        for (AbstractFilterBox a : afb.getIngoingConnections()) {
          connections.get(afb.getId()).add(a.getId());
        }
      }
      oos.writeObject(sBoxes);
      oos.writeObject(sTerminalBoxes);
      oos.writeObject(connections);
      oos.close();
      fos.close();
    } catch (IOException ex) {
      return false;
    }
    return true;
  }

  @SuppressWarnings("unchecked")
  public boolean loadLastConfiguration() {
    try {
      FileInputStream fis = new FileInputStream(new File("./data/filters/filter1.ser"));
      ObjectInputStream ois = new ObjectInputStream(fis);
      ArrayList<AbstractSerializableBox> sBoxes =
          (ArrayList<AbstractSerializableBox>) ois.readObject();
      ArrayList<AbstractSerializableBox> sTerminalBoxes =
          (ArrayList<AbstractSerializableBox>) ois.readObject();
      HashMap<Integer, ArrayList<Integer>> connections =
          (HashMap<Integer, ArrayList<Integer>>) ois.readObject();

      terminalCount = terminalBoxes.size();
      for (AbstractSerializableBox asb : sBoxes) {
        AbstractFilterBox afb = null;
        if (asb instanceof SerializableFilterBox) {
          afb = new FilterBox((SerializableFilterBox) asb, this);
        }
        if (asb instanceof SerializableTemporalBox) {
          afb = new FilterBoxTemporal((SerializableTemporalBox) asb, this);
        }
        addTouchSubscriber(afb);
        this.boxes.add(afb);
      }
      for (AbstractSerializableBox asb : sTerminalBoxes) {
        terminalCount++;
        AbstractFilterBox afb =
            new TerminalFilterBox((SerializableTerminalBox) asb, colors[terminalCount
                % colors.length], this);
        afb.setup();
        addTouchSubscriber(afb);
        this.terminalBoxes.add(afb);
      }
      for (AbstractFilterBox asb : boxes) {
        for (Integer i : connections.get(asb.getId())) {
          addConection(getBoxById(i), asb);
        }
      }
      for (AbstractFilterBox asb : terminalBoxes) {
        for (Integer i : connections.get(asb.getId())) {
          addConection(getBoxById(i), asb);
        }
      }
      ois.close();
      fis.close();
    } catch (IOException e) {
      return false;
    } catch (ClassNotFoundException e) {
      return false;
    }
    return true;
  }

  private AbstractFilterBox getBoxById(Integer i) {
    for (AbstractFilterBox afb : boxes) {
      if (afb.getId().equals(i)) return afb;
    }
    for (AbstractFilterBox afb : terminalBoxes) {
      if (afb.getId().equals(i)) return afb;
    }
    return null;
  }

  private void removeFilter(Integer id) {
    for (Iterator<AbstractFilterBox> it = boxes.iterator(); it.hasNext();) {
      AbstractFilterBox a = it.next();
      if (a.getId().equals(id)) {
        it.remove();

      } else {
        for (Iterator<AbstractFilterBox> it2 = a.getIngoingConnections().iterator(); it2.hasNext();) {
          AbstractFilterBox in = it2.next();
          if (in.getId().equals(id)) {
            it2.remove();
          }
        }
      }
    }
    for (Iterator<AbstractFilterBox> it = terminalBoxes.iterator(); it.hasNext();) {
      AbstractFilterBox a = it.next();
      if (a.getId().equals(id)) {
        it.remove();

      } else {
        for (Iterator<AbstractFilterBox> it2 = a.getIngoingConnections().iterator(); it2.hasNext();) {
          AbstractFilterBox in = it2.next();
          if (in.getId().equals(id)) {
            it2.remove();
          }
        }
      }
    }

  }

  AbstractFilterBox boxToBeDropped = null;

  @Override
  public void eventReceived(EventName eventName, Object data) {
    if (eventName.equals(EventName.BUTTON_TOUCHED)) {
      if (data.toString().equals("Add TFilterButton")) boxToBeDropped = newFilterBoxTemporal();
      if (data.toString().equals("Add FilterButton")) boxToBeDropped = newFilterBox();
      if (data.toString().equals("Add OutputButton")) boxToBeDropped = newTerminalBox();
      if (data.toString().equals("ApplyButton")) {
        log(boxes.get(0).getFilter());
        // ArrayList<Filter> filters = new ArrayList<Filter>();
        // int i = 0;
        // for (AbstractFilterBox tb : terminalBoxes) {
        // filters.add(new Filter(i, tb.COLOR, new FilterBuilder().getFilterString(tb)));
        // i++;
        // }
        // int[] interval = {1, 2};
        // TreeMap<Filter, ArrayList<Tweet>> tweets =
        // DataSourceSQL.instance.getTweets(filters, interval);
        // m.notificationCenter.notifyEvent(EventName.FILTERS_UPDATED, tweets);
      }
      if (data.toString().equals("SaveButton")) storeCurrentConfiguration();
      if (data.toString().contains("remove")) {
        Integer toRemoveId = Integer.parseInt(data.toString().split("\\|")[0]);
        removeFilter(toRemoveId);
      }
    }

  }

  private AbstractFilterBox newFilterBoxTemporal() {
    FilterBoxTemporal fb = new FilterBoxTemporal(0, 0, FILTER_BOX_WIDTH, BOX_HEIGHT, this);
    fb.setup();
    return fb;
  }
}
