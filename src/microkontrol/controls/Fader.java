package microkontrol.controls;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class Fader{
	int value;
	private ArrayList<FaderListener> listeners = new ArrayList<FaderListener>();

	public float getProportion() {
		return (float) value / 127.0f;
	}

	public void set(int value) {
		this.value = value;
		for (int i = 0; i < listeners.size(); i++) {
			FaderListener listener = (FaderListener) listeners.get(i);
			dispatchMoved(listener);
		}
	}
	private void dispatchMoved(FaderListener listener) {
		try {
			Method handler = listener.getClass().getMethod("moved",new Class[] { Float.class });
			handler.invoke(listener, new Object[] { getProportion() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void listen(FaderListener listener) {
		listeners.add(listener);
	}
}


