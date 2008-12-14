import rwmidi.*;
import microkontrol.*;
import microkontrol.controls.*;
MicroKontrol microKontrol;
void setup(){
  microKontrol = new MicroKontrol(this);
  microKontrol.main.set("Move", LCD.RED);
  microKontrol.lcds[0].set("faders", LCD.RED);
  for(int i=0; i<microKontrol.faders.length; i++){
    new FaderView( "Fader " + (i+1), microKontrol.faders[i]);
  }

  
}

public class FaderView implements Observer{
  Fader model;
  String name;
  FaderView(String name, Fader model){
    this.name = name;
    this.model = model; 
    model.addObserver(this);
  }
  void update(Observable o, Object e){
    println(name + " = " + model.getProportion());
  }
}
