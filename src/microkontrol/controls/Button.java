package microkontrol.controls;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class Button {
	boolean isOn = false;

	private ArrayList<ButtonListener> listeners = new ArrayList<ButtonListener>();

	public boolean isOn(){
		return isOn;
	}
	public void press(){
		dispatchToListeners("pressed");
	}
	public void release(){
		dispatchToListeners("released");
	}
	public void set(boolean on) {
		isOn = on;
		update();
	}
	public void toggle() {
		isOn = !isOn;
		update();
	}
	private void update() {
		dispatchToListeners("updated");
	}



	private void dispatchToListeners(String methodName) {
		for (int i = 0; i < listeners.size(); i++) {
			dispatch(listeners.get(i), methodName);
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
	public void listen(ButtonListener listener) {
		listeners.add(listener);
	}
}

