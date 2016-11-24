package platformer.Entity;

import java.awt.Graphics;
import java.util.Random;

import platformer.Game;
import platformer.Handler;
import platformer.Id;
import platformer.Tile.Tile;

public class PowerStar extends Entity{
  
  //private Random rand;
  
   public PowerStar(int x,int y,int width,int height,boolean soild,Id id,Handler handler){
     super(x,y,width,height,soild,id,handler);
   //  rand = new Random();
        setVelX(1);
   } 
   
   @Override
   public void render(Graphics g){
     g.drawImage(Game.star.getBufferedImage(),x,y,width,height,null);
   }
   
  @Override
  public void tick(){
    x+=velX;
    y+=velY;
    
  for(int i=0;i<handler.tile.size();i++){
      Tile t = handler.tile.get(i); 
      if(t.soild){
        if(getBoundsBottom().intersects(t.getBounds())){
          jumping = true;
          falling=false;
          gravity = 0.8;
        }
     /*   if(getBoundsBottom().intersects(t.getBounds())){
          setVelY(0);
          if(falling)falling=false;
        }else{
          if(!falling){
            gravity=0.0;
            falling=true;
          }
        }*/
        if(getBoundsLeft().intersects(t.getBounds())){
          setVelX(5);
        }
        if(getBoundsRight().intersects(t.getBounds())){
          setVelX(-5);
        }
      }
  }
    
  if(jumping){
      gravity-=0.13;
      setVelY((int)-gravity);
      if(gravity<=0.6){
        jumping=false;
        falling=true;
      }
  }
  if(falling){
      gravity+=0.13;
      setVelY((int)gravity);
  }
  
   
}
  
  
}
