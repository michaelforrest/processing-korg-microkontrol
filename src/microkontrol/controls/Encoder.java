package microkontrol.controls;

import java.util.Observable;

public class Encoder extends Observable {

	private int change;

	public void set(int change) {
		this.change = change;
		setChanged();
		notifyObservers();
	}

	public int getChange() {
		return change;
	}


}
