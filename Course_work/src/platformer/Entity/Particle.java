package platformer.Entity;

import java.awt.Graphics;

import platformer.Game;
import platformer.Handler;
import platformer.Id;

public class Particle extends Entity{
  
  private int frame = 0;
  private int frameDelay = 0;
  
  private boolean fading = false;
  
  public Particle(int x,int y,int width,int height,boolean soild,Id id,Handler handler){
    super(x,y,width,height,soild,id,handler);
  }
  
  
  @Override 
  public void render(Graphics g){
    if(!fading)g.drawImage(Game.particle[frame].getBufferedImage(),x,y,width,height,null);
    else g.drawImage(Game.particle[2*Game.particle.length-frame-1].getBufferedImage(),x,y,width,height,null);
  }
  
  @Override
  public void tick(){
    frameDelay++;
    
    if(frameDelay>=2){
      frame++;
      frameDelay=0;
    }
    if(frame>=Game.particle.length)fading =true;
    if(frame>=12)die();
  }
  
}
