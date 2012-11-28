package com.project4.map;

import java.util.ArrayList;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizButton;
import com.anotherbrick.inthewall.VizPanel;

public class FilterButtons extends VizPanel implements TouchEnabled {

  private ArrayList<VizButton> buttons = new ArrayList<VizButton>();

  public static final float height = 20;
  public static final float width = 60;

  public FilterButtons(float x0, float y0, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  @Override
  public void setup() {
    setupFilterButtons();
  }

  public void setupFilterButtons() {
    String[] buttonNames = {"filter1", "filter2", "filter3"};
    for (int i = 0; i < buttonNames.length; i++) {
      VizButton button = new VizButton(20 * i, 0, 20, getHeight(), this);
      button.name = buttonNames[i] + "Button";
      button.setStyleFromButton(m.p.getApplication().standardButton);
      button.fillAlpha = 50f;
      button.selectedFillAlpha = 200f;
      button.selectedStrokeAlpha = 200f;
      button.selectedStrokeColor = MyColorEnum.BLACK;
      switch (i + 1) {
        case 1:
          button.backgroundColor = MyColorEnum.FILTER_1;
          button.selectedBackgroundColor = MyColorEnum.FILTER_1;
          break;
        case 2:
          button.backgroundColor = MyColorEnum.FILTER_2;
          button.selectedBackgroundColor = MyColorEnum.FILTER_2;
          break;
        case 3:
          button.backgroundColor = MyColorEnum.FILTER_3;
          button.selectedBackgroundColor = MyColorEnum.FILTER_3;
          break;
        default:
          break;
      }
      button.setup();
      addTouchSubscriber(button);
      addChild(button);
      buttons.add(button);
    }
  }

  @Override
  public boolean draw() {
    for (VizButton button : buttons) {
      button.draw();
    }
    return false;
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    return propagateTouch(x, y, down, touchType);
  }

}
