package microkontrol.controls;

import java.lang.reflect.Method;
import java.util.ArrayList;

import processing.core.PApplet;

public class Button {
	public static final String TOGGLE = "toggle";

	boolean isOn = false;

	private String mode = TOGGLE;

	private ArrayList<ButtonListener> listeners = new ArrayList<ButtonListener>();

	public void set(boolean on) {
		isOn = on;
	}
	public boolean isOn(){
		return isOn;
	}
	public void press(){
		if(mode == TOGGLE) toggle();
		for (int i = 0; i < listeners.size(); i++) {
			dispatch(listeners.get(i), "pressed");
		}
	}
	private void dispatch(ButtonListener listener, String methodName) {
		try {
			Method handler = listener.getClass().getMethod(methodName);
			handler.invoke(listener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void release(){
		for (int i = 0; i < listeners.size(); i++) {
			dispatch(listeners.get(i), "released");
		}
	}

	public void toggle() {
		isOn = !isOn;
	}
	public void listen(ButtonListener listener) {
		listeners.add(listener);
	}
}

