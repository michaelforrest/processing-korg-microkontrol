class MicroKontrolView implements Observer{
  PImage bg;
  MicroKontrol model;

  PadView[] pads;
  FaderView[] faders;

  MicroKontrolView(MicroKontrol model){
    this.model = model;
    addPadViews();
    addFaderViews();
    bg = loadImage("microkontrol.png");
  }
  void addPadViews(){
    pads = new PadView[model.pads.length];
    Rectangle first = new Rectangle(75,312,29,29);
    for(int i=0; i<pads.length; i++){
      PadView view = new PadView(model.pads[i]);
      view.p = first.topLeft().add(new Point(first.width * (i%4), first.height * (int)(i / 4.0 )));
      view.rect = new Rectangle(view.p.x, view.p.y, first.width, first.height);
      pads[i] = view;
    }
  }
  void addFaderViews(){
    faders = new FaderView[model.faders.length];
    Rectangle first = new Rectangle(272,369,43,64);
    for(int i=0; i<model.faders.length; i++){
      FaderView view = new FaderView(model.faders[i]);
      view.p = first.topLeft().add(new Point(first.width * i,0)); 
      faders[i] = view;
    }
  }
  void update(Observable obj, Object e){

  } 
  void draw(){
    image(bg,0,0); 
    drawControls(pads);
    drawControls(faders);
  }
  void drawControls(ControlView[] views){
    for(int i=0; i< views.length; i++){
      ControlView view = (ControlView) views[i];
      if(view == null) return;
      view.draw();
    } 
  }
  class ControlView {
    PImage image;
    Rectangle rect;
    Point p;
    void draw(){ 
      image(image, p.x,p.y); 
    }
  }
  public class ButtonView extends ControlView implements ButtonListener{
    microkontrol.controls.Button button;
    ButtonView(microkontrol.controls.Button button){
      this.button = button;
      button.listen(this);

    }
    void setup(){
    }
    void pressed(){
      //button.toggle();
    }
    void released(){
    }
    void updated(){

    }


  }
  Boolean paintWas;
  public class PadView extends ButtonView{
    Pad pad;
    PadView(Pad pad){
      super(pad);
      this.pad = pad;
     
      updateImage();
    }
    void updated(){
      updateImage();
    }
    void updateImage(){
      image = (pad.isOn()) ? image("PadView.on") : image("PadView.off"); 
    }

    void draw(){
      if(mousePressed){
        if(mouse.within(rect)){
          if( paintWas==null) paintWas = !pad.isOn();
          pad.set(paintWas);
        }
      }
      else{ 
        paintWas = null; 
      }
      super.draw();
    }
  }
  public class FaderView extends ControlView implements FaderListener{
    Fader fader;
    int TOP = 369;
    int TRAVEL = 36;
    FaderView(Fader fader){
      this.fader = fader; 
      fader.listen(this);
      image = image("Fader");
    }
    void moved(Float proportion){
      p.y = TOP + ((1-fader.getProportion()) * TRAVEL);

    }

  }
}
