import rwmidi.*;
import microkontrol.*;
import microkontrol.controls.*;
MicroKontrol microKontrol;
Encoder main;
void setup(){
  size(50, 50);
  microKontrol = new MicroKontrol(this);
  main = microKontrol.encoders[8];
  /*
  simpler way to map an event - rotate the main encoder to see events
  */
  main.listen(this, "onMainEncoderMoved");
 
}
void onMainEncoderMoved(Integer change){
  println("Encoder " + this + " changed by " + change); 
}

void draw(){
  background(255);
  drawCircle(25, (float)(main.getValue() % 360)/PI);
}

void drawCircle(int x, float angle){
  ellipseMode(CENTER);
  ellipse(x, 20, 38, 38);
  line(x, 20, x + (19 * sin(angle)), 20 -(19 * cos(angle)));
}

