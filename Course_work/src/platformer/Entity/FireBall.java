package platformer.Entity;

import java.awt.Graphics;

import platformer.Game;
import platformer.Handler;
import platformer.Id;
import platformer.Tile.Tile;

public class FireBall extends Entity{
  public FireBall(int x,int y,int width,int height,boolean soild,Id id,Handler handler,int facing){
    super(x,y,width,height,soild,id,handler);
    if(facing==0)
      setVelX(-8);
    else if(facing==1)
      setVelX(8);
  } 
  
  @Override 
  public void render(Graphics g){
    g.drawImage(Game.fireball.getBufferedImage(),x,y,width,height,null);
  }
  
  @Override
  public void tick(){
    x+=velX;
    y+=velY;
    
    for(int i=0;i<handler.tile.size();i++){
      Tile t = handler.tile.get(i);
      if(t.soild){
        if(getBoundsLeft().intersects(t.getBounds())||getBoundsRight().intersects(t.getBounds()))die();//velX*=-1;

        if(getBoundsBottom().intersects(t.getBounds())){
          jumping = true;
          falling = false;
          gravity = 4.0;
        }else if (!falling&&!jumping){
          falling = true;
          gravity = 1.0;
        }
      }
    }
    
    for(int i=0;i<handler.entity.size();i++){
      Entity e = handler.entity.get(i);
      
      if(e.getId()==Id.goomba||e.getId()==Id.koopas||e.getId()==Id.mushroom||e.getId()==Id.towerboss){
        if(e.getBounds().intersects(getBounds())){
        e.die();
        die();
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