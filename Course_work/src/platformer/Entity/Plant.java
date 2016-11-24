package platformer.Entity;

import java.awt.Color;
import java.awt.Graphics;

import platformer.Handler;
import platformer.Id;

public class Plant extends Entity{
  
  private int wait;
  private boolean moving;
  private boolean insidePipe;
  private int pixelsTravelled = 0;
  
  public Plant(int x,int y,int width,int height,boolean soild,Id id,Handler handler){
    super(x,y,width,height,soild,id,handler);
    moving=false;
    insidePipe=true;
  }
  
  @Override
  public void render(Graphics g){
    g.setColor(Color.RED);
    g.fillRect(x,y,width,height);
  }
  
  @Override
  public void tick(){
    y+=velY;
    
    
    
    if(!moving)wait++;
    
    if(wait>=180){
      if(insidePipe)insidePipe=false;
      moving=true;
      wait=0;
    }
    
    if(moving){
      if(insidePipe)setVelY(-3);
      else setVelY(3);
      pixelsTravelled+=Math.abs(velY);
      if(pixelsTravelled>=height){
        pixelsTravelled = 0;
        moving = false;
        setVelY(0);
      }
    }
    
  }
}
