package microkontrol.controls;

import java.util.Observable;

public class LED extends Observable{
	public static Integer OFF = 0;
	public static Integer ON = 32;
	public static Integer ONE_SHOT = 64;
	public static Integer BLINK = 96;
	public void set(Integer state ) {
		setChanged();
		notifyObservers( state);
	}



}
