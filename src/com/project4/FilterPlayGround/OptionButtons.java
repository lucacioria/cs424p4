package com.project4.FilterPlayGround;

import java.util.ArrayList;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizButton;
import com.anotherbrick.inthewall.VizPanel;

public class OptionButtons extends VizPanel implements TouchEnabled {

  private ArrayList<VizButton> buttons = new ArrayList<VizButton>();

  public OptionButtons(float x0, float y0, VizPanel parent) {
    super(x0, y0, 60, 100, parent);
  }

  @Override
  public void setup() {
    setupButtons();
  }

  public void setupButtons() {
    String[] buttonNames = {"Add Filter", "Add Output", "Apply", "Save"};
    for (int i = 0; i < buttonNames.length; i++) {
      VizButton button = new VizButton(0, 20 * i, getWidth(), 20, this);
      button.name = buttonNames[i] + "Button";
      button.text = buttonNames[i];
      button.setStyle(MyColorEnum.LIGHT_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY, 155f, 155f,
          10);
      button.setStylePressed(MyColorEnum.MEDIUM_GRAY, MyColorEnum.WHITE, MyColorEnum.DARK_GRAY,
          155f, 10);
      addTouchSubscriber(button);
      buttons.add(button);
    }
  }

  @Override
  public boolean draw() {
    for (VizButton button : buttons)
      button.draw();
    return false;
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    return propagateTouch(x, y, down, touchType);
  }

}
