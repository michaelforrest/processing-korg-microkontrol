import rwmidi.*;
import microkontrol.*;
import microkontrol.controls.*;
MicroKontrol microKontrol;
void setup(){
  size(325, 50);
  microKontrol = new MicroKontrol(this);
  for(int i=0; i<microKontrol.encoders.length; i++){
     new EncoderTracker(microKontrol.encoders[i]); 
  }
  ellipseMode(CENTER);
}

void draw(){
  background(255);
  for(int i=0; i<microKontrol.encoders.length; i++){
    Encoder encoder = microKontrol.encoders[i];
    drawCircle((i * 40) + 20, (float)(encoder.getValue() % 360)/PI);
  }
}

void drawCircle(int x, float angle){
  ellipse(x, 20, 38, 38);
  line(x, 20, x + (19 * sin(angle)), 20 -(19 * cos(angle)));
}

/**
 Track changes with a listener:
**/
public class EncoderTracker implements EncoderListener{
  EncoderTracker(Encoder model){
    model.listen(this); 
  }
  public void moved(Integer change){
    println("Encoder " + this + " changed by " + change); 
  }
}


