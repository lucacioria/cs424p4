package com.project4.FilterPlayGround;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.VizButton;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;
import com.project4.FilterPlayGround.serializables.AbstractSerializableBox;
import com.project4.FilterPlayGround.serializables.SerializableTemporalBox;

public class FilterBoxTemporal extends AbstractFilterBox implements EventSubscriber {

  public FilterBoxTemporal(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
    outputConnector =
        new BoxConnectorOutgoing(getWidth(), getHeight() / 2, CONNECTOR_SIZE, CONNECTOR_SIZE, this);
    m.notificationCenter.registerToEvent(EventName.BUTTON_PRESSED, this);
  }

  public FilterBoxTemporal(SerializableTemporalBox asb, VizPanel parent) {
    this(asb.getX0(), asb.getY0(), asb.getWidth(), asb.getHeight(), parent);
    setup();
    this.setId(asb.getId());
  }

  public static MyColorEnum TEXT_COLOR = MyColorEnum.BLACK;
  public static float TEXT_X = 10;
  public static float TEXT_Y = 20;
  public static float TEXT_SIZE = 12;
  public static float FROM_X = 15;
  public static float TO_X = 40;
  public static float BUTTONS_Y = 25;
  public static float BUTTONS_SIZE = 30;
  public static float REMOVE_BUTTON_X = 60;
  private VizButton fromButton;
  private VizButton toButton;

  @Override
  public void modifyPositionWithAbsoluteValue(float newX0, float newY0) {
    super.modifyPositionWithAbsoluteValue(newX0, newY0);
    fromButton.modifyPosition(FROM_X, BUTTONS_Y);
    toButton.modifyPosition(TO_X, BUTTONS_Y);

  }

  @Override
  public boolean needKeyboard() {
    return false;
  }

  @Override
  public boolean draw() {
    super.draw();
    fromButton.draw();
    toButton.draw();
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
    super.setup();
    setupToButton();
    setupFromButton();
  }

  private void setupToButton() {
    toButton = new VizButton(0, 0, BUTTONS_SIZE, BUTTONS_SIZE, this);
    toButton.setup();
    toButton.name = "toButton|" + getId();
    toButton.text = "to";
    toButton.setVisible(true);
    toButton.setStyle(MyColorEnum.LIGHT_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY, 255f, 255f,
        10);
    toButton.setStylePressed(MyColorEnum.MEDIUM_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY,
        255f, 10);
    addTouchSubscriber(toButton);
  }

  private void setupFromButton() {
    fromButton = new VizButton(0, 0, BUTTONS_SIZE, BUTTONS_SIZE, this);
    fromButton.setup();
    fromButton.name = "fromButton|" + getId();
    fromButton.text = "from";
    fromButton.setVisible(true);

    fromButton.setStyle(MyColorEnum.LIGHT_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY, 255f,
        255f, 10);
    fromButton.setStylePressed(MyColorEnum.MEDIUM_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY,
        255f, 10);
    addTouchSubscriber(fromButton);
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    propagateTouch(x, y, down, touchType);
    return super.touch(x, y, down, touchType);
  }

  @Override
  public AbstractSerializableBox serialize() {
    return new SerializableTemporalBox(getId(), getX0(), getY0(), getWidth(), getHeight());
  }

  @Override
  public void eventReceived(EventName eventName, Object data) {
    System.out.println(data.toString());
    System.out.println(eventName.toString());
  }
}
