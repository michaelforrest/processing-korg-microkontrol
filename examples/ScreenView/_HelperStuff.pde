
public class Point{
  public float x;
  public float y;
  public Point(float $x,float $y){
    x = $x;
    y = $y;
  }
  public boolean within(Rectangle area){
    return x > area.x && x < area.right() && y > area.y && y < area.bottom() ;
  }
  public Point add(Point p){
    return new Point(x + p.x, y + p.y);
  }
}
public class Rectangle extends Point{
  public float width;
  public float height;
  public Rectangle(float $x, float $y, float $width, float $height){
    super($x,$y);
    width = $width;
    height = $height; 
  }
  public void setTopLeft(Point p){
    x = p.x;
    y = p.y; 
  }
  public Point topLeft(){
    return new Point(x,y);
  }

  public Point centre(){
    return new Point( x + (width /2), y + (height/2)); 
  }
  public float right(){
    return x + width;
  }
  public float bottom(){
    return y + height;
  }
  public Rectangle scaleFromMiddle(float amount){
    float horizontal = amount * width / 2;
    float vertical = amount * height / 2;
    return new Rectangle(centre().x-horizontal, centre().y - vertical, 2 *horizontal, 2 * vertical);
  }
}

public class Mouse extends Point{
  Events events = new Events();
  Point position;
  Mouse(int x, int y){
    super(x,y);
  }
  void setPosition(int x,int y){
    this.x =x;
    this.y= y; 
  }
  void clicked(){ 
    events.clicked();
  }
  class Events extends Observable{
    void clicked(){
      setChanged();
      notifyObservers(CLICK); 
    }   
  }
}
String CLICK = "click";
boolean findInList(Object value, Object[] list){
  for(int i=0; i < list.length; i++){
    if(list[i].equals(value)) return true;
  } 
  return false;
}
String findInArray(String regex, String[] array){
  for(int i = 0; i < array.length; i++){
    String string = array[i];
    if(match(string, regex) != null ) return string; 
  }
  return null;
}


