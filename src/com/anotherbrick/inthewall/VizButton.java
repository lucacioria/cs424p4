package com.anotherbrick.inthewall;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;

public class VizButton extends VizPanel implements TouchEnabled, EventSubscriber {

  public String text = "";
  public int repeatRate = -1;
  // common styles
  public String name;
  private float round_a = 0;
  private float round_b = 0;
  private float round_c = 0;
  private float round_d = 0;
  private int textSize;
  // standard
  public MyColorEnum backgroundColor = MyColorEnum.WHITE;
  public MyColorEnum strokeColor = MyColorEnum.BLACK;
  public MyColorEnum textColor = MyColorEnum.WHITE;
  public float fillAlpha = 255f;
  public float strokeAlpha = 255f;
  public PImage backgroundImage;
  public PShape backgroundShape;
  // pressed
  public MyColorEnum pressedTextColor = MyColorEnum.WHITE;
  public MyColorEnum pressedStrokeColor = MyColorEnum.LIGHT_BLUE;
  public MyColorEnum pressedBackgroundColor = MyColorEnum.DARKER_BLUE;
  public float pressedStrokeAlpha = 255f;
  public float pressedFillAlpha = 255f;
  public PImage pressedBackgroundImage;
  // selected
  public MyColorEnum selectedBackgroundColor = backgroundColor;
  public MyColorEnum selectedStrokeColor;
  public float selectedFillAlpha;
  public float selectedStrokeAlpha;
  // disabled
  public MyColorEnum disabledBackgroundColor = backgroundColor;
  public MyColorEnum disabledStrokeColor;
  public float disabledFillAlpha;
  public float disabledStrokeAlpha;

  //
  public static enum StateEnum {
    SELECTED, STARDARD, DISABLED
  };

  private StateEnum state = StateEnum.STARDARD;

  private MyColorEnum shapeFillingColor;
  private boolean pressed;
  private int touchDownTime;
  private int lastRepeatAction = 1;
  public boolean eventOnPressed = false;

  public boolean isSelected() {
    return state == StateEnum.SELECTED;
  }

  public boolean isDisabled() {
    return state == StateEnum.DISABLED;
  }

  public void setSelected(boolean selected) {
    state = selected ? StateEnum.SELECTED : StateEnum.STARDARD;
  }

  public void setDisabled(boolean disabled) {
    state = disabled ? StateEnum.DISABLED : StateEnum.STARDARD;
  }

