package microkontrol.controls;

import java.util.Observable;

public class LCD extends Observable{

	public String colour = "green";
    public String text;

    public void setText(String text){
      this.text = text;
      update();
    }
    public void setColour(String colour){
      this.colour = colour;
      update();
    }
    public void update(){
      setChanged();
      notifyObservers();
    }
}
