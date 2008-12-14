package microkontrol.controls;

import java.util.Observable;

import processing.core.PApplet;

public class LCD extends Observable {
	public static String GREEN = "green";
	public static String ORANGE = "orange";
	public static String RED = "red";
	public static String OFF = "off";

	String colour = "green";
	String text;

	public void setText(String text) {
		this.text = text;
		update();
	}

	public void setColor(String color){
		if (color.matches("^red$|^green$|^orange$|^off$")) {
		} else {
			PApplet.println("ERROR! LCD.setColor should be be 'red', 'green', 'orange' or 'off'");
		}

		this.colour = color;
		update();
	}

	public void update() {
		setChanged();
		notifyObservers();
	}

	public String getText() {
		return text;
	}

	public String getColour() {
		// TODO Auto-generated method stub
		return colour;
	}


}
