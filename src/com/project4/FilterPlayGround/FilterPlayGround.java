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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizList;
import com.anotherbrick.inthewall.VizList.SelectionMode;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;
import com.project4.FilterPlayGround.serializables.AbstractSerializableBox;
import com.project4.FilterPlayGround.serializables.SerializableFilterBox;
import com.project4.FilterPlayGround.serializables.SerializableTemporalBox;
import com.project4.FilterPlayGround.serializables.SerializableTerminalBox;
import com.project4.datasource.Filter;

public final class FilterPlayGround extends VizPanel implements TouchEnabled, EventSubscriber {

  private static final int TERMINAL_COUNT_LIMIT = 3;
  public static float BOX_HEIGHT = 50;
  public static float FILTER_BOX_WIDTH = 100;
  public static float TERMINAL_BOX_WIDTH = 30;
  public static MyColorEnum LINES_COLOR = MyColorEnum.BLACK;

  private ArrayList<AbstractFilterBox> boxes = new ArrayList<AbstractFilterBox>();
  private ArrayList<AbstractFilterBox> terminalBoxes = new ArrayList<AbstractFilterBox>();

  private MyColorEnum[] colors = {MyColorEnum.FILTER_1, MyColorEnum.FILTER_2, MyColorEnum.FILTER_3};
  private int terminalCount = 0;
  OptionButtons buttons = new OptionButtons(0, getHeight() / 2, this);

  private ArrayList<String> hours = new ArrayList<String>(Arrays.asList("1", "2", "3", "4", "5",
      "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21",
      "22", "23", "24"));

  Keyboard keyboard = new Keyboard(100, getHeight() - 90, 250, 90, this);
  VizList selectFromTime = new VizList(100, getHeight() - 90, 80, 90, this);;
  VizList selectToTime = new VizList(100, getHeight() - 90, 80, 90, this);;

  public FilterPlayGround(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
    DictionaryAccess.instanciate("./data/wordnet/dict");
  }

  @Override
  public void setup() {
    m.notificationCenter.registerToEvent(EventName.BUTTON_TOUCHED, this);
    m.notificationCenter.registerToEvent(EventName.TIME_UPDATED, this);
    buttons.setup();
    addTouchSubscriber(buttons);
    keyboard.setup();
    keyboard.setVisible(false);

    selectFromTime.setup(MyColorEnum.LIGHT_GRAY, MyColorEnum.MEDIUM_GRAY, 4, hours, false,
        SelectionMode.SINGLE);
    selectFromTime.setVisible(false);
    addTouchSubscriber(selectFromTime);
    selectFromTime.name = "fromList";
    selectToTime.setup(MyColorEnum.LIGHT_GRAY, MyColorEnum.MEDIUM_GRAY, 4, hours, false,
        SelectionMode.SINGLE);
    selectToTime.setVisible(false);
    selectToTime.name = "selectToTime";
    addTouchSubscriber(selectToTime);
    addTouchSubscriber(keyboard);
    loadLastConfiguration();
  }

  public AbstractFilterBox newTerminalBox() {
    if (terminalCount >= TERMINAL_COUNT_LIMIT) return null;
    terminalCount++;
    TerminalFilterBox tfb =
        new TerminalFilterBox(0, 0, TERMINAL_BOX_WIDTH, BOX_HEIGHT, colors[(terminalCount - 1)
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
    fill(MyColorEnum.LIGHT_GRAY);
    rect(0, 0, getWidth(), getHeight());
    drawBoxes(boxes);
    drawBoxes(terminalBoxes);
    buttons.draw();
    keyboard.draw();
    selectFromTime.draw();
    selectToTime.draw();
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
    if (down && !keyboard.containsPoint(x, y) && !selectFromTime.containsPoint(x, y)
        && !selectToTime.containsPoint(x, y)) {
      selectFromTime.setVisible(false);
      selectToTime.setVisible(false);

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
    if (afb.containsPoint(x, y) && !afb.isTerminal() && afb.needKeyboard()) {
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
            new TerminalFilterBox((SerializableTerminalBox) asb, colors[(terminalCount - 1)
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
      lastId = boxes.size() + terminalBoxes.size();
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
        terminalCount--;
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

  int temporalSelectedId = 0;

  @Override
  public void eventReceived(EventName eventName, Object data) {
    if (eventName.equals(EventName.BUTTON_TOUCHED)) {
      if (data.toString().equals("Add TFilterButton")) boxToBeDropped = newFilterBoxTemporal();
      if (data.toString().equals("Add FilterButton")) boxToBeDropped = newFilterBox();
      if (data.toString().equals("Add OutputButton")) boxToBeDropped = newTerminalBox();
      if (data.toString().equals("ApplyButton")) {
        ArrayList<Filter> filters = new ArrayList<Filter>();
        int i = 1;
        for (AbstractFilterBox tb : terminalBoxes) {
          filters.add(new Filter(i, tb.COLOR, new FilterBuilder().getFilterString(tb)));
          i++;
        }
        m.notificationCenter.notifyEvent(EventName.FILTERS_UPDATED, filters);
      }
      if (data.toString().equals("SaveButton")) storeCurrentConfiguration();
      if (data.toString().contains("remove")) {
        Integer toRemoveId = Integer.parseInt(data.toString().split("\\|")[0]);
        removeFilter(toRemoveId);
      }
      if (data.toString().contains("toButton")) {
        temporalSelectedId = Integer.parseInt(data.toString().split("\\|")[1]);
        log(data.toString());
        selectToTime.setVisible(true);
      }
      if (data.toString().contains("fromButton")) {
        temporalSelectedId = Integer.parseInt(data.toString().split("\\|")[1]);
        selectFromTime.setVisible(true);
      }
    }
    if (eventName.equals(EventName.TIME_UPDATED)) {
      if (data.toString().contains("fromList")) {
        selectFromTime.setVisible(false);
        ((FilterBoxTemporal) getBoxById(temporalSelectedId)).setFrom(Integer.parseInt(data
            .toString().split("\\|")[1]));
      }
      log(data.toString());
      if (data.toString().contains("selectToTime")) {
        selectToTime.setVisible(false);
        ((FilterBoxTemporal) getBoxById(temporalSelectedId)).setTo(Integer.parseInt(data.toString()
            .split("\\|")[1]));
      }
    }

  }

  private AbstractFilterBox newFilterBoxTemporal() {
    FilterBoxTemporal fb = new FilterBoxTemporal(0, 0, FILTER_BOX_WIDTH, BOX_HEIGHT, this);
    fb.setup();
    return fb;
  }
}
