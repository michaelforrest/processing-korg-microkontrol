package microkontrol.controls;

import java.util.Observable;

public class Joystick extends Observable {

	private float x;
	private float y;

	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}


}
