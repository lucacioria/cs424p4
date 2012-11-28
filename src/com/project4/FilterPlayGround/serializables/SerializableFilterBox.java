package com.project4.FilterPlayGround.serializables;

public class SerializableFilterBox extends AbstractSerializableBox {

  private static final long serialVersionUID = 5811984146844183198L;


  private String filter = "";
  private boolean excludeSelected;
  private boolean synonymSelected;

  public SerializableFilterBox(Integer id, float x0, float y0, float width, float height,
      String filter, boolean c, boolean b) {
    super(id, x0, y0, width, height);
    this.filter = filter;
    this.excludeSelected = c;
    this.synonymSelected = b;
  }

  public boolean isSynonymSelected() {
    return synonymSelected;
  }

  public void setSynonymSelected(boolean synonymSelected) {
    this.synonymSelected = synonymSelected;
  }

  public boolean isExcludeSelected() {
    return excludeSelected;
  }

  public void setExcludeSelected(boolean selected) {
    this.excludeSelected = selected;
  }

  public String getFilter() {
    return filter;
  }

  public void setFilter(String filter) {
    this.filter = filter;
  }
}
