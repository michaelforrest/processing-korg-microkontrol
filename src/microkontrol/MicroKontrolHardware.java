package microkontrol;


import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;

import microkontrol.controls.*;
import processing.core.PApplet;
import rwmidi.MidiInput;
import rwmidi.MidiOutput;
import rwmidi.Note;
import rwmidi.RWMidi;
import rwmidi.SysexMessage;

public class MicroKontrolHardware {
	public static String input_device_a = "PORT A(.*)KORG INC.";
	public static String input_device_b = "PORT B(.*)KORG INC.";
	public static String output_device = "CTRL(.*)KORG INC.";

	MidiInput inputA;
	MidiInput inputB;
	MidiOutput output;

	MicroKontrol model;

	public static final String[] BUTTON_NAMES = { "<", ">", "ENTER",
			"HEX LOCK", "EXIT", "SCENE", "MESSAGE", "SETTING", "JOYSTICK", "PEDAL" };

	public static final String[] BUTTON_LIGHTS = {"SETTING","MESSAGE","SCENE","EXIT","ENTER","HEX LOCK"};//,"Tempo"//,">Grn","<Grn",">Red","<Red"};

	private int COMMAND_BYTE = 5; // sysex - index of command byte

	Hashtable<Integer, String> messages = new Hashtable<Integer, String>();
	Hashtable<String, Integer> colours = new Hashtable<String, Integer>();

	void setupColours() {
		colours.put("off", 0);
		colours.put("red", 0x10);
		colours.put("green", 0x20);
		colours.put("orange", 0x30);
	}
	MicroKontrolHardware(MicroKontrol model) {
		this.model = model;
		setupColours();
		setupMessages();
		setupIO();
		addPadViews();
		addLCDViews();
		addButtonViews();
	}

	private void addButtonViews() {
		for (int i = 0; i < BUTTON_LIGHTS.length; i++) {
			String id = BUTTON_LIGHTS[i];
			Button button = model.buttons.get(id);
			new ButtonView(0x10 + i, button);
		}

	}
	void addLCDViews() {
		for (int i = 0; i < model.lcds.length; i++) {
			new LCDView(i, model.lcds[i]);
		}
	}

	void addPadViews() {
		for (int i = 0; i < model.pads.length; i++) {
			Button pad = model.pads[i];
			// pads have incremental IDs - this maps onto how the hardware works
			// (and hence is a view-based id)
			new ButtonView(i, pad);
		}
	}

	public class ButtonView implements Observer   {
		int DISPLAY_LED_COMMAND = 0x01;

		Button pad;
		private int id;

		ButtonView(int id, Button pad) {
			this.id = id;
			this.pad = pad;
			pad.led.addObserver(this);
		}

		void turn(int state) {
			send(new int[] { DISPLAY_LED_COMMAND, id, state });
		}

		public void update(Observable o, Object arg) {
			turn((Integer) arg);
		}
	}

	public class LCDView implements Observer {
		LCD model;
		private int DISPLAY_LCD = 0x22;
		private final int id;

		LCDView(int id, LCD model) {
			this.id = id;
			this.model = model;
			model.addObserver(this);
		}

		int colour(String id) {
			return ((Number) colours.get(id)).intValue();
		}

		public void update(Observable o, Object e) {
			String text = model.getText() + "        "; // really grubby way to pad
			// the string...
			byte[] t = text.getBytes();
			int[] ints = { DISPLAY_LCD, 9, colour(model.getColour()) + id, t[0],
					t[1], t[2], t[3], t[4], t[5], t[6], t[7] };
			send(ints);
		}
	}

	byte DATA_DUMP_COMMAND = (byte) 0x5F;

	byte PAD_OUTPUT_COMMAND = (byte) 0x45;
	byte ENCODER_OUTPUT_COMMAND = (byte) 0x43;
	byte SLIDER_OUTPUT_COMMAND = (byte) 0x44;
	byte PEDAL_OUTPUT_COMMAND = (byte) 0x47;
	byte SWITCH_OUTPUT_COMMAND = (byte) 0x48;
	byte JOYSTICK_OUTPUT_COMMAND = (byte) 0x4b;

