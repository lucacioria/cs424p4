package com.project4.FilterPlayGround.serializables;

public class SerializableTemporalBox extends AbstractSerializableBox {

  private static final long serialVersionUID = -7848369794849629797L;
  int from, to;

  public int getFrom() {
    return from;
  }

  public void setFrom(int from) {
    this.from = from;
  }

  public int getTo() {
    return to;
  }

  public void setTo(int to) {
    this.to = to;
  }

  public SerializableTemporalBox(Integer id, float x0, float y0, float width, float height,
      int from, int to) {
    super(id, x0, y0, width, height);
  }
}
