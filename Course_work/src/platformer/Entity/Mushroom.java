package platformer.Entity;

import java.awt.Graphics;
import java.util.Random;

import platformer.Game;
import platformer.Handler;
import platformer.Id;
import platformer.Tile.Tile;

public class Mushroom extends Entity{
  
  private Random rand = new Random();
  public int type;
  
  
    public Mushroom(int x,int y,int width,int height,boolean soild,Id id,Handler handler,int type){
      super(x,y,width,height,soild,id,handler);
      this.type = type;
      int dir=rand.nextInt(2);
      if(dir==1)
        setVelX(1);
      else 
        setVelX(-1);
  }
  
  @Override
  public void tick(){
    x+=velX;
    y+=velY;
    if(x<=0)x=0;
    if(y<=0)y=0;
    //if(x+width>=810)x=810-width;
    //if(y+height>=576)y=576-height;
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
        }
        if(getBoundsRight().intersects(t.getBounds())){
          setVelX(-5);
        }
      }
    }
    if(falling){
      gravity+=0.1;
      setVelY((int)gravity);
    }
  }
  
  @Override 
  public void render(Graphics g){
    switch(type){
      case 0:
        g.drawImage(Game.mushroom.getBufferedImage(),x,y,width,height,null);
        break;
      case 1:
        g.drawImage(Game.liveMushroom.getBufferedImage(),x,y,width,height,null);
        break;
        
    }
  }
  
}