	public void sysexReceived(SysexMessage sysex) {
		byte[] m = sysex.getMessage();
//		PApplet.println("received sysex " + sysex.toString());
		byte command = m[5];
		// if(marker == 0x40) onEnterNativeMode();
//		if(command == DATA_DUMP_COMMAND) receiveDataDump(m);
		if (command == PAD_OUTPUT_COMMAND) receivePad(m[COMMAND_BYTE + 1]);
		if (command == SLIDER_OUTPUT_COMMAND) receiveFader(m[6], m[7]);
		if (command == ENCODER_OUTPUT_COMMAND) receiveEncoder(m[6], m[7]);
		if (command == PEDAL_OUTPUT_COMMAND) receivePedal(m[7]);
		if (command == SWITCH_OUTPUT_COMMAND) receiveSwitch(m[6], m[7]);
		if (command == JOYSTICK_OUTPUT_COMMAND) receiveJoystick(m[6], m[7]);
	}

	void receiveDataDump(byte[] m) {
		PApplet.println("MESSAGE BACK:");
		PApplet.println(message(m[6]));
		for (int i = 7; i < m.length - 1; i++)
			PApplet.println(m[i]);
	}

	void receiveJoystick(byte rawX, byte rawY) {
		Joystick joystick = model.joystick;
		float x = ((float) rawX - 64) / 64f;
		float y = ((float) rawY - 64) / 64f;
		joystick.set(x,y);
	}

	void receiveSwitch(byte id, byte value) {
		String name = BUTTON_NAMES[(int) id];
		Button button = model.buttons.get(name);
		if(value == 0){
			button.release();
		}else{
			button.press();
		}
	}

	void receivePedal(byte state) {
		boolean on = state == (byte) 127;
		if(on){
			model.pedal.press();
		}else{
			model.pedal.release();
		}
	}

	void receiveEncoder(byte id, byte changeCode) {
		int change = (changeCode >= 64) ? -(128 - changeCode) : changeCode;
		//PApplet.println("encoder value " + changeCode + " ->" + change);
		model.encoders[id].set(change);
	}

	void receiveFader(byte id, byte value) {
		model.faders[id].set(value);
	}

	void receivePad(byte info) {
		int id = info & 0x0f;
		Pad pad = model.pads[id];
		int conditionBit = 64 & info;
		if (conditionBit == 0){
			pad.release();
		}else{
			pad.press();
		}

	}

	/***************************************************************************
	 * MESSAGES
	 **************************************************************************/
	void setupMessages() {
		message(0x00, "1st Packet Data");
		message(0x01, "2nd Packet Data");
		message(0x02, "3rd Packet Data");
		message(0x03, "4th Packet Data");
		message(0x26, "Data Format message");
		message(0x23, "Data Load Completed");
		message(0x24, "Data Load message");
		message(0x21, "Write Completed");
		message(0x22, "Write message");
	}

	private void message(int code, String description) {
		messages.put(code, description);
	}

	private String message(byte code) {
		return (String) (messages.get(Integer.toHexString(code)));
	}

	public static void displayInterfaces() {
		String[] inputs = RWMidi.getInputDeviceNames();
		PApplet.println("INPUTS: ");
		PApplet.println(PApplet.join(inputs, "\n"));
		PApplet.println("OUTPUTS: ");
		String[] outputs = RWMidi.getOutputDeviceNames();
		PApplet.println(PApplet.join(outputs,  "\n"));

	}

	private int EXCLUSIVE = 0xf0;
	private int KORG = 0x42;
	private int GLOBAL_MIDI_CHANNEL = 0x40;
	private int CHANNEL = 0;
	private int SOFTWARE_PROJECT = 0x6e;
	private int MICROKONTROL_SUB_ID = 0x0;
	private int[] START = { EXCLUSIVE, KORG, GLOBAL_MIDI_CHANNEL + CHANNEL,
			SOFTWARE_PROJECT, MICROKONTROL_SUB_ID };
	private int[] END = { 0xf7 };
	private int[] NATIVE = { 0x0, 0x0, 0x1 };

	int transpose = 0; // bytes 0-5 -- 0+/-24
	int midiChannel = 0;
	int pitchBendChannel = 0;
	int padsSendMidiNotes = 0; // 1 = true, 0 = false

