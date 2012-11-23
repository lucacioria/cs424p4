package com.project4.FilterPlayGround;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;
import com.project4.FilterPlayGround.serializables.AbstractSerializableBox;
import com.project4.FilterPlayGround.serializables.SerializableFilterBox;

public class FilterBox extends AbstractFilterBox implements EventSubscriber {

  public FilterBox(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);

    outputConnector =
        new BoxConnectorOutgoing(getWidth(), getHeight() / 2, CONNECTOR_SIZE, CONNECTOR_SIZE, this);
    m.notificationCenter.registerToEvent(EventName.BUTTON_PRESSED, this);
  }

  public FilterBox(SerializableFilterBox asb, VizPanel parent) {
    this(asb.getX0(), asb.getY0(), asb.getWidth(), asb.getHeight(), parent);
    this.content = asb.getFilter();
  }

  public static MyColorEnum TEXT_COLOR = MyColorEnum.BLACK;
  public static float TEXT_X = 12;
  public static float TEXT_Y = 20;
  public static float TEXT_SIZE = 12;

  private String content = "";

  @Override
  public boolean isTerminal() {
    return false;
  }

  @Override
  public void setup() {

  }

  @Override
  public boolean draw() {
    super.draw();
    pushStyle();
    fill(TEXT_COLOR);
    textSize(TEXT_SIZE);
    text(content, TEXT_X, TEXT_Y);
    popStyle();
    return false;
  }

  @Override
  public String getFilter() {
    return content;
  }

  @Override
  public void eventReceived(EventName eventName, Object data) {
    String d = (String) data;
    if (hasFocus() && d.contains("keyboard|")) {
      String symbol = d.split("\\|")[1];
      content += symbol;
      System.out.println(symbol);
    }
  }

  @Override
  public AbstractSerializableBox serialize() {
    return new SerializableFilterBox(getX0(), getY0(), getWidth(), getHeight(), getFilter());
  }
}
