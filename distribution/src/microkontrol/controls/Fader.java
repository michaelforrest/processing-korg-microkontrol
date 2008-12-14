package microkontrol.controls;

import java.util.Observable;

public class Fader extends Observable {
	int value;
	public float getProportion() {
		return (float) value / 127.0f;
	}

	public void set(int value) {
		this.value = value;
		setChanged();
		notifyObservers();
	}
}


