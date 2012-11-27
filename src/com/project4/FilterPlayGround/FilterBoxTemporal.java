package com.project4.FilterPlayGround;

import java.util.ArrayList;
import java.util.Arrays;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.VizButton;
import com.anotherbrick.inthewall.VizList;
import com.anotherbrick.inthewall.VizList.SelectionMode;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;
import com.project4.FilterPlayGround.serializables.AbstractSerializableBox;
import com.project4.FilterPlayGround.serializables.SerializableTemporalBox;

public class FilterBoxTemporal extends AbstractFilterBox implements EventSubscriber {

  public FilterBoxTemporal(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
    instanceCount++;
    myCount = instanceCount;
    outputConnector =
        new BoxConnectorOutgoing(getWidth(), getHeight() / 2, CONNECTOR_SIZE, CONNECTOR_SIZE, this);
    m.notificationCenter.registerToEvent(EventName.BUTTON_PRESSED, this);
  }

  public FilterBoxTemporal(SerializableTemporalBox asb, VizPanel parent) {
    this(asb.getX0(), asb.getY0(), asb.getWidth(), asb.getHeight(), parent);
    this.setId(asb.getId());
  }

  private static int instanceCount = 0;
  private int myCount;
  public static MyColorEnum TEXT_COLOR = MyColorEnum.BLACK;
  public static float TEXT_X = 12;
  public static float TEXT_Y = 20;
  public static float TEXT_SIZE = 12;
  private VizList fromList;
  private VizList toList;
  private VizButton fromButton;
  private boolean toggleFromList;
  private ArrayList<String> hours = new ArrayList<String>(Arrays.asList("1", "2", "3", "4", "5",
      "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21",
      "22", "23", "24"));
  private VizButton toButton;
  private boolean toggleToList;

  @Override
  public void modifyPositionWithAbsoluteValue(float newX0, float newY0) {
    super.modifyPositionWithAbsoluteValue(newX0, newY0);
    fromButton.modifyPositionWithAbsoluteValue(newX0 + inputConnector.getWidth() / 2, newY0
        + getHeight() / 6);
    fromList.modifyPositionWithAbsoluteValue(newX0 + inputConnector.getWidth() / 2, newY0
        + getHeight() / 6);
    toButton.modifyPositionWithAbsoluteValue(newX0 + getWidth() / 2, newY0 + getHeight() / 6);
    toList.modifyPositionWithAbsoluteValue(newX0 + getWidth() / 2, newY0 + getHeight() / 6);
  }

  @Override
  public boolean draw() {
    super.draw();
    fromButton.draw();
    toButton.draw();

    if (toggleFromList) fromList.draw();
    if (toggleToList) toList.draw();
    return false;
  }

  @Override
  public String getFilter() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isTerminal() {
    return false;
  }

  @Override
  public void setup() {
    toggleFromList = false;
    toggleToList = false;
    setupToButton();

    setupFromButton();
    setupFromVizList();
    setupToVizList();
  }

  private void setupFromVizList() {
    fromList = new VizList(0, 0, getWidth(), getHeight() * 3, this);
    fromList.setup(MyColorEnum.LIGHT_GRAY, MyColorEnum.MEDIUM_GRAY, 4, hours, false,
        SelectionMode.SINGLE);
    addTouchSubscriber(fromList);
  }

  private void setupToVizList() {
    toList = new VizList(0, 0, getWidth(), getHeight() * 3, this);
    toList.setup(MyColorEnum.LIGHT_GRAY, MyColorEnum.MEDIUM_GRAY, 4, hours, false,
        SelectionMode.SINGLE);
    addTouchSubscriber(toList);
  }

  private void setupToButton() {
    toButton =
        new VizButton(0, 0, getWidth() / 2 - inputConnector.getWidth() / 2, getHeight() / 1.5f,
            this);
    toButton.setup();
    toButton.name = "toButton" + myCount;
    toButton.text = "to";
    toButton.setStyle(MyColorEnum.LIGHT_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY, 255f, 255f,
        10);
    toButton.setStylePressed(MyColorEnum.MEDIUM_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY,
        255f, 10);
    addTouchSubscriber(toButton);
  }

  private void setupFromButton() {
    fromButton =
        new VizButton(0, 0, getWidth() / 2 - inputConnector.getWidth() / 2, getHeight() / 1.5f,
            this);
    fromButton.setup();
    fromButton.name = "fromButton" + myCount;
    fromButton.text = "from";
    fromButton.setStyle(MyColorEnum.LIGHT_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY, 255f,
        255f, 10);
    fromButton.setStylePressed(MyColorEnum.MEDIUM_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY,
        255f, 10);
    addTouchSubscriber(fromButton);
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    propagateTouch(x, y, down, touchType);
    return true;
  }



  @Override
  public AbstractSerializableBox serialize() {
    return new SerializableTemporalBox(getId(), getX0(), getY0(), getWidth(), getHeight());
  }

  @Override
  public void eventReceived(EventName eventName, Object data) {
    System.out.println(data.toString());
    System.out.println(eventName.toString());
    if (eventName.equals(EventName.BUTTON_PRESSED)) {
      if (("fromButton" + myCount).equals((String) data)) {
        toggleFromList = toggleFromList ? false : true;
      }
      if (("toButton" + myCount).equals((String) data)) {
        toggleToList = toggleToList ? false : true;
      }
    }
  }
}
