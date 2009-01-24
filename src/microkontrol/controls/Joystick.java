package microkontrol.controls;

import java.util.Observable;

public class Joystick extends Observable {

	private static final String MOVED = "moved";
	private float x;
	private float y;

	public void set(float x, float y) {
		this.x = x;
		this.y = y;
		setChanged();
		notifyObservers(MOVED);
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}


}
