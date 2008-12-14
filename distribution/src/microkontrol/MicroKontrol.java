/*
  Library for using Korg MicroKontrol with Processing

  (c) copyright

  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General
  Public License along with this library; if not, write to the
  Free Software Foundation, Inc., 59 Temple Place, Suite 330,
  Boston, MA  02111-1307  USA
 */

package microkontrol;


import java.util.*;

import microkontrol.controls.*;
import processing.core.PApplet;

/**
 * @author Michael Forrest
 *
 */
public class MicroKontrol {

	PApplet applet;

	Hashtable<String, Button> buttons = new Hashtable<String, Button>();

	public Pad[] pads = new Pad[16];
	public Fader[] faders = new Fader[8];
	public LCD[] lcds = new LCD[9];
	public LCD main;
	public final String VERSION = "0.1.0";

	private MicroKontrolHardware hardware;

	public MicroKontrol(PApplet applet) {
		this.applet = applet;

		for (int i = 0; i < 16; i++)
			pads[i] = new Pad();

		for (int i = 0; i < 8; i++)
			faders[i] = new Fader();

		for (int i = 0; i < 9; i++)
			lcds[i] = new LCD();

		main = lcds[8];

		for (int i = 0; i < MicroKontrolHardware.BUTTON_NAMES.length; i++) {
			String name = MicroKontrolHardware.BUTTON_NAMES[i];
			buttons.put(name, new Button());
		}

		if (MicroKontrolHardware.isAvailable()){
			hardware = new MicroKontrolHardware(this);
			PApplet.println("Found and initialized MicroKontrol hardware.");
		}

		go();
	}
	void go() {
		main.setText("");
		main.setColor("red");
		String[] lcdText = { "Processin", "ng librar", "ry by Mic",
				"chael For", "rrest - g", "grimacewo", "orks.com ", " 2008" };
		for (int i = 0; i < lcdText.length; i++)
			lcds[i].setText(lcdText[i]);

	}
	public class Button extends Observable {
		boolean isOn = false;

		void set(boolean on) {
			isOn = on;
			update();
		}

		void toggle() {
			PApplet.println("Toggling button");
			isOn = !isOn;
			update();
		}

		void update() {
			setChanged();
			notifyObservers();
		}
	}

	public class Pad extends Button {

	}

	public class Joystick {

	}

	public class Fader extends Observable {
		int value;
		float getProportion() {
			return (float) value / 127.0f;
		}

		void set(int value) {
			this.value = value;
			setChanged();
			notifyObservers();
		}
	}



	/**
	 * return the version of the library.
	 *
	 * @return String
	 */
	public String version() {
		return VERSION;
	}

}
