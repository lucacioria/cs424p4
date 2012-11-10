package com.anotherbrick.inthewall;

import com.anotherbrick.inthewall.VizNotificationCenter.EventName;

public interface EventSubscriber {

  public void eventReceived(EventName eventName, Object data);

}
