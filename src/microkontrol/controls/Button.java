package microkontrol.controls;

import java.lang.reflect.Method;
import java.util.ArrayList;

import processing.core.PApplet;

public class Button extends KorgControl{
	public static final String UPDATED = "updated";

	public static final String RELEASED = "released";

	public static final String PRESSED = "pressed";

	boolean isOn = false;

	private ArrayList<ButtonListener> listeners = new ArrayList<ButtonListener>();

	private ArrayList<CallBack> pressHandlers = new ArrayList<CallBack>();

	private ArrayList<CallBack> releaseHandlers = new ArrayList<CallBack>();

	private ArrayList<CallBack> updateHandlers = new ArrayList<CallBack>();

	public final LED led = new LED();

	public void press(){
		dispatchToListeners(PRESSED);
		dispatchTo(pressHandlers);
	}
	private void dispatchTo(ArrayList<CallBack> handlers) {
		for (int i = 0; i < handlers.size(); i++) {
			CallBack handler =  handlers.get(i);
			try {
				handler.invoke();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				PApplet.println("error in pad - dispatchTo() for handler " + handler);
			}
		}

	}
	public void release(){
		dispatchToListeners(RELEASED);
		dispatchTo(releaseHandlers);
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
	public void listen(String eventName, Object object, String methodName) {
		Method handler;
		try {
			handler = object.getClass().getMethod(methodName);
			ArrayList<CallBack> list = getCallBackListByName(eventName);
			list.add(new CallBack(object,handler));
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private ArrayList<CallBack> getCallBackListByName(String eventName) {
		if(eventName.equals(PRESSED)) return pressHandlers;
		if(eventName.equals(RELEASED)) return releaseHandlers;
		if(eventName.equals(UPDATED)) return updateHandlers;
		return null;
	}

}