	private int PACKET_COMMUNICATION_COMMAND = 0x3f;
	private int[] PACKET1 = { PACKET_COMMUNICATION_COMMAND, 39, transpose,
			midiChannel, pitchBendChannel,
			0x0, // ?
			0x7f * padsSendMidiNotes, 0x7f * padsSendMidiNotes,
			0x3 * padsSendMidiNotes, // pad note transmit
			15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, // 16 x
			// Pad
			// Midi
			// Channel
			48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63 // pad
	// note
	// numbers
	};

	int buttonsLit = 0;

	private int[] PACKET2 = { PACKET_COMMUNICATION_COMMAND, 33, 2,
			0x7f * buttonsLit, 0x7f * buttonsLit, 0x7f * buttonsLit, // pads,
			// pads,
			// enter,
			// scene
			// etc..
			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,// all
			// to
			// do
			// with
			// stuff
			// I've
			// done
			// in
			// the
			// model
			2, 2, 2, 2, 2, 2, 2, 2 // Main LCD Text
	};
	private int[] PACKET3 = { PACKET_COMMUNICATION_COMMAND, 33, 3, 1, 1, 1, 1,
			1, 1, 1, 1, // LCD1 text
			1, 1, 1, 1, 1, 1, 1, 1, // LCD2 text
			1, 1, 1, 1, 1, 1, 1, 1, // LCD3 text
			1, 1, 1, 1, 1, 1, 1, 1 // LCD4 text
	};
	private int[] PACKET4 = { PACKET_COMMUNICATION_COMMAND, 18, 1, 0, 0, 0, 0,
			0, 0, 0, 0, 3, 77, 73, 67, 72, 65, 69, 76, 33 };

	// SEND DATA

	void send(int[] ints) {
		byte[] bytes = new byte[START.length + ints.length + END.length];
		bytes = addBytes(bytes, START, 0);
		bytes = addBytes(bytes, ints, START.length);
		bytes = addBytes(bytes, END, START.length + ints.length);
		// println("**sending");
		// for(int i=0; i<bytes.length; i++)
		// println(Integer.toHexString(bytes[i]));
		output.sendSysex(bytes);

	}

	byte[] addBytes(byte[] bytes, int[] toAdd, int offset) {
		for (int i = 0; i < toAdd.length; i++)
			bytes[offset + i] = (byte) toAdd[i];
		return bytes;
	}

	void setupIO() {
		//displayInterfaces();
		inputA = RWMidi.getInputDevice(findInput(input_device_a)).createInput(this);
		PApplet.println("Input A: " + inputA.getName());
		inputB = RWMidi.getInputDevice(findInput(input_device_b)).createInput(this);
		PApplet.println("Input B: " + inputB.getName());
		output = RWMidi.getOutputDevice(findOutput(output_device)).createOutput();
		PApplet.println("output : " + output.getName());
		send(NATIVE);
		send(PACKET1);
		send(PACKET2);
		send(PACKET3);
		send(PACKET4);
	}

	void plugKeyboard(Object to) {
		inputB.plug(to);
	}

	/**
	 * MIDI IN
	 */
	public void noteOnReceived(Note note) {
		// setChanged();
		// notifyObservers(note);
		PApplet.println("note on " + note.getPitch());
		// // toLX2.sendNoteOn(13, note.getPitch(), 127);
		// // toLX2.sendNoteOn(13, note.getPitch()+1, 127);
	}

	// void noteOffReceived(Note note){
	// setChanged();
	// notifyObservers(note);
	// }
	// void controllerChangeReceived(Controller controller){
	// println("controller " + controller.getValue());
	// }
	public static boolean isAvailable() {
		return findInput(input_device_a) != null;
	}
	private static String findOutput(String regex) {
		return findInArray(regex, RWMidi.getOutputDeviceNames());
	}

	private static String findInArray(String regex, String[] array) {
		for (int i = 0; i < array.length; i++) {
			String string = array[i];
			if (PApplet.match(string, regex) != null) return string;
		}
		return null;
	}

	private static String findInput(String regex) {
		return findInArray(regex, RWMidi.getInputDeviceNames());
	}
}
