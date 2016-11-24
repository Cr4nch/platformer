package platformer.Entity;

import java.awt.Graphics;
import java.util.Random;

import platformer.Game;
import platformer.Handler;
import platformer.Id;
import platformer.Tile.Tile;

public class Goomba extends Entity{
  private int frame = 0;
private int frameDelay = 0;
//private Random rand = new Random();

public Goomba(int x,int y,int width,int height,boolean soild,Id id,Handler handler){
  super(x,y,width,height,soild,id,handler);
      setVelX(1);
      facing=1;
}

public void render(Graphics g){
  if(facing==2)
    g.drawImage(Game.goomba[0].getBufferedImage(),x,y,width,height,null);
  else if(facing==0)
    g.drawImage(Game.goomba[frame+1].getBufferedImage(),x,y,width,height,null);
  else if(facing==1)
    g.drawImage(Game.goomba[frame+4].getBufferedImage(),x,y,width,height,null);
        
}

public void tick(){
  x+=velX;
  y+=velY;
  
   for(Tile t:handler.tile){
    if(!t.soild)break;
    if(t.getId()==Id.wall){
      if(getBoundsBottom().intersects(t.getBounds())){
        setVelY(0);
        if(falling)falling=false;
      }else{
        if(!falling){
          gravity=0.0;
          falling=true;
        }
      }
      if(getBoundsLeft().intersects(t.getBounds())){
        setVelX(5);
        facing=1;
      }
      if(getBoundsRight().intersects(t.getBounds())){
        setVelX(-5);
        facing=0;
      }
    }
  }
  if(falling){
    gravity+=0.1;
    setVelY((int)gravity);
  }
  
  if(velX==0 && velY==0){
    facing=2;
    frame=0;
    frameDelay=0;
  }
  
  if(velX!=0){
  frameDelay++;
  if(frameDelay>=10){
    frame++;
    if(frame>=3)
      frame=0;
    frameDelay=0;
  }
  }
}



}
