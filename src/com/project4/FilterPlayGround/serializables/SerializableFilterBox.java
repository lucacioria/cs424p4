package com.project4.FilterPlayGround.serializables;

public class SerializableFilterBox extends AbstractSerializableBox {

  private static final long serialVersionUID = 5811984146844183198L;


  private String filter = "";
  private boolean selected;

  public SerializableFilterBox(float x0, float y0, float width, float height, String filter,
      boolean c) {
    super(x0, y0, width, height);
    this.filter = filter;
    this.selected = c;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  public String getFilter() {
    return filter;
  }

  public void setFilter(String filter) {
    this.filter = filter;
  }
}
