import rwmidi.*;
import microkontrol.*;
import microkontrol.controls.*;
MicroKontrol microKontrol;
void setup(){
  size(325, 101);
  microKontrol = new MicroKontrol(this);
}
void draw(){
  background(255);
  for(int i=0; i<microKontrol.faders.length; i++){
    drawTrack(i);
    drawMarker(i, microKontrol.faders[i].getProportion());
  } 
}
void drawTrack(int i){
  fill(200);
  rect(5 + (i * 40), 0, 35, 100);
}
void drawMarker(int i, float proportion){
  fill(50);
  rect(5 + (i * 40), (1-proportion) * 95, 35, 5); 
}
