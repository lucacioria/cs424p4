package com.project4.FilterPlayGround.serializables;

public class SerializableFilterBox extends AbstractSerializableBox {

  private static final long serialVersionUID = 5811984146844183198L;

  public SerializableFilterBox() {
    super();
  }

  private String filter = "";

  public SerializableFilterBox(float x0, float y0, float width, float height, String filter) {
    super(x0, y0, width, height);
    this.filter = filter;
  }

  public String getFilter() {
    return filter;
  }

  public void setFilter(String filter) {
    this.filter = filter;
  }
}
