import rwmidi.*;
import microkontrol.*;
import microkontrol.controls.*;
MicroKontrol microKontrol;
void setup(){
  microKontrol = new MicroKontrol(this);
  for(int i=0; i<microKontrol.encoders.length; i++){
     new EncoderTracker(microKontrol.encoders[i]); 
  }
}
/**
Encoders are relative so we have to be OOP 
**/
public class EncoderTracker implements EncoderListener{
  int value = 0;
  EncoderView(Encoder model){
    model.listen(this); 
  }
  public void moved(Integer change){
    value += change;
    println("Encoder changed by " + change + " to " + value); 
  }
}


