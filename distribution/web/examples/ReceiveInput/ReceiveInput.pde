import rwmidi.*;
import microkontrol.*;
import microkontrol.controls.*;
MicroKontrol microKontrol;
void setup(){
  microKontrol = new MicroKontrol(this);
  microKontrol.main.setText("Move");
  microKontrol.main.setColor(LCD.RED);
  microKontrol.lcds[0].setText("faders");
  microKontrol.lcds[0].setColor(LCD.RED);
}
