/**
  Not amazingly useful but if you want a starting point
  for an on-screen proxy view, here's one. I've decided
  to go more abstract in my own project.
**/


import rwmidi.*;
import microkontrol.*;
import microkontrol.controls.*;
MicroKontrol microKontrol;
MicroKontrolView view;
Mouse mouse = new Mouse(0,0);
void setup(){
  size(800,515);
  loadAssets();
  microKontrol = new MicroKontrol(this);
  view = new MicroKontrolView(microKontrol);
}
void draw(){
  mouse.setPosition(mouseX, mouseY);
   background(0);
   view.draw(); 
}

Hashtable images = new Hashtable();
void loadAssets(){
  images.put("PadView.on", loadImage("pad-on.jpg"));
  images.put("PadView.off", loadImage("pad-off.jpg"));
  images.put("Fader", loadImage("fader.jpg"));
}
PImage image(String id){
  return (PImage) images.get(id);
}
