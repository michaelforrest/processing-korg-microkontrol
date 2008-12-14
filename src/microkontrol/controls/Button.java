package microkontrol.controls;

import java.util.Observable;

import processing.core.PApplet;

public class Button extends Observable {
	boolean isOn = false;

	public void set(boolean on) {
		isOn = on;
		update();
	}
	public boolean isOn(){
		return isOn;
	}
	public void toggle() {
		PApplet.println("Toggling button");
		isOn = !isOn;
		update();
	}

	void update() {
		setChanged();
		notifyObservers();
	}
}

