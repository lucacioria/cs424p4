package com.project4;

import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;

public class Map extends VizPanel implements TouchEnabled, EventSubscriber {

	public Map(float x0, float y0, float width, float height, VizPanel parent) {
		super(x0, y0, width, height, parent);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void eventReceived(EventName eventName, Object data) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setup() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean draw() {
		// TODO Auto-generated method stub
		return false;
	}

}