  public VizButton(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  public void setStyleFromButton(VizButton b) {
    this.backgroundColor = b.backgroundColor;
    this.strokeColor = b.strokeColor;
    this.fillAlpha = b.fillAlpha;
    this.strokeAlpha = b.strokeAlpha;
    this.textSize = b.textSize;
    this.textColor = b.textColor;
   
    this.selectedBackgroundColor = b.selectedBackgroundColor;
    this.selectedStrokeColor = b.selectedStrokeColor;
    this.selectedFillAlpha = b.selectedFillAlpha;
    this.selectedStrokeAlpha = b.selectedStrokeAlpha;
    
    this.disabledBackgroundColor = b.disabledBackgroundColor; 
    this.disabledStrokeColor = b.disabledStrokeColor;
    this.disabledFillAlpha = b.disabledFillAlpha;
    this.disabledStrokeAlpha = b.disabledStrokeAlpha;
    
    this.pressedBackgroundColor = b.pressedBackgroundColor;
    this.pressedStrokeColor = b.pressedStrokeColor;
    this.pressedFillAlpha = b.pressedFillAlpha;
    this.pressedStrokeAlpha = b.pressedStrokeAlpha;
    this.pressedTextColor = b.pressedTextColor;
  }
  
  public void setStyle(MyColorEnum backgroundColor, MyColorEnum textColor, MyColorEnum strokeColor,
      float fillAlpha, float strokeAlpha, int textSize) {
    this.backgroundColor = backgroundColor;
    this.strokeColor = strokeColor;
    this.fillAlpha = fillAlpha;
    this.strokeAlpha = strokeAlpha;
    this.textSize = textSize;
    this.textColor = textColor;
  }

  public void setStyleSelected(MyColorEnum backgroundColor, MyColorEnum textColor,
      MyColorEnum strokeColor, float fillAlpha, float strokeAlpha, int textSize) {
    this.selectedBackgroundColor = backgroundColor;
    this.selectedStrokeColor = strokeColor;
    this.selectedFillAlpha = fillAlpha;
    this.selectedStrokeAlpha = strokeAlpha;
  }

  public void setStyleDisabled(MyColorEnum backgroundColor, MyColorEnum textColor,
      MyColorEnum strokeColor, float fillAlpha, float strokeAlpha, int textSize) {
    this.disabledBackgroundColor = backgroundColor;
    this.disabledStrokeColor = strokeColor;
    this.disabledFillAlpha = fillAlpha;
    this.disabledStrokeAlpha = strokeAlpha;
  }

  public void setRoundedCornerd(float a, float b, float c, float d) {
    this.round_a = a;
    this.round_b = b;
    this.round_c = c;
    this.round_d = d;
  }

  public void setImage(String imageName, String ext) {
    this.backgroundImage = c.getImage(imageName, ext);
  }

  public void setImagePressed(String imageName, String ext) {
    this.pressedBackgroundImage = c.getImage(imageName, ext);
  }

  public void setShape(String imageName) {
    this.backgroundShape = c.getShape(imageName);
  }

  public void setSelectedStrokeColor(MyColorEnum color) {
    this.selectedStrokeColor = color;
  }

  public void setStylePressed(MyColorEnum backgroundColor, MyColorEnum strokeColor,
      MyColorEnum pressedTextColor, float fillAlpha, float strokeAlpha) {
    this.pressedBackgroundColor = backgroundColor;
    this.pressedStrokeColor = strokeColor;
    this.pressedFillAlpha = fillAlpha;
    this.pressedStrokeAlpha = strokeAlpha;
    this.pressedTextColor = textColor;
  }

  @Override
  public boolean draw() {
    if (pressed) {
      drawPressed();
      if (repeatRate > 0) {
        if ((millis() - touchDownTime) / repeatRate > lastRepeatAction) {
          sendTouchedEvent();
          lastRepeatAction++;
        }
      }
      return false;
    }
    switch (state) {
      case STARDARD:
        drawStandard();
        break;
      case SELECTED:
        drawSelected();
        break;
      case DISABLED:
        drawDisabled();
        break;
    }
    drawTextCentered();
    return false;
  }

  private void drawPressed() {
    pushStyle();
    if (pressedBackgroundImage != null) {
      image(pressedBackgroundImage, 0, 0);
    } else if (backgroundShape != null) {
      backgroundShape.disableStyle();
      fill(pressedBackgroundColor);
      stroke(pressedStrokeColor);
      shape(backgroundShape, 0, 0);
    } else {
      fill(pressedBackgroundColor, pressedFillAlpha);
      stroke(pressedStrokeColor, pressedStrokeAlpha);
      rect(0, 0, getWidth(), getHeight(), round_a, round_b, round_c, round_d);
    }
    popStyle();
  }

  private void drawStandard() {
    pushStyle();
    if (backgroundImage != null) {
      image(backgroundImage, 0, 0);
    } else if (backgroundShape != null) {
      backgroundShape.enableStyle();
      shape(backgroundShape, 0, 0);
    } else {
      fill(backgroundColor, fillAlpha);
      stroke(strokeColor, strokeAlpha);
      rect(0, 0, getWidth(), getHeight(), round_a, round_b, round_c, round_d);
    }
    popStyle();
  }

  private void drawSelected() {
    pushStyle();
    if (backgroundShape != null) {
      backgroundShape.disableStyle();
      if (shapeFillingColor != null) {
        fill(shapeFillingColor);
      } else {
        fill(MyColorEnum.RED);
      }
      stroke(MyColorEnum.WHITE);
      shape(backgroundShape, 0, 0);
    } else {
      fill(selectedBackgroundColor, selectedFillAlpha);
      strokeWeight(1);
      stroke(selectedStrokeColor, selectedStrokeAlpha);
      rect(0, 0, getWidth(), getHeight(), round_a, round_b, round_c, round_d);
    }

    popStyle();
  }

  private void drawDisabled() {
    pushStyle();
    fill(disabledBackgroundColor, disabledFillAlpha);
    stroke(disabledStrokeColor, disabledStrokeAlpha);
    rect(0, 0, getWidth(), getHeight(), round_a, round_b, round_c, round_d);
    popStyle();
  }

  public void drawTextCentered() {
    if (text.length() > 0) {
      pushStyle();
      textSize(textSize);
      fill(textColor);
      textAlign(PApplet.CENTER, PApplet.CENTER);
      text(text, getWidth() / 2f, getHeight() / 2f);
      popStyle();
    }
  }

  public void setText(String string) {
    this.text = string;
  }

  public String getText() {
    return text;
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    if (state == StateEnum.DISABLED) return false;
    if (down) {
      pressed = true;
      touchDownTime = millis();
      sendPressedEvent();
    } else {
      lastRepeatAction = 1;
      pressed = false;
      sendTouchedEvent();
    }
    return true;
  }

  private void sendTouchedEvent() {
    m.notificationCenter.notifyEvent(EventName.BUTTON_TOUCHED, name);
  }

  private void sendPressedEvent() {
    m.notificationCenter.notifyEvent(EventName.BUTTON_PRESSED, name);
  }

  public void setShapeFillingColor(MyColorEnum shapeFillingColor) {
    this.shapeFillingColor = shapeFillingColor;
  }

  @Override
  public void setup() {
    m.notificationCenter.registerToEvent(EventName.BUTTON_SELECTED, this);
    m.notificationCenter.registerToEvent(EventName.BUTTON_DESELECTED, this);
  }

  @Override
  public void eventReceived(EventName eventName, Object data) {
    if (eventName == EventName.BUTTON_SELECTED && data.toString().equals(name)) {
      setSelected(true);
    } else if (eventName == EventName.BUTTON_DESELECTED && data.toString().equals(name)) {
      setSelected(false);
    }

  }

}
