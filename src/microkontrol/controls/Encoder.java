package microkontrol.controls;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class Encoder {
	private int value = 0;
	private int change;

	private ArrayList<EncoderListener> listeners = new ArrayList<EncoderListener>();

	public void listen(EncoderListener listener) {
		listeners.add(listener);
	}

	public void set(int change) {
		this.change = change;
		value += change;
		for (int i = 0; i < listeners.size(); i++) {
			EncoderListener listener = (EncoderListener) listeners.get(i);
			dispatchMoved(listener);
		}
	}

	private void dispatchMoved(EncoderListener listener) {
		try {
			Method handler = listener.getClass().getMethod("moved",new Class[] { Integer.class });
			handler.invoke(listener, new Object[] { change });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getChange() {
		return change;
	}
	public int getValue(){
		return value;
	}
	public void setValue(int value){
		this.value = value;
	}

}
