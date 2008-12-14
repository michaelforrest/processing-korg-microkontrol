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


import java.util.Hashtable;
import java.util.Observable;

import microkontrol.controls.*;
import processing.core.PApplet;

/**
 * @author Michael Forrest
 *
 */
public class MicroKontrol {

	PApplet applet;
	public final String VERSION = "0.1.0";

	private MicroKontrolHardware hardware;

	public Pad[] pads = new Pad[16];
	public Hashtable<String, Button> buttons = new Hashtable<String, Button>();
	public Encoder[] encoders = new Encoder[9];
	public Fader[] faders = new Fader[8];
	public LCD[] lcds = new LCD[9];
	public LCD main;
	public Joystick joystick = new Joystick();


	public MicroKontrol(PApplet applet) {
		this.applet = applet;

		for (int i = 0; i < pads.length; i++)
			pads[i] = new Pad();

		for(int i = 0; i < encoders.length; i++)
			encoders[i] = new Encoder();

		for (int i = 0; i < faders.length; i++)
			faders[i] = new Fader();

		for (int i = 0; i < lcds.length; i++)
			lcds[i] = new LCD();

		main = lcds[8];

		for (int i = 0; i < MicroKontrolHardware.BUTTON_NAMES.length; i++) {
			String name = MicroKontrolHardware.BUTTON_NAMES[i];
			buttons.put(name, new Button());
		}

		if (MicroKontrolHardware.isAvailable()){
			hardware = new MicroKontrolHardware(this);
			PApplet.println("Found and initialized MicroKontrol hardware.");
		}else{
			PApplet.println("Couldn't find hardware!");
			PApplet.println("Available interfaces: " );
			MicroKontrolHardware.displayInterfaces();
			PApplet.println("\nIf you can see your device ports in the list above, do the following before instantiating MicroKontrol, substituting the correct port names (regular expressions work):");
			PApplet.println(
					"MicroKontrolHardware.input_device_a = \"" + MicroKontrolHardware.input_device_a + "\";\n" +
					"MicroKontrolHardware.input_device_b = \"" + MicroKontrolHardware.input_device_b + "\";\n" +
					"MicroKontrolHardware.output_device = \"" + MicroKontrolHardware.output_device + "\";");
			PApplet.println("\nNote that you can still use the MicroKontrol class as normal even if the hardware unit is not connected.");
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
	/**
	 * return the version of the library.
	 *
	 * @return String
	 */
	public String version() {
		return VERSION;
	}

}
