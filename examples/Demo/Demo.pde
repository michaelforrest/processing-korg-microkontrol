import rwmidi.*;
import microkontrol.*;
import microkontrol.controls.*;
MicroKontrol microKontrol;
void setup(){
  microKontrol = new MicroKontrol(this);
  microKontrol.main.setText("Hello!");
  microKontrol.main.setColor(LCD.GREEN);
}
