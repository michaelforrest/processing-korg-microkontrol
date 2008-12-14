import rwmidi.*;
import microkontrol.*;
import microkontrol.controls.*;
MicroKontrol microKontrol;
void setup(){
  size(100,100);
  microKontrol = new MicroKontrol(this);
}
void draw(){
  background(255);
  fill(100);
  ellipse(50 + (microKontrol.joystick.getX() * 50), 
          50 - (microKontrol.joystick.getY() * 50), 
          5,5);
}
