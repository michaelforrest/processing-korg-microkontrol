import rwmidi.*;
import microkontrol.*;
import microkontrol.controls.*;
MicroKontrol microKontrol;
void setup(){
  size(325, 101);
  microKontrol = new MicroKontrol(this);

  new KorgButtonListener("JOYSTICK");
}
public class KorgButtonListener implements ButtonListener{
  String name;
   KorgButtonListener(String id){
     this.name = id;
     microkontrol.controls.Button model = microKontrol.buttons.get(id);
     
     model.listen(this);
   }
   void pressed(){
     println(name + " pressed!");
   }
   void released(){
     println(name + " released!");
   }
   
}
