package com.example.app;

import com.anotherbrick.inthewall.EventSubscriber;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizNotificationCenter.EventName;
import com.anotherbrick.inthewall.VizPanel;
import com.anotherbrick.inthewall.FilterPlayGround.FilterPlayGround;

public class Application extends VizPanel implements TouchEnabled,
	EventSubscriber {

    public Application(float x0, float y0, float width, float height) {
	super(x0, y0, width, height);
    }

    FilterPlayGround fpg;

    @Override
    public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
	return propagateTouch(x, y, down, touchType);
    }

    @Override
    public void setup() {
	m.notificationCenter.registerToEvent(EventName.BUTTON_TOUCHED, this);
	if (c.initializeVisualization)
	    initializeVisualization();
	setupFilterPlayGround();
    }

    private void setupFilterPlayGround() {
	fpg = new FilterPlayGround(0, 0, getWidth(), getHeight(), this);
	fpg.setup();
	addTouchSubscriber(fpg);
	fpg.addTerminalBox();
    }

    private void initializeVisualization() {
    }

    @Override
    public boolean draw() {
	pushStyle();
	fpg.draw();
	popStyle();
	return false;
    }

    @Override
    public void eventReceived(EventName eventName, Object data) {
    }

}
