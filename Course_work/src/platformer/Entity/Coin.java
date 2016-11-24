package platformer.Entity;

import java.awt.Graphics;

import platformer.Game;
import platformer.Handler;
import platformer.Id;

public class Coin extends Entity{
  public int frame=0;
  public int frameDelay=0;
  public Coin(int x,int y,int width,int height,boolean soild,Id id,Handler handler){
    super(x,y,width,height,soild,id,handler);  
  }
  
  @Override
  public void render(Graphics g){
    g.drawImage(Game.coin[frame].getBufferedImage(),x+width/4,y+height/2,width/2,height/2,null);
  }
  
  
  @Override 
  public void tick(){
    frameDelay++;
    if(frameDelay>=10){
      frame++;
      if(frame>=8)
        frame=0;
      frameDelay=0;
    }
  }
  
  
}
